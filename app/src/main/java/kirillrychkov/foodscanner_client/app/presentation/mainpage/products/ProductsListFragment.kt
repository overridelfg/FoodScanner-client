package kirillrychkov.foodscanner_client.app.presentation.mainpage.products

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.liveData
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.map
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kirillrychkov.foodscanner_client.R
import kirillrychkov.foodscanner_client.app.domain.entity.Product
import kirillrychkov.foodscanner_client.app.domain.entity.ProductRestriction
import kirillrychkov.foodscanner_client.app.presentation.FoodScannerApp
import kirillrychkov.foodscanner_client.app.presentation.ViewModelFactory
import kirillrychkov.foodscanner_client.app.presentation.ViewState
import kirillrychkov.foodscanner_client.app.presentation.mainpage.favorites.FavoritesFragment
import kirillrychkov.foodscanner_client.databinding.FragmentProductsListBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.URLEncoder
import java.nio.charset.StandardCharsets.UTF_8
import javax.inject.Inject


class ProductsListFragment : Fragment() {
    private var _binding: FragmentProductsListBinding? = null
    private val binding: FragmentProductsListBinding
        get() = _binding ?: throw RuntimeException("FragmentProductsListBinding == null")


    private lateinit var viewModel: ProductsListViewModel

    private lateinit var adapter: ProductsListAdapter
    private var listOfProducts = mutableListOf<Product>()
    private var query: String = ""
    private var currentSort = "normal"

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        FoodScannerApp.appComponent
    }

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(
            requireActivity(),
            viewModelFactory
        )[ProductsListViewModel::class.java]
        _binding = FragmentProductsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        subscribeGetProductsBySearch()
        bindSearchButton()
        setOnTextChange()
        subscribeAddFavoriteResult()
        setupSwipeToRefreshLayout()
        subscribeGetProductsList()
        subscribeSortResult()
        viewModel.submitData("","normal")
        binding.btnSort.setOnClickListener {
            binding.etSearchProduct.setText("")
            viewModel.submitData("", currentSort)
        }
    }

    private fun subscribeGetProductsList() {
        lifecycleScope.launch {
            viewModel.productsDataList.observe(viewLifecycleOwner) {
                adapter.submitData(viewLifecycleOwner.lifecycle, it)
            }
        }


        lifecycleScope.launchWhenStarted {
            adapter.loadStateFlow.collect {
                val state = it.refresh
                binding.pbProductList.isVisible = state is LoadState.Loading
                val errorState = when {
                    it.append is LoadState.Error -> it.append as LoadState.Error
                    it.prepend is LoadState.Error -> it.prepend as LoadState.Error
                    it.refresh is LoadState.Error -> it.refresh as LoadState.Error
                    else -> null
                }
                if (errorState != null) {
                    if (
                        errorState.error.cause.toString().split(':')[0] == "java.net.ConnectException") {
                        if (adapter.snapshot().isEmpty()) {
                            binding.errorImage.isVisible = true
                            binding.errorButton.isVisible = true
                            binding.errorTxt.isVisible = true

                            binding.pbProductList.isVisible = false
                            binding.errorButton.setOnClickListener {
                                adapter.refresh()
                            }
                        }
                    }
                } else {
                    binding.errorImage.isVisible = false
                    binding.errorButton.isVisible = false
                    binding.errorTxt.isVisible = false
                }
            }
        }

    }

    private fun subscribeGetProductsBySearch() {
        viewModel.productsSearchList.observe(viewLifecycleOwner) {
            when (it) {
                is ViewState.Success -> {
                    binding.pbProductList.isVisible = false
                    adapter.productsList = it.result
                }
                is ViewState.Loading -> {
                    binding.pbProductList.isVisible = true
                }
                is ViewState.Error -> {
                    binding.pbProductList.isVisible = false
                    Snackbar.make(
                        requireView(),
                        it.result.toString(),
                        Snackbar.LENGTH_LONG
                    ).setAction("OK") {
                    }.show()
                }
            }
        }
    }


    private fun setupRecyclerView() {

        val rvProductsList = binding.rvProductsList
        val layoutManager = GridLayoutManager(requireContext(), 2)
        rvProductsList.layoutManager = layoutManager
        adapter = ProductsListAdapter()
        adapter.onProductSelectListener = {
            viewModel.sendProductDetails(it)
            val bottomSheetRoot = binding.bottomSheet.bottomSheetRoot
            val mBottomBehavior =
                BottomSheetBehavior.from(bottomSheetRoot)
            mBottomBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            bottomSheetRoot.visibility = View.VISIBLE
        }
        adapter.onProductFavoriteClickListener = {
            viewModel.addToFavorite(it)
        }
        val footerAdapter = ProductListLoaderStateAdapter() {
            adapter.retry()
        }
        rvProductsList.adapter = adapter.withLoadStateFooter(footerAdapter)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == adapter.itemCount && footerAdapter.itemCount > 0) {
                    2
                } else {
                    1
                }
            }
        }

        adapter.addLoadStateListener {
            binding.pbProductList.isVisible = it.source.refresh is LoadState.Loading

        }
    }

    private fun bindSearchButton() {

        binding.etSearchProduct.setOnEditorActionListener { view, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val data = binding.etSearchProduct.text.toString()
                viewModel.submitData(data, "normal")
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun setupSwipeToRefreshLayout() {
        binding.swipeLayout.setOnRefreshListener {
            binding.swipeLayout.isRefreshing = false
            binding.etSearchProduct.setText("")
            viewModel.submitData("", "normal")
            adapter.refresh()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.submitData("", "normal")
        binding.etSearchProduct.setText("")
        adapter.refresh()
    }


    private fun subscribeAddFavoriteResult() {
        viewModel.addToFavoriteResult.observe(viewLifecycleOwner) {
            when (it) {
                is ViewState.Success -> {
                    binding.pbProductList.isVisible = false
                }
                is ViewState.Loading -> {
                    binding.pbProductList.isVisible = true
                }
                is ViewState.Error -> {
                    binding.pbProductList.isVisible = false
                    Snackbar.make(
                        requireView(),
                        it.result.toString(),
                        Snackbar.LENGTH_LONG
                    ).setAction("OK") {
                    }.show()
                }
            }
        }
    }

    private fun subscribeSortResult() {
        viewModel.currentSort.observe(viewLifecycleOwner) {
            if(it == "normal"){
                binding.btnSort.setBackgroundResource(R.drawable.ic_baseline_sort_24)
                currentSort = "valid"
            }else{
                binding.btnSort.setBackgroundResource(R.drawable.ic_baseline_sort_selected)
                currentSort = "normal"
            }
        }
    }


    private fun setOnTextChange() {
        binding.etSearchProduct.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

//                viewModel.getProductsBySearch(binding.etSearchProduct.text.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }

}
package kirillrychkov.foodscanner_client.app.presentation.mainpage.favorites

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
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
import kirillrychkov.foodscanner_client.app.presentation.mainpage.products.ProductsListAdapter
import kirillrychkov.foodscanner_client.app.presentation.mainpage.products.ProductsListRecyclerAdapter
import kirillrychkov.foodscanner_client.app.presentation.mainpage.products.ProductsListViewModel
import kirillrychkov.foodscanner_client.databinding.FragmentFavoritesBinding
import kirillrychkov.foodscanner_client.databinding.FragmentProductsListBinding
import javax.inject.Inject

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding: FragmentFavoritesBinding
        get() = _binding ?: throw RuntimeException("FragmentFavoritesBinding == null")


    private lateinit var viewModel: ProductsListViewModel

    private lateinit var adapter: ProductsListRecyclerAdapter

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
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)[ProductsListViewModel::class.java]
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        subscribeGetProductRestrictionsDetails()
        viewModel.getFavorites()
        subscribeGetFavoriteList()
        subscribeAddFavoriteResult()
        handleErrors()
        setupSwipeToRefreshLayout()
    }

    private fun setupSwipeToRefreshLayout(){
        binding.swipeLayout.setOnRefreshListener {
            binding.swipeLayout.isRefreshing = false
            viewModel.getFavorites()
        }
    }

    private fun subscribeGetFavoriteList() {

        viewModel.favoriteList.observe(viewLifecycleOwner) {
            when (it) {
                is ViewState.Success -> {
                    binding.pbFavoriteList.isVisible = false
                    adapter.productsList = it.result
                    binding.errorButton.isVisible = false
                    binding.errorImage.isVisible = false
                    binding.errorTxt.isVisible = false
                }
                is ViewState.Loading -> {
                    binding.pbFavoriteList.isVisible = true
                }
                is ViewState.Error -> {
                    binding.pbFavoriteList.isVisible = false
                    if(it.result == "java.net.ConnectException"){
                        binding.errorButton.isVisible = true
                        binding.errorImage.isVisible = true
                        binding.errorTxt.isVisible = true
                    }
                }
            }
        }
    }

    private fun handleErrors(){
        binding.errorButton.setOnClickListener {
            viewModel.getFavorites()
        }
    }

    private fun setupRecyclerView(){
        val rvProductsList = binding.rvProductsList
        rvProductsList.layoutManager = GridLayoutManager(requireContext(), 2)
        adapter = ProductsListRecyclerAdapter()
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
        rvProductsList.adapter = adapter
    }

    private fun subscribeGetProductRestrictionsDetails(){
        viewModel.productDetails.observe(viewLifecycleOwner){
            when (it) {
                is ViewState.Success -> {
                    binding.pbFavoriteList.isVisible = false
                }
                is ViewState.Loading -> {
                    binding.pbFavoriteList.isVisible = true
                }
                is ViewState.Error -> {
                    binding.pbFavoriteList.isVisible = false
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



    private fun subscribeAddFavoriteResult(){
        viewModel.addToFavoriteResult.observe(viewLifecycleOwner){
            when (it) {
                is ViewState.Success -> {
                }
                is ViewState.Loading -> {
                }
                is ViewState.Error -> {
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

}


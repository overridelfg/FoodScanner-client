package kirillrychkov.foodscanner_client.app.presentation.mainpage.barcodescan.history

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
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
import kirillrychkov.foodscanner_client.app.presentation.mainpage.barcodescan.BarcodeScannerViewModel
import kirillrychkov.foodscanner_client.app.presentation.mainpage.products.ProductsListAdapter
import kirillrychkov.foodscanner_client.app.presentation.mainpage.products.ProductsListRecyclerAdapter
import kirillrychkov.foodscanner_client.app.presentation.mainpage.products.ProductsListViewModel
import kirillrychkov.foodscanner_client.databinding.FragmentBarcodeScannerHistoryBinding
import javax.inject.Inject


class BarcodeScannerHistoryFragment : Fragment() {

    private var _binding: FragmentBarcodeScannerHistoryBinding? = null
    private val binding: FragmentBarcodeScannerHistoryBinding
        get() = _binding ?: throw RuntimeException("FragmentBarcodeScannerHistoryBinding == null")


    private lateinit var viewModel: BarcodeScannerViewModel
    private lateinit var viewModelProductDetails: ProductsListViewModel

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
        viewModelProductDetails = ViewModelProvider(requireActivity(), viewModelFactory)[ProductsListViewModel::class.java]
        viewModel = ViewModelProvider(this, viewModelFactory)[BarcodeScannerViewModel::class.java]
        _binding = FragmentBarcodeScannerHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStop() {
        super.onStop()
//        val fragment = parentFragmentManager.findFragmentById(R.id.barcodeScannerHistoryFragment)!!
//        parentFragmentManager.beginTransaction().remove(fragment).commit();
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        subscribeGetProductRestrictionsDetails()
        viewModel.getBarcodeScanHistory()
        subscribeGetHistoryList()
        subscribeAddFavoriteResult()
        setupSwipeToRefreshLayout()
        binding.errorButton.setOnClickListener {
            viewModel.getBarcodeScanHistory()
        }
    }

    private fun setupSwipeToRefreshLayout(){
        binding.swipeLayout.setOnRefreshListener {
            binding.swipeLayout.isRefreshing = false
            viewModel.getBarcodeScanHistory()
        }
    }

    private fun subscribeGetHistoryList() {
        viewModel.barcodeScanHistoryResult.observe(viewLifecycleOwner) {
            when (it) {
                is ViewState.Success -> {
                    binding.pbHistoryList.isVisible = false
                    binding.errorButton.isVisible = false
                    binding.errorImage.isVisible = false
                    binding.errorTxt.isVisible = false
                    adapter.productsList = it.result
                }
                is ViewState.Loading -> {
                    binding.pbHistoryList.isVisible = true
                }
                is ViewState.Error -> {
                    binding.pbHistoryList.isVisible = false
                    if(it.result == "Network is unreachable"){
                        binding.errorButton.isVisible = true
                        binding.errorImage.isVisible = true
                        binding.errorTxt.isVisible = true
                    }
                }
            }

        }
    }

    private fun setupRecyclerView(){
        val rvProductsList = binding.rvProductsList
        rvProductsList.layoutManager = GridLayoutManager(requireContext(), 2)
        adapter = ProductsListRecyclerAdapter()
        adapter.onProductSelectListener = {
            viewModelProductDetails.sendProductDetails(it)
            viewModel.getProductRestrictionsDetails(it.id)
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
        viewModel.productRestrictionDetails.observe(viewLifecycleOwner){
            when (it) {
                is ViewState.Success -> {
                    binding.pbHistoryList.isVisible = false
                }
                is ViewState.Loading -> {
                    binding.pbHistoryList.isVisible = true
                }
                is ViewState.Error -> {
                    binding.pbHistoryList.isVisible = false
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
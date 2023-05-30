package kirillrychkov.foodscanner_client.app.presentation.mainpage.barcodescan.history

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import kirillrychkov.foodscanner_client.R
import kirillrychkov.foodscanner_client.app.presentation.FoodScannerApp
import kirillrychkov.foodscanner_client.app.presentation.ViewModelFactory
import kirillrychkov.foodscanner_client.app.presentation.ViewState
import kirillrychkov.foodscanner_client.app.presentation.mainpage.barcodescan.BarcodeScannerViewModel
import kirillrychkov.foodscanner_client.app.presentation.mainpage.products.ProductsListRecyclerAdapter
import kirillrychkov.foodscanner_client.app.presentation.mainpage.products.ProductsListViewModel
import kirillrychkov.foodscanner_client.databinding.ActivityBarcodeScannerHistoryBinding
import kirillrychkov.foodscanner_client.databinding.ActivityMainBinding
import javax.inject.Inject

class BarcodeScannerHistoryActivity : AppCompatActivity() {

    private var _binding: ActivityBarcodeScannerHistoryBinding? = null
    private val binding: ActivityBarcodeScannerHistoryBinding
        get() = _binding ?: throw RuntimeException("ActivityBarcodeScannerHistoryBinding == null")

    private lateinit var viewModel: BarcodeScannerViewModel
    private lateinit var viewModelProductDetails: ProductsListViewModel

    private lateinit var adapter: ProductsListRecyclerAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        FoodScannerApp.appComponent
    }

    override fun attachBaseContext(newBase: Context?) {
        component.inject(this)
        super.attachBaseContext(newBase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityBarcodeScannerHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModelProductDetails = ViewModelProvider(this, viewModelFactory)[ProductsListViewModel::class.java]
        viewModel = ViewModelProvider(this, viewModelFactory)[BarcodeScannerViewModel::class.java]

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
        viewModel.barcodeScanHistoryResult.observe(this) {
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
                    if(it.result == "java.net.ConnectException"){
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
        rvProductsList.layoutManager = GridLayoutManager(this, 2)
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
        viewModel.productRestrictionDetails.observe(this){
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
                        binding.mainLayout,
                        it.result.toString(),
                        Snackbar.LENGTH_LONG
                    ).setAction("OK") {
                    }.show()
                }
            }
        }
    }



    private fun subscribeAddFavoriteResult(){
        viewModel.addToFavoriteResult.observe(this){
            when (it) {
                is ViewState.Success -> {
                }
                is ViewState.Loading -> {
                }
                is ViewState.Error -> {
                    Snackbar.make(
                        binding.mainLayout,
                        it.result.toString(),
                        Snackbar.LENGTH_LONG
                    ).setAction("OK") {
                    }.show()
                }
            }
        }
    }
}
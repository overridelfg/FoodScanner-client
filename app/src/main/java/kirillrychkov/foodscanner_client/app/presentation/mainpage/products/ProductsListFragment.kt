package kirillrychkov.foodscanner_client.app.presentation.mainpage.products

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kirillrychkov.foodscanner_client.app.domain.entity.Product
import kirillrychkov.foodscanner_client.app.presentation.FoodScannerApp
import kirillrychkov.foodscanner_client.app.presentation.ViewModelFactory
import kirillrychkov.foodscanner_client.app.presentation.ViewState
import kirillrychkov.foodscanner_client.databinding.FragmentProductsListBinding
import javax.inject.Inject


class ProductsListFragment : Fragment() {
    private var _binding: FragmentProductsListBinding? = null
    private val binding: FragmentProductsListBinding
        get() = _binding ?: throw RuntimeException("FragmentProductsListBinding == null")


    private lateinit var viewModel: ProductsListViewModel

    private lateinit var adapter: ProductsListAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy{
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
        viewModel = ViewModelProvider(this, viewModelFactory)[ProductsListViewModel::class.java]
        _binding = FragmentProductsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        viewModel.getProducts()
        subscribeGetProductsList()
    }

    private fun subscribeGetProductsList() {
        viewModel.productsList.observe(viewLifecycleOwner){
            when (it) {
                is ViewState.Success -> {
                    adapter.productsList = it.result
                }
                is ViewState.Loading -> ""
                is ViewState.Error -> {
                    "err"
                }
            }
        }
    }

    private fun setupRecyclerView(){
        val rvProductsList = binding.rvProductsList
        rvProductsList.layoutManager = GridLayoutManager(requireContext(), 2)
        adapter = ProductsListAdapter()
        adapter.onProductSelectListener = {
            val bottomSheetRoot = binding.bottomSheet.bottomSheetRoot
            val mBottomBehavior =
                BottomSheetBehavior.from(bottomSheetRoot)
            mBottomBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            bottomSheetRoot.visibility = View.VISIBLE
            val product = it
            val productWeight = product.Weight.replace("\\s".toRegex(), "")
            binding.bottomSheet.tvProductTitle.text = product.Name + " " + productWeight
            binding.bottomSheet.tvProductIngredients.text =
                "Ингредиенты:" + " " + product.Description
            binding.bottomSheet.tvProteins.text =
                "Белки: " + "\n" + product.Proteins
            binding.bottomSheet.tvFats.text =
                "Жиры" + "\n" + product.Fats
            binding.bottomSheet.tvCarbohydrates.text =
                "Углеводы" + "\n" + product.Carbohydrates
            try{
                Glide.with(requireContext()).load(product.Jpg).into(binding.bottomSheet.ivProductImg)
            }catch(e: Exception) {
                Log.d("FoodEx", e.message.toString())
            }

        }
        rvProductsList.adapter = adapter
    }
}
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
import kirillrychkov.foodscanner_client.app.presentation.mainpage.favorites.FavoritesFragment
import kirillrychkov.foodscanner_client.databinding.FragmentProductsListBinding
import kotlinx.coroutines.delay
import java.net.URLEncoder
import java.nio.charset.StandardCharsets.UTF_8
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
        subscribeGetProductsBySearch()
        bindSearchButton()
        setOnTextChange()
        subscribeAddFavoriteResult()
        setupSwipeToRefreshLayout()
        subscribeGetProductRestrictionsDetails()
        viewModel.getProducts()
        subscribeGetProductsList()
        viewModel.getProductRestrictionsDetails(10371)
    }

    private fun subscribeGetProductsList() {
        viewModel.productsList.observe(viewLifecycleOwner){
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

    private fun subscribeGetProductsBySearch(){
        viewModel.productsSearchList.observe(viewLifecycleOwner){
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

    private fun setupRecyclerView(){
        val rvProductsList = binding.rvProductsList
        rvProductsList.layoutManager = GridLayoutManager(requireContext(), 2)
        adapter = ProductsListAdapter()
        adapter.onProductSelectListener = {
            viewModel.getProductRestrictionsDetails(it.id)
            val bottomSheetRoot = binding.bottomSheet.bottomSheetRoot
            val mBottomBehavior =
                BottomSheetBehavior.from(bottomSheetRoot)
            mBottomBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            bottomSheetRoot.visibility = View.VISIBLE
            fillProductListUI(it)
        }
        adapter.onProductFavoriteClickListener = {
            viewModel.addToFavorite(it)
        }
        rvProductsList.adapter = adapter
    }

    private fun bindSearchButton(){
        binding.etSearchProduct.setOnEditorActionListener { view, actionId, keyEvent ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH){
                val data = binding.etSearchProduct.text.toString()
                viewModel.getProductsBySearch(data)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun setupSwipeToRefreshLayout(){
        binding.swipeLayout.setOnRefreshListener {
            binding.swipeLayout.isRefreshing = false
            binding.etSearchProduct.setText("")
            viewModel.getProducts()
        }
    }

    private fun subscribeGetProductRestrictionsDetails(){
        viewModel.productDetails.observe(viewLifecycleOwner){
            when (it) {
                is ViewState.Success -> {
                    binding.pbProductList.isVisible = false
                    fillProductRestrictionUI(it.result)
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

    private fun subscribeAddFavoriteResult(){
        viewModel.addToFavoriteResult.observe(viewLifecycleOwner){
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

    private fun fillProductRestrictionUI(productRestriction: ProductRestriction){
        if(productRestriction.status == false){
            val productRestrictionsSet = mutableSetOf<String>()
            for(i in 0 until productRestriction.answer.size){
                val el = productRestriction.answer[i].split(':')[1].split(',')
                for(j in 0 until el.size){
                    productRestrictionsSet.add(el[j])
                }
            }

            binding.bottomSheet.tvRestrictedIngredients.text = "Запрещенные игредиенты: " +
                    productRestrictionsSet.joinToString(", ")
            binding.bottomSheet.tvRestrictedIngredients.isVisible = true
            binding.bottomSheet.tvIsRestricted.text = "Продукт не подходит под ваши ограничения"
            binding.bottomSheet.ivInfoIcon.isVisible = true
            binding.bottomSheet.ivIsRestrictedIcon.setBackgroundResource(R.drawable.ic_restricted)
            binding.bottomSheet.layoutIsRestricted.setBackgroundResource(R.drawable.background_restricted_layout)
            binding.bottomSheet.ivInfoIcon.setOnClickListener {
                val bundle = Bundle()
                val answer = ArrayList(productRestriction.answer)
                bundle.putStringArrayList("ANSWER", answer)
                findNavController().navigate(R.id.action_productsListFragment_to_productRestrictionsDetailsFragment, bundle)
            }

        }else{
            binding.bottomSheet.tvRestrictedIngredients.isVisible = false
            binding.bottomSheet.ivInfoIcon.isVisible = false
            binding.bottomSheet.tvIsRestricted.text = "Продукт подходит под ваши ограничения"
            binding.bottomSheet.ivIsRestrictedIcon.setBackgroundResource(R.drawable.ic_check_circle)
            binding.bottomSheet.layoutIsRestricted.setBackgroundResource(R.drawable.background_not_restricted_layout)
        }

    }
    private fun fillProductListUI(product: Product){

        val productWeight = product.Weight.replace("\\s".toRegex(), "")
        binding.bottomSheet.tvProductTitle.text = product.Name + " " + productWeight
        binding.bottomSheet.tvProductIngredients.text =
            "Ингредиенты:" + " " + product.Description
        binding.bottomSheet.tvProteins.text =
            "Белки " + "\n" + product.Proteins
        binding.bottomSheet.tvFats.text =
            "Жиры" + "\n" + product.Fats
        binding.bottomSheet.tvCarbohydrates.text =
            "Углеводы" + "\n" + product.Carbohydrates
        if(product.Jpg.isBlank()){
            Picasso.get().load(R.drawable.no_pictures).into(binding.bottomSheet.ivProductImg);
        }else{
            try{
                Picasso.get().isLoggingEnabled = true
                Picasso.get().load(product.Jpg)
                    .placeholder(R.color.white)
                    .error(R.drawable.no_pictures)
                    .fit()
                    .centerInside().
                    into(binding.bottomSheet.ivProductImg);
            }catch (e: Exception){
                Picasso.get().load(R.drawable.no_pictures).into(binding.bottomSheet.ivProductImg)
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
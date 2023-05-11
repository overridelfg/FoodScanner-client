package kirillrychkov.foodscanner_client.app.presentation.mainpage.products.details

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kirillrychkov.foodscanner_client.R
import kirillrychkov.foodscanner_client.app.domain.entity.Product
import kirillrychkov.foodscanner_client.app.domain.entity.ProductRestriction
import kirillrychkov.foodscanner_client.app.presentation.FoodScannerApp
import kirillrychkov.foodscanner_client.app.presentation.ViewModelFactory
import kirillrychkov.foodscanner_client.app.presentation.ViewState
import kirillrychkov.foodscanner_client.app.presentation.mainpage.products.ProductsListViewModel
import kirillrychkov.foodscanner_client.databinding.FragmentProductDetailsBinding
import kirillrychkov.foodscanner_client.databinding.FragmentProductRestrictionsDetailsBinding
import javax.inject.Inject

class ProductDetailsFragment : Fragment() {

    private var _binding: FragmentProductDetailsBinding? = null
    private val binding: FragmentProductDetailsBinding
        get() = _binding ?: throw RuntimeException("FragmentProductDetailsBinding == null")

    private lateinit var viewModel: ProductsListViewModel

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
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)[ProductsListViewModel::class.java]
        _binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getProductDetailsData()
        subscribeGetProductRestrictionsDetails()
    }

    private fun getProductDetailsData(){
        viewModel.getProductDetails.observe(viewLifecycleOwner){
            fillProductDetailsUI(it)
            viewModel.getProductRestrictionsDetails(it.id)
        }
    }

    private fun subscribeGetProductRestrictionsDetails(){
        viewModel.productDetails.observe(viewLifecycleOwner){
            when (it) {
                is ViewState.Success -> {
                    fillProductRestrictionUI(it.result)
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


    private fun fillProductRestrictionUI(productRestriction: ProductRestriction){
        if(productRestriction.status == false){
            val productRestrictionsSet = mutableSetOf<String>()
            for(i in 0 until productRestriction.answerDiets.size){
                val el = productRestriction.answerDiets[i].split(':')[1].split(',')
                for(j in 0 until el.size){
                    productRestrictionsSet.add(el[j])
                }
            }

            val productRestrictionsAllergensSet = mutableSetOf<String>()
            for(i in 0 until productRestriction.answerAllergens.size){
                val el = productRestriction.answerAllergens[i].split(':')[1].split(',')
                for(j in 0 until el.size){
                    productRestrictionsAllergensSet.add(el[j])
                }
            }
            if(productRestrictionsSet.isEmpty()){
                binding.tvRestrictedIngredients.isVisible = false
            }else{
                binding.tvRestrictedIngredients.text = "Запрещенные игредиенты: " +
                        productRestrictionsSet.joinToString(", ")
                binding.tvRestrictedIngredients.isVisible = true
            }

            if(productRestrictionsAllergensSet.isEmpty()){
                binding.tvRestrictedAllergensIngredients.isVisible = false
            }else{
                binding.tvRestrictedAllergensIngredients.text = "Запрещенные аллергены: " +
                        productRestrictionsAllergensSet.joinToString(", ")
                binding.tvRestrictedAllergensIngredients.isVisible = true
            }

            binding.tvIsRestricted.text = "Продукт не подходит под ваши ограничения"
            binding.ivInfoIcon.isVisible = true
            binding.ivIsRestrictedIcon.setBackgroundResource(R.drawable.ic_restricted)
            binding.layoutIsRestricted.setBackgroundResource(R.drawable.background_restricted_layout)
            binding.ivInfoIcon.setOnClickListener {
                val bundle = Bundle()
                val answerDiets = ArrayList(productRestriction.answerDiets)
                bundle.putStringArrayList("ANSWER_DIETS", answerDiets)
                val answerAllergens = ArrayList(productRestriction.answerAllergens)
                bundle.putStringArrayList("ANSWER_ALLERGENS", answerAllergens)
                findNavController().navigate(R.id.action_productDetailsFragment_to_productRestrictionsDetailsFragment, bundle)
            }

        }else{
            binding.tvRestrictedIngredients.isVisible = false
            binding.tvRestrictedAllergensIngredients.isVisible = false
            binding.ivInfoIcon.isVisible = false
            binding.tvIsRestricted.text = "Продукт подходит под ваши ограничения"
            binding.ivIsRestrictedIcon.setBackgroundResource(R.drawable.ic_check_circle)
            binding.layoutIsRestricted.setBackgroundResource(R.drawable.background_not_restricted_layout)
        }

    }
    private fun fillProductDetailsUI(product: Product){
        val productWeight = product.Weight.replace("\\s".toRegex(), "")
        binding.tvProductTitle.text = product.Name + " " + productWeight
        binding.tvProductIngredients.text =
            "Ингредиенты:" + " " + product.Description
        binding.tvProteins.text =
            "Белки " + "\n" + product.Proteins
        binding.tvFats.text =
            "Жиры" + "\n" + product.Fats
        binding.tvCarbohydrates.text =
            "Углеводы" + "\n" + product.Carbohydrates
        if(product.Jpg.isBlank()){
            Picasso.get().load(R.drawable.no_pictures).into(binding.ivProductImg);
        }else{
            try{
                Picasso.get().isLoggingEnabled = true
                Picasso.get().load(product.Jpg)
                    .placeholder(R.color.white)
                    .error(R.drawable.no_pictures)
                    .fit()
                    .centerInside().
                    into(binding.ivProductImg);
            }catch (e: Exception){
                Picasso.get().load(R.drawable.no_pictures).into(binding.ivProductImg)
            }
        }
    }

}
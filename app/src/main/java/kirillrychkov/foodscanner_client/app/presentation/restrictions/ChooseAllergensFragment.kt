package kirillrychkov.foodscanner_client.app.presentation.restrictions

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import kirillrychkov.foodscanner_client.R
import kirillrychkov.foodscanner_client.app.domain.entity.Allergen
import kirillrychkov.foodscanner_client.app.domain.entity.Diet
import kirillrychkov.foodscanner_client.databinding.FragmentChooseAllergensBinding
import kirillrychkov.foodscanner_client.app.presentation.FoodScannerApp
import kirillrychkov.foodscanner_client.app.presentation.ViewModelFactory
import kirillrychkov.foodscanner_client.app.presentation.ViewState
import kirillrychkov.foodscanner_client.app.presentation.auth.AuthViewModel
import javax.inject.Inject

class ChooseAllergensFragment : Fragment() {

    private lateinit var adapter: ChooseRestrictionsAdapter
    private var selectedAllergens: MutableList<Allergen> = mutableListOf()

    private var _binding: FragmentChooseAllergensBinding? = null
    private val binding: FragmentChooseAllergensBinding
        get() = _binding ?: throw RuntimeException("FragmentChooseAllergensBinding == null")
    private lateinit var viewModel: ChooseRestrictionsViewModel
    private lateinit var authViewModel: AuthViewModel

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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this, viewModelFactory)[ChooseRestrictionsViewModel::class.java]
        authViewModel = ViewModelProvider(requireActivity(), viewModelFactory)[AuthViewModel::class.java]
        _binding = FragmentChooseAllergensBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeDietsList()
        subscribeSelectedAllergens()
        setupRecyclerView()
        setupSwipeToRefreshLayout()
        binding.nextButton.setOnClickListener {
                findNavController().navigate(R.id.action_chooseAllergensFragment_to_registerFragment)
            }
        viewModel.getAllergensList()

        binding.errorButton.setOnClickListener {
            viewModel.getAllergensList()
        }
    }

    private fun subscribeDietsList(){
        viewModel.allergensList.observe(viewLifecycleOwner){
            when (it) {
                is ViewState.Success -> {
                    binding.nextButton.isEnabled = true
                    binding.pbChooseAllergens.isVisible = false
                    binding.errorButton.isVisible = false
                    binding.errorImage.isVisible = false
                    binding.errorTxt.isVisible = false
                    adapter.restrictionsList = it.result
                }
                is ViewState.Loading -> {
                    binding.nextButton.isEnabled = false
                    binding.pbChooseAllergens.isVisible = true
                }
                is ViewState.Error -> {
                    binding.nextButton.isEnabled = false
                    binding.pbChooseAllergens.isVisible = false
                    if(it.result == "Network is unreachable"){
                        binding.errorButton.isVisible = true
                        binding.errorImage.isVisible = true
                        binding.errorTxt.isVisible = true
                    }
                }
            }

        }
    }

//    private fun subscribeUpdateRestrictions(){
//        viewModel.updateRestrictionsResult.observe(viewLifecycleOwner){
//            when (it) {
//                is ViewState.Success -> {
//                    binding.pbChooseAllergens.isVisible = false
//                }
//                is ViewState.Loading -> {
//                    binding.pbChooseAllergens.isVisible = true
//                }
//                is ViewState.Error -> {
//                    binding.pbChooseAllergens.isVisible = false
//                    Snackbar.make(
//                        requireView(),
//                        it.result.toString(),
//                        Snackbar.LENGTH_LONG
//                    ).show()
//                }
//            }
//
//        }
//    }

    private fun setupSwipeToRefreshLayout(){
        binding.swipeLayout.setOnRefreshListener {
            binding.swipeLayout.isRefreshing = false
            viewModel.getAllergensList()
        }
    }

    private fun setupRecyclerView(){
        val rvShopList = binding.rvRestrictionsList
        adapter = ChooseRestrictionsAdapter()
        authViewModel.getSelectedAllergens()
        adapter.onRestrictionCheckListener = {
            selectedAllergens.add(it as Allergen)
            authViewModel.setSelectedAllergens(selectedAllergens)
        }
        adapter.onRestrictionUncheckListener = {
            selectedAllergens.remove(it as Allergen)
            authViewModel.setSelectedAllergens(selectedAllergens)
        }
        adapter.onRestrictionInfoListener = {
            val bundle = Bundle()
            bundle.putString("HEADER", "Информация об аллергене")
            bundle.putString("TITLE", it.title)
            bundle.putString("DESCRIPTION", it.description)
            findNavController().navigate(R.id.action_chooseAllergensFragment_to_restrictionInfoFragment, bundle)
        }

        rvShopList.adapter = adapter
    }

    private fun subscribeSelectedAllergens(){
        authViewModel.selectedAllergensList.observe(viewLifecycleOwner){
            if(it.isEmpty()){
                binding.nextButton.text = "Пропустить"
            }else{
                binding.nextButton.text = "Далее"
                adapter.selectedAllergens = it
                selectedAllergens = it
            }
        }
    }



}
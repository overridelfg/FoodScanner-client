package kirillrychkov.foodscanner_client.presentation.presentation.restrictions

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kirillrychkov.foodscanner_client.R
import kirillrychkov.foodscanner_client.databinding.FragmentChooseDietsBinding
import kirillrychkov.foodscanner_client.presentation.domain.entity.Diet
import kirillrychkov.foodscanner_client.presentation.domain.repository.AuthRepository
import kirillrychkov.foodscanner_client.presentation.presentation.FoodScannerApp
import kirillrychkov.foodscanner_client.presentation.presentation.MainActivity
import kirillrychkov.foodscanner_client.presentation.presentation.ViewModelFactory
import kirillrychkov.foodscanner_client.presentation.presentation.ViewState
import javax.inject.Inject


class ChooseDietsFragment : Fragment() {

    private var _binding: FragmentChooseDietsBinding? = null
    private val binding: FragmentChooseDietsBinding
        get() = _binding ?: throw RuntimeException("FragmentChooseDietsBinding == null")
    private lateinit var viewModel: ChooseRestrictionsViewModel

    private lateinit var adapter: ChooseRestrictionsAdapter
    private val selectedDiets: MutableList<Diet> = mutableListOf()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var authRepository: AuthRepository

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
        _binding = FragmentChooseDietsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeDietsList()
        setupRecyclerView()
        subscribeIsSelectedDiets()
        viewModel.getDietsList()
        launchChooseAllergensFragment()
        if(selectedDiets.isEmpty()){
            viewModel.resetSelectedDietsEmpty()
        }
    }

    private fun subscribeDietsList(){
        viewModel.dietsList.observe(viewLifecycleOwner){
            when (it) {
                is ViewState.Success -> {
                    adapter.restrictionsList = it.result
                }
                is ViewState.Loading -> ""
                is ViewState.Error -> {
                    ""
                }
            }
        }
    }

    private fun launchChooseAllergensFragment(){
        binding.nextButton.setOnClickListener {
            Log.d("Diets", selectedDiets.toString())
            findNavController().navigate(R.id.action_chooseDietsFragment_to_chooseAllergensFragment)
        }
    }

    private fun setupRecyclerView(){
        val rvShopList = binding.rvRestrictionsList
        adapter = ChooseRestrictionsAdapter()
        adapter.onRestrictionCheckListener = {
            selectedDiets.add(it as Diet)
            if(selectedDiets.size == 1){
                viewModel.resetSelectedDietsNotEmpty()
            }
        }
        adapter.onRestrictionUncheckListener = {
            selectedDiets.remove(it as Diet)
            if(selectedDiets.isEmpty()){
                viewModel.resetSelectedDietsEmpty()
            }
        }
        rvShopList.adapter = adapter
    }

    private fun subscribeIsSelectedDiets(){
        viewModel.isSelectedDiets.observe(viewLifecycleOwner){
            if(it){
                binding.nextButton.text = "Next"
            }else{
                binding.nextButton.text = "Skip"
            }
        }
    }
}
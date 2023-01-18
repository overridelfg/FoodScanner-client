package kirillrychkov.foodscanner_client.presentation.presentation.restrictions

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import kirillrychkov.foodscanner_client.R
import kirillrychkov.foodscanner_client.databinding.FragmentChooseDietsBinding
import kirillrychkov.foodscanner_client.presentation.domain.entity.Diet
import kirillrychkov.foodscanner_client.presentation.presentation.ViewState
import kirillrychkov.foodscanner_client.presentation.presentation.base.BaseFragment


class ChooseDietsFragment : BaseFragment<FragmentChooseDietsBinding, ChooseRestrictionsViewModel>(
    FragmentChooseDietsBinding::inflate
) {

    private lateinit var adapter: ChooseRestrictionsAdapter
    private val selectedDiets: MutableList<Diet> = mutableListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeDietsList()
        setupRecyclerView()
        viewModel.getDietsList()
        launchChooseAllergensFragment()
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
            findNavController().navigate(R.id.action_chooseDietsFragment_to_chooseAllergensFragment)
        }
    }

    private fun setupRecyclerView(){
        val rvShopList = binding.rvRestrictionsList
        adapter = ChooseRestrictionsAdapter()
        adapter.onRestrictionCheckListener = {
            selectedDiets.add(it as Diet)
        }
        adapter.onRestrictionUncheckListener = {
            selectedDiets.remove(it as Diet)
        }
        rvShopList.adapter = adapter
    }

    override fun getViewModel(): Class<ChooseRestrictionsViewModel> {
        return ChooseRestrictionsViewModel::class.java
    }

}
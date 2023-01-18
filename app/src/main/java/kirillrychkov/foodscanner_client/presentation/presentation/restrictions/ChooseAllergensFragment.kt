package kirillrychkov.foodscanner_client.presentation.presentation.restrictions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kirillrychkov.foodscanner_client.R
import kirillrychkov.foodscanner_client.databinding.FragmentChooseAllergensBinding
import kirillrychkov.foodscanner_client.databinding.FragmentChooseDietsBinding
import kirillrychkov.foodscanner_client.presentation.presentation.base.BaseFragment

class ChooseAllergensFragment : BaseFragment<FragmentChooseAllergensBinding, ChooseRestrictionsViewModel>(
    FragmentChooseAllergensBinding::inflate
) {

    private lateinit var adapter: ChooseRestrictionsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeDietsList()
        setupRecyclerView()
    }

    private fun subscribeDietsList(){
        viewModel.allergensList.observe(viewLifecycleOwner){
            adapter.restrictionsList = it
        }
    }

    private fun setupRecyclerView(){
        val rvShopList = binding.rvRestrictionsList
        adapter = ChooseRestrictionsAdapter()
        rvShopList.adapter = adapter
    }

    override fun getViewModel(): Class<ChooseRestrictionsViewModel> {
        return ChooseRestrictionsViewModel::class.java
    }

}
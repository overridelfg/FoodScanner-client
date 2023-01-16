package kirillrychkov.foodscanner_client.presentation.presentation.restrictions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import kirillrychkov.foodscanner_client.R
import kirillrychkov.foodscanner_client.databinding.FragmentChooseDietsBinding
import kirillrychkov.foodscanner_client.presentation.presentation.auth.AuthViewModel
import kirillrychkov.foodscanner_client.presentation.presentation.base.BaseFragment


class ChooseDietsFragment : BaseFragment<FragmentChooseDietsBinding, AuthViewModel>(
    FragmentChooseDietsBinding::inflate
) {

    private lateinit var adapter: ChooseRestrictionsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView(){
        val rvShopList = binding.rvRestrictionsList
        adapter = ChooseRestrictionsAdapter()
        adapter.restrictionsList.add("adad")
        adapter.restrictionsList.add("adad")
        adapter.restrictionsList.add("adad")
        adapter.restrictionsList.add("adad")
        adapter.restrictionsList.add("adad")
        adapter.restrictionsList.add("adad")
        adapter.restrictionsList.add("adad")
        adapter.restrictionsList.add("adad")
        adapter.restrictionsList.add("adad")
        adapter.restrictionsList.add("adad")
        adapter.restrictionsList.add("adad")
        adapter.restrictionsList.add("adad")
        adapter.restrictionsList.add("adad")

        rvShopList.adapter = adapter
    }

    override fun getViewModel(): Class<AuthViewModel> {
        return AuthViewModel::class.java
    }

}
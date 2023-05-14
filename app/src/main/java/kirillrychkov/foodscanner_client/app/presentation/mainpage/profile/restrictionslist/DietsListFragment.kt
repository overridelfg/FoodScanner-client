package kirillrychkov.foodscanner_client.app.presentation.mainpage.profile.restrictionslist

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kirillrychkov.foodscanner_client.R
import kirillrychkov.foodscanner_client.app.presentation.FoodScannerApp
import kirillrychkov.foodscanner_client.app.presentation.ViewModelFactory
import kirillrychkov.foodscanner_client.app.presentation.ViewState
import kirillrychkov.foodscanner_client.app.presentation.mainpage.profile.ProfileViewModel
import kirillrychkov.foodscanner_client.databinding.FragmentAllergensListBinding
import kirillrychkov.foodscanner_client.databinding.FragmentDietsListBinding
import javax.inject.Inject


class DietsListFragment : Fragment() {

    private var _binding: FragmentDietsListBinding? = null
    private val binding: FragmentDietsListBinding
        get() = _binding ?: throw RuntimeException("FragmentDietsListBinding == null")

    private lateinit var adapter: ProfileRestrictionsAdapter
    private lateinit var viewModel: ProfileViewModel

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
        viewModel = ViewModelProvider(this, viewModelFactory)[ProfileViewModel::class.java]
        _binding = FragmentDietsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecyclerView()
        viewModel.getUserDiets()
        subscribeDietsList()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getUserDiets()
    }

    private fun setupRecyclerView(){
        val rvShopList = binding.rvDietsList
        adapter = ProfileRestrictionsAdapter()
        adapter.onRestrictionInfoListener = {
            val bundle = Bundle()
            bundle.putString("HEADER", "Информация о диете")
            bundle.putString("TITLE", it.title)
            bundle.putString("DESCRIPTION", it.description)
            findNavController().navigate(R.id.action_profileFragment_to_restrictionInfoFragment2, bundle)
        }
        rvShopList.adapter = adapter
    }

    private fun subscribeDietsList(){
        viewModel.userDiets.observe(viewLifecycleOwner){
            when (it) {
                is ViewState.Success -> {
                    binding.pbUserDiets.isVisible = false
                    adapter.restrictionsList = it.result
                }
                is ViewState.Loading -> {
                    binding.pbUserDiets.isVisible = true
                }
                is ViewState.Error -> {
                    binding.pbUserDiets.isVisible = false
                    Snackbar.make(
                        requireView(),
                        it.result.toString(),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

}
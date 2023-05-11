package kirillrychkov.foodscanner_client.app.presentation.mainpage.profile

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import kirillrychkov.foodscanner_client.R
import kirillrychkov.foodscanner_client.app.presentation.FoodScannerApp
import kirillrychkov.foodscanner_client.app.presentation.ViewModelFactory
import kirillrychkov.foodscanner_client.app.presentation.ViewState
import kirillrychkov.foodscanner_client.databinding.FragmentAllergensListBinding
import kirillrychkov.foodscanner_client.databinding.FragmentDietsListBinding
import javax.inject.Inject


class AllergensListFragment : Fragment() {
    private var _binding: FragmentAllergensListBinding? = null
    private val binding: FragmentAllergensListBinding
        get() = _binding ?: throw RuntimeException("FragmentAllergensListBinding == null")

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
        _binding = FragmentAllergensListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecyclerView()
        subscribeAllergensList()
        viewModel.getUserAllergens()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupRecyclerView(){
        val rvShopList = binding.rvAllergensList
        adapter = ProfileRestrictionsAdapter()
        rvShopList.adapter = adapter
    }

    private fun subscribeAllergensList(){
        viewModel.userAllergens.observe(viewLifecycleOwner){
            when (it) {
                is ViewState.Success -> {
                    binding.pbUserAllergens.isVisible = false
                    adapter.restrictionsList = it.result
                }
                is ViewState.Loading -> {
                    binding.pbUserAllergens.isVisible = true
                }
                is ViewState.Error -> {
                    binding.pbUserAllergens.isVisible = false
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
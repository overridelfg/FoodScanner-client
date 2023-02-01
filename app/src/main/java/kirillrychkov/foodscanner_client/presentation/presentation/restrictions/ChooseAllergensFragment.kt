package kirillrychkov.foodscanner_client.presentation.presentation.restrictions

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kirillrychkov.foodscanner_client.databinding.FragmentChooseAllergensBinding
import kirillrychkov.foodscanner_client.presentation.presentation.FoodScannerApp
import kirillrychkov.foodscanner_client.presentation.presentation.ViewModelFactory
import kirillrychkov.foodscanner_client.presentation.presentation.ViewState
import javax.inject.Inject

class ChooseAllergensFragment : Fragment() {

    private lateinit var adapter: ChooseRestrictionsAdapter

    private var _binding: FragmentChooseAllergensBinding? = null
    private val binding: FragmentChooseAllergensBinding
        get() = _binding ?: throw RuntimeException("FragmentChooseAllergensBinding == null")
    private lateinit var viewModel: ChooseRestrictionsViewModel

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
        _binding = FragmentChooseAllergensBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeDietsList()
        setupRecyclerView()
        viewModel.getAllergensList()
    }

    private fun subscribeDietsList(){
        viewModel.allergensList.observe(viewLifecycleOwner){
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

    private fun setupRecyclerView(){
        val rvShopList = binding.rvRestrictionsList
        adapter = ChooseRestrictionsAdapter()
        rvShopList.adapter = adapter
    }
}
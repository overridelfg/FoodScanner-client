package kirillrychkov.foodscanner_client.app.presentation.restrictions

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import kirillrychkov.foodscanner_client.R
import kirillrychkov.foodscanner_client.databinding.FragmentChooseDietsBinding
import kirillrychkov.foodscanner_client.app.domain.entity.Diet
import kirillrychkov.foodscanner_client.app.domain.repository.AuthRepository
import kirillrychkov.foodscanner_client.app.presentation.FoodScannerApp
import kirillrychkov.foodscanner_client.app.presentation.ViewModelFactory
import kirillrychkov.foodscanner_client.app.presentation.ViewState
import javax.inject.Inject


class ChooseDietsFragment : Fragment() {

    private var _binding: FragmentChooseDietsBinding? = null
    private val binding: FragmentChooseDietsBinding
        get() = _binding ?: throw RuntimeException("FragmentChooseDietsBinding == null")
    private lateinit var viewModel: ChooseRestrictionsViewModel

    private lateinit var adapter: ChooseRestrictionsAdapter
    private var selectedDiets: MutableList<Diet> = mutableListOf()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var authRepository: AuthRepository

    private val component by lazy{
        FoodScannerApp.appComponent
    }

    override fun onAttach(context: Context) {
        component.inject(this)
        handleOnBackPressed()
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
        viewModel.getDietsList()
        subscribeSelectedDiets()
        setupRecyclerView()
        setupSwipeToRefreshLayout()
        launchChooseAllergensFragment()
    }

    private fun subscribeDietsList(){
        viewModel.dietsList.observe(viewLifecycleOwner){
            when (it) {
                is ViewState.Success -> {
                    binding.pbChooseDiets.isVisible = false
                    binding.nextButton.isEnabled = true
                    adapter.restrictionsList = it.result
                }
                is ViewState.Loading -> {
                    binding.nextButton.isEnabled = false
                    binding.pbChooseDiets.isVisible = true
                }
                is ViewState.Error -> {
                    binding.nextButton.isEnabled = false
                    binding.pbChooseDiets.isVisible = false
                    Snackbar.make(
                        requireView(),
                        it.result.toString(),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun setupSwipeToRefreshLayout(){
        binding.swipeLayout.setOnRefreshListener {
            binding.swipeLayout.isRefreshing = false
            viewModel.getDietsList()
        }
    }

    private fun launchChooseAllergensFragment(){
        binding.nextButton.setOnClickListener {
            viewModel.postSelectedDiets(selectedDiets)
            val isUpdate: Boolean? = arguments?.getBoolean("UPDATE_RESTRICTIONS")
            if(isUpdate != null){
                val bundle = Bundle()
                bundle.putBoolean("UPDATE_RESTRICTIONS", isUpdate)
                findNavController().navigate(R.id.action_chooseDietsFragmentUpdate_to_chooseAllergensFragmentUpdate, bundle)
            }else{
                findNavController().navigate(R.id.action_chooseDietsFragment_to_chooseAllergensFragment)
            }
        }
    }

    private fun setupRecyclerView(){
        val rvShopList = binding.rvRestrictionsList
        adapter = ChooseRestrictionsAdapter()
        viewModel.getSelectedDiets()
        adapter.onRestrictionCheckListener = {
            selectedDiets.add(it as Diet)
        }
        adapter.onRestrictionUncheckListener = {
            selectedDiets.remove(it as Diet)
        }

        adapter.onRestrictionInfoListener = {
            val bundle = Bundle()
            bundle.putString("HEADER", "Информация о диете")
            bundle.putString("TITLE", it.title)
            bundle.putString("DESCRIPTION", it.description)
            findNavController().navigate(R.id.action_chooseDietsFragment_to_restrictionInfoFragment, bundle)
        }
        rvShopList.adapter = adapter
    }

    private fun subscribeSelectedDiets(){
        viewModel.selectedDietsList.observe(viewLifecycleOwner){
            adapter.selectedDiets = it
            selectedDiets = it
        }
    }

    private fun handleOnBackPressed(){
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true)
            {
                override fun handleOnBackPressed() {
                    val isUpdate: Boolean? = arguments?.getBoolean("UPDATE_RESTRICTIONS")
                    if(isUpdate != null){
                        findNavController().navigate(R.id.action_chooseDietsFragmentUpdate_to_profileFragment)
                    }else{
                        viewModel.removeSelectedRestrictions()
                        findNavController().navigate(R.id.action_chooseDietsFragment_to_loginFragment)
                    }
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            callback
        )
    }

}
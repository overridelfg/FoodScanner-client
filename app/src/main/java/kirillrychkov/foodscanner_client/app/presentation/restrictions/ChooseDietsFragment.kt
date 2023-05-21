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
import kirillrychkov.foodscanner_client.app.presentation.auth.AuthViewModel
import javax.inject.Inject


class ChooseDietsFragment : Fragment() {

    private var _binding: FragmentChooseDietsBinding? = null
    private val binding: FragmentChooseDietsBinding
        get() = _binding ?: throw RuntimeException("FragmentChooseDietsBinding == null")
    private lateinit var viewModel: ChooseRestrictionsViewModel
    private lateinit var authViewModel: AuthViewModel

    private lateinit var adapter: ChooseRestrictionsAdapter
    private var selectedDiets: MutableList<Diet> = mutableListOf()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var authRepository: AuthRepository

    private val component by lazy {
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
        viewModel =
            ViewModelProvider(this, viewModelFactory)[ChooseRestrictionsViewModel::class.java]
        authViewModel =
            ViewModelProvider(requireActivity(), viewModelFactory)[AuthViewModel::class.java]
        _binding = FragmentChooseDietsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeDietsList()
        viewModel.getDietsList()
        setupRecyclerView()
        setupSwipeToRefreshLayout()
        subscribeGetSelectedDiets()
        launchChooseAllergensFragment()
        binding.errorButton.setOnClickListener {
            viewModel.getDietsList()
        }
    }

    private fun subscribeDietsList() {
        viewModel.dietsList.observe(viewLifecycleOwner) {
            when (it) {
                is ViewState.Success -> {
                    binding.pbChooseDiets.isVisible = false
                    binding.nextButton.isEnabled = true
                    adapter.restrictionsList = it.result
                    binding.errorButton.isVisible = false
                    binding.errorImage.isVisible = false
                    binding.errorTxt.isVisible = false
                }
                is ViewState.Loading -> {
                    binding.nextButton.isEnabled = false
                    binding.pbChooseDiets.isVisible = true
                }
                is ViewState.Error -> {
                    binding.nextButton.isEnabled = false
                    binding.pbChooseDiets.isVisible = false
                    if(it.result == "Network is unreachable"){
                        binding.errorButton.isVisible = true
                        binding.errorImage.isVisible = true
                        binding.errorTxt.isVisible = true
                    }
                }
            }
        }
    }

    private fun setupSwipeToRefreshLayout() {
        binding.swipeLayout.setOnRefreshListener {
            binding.swipeLayout.isRefreshing = false
            viewModel.getDietsList()
        }
    }

    private fun launchChooseAllergensFragment() {
        binding.nextButton.setOnClickListener {
            findNavController().navigate(R.id.action_chooseDietsFragment_to_chooseAllergensFragment)
        }
    }

    private fun subscribeGetSelectedDiets() {
        authViewModel.selectedDietsList.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.nextButton.text = "Пропустить"
            } else {
                binding.nextButton.text = "Далее"
                adapter.selectedDiets = it
                selectedDiets = it
            }

        }
    }

    private fun setupRecyclerView() {
        val rvShopList = binding.rvRestrictionsList
        adapter = ChooseRestrictionsAdapter()
        authViewModel.getSelectedDiets()
        adapter.onRestrictionCheckListener = {
            selectedDiets.add(it as Diet)
            authViewModel.setSelectedDiets(selectedDiets)
        }
        adapter.onRestrictionUncheckListener = {
            selectedDiets.remove(it as Diet)
            authViewModel.setSelectedDiets(selectedDiets)
        }

        adapter.onRestrictionInfoListener = {
            val bundle = Bundle()
            bundle.putString("HEADER", "Информация о диете")
            bundle.putString("TITLE", it.title)
            bundle.putString("DESCRIPTION", it.description)
            findNavController().navigate(
                R.id.action_chooseDietsFragment_to_restrictionInfoFragment,
                bundle
            )
        }
        rvShopList.adapter = adapter
    }

}
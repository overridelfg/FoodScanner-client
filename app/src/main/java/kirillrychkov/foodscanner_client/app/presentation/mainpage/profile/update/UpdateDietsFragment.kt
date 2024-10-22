package kirillrychkov.foodscanner_client.app.presentation.mainpage.profile.update

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kirillrychkov.foodscanner_client.R
import kirillrychkov.foodscanner_client.app.domain.entity.Allergen
import kirillrychkov.foodscanner_client.app.domain.entity.Diet
import kirillrychkov.foodscanner_client.app.presentation.FoodScannerApp
import kirillrychkov.foodscanner_client.app.presentation.ViewModelFactory
import kirillrychkov.foodscanner_client.app.presentation.ViewState
import kirillrychkov.foodscanner_client.app.presentation.auth.AuthViewModel
import kirillrychkov.foodscanner_client.app.presentation.restrictions.ChooseRestrictionsAdapter
import kirillrychkov.foodscanner_client.app.presentation.restrictions.ChooseRestrictionsViewModel
import kirillrychkov.foodscanner_client.app.presentation.restrictions.RestrictionInfoFragment
import kirillrychkov.foodscanner_client.databinding.FragmentChooseDietsBinding
import kirillrychkov.foodscanner_client.databinding.FragmentUpdateDietsBinding
import javax.inject.Inject

class UpdateDietsFragment : Fragment() {
    private var selectedDiets: ArrayList<Diet> = arrayListOf()
    private var selectedAllergens: ArrayList<Allergen> = arrayListOf()
    private var currentSelectedDiets: MutableList<Diet> = mutableListOf()

    private var _binding: FragmentUpdateDietsBinding? = null
    private val binding: FragmentUpdateDietsBinding
        get() = _binding ?: throw RuntimeException("FragmentUpdateDietsBinding == null")

    private lateinit var viewModel: UpdateRestrictionsViewModel

    private lateinit var adapter: ChooseRestrictionsAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        FoodScannerApp.appComponent
    }

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        selectedDiets = requireArguments().getParcelableArrayList("UPDATE_DIETS")
            ?: throw RuntimeException("Selected diets could not be a null")

        selectedAllergens = requireArguments().getParcelableArrayList("UPDATE_ALLERGENS")
            ?: throw RuntimeException("Selected allergens could not be a null")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel =
            ViewModelProvider(
                requireActivity(),
                viewModelFactory
            )[UpdateRestrictionsViewModel::class.java]
        _binding = FragmentUpdateDietsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setSelectedDiets(selectedDiets)
        subscribeDietsList()
        viewModel.getDietsList()
        setupRecyclerView()
        setupSwipeToRefreshLayout()
        subscribeGetSelectedDiets()
        val fragment = UpdateAllergensFragment.newInstance(selectedAllergens)
        binding.nextButton.setOnClickListener {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .addToBackStack("update_restrictions")
                .add(R.id.update_restrictions_container, fragment)
                .commit()
        }
    }

    private fun subscribeDietsList() {
        viewModel.dietsList.observe(viewLifecycleOwner) {
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

    private fun setupSwipeToRefreshLayout() {
        binding.swipeLayout.setOnRefreshListener {
            binding.swipeLayout.isRefreshing = false
            viewModel.getDietsList()
        }
    }

    private fun subscribeGetSelectedDiets() {
        viewModel.selectedDietsList.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.nextButton.text = "Пропустить"
            } else {
                binding.nextButton.text = "Далее"
                adapter.selectedDiets = it
                currentSelectedDiets = it
            }

        }
    }

    private fun setupRecyclerView() {
        val rvShopList = binding.rvRestrictionsList
        adapter = ChooseRestrictionsAdapter()
        viewModel.getSelectedDiets()
        adapter.onRestrictionCheckListener = {
            selectedDiets.add(it as Diet)
            viewModel.setSelectedDiets(selectedDiets)
        }
        adapter.onRestrictionUncheckListener = {
            selectedDiets.remove(it as Diet)
            viewModel.setSelectedDiets(selectedDiets)
        }

        adapter.onRestrictionInfoListener = {
            val bundle = Bundle()
            bundle.putString("HEADER", "Информация о диете")
            bundle.putString("TITLE", it.title)
            bundle.putString("DESCRIPTION", it.description)
            val fragment = RestrictionInfoFragment()
            fragment.arguments = bundle
            fragment.show(requireActivity().supportFragmentManager, "Dialog")
        }
        rvShopList.adapter = adapter
    }

//    private fun onBackPressed() {
//        requireActivity()
//            .onBackPressedDispatcher
//            .addCallback(this, object : OnBackPressedCallback(true) {
//                override fun handleOnBackPressed() {
//                    requireActivity().finish()
//                }
//            }
//        )
//    }

    companion object {
        const val ARG_SELECTED_DIETS = "SELECTED_DIETS"
        const val ARG_SELECTED_ALLERGENS = "SELECTED_ALLERGENS"
        @JvmStatic
        fun newInstance(selectedDiets: ArrayList<Diet>, selectedAllergens: ArrayList<Allergen>) =
            UpdateDietsFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(ARG_SELECTED_DIETS, selectedDiets)
                    putParcelableArrayList(ARG_SELECTED_ALLERGENS, selectedAllergens)
                }
            }
    }
}
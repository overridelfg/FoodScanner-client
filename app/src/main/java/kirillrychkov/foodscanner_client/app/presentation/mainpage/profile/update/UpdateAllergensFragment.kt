package kirillrychkov.foodscanner_client.app.presentation.mainpage.profile.update

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import kirillrychkov.foodscanner_client.R
import kirillrychkov.foodscanner_client.app.domain.entity.Allergen
import kirillrychkov.foodscanner_client.app.domain.entity.Diet
import kirillrychkov.foodscanner_client.app.presentation.FoodScannerApp
import kirillrychkov.foodscanner_client.app.presentation.MainActivity
import kirillrychkov.foodscanner_client.app.presentation.ViewModelFactory
import kirillrychkov.foodscanner_client.app.presentation.ViewState
import kirillrychkov.foodscanner_client.app.presentation.restrictions.ChooseRestrictionsAdapter
import kirillrychkov.foodscanner_client.app.presentation.restrictions.RestrictionInfoFragment
import kirillrychkov.foodscanner_client.databinding.FragmentUpdateAllergensBinding
import kirillrychkov.foodscanner_client.databinding.FragmentUpdateDietsBinding
import javax.inject.Inject

private const val ARG_SELECTED_ALLERGENS = "SELECTED_ALLERGENS"

class UpdateAllergensFragment : Fragment() {
    private var selectedDiets: MutableList<Diet> = mutableListOf()
    private var currentSelectedAllergens: MutableList<Allergen> = mutableListOf()
    private var selectedAllergens: ArrayList<Allergen> = arrayListOf()

    private var _binding: FragmentUpdateAllergensBinding? = null
    private val binding: FragmentUpdateAllergensBinding
        get() = _binding ?: throw RuntimeException("FragmentUpdateAllergensBinding == null")

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
        selectedAllergens = requireArguments().getParcelableArrayList(ARG_SELECTED_ALLERGENS)!!
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
        _binding = FragmentUpdateAllergensBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selectedDiets = viewModel.getSelectedDiets() ?: throw RuntimeException("selected diets could not be a null")
        viewModel.setSelectedAllergens(selectedAllergens)
        subscribeAllergensList()
        viewModel.getAllergensList()
        setupRecyclerView()
        subscribeUpdateData()
        setupSwipeToRefreshLayout()
        subscribeGetSelectedAllergens()
        binding.nextButton.setOnClickListener {
            selectedDiets.sortBy {
                it.id
            }
            currentSelectedAllergens.sortBy {
                it.id
            }
            viewModel.updateRestrictions(selectedDiets, currentSelectedAllergens)

        }
    }

    private fun subscribeUpdateData(){
        viewModel.updateRestrictionsResult.observe(viewLifecycleOwner) {
            when (it) {
                is ViewState.Success -> {
                    requireActivity().finish()
                    binding.pbChooseAllergens.isVisible = false
                }
                is ViewState.Loading -> {
                    binding.pbChooseAllergens.isVisible = true
                }
                is ViewState.Error -> {
                    binding.pbChooseAllergens.isVisible = false
                    Snackbar.make(
                        requireView(),
                        it.result.toString(),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun subscribeAllergensList() {
        viewModel.allergensList.observe(viewLifecycleOwner) {
            when (it) {
                is ViewState.Success -> {
                    binding.pbChooseAllergens.isVisible = false
                    binding.nextButton.isEnabled = true
                    adapter.restrictionsList = it.result
                }
                is ViewState.Loading -> {
                    binding.nextButton.isEnabled = false
                    binding.pbChooseAllergens.isVisible = true
                }
                is ViewState.Error -> {
                    binding.nextButton.isEnabled = false
                    binding.pbChooseAllergens.isVisible = false
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
            viewModel.getAllergensList()
        }
    }

    private fun subscribeGetSelectedAllergens() {
        viewModel.selectedAllergensList.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.nextButton.text = "Пропустить"
            } else {
                binding.nextButton.text = "Далее"
                adapter.selectedAllergens = it
                currentSelectedAllergens = it
            }

        }
    }

    private fun setupRecyclerView() {
        val rvShopList = binding.rvRestrictionsList
        adapter = ChooseRestrictionsAdapter()
        viewModel.getSelectedAllergens()
        adapter.onRestrictionCheckListener = {
            currentSelectedAllergens.add(it as Allergen)
            viewModel.setSelectedAllergens(currentSelectedAllergens)
        }
        adapter.onRestrictionUncheckListener = {
            currentSelectedAllergens.remove(it as Allergen)
            viewModel.setSelectedAllergens(currentSelectedAllergens)
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

    companion object {
        @JvmStatic
        fun newInstance(selectedAllergens: ArrayList<Allergen>) =
            UpdateAllergensFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(ARG_SELECTED_ALLERGENS, selectedAllergens)
                }
            }
    }
}
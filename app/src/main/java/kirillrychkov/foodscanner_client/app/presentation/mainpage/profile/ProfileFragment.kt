package kirillrychkov.foodscanner_client.app.presentation.mainpage.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import kirillrychkov.foodscanner_client.R
import kirillrychkov.foodscanner_client.app.domain.entity.Allergen
import kirillrychkov.foodscanner_client.app.domain.entity.Diet
import kirillrychkov.foodscanner_client.app.domain.repository.AuthRepository
import kirillrychkov.foodscanner_client.app.presentation.FoodScannerApp
import kirillrychkov.foodscanner_client.app.presentation.ViewModelFactory
import kirillrychkov.foodscanner_client.app.presentation.ViewState
import kirillrychkov.foodscanner_client.app.presentation.auth.AuthActivity
import kirillrychkov.foodscanner_client.app.presentation.mainpage.profile.restrictionslist.ProfileRestrictionsListPageAdapter
import kirillrychkov.foodscanner_client.app.presentation.mainpage.profile.update.UpdateRestrictionsActivity
import kirillrychkov.foodscanner_client.databinding.FragmentProfileBinding
import javax.inject.Inject


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding
        get() = _binding ?: throw RuntimeException("FragmentProfileBinding == null")

    private lateinit var viewModel: ProfileViewModel

    @Inject
    lateinit var authRepository: AuthRepository
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var pagerAdapter : ProfileRestrictionsListPageAdapter

    private var selectedDiets = arrayListOf<Diet>()
    private var selectedAllergens = arrayListOf<Allergen>()


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
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)[ProfileViewModel::class.java]
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.getUserDiets()
        viewModel.getUserAllergens()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bindTabLayout()
        setUserData()
        bindLogoutButton()
        viewModel.getUserDiets()
        viewModel.getUserAllergens()
        subscribeUserDietsList()
        subscribeAllergensList()
        binding.updateRestrictionsButton.setOnClickListener {
            val intent = Intent(requireActivity(), UpdateRestrictionsActivity::class.java)
            intent.putParcelableArrayListExtra("UPDATE_DIETS", selectedDiets)
            intent.putParcelableArrayListExtra("UPDATE_ALLERGENS", selectedAllergens)
            startActivity(intent)
        }

        super.onViewCreated(view, savedInstanceState)
    }


    private fun subscribeUserDietsList(){
        viewModel.userDiets.observe(viewLifecycleOwner){
            when (it) {
                is ViewState.Success -> {
                    selectedDiets = arrayListOf()
                    for(diet in it.result){
                        selectedDiets.add(diet)
                    }
                }
                is ViewState.Loading -> {
                }
                is ViewState.Error -> {
                    Snackbar.make(
                        requireView(),
                        it.result.toString(),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun subscribeAllergensList(){
        viewModel.userAllergens.observe(viewLifecycleOwner){
            when (it) {
                is ViewState.Success -> {
                    selectedAllergens = arrayListOf()
                    for(allergen in it.result){
                        selectedAllergens.add(allergen)
                    }
                }
                is ViewState.Loading -> {
                }
                is ViewState.Error -> {
                    Snackbar.make(
                        requireView(),
                        it.result.toString(),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun bindTabLayout(){
        pagerAdapter = ProfileRestrictionsListPageAdapter(this)
        binding.viewPagerProfile.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayoutProfile, binding.viewPagerProfile){tab, position ->
            when(position){
                0 -> tab.text = "Диеты"
                1 -> tab.text = "Аллергены"
            }
        }.attach()
    }

    private fun setUserData(){
        binding.tvEmail.text = viewModel.getUser.value!!.email
        binding.tvUsername.text = viewModel.getUser.value!!.name
    }

    private fun bindLogoutButton(){
        binding.logoutButton.setOnClickListener {
            authRepository.logout()
            val intent = AuthActivity.newIntentAuthActivity(requireContext())
            startActivity(intent)
        }
    }
}
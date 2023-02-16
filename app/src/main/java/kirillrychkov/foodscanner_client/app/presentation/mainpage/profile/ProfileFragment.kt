package kirillrychkov.foodscanner_client.app.presentation.mainpage.profile

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import kirillrychkov.foodscanner_client.R
import kirillrychkov.foodscanner_client.app.domain.entity.Diet
import kirillrychkov.foodscanner_client.app.domain.repository.AuthRepository
import kirillrychkov.foodscanner_client.app.presentation.FoodScannerApp
import kirillrychkov.foodscanner_client.app.presentation.ViewModelFactory
import kirillrychkov.foodscanner_client.app.presentation.auth.AuthActivity
import kirillrychkov.foodscanner_client.app.presentation.auth.AuthViewModel
import kirillrychkov.foodscanner_client.app.presentation.restrictions.ChooseRestrictionsAdapter
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
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bindTabLayout()
        setUserData()
        bindLogoutButton()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun bindTabLayout(){
        pagerAdapter = ProfileRestrictionsListPageAdapter(this)
        binding.viewPagerProfile.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayoutProfile, binding.viewPagerProfile){tab, position ->
            when(position){
                0 -> tab.text = "Diets"
                1 -> tab.text = "Allergens"
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
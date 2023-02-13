package kirillrychkov.foodscanner_client.app.presentation.mainpage.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kirillrychkov.foodscanner_client.R
import kirillrychkov.foodscanner_client.databinding.FragmentProfileBinding
import kirillrychkov.foodscanner_client.databinding.FragmentRegisterBinding


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding
        get() = _binding ?: throw RuntimeException("FragmentProfileBinding == null")

    private lateinit var pagerAdapter : ProfileRestrictionsListPageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bindTabLayout()
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
}
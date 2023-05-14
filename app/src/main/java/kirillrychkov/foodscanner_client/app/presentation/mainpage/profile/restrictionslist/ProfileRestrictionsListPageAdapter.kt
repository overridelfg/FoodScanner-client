package kirillrychkov.foodscanner_client.app.presentation.mainpage.profile.restrictionslist

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ProfileRestrictionsListPageAdapter(
    fragment: Fragment
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            DietsListFragment()
        } else {
            AllergensListFragment()
        }
    }
}
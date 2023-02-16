package kirillrychkov.foodscanner_client.app.presentation.mainpage.profile

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import kirillrychkov.foodscanner_client.R
import kirillrychkov.foodscanner_client.app.presentation.FoodScannerApp
import kirillrychkov.foodscanner_client.app.presentation.ViewModelFactory
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
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupRecyclerView(){
        val rvShopList = binding.rvAllergensList
        adapter = ProfileRestrictionsAdapter()
        adapter.restrictionsList = viewModel.getUser.value!!.allergens
        rvShopList.adapter = adapter
    }
}
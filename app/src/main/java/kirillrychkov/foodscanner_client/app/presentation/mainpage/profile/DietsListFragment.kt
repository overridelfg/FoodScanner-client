package kirillrychkov.foodscanner_client.app.presentation.mainpage.profile

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import kirillrychkov.foodscanner_client.app.presentation.FoodScannerApp
import kirillrychkov.foodscanner_client.app.presentation.ViewModelFactory
import kirillrychkov.foodscanner_client.databinding.FragmentDietsListBinding
import javax.inject.Inject


class DietsListFragment : Fragment() {

    private var _binding: FragmentDietsListBinding? = null
    private val binding: FragmentDietsListBinding
        get() = _binding ?: throw RuntimeException("FragmentDietsListBinding == null")

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
        _binding = FragmentDietsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecyclerView()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupRecyclerView(){
        val rvShopList = binding.rvDietsList
        adapter = ProfileRestrictionsAdapter()
        adapter.restrictionsList = viewModel.getUser.value!!.diets
        rvShopList.adapter = adapter
    }

}
package kirillrychkov.foodscanner_client.app.presentation.mainpage.products.details

import android.animation.LayoutTransition
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kirillrychkov.foodscanner_client.R
import kirillrychkov.foodscanner_client.app.presentation.mainpage.products.ProductsListAdapter
import kirillrychkov.foodscanner_client.databinding.FragmentProductRestrictionsDetailsBinding
import kirillrychkov.foodscanner_client.databinding.FragmentProductsListBinding

class ProductRestrictionsDetailsFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentProductRestrictionsDetailsBinding? = null
    private val binding: FragmentProductRestrictionsDetailsBinding
        get() = _binding ?: throw RuntimeException("FragmentProductRestrictionsDetailsBinding == null")

    private lateinit var adapter: ProductsRestrictionsDetailsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme);
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentProductRestrictionsDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        (dialog as BottomSheetDialog).behavior.state = BottomSheetBehavior.STATE_EXPANDED
        setupRecyclerView()
    }

    private fun setupRecyclerView(){
        val rvProductsList = binding.rvProductDetails
        adapter = ProductsRestrictionsDetailsAdapter()
        val arguments = requireArguments().getStringArrayList("ANSWER")
        if (arguments != null) {
            adapter.productsList = arguments.toList()
        }
        adapter.productsList
        rvProductsList.adapter = adapter
    }


}
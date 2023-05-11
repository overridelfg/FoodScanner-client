package kirillrychkov.foodscanner_client.app.presentation.restrictions

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kirillrychkov.foodscanner_client.R
import kirillrychkov.foodscanner_client.databinding.FragmentRestrictionInfoBinding


class RestrictionInfoFragment : BottomSheetDialogFragment() {


    private var _binding: FragmentRestrictionInfoBinding? = null
    private val binding: FragmentRestrictionInfoBinding
        get() = _binding ?: throw RuntimeException("FragmentRestrictionInfoBinding == null")

    private lateinit var mBottomBehavior:  BottomSheetBehavior<View>
    override fun getTheme(): Int {
        return R.style.BottomSheetDialogTheme
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRestrictionInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBottomBehavior =
            BottomSheetBehavior.from(view.parent as View)
        mBottomBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        val header = requireArguments().getString("HEADER")
        val title = requireArguments().getString("TITLE")
        val description = requireArguments().getString("DESCRIPTION")
        binding.tvTitleInfoRestriction.text = header
        binding.tvRestrictionInfo.text = description
        binding.tvRestrictionTitle.text = title
    }
}
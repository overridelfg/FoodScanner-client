package kirillrychkov.foodscanner_client.app.presentation.mainpage.feedbacks.nonexistent

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kirillrychkov.foodscanner_client.R
import kirillrychkov.foodscanner_client.app.presentation.FoodScannerApp
import kirillrychkov.foodscanner_client.app.presentation.ViewModelFactory
import kirillrychkov.foodscanner_client.databinding.FragmentFirstProductPhotoImageBinding
import javax.inject.Inject


class FirstProductPhotoImageFragment : Fragment() {


    private var _binding: FragmentFirstProductPhotoImageBinding? = null
    private val binding: FragmentFirstProductPhotoImageBinding
        get() = _binding ?: throw RuntimeException("FragmentFirstProductPhotoImageBinding == null")

    private lateinit var viewModel: FeedbackViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
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
        viewModel = ViewModelProvider(
            requireActivity(),
            viewModelFactory
        )[FeedbackViewModel::class.java]
        _binding = FragmentFirstProductPhotoImageBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getFirstImage.observe(viewLifecycleOwner){
            binding.imagePic.setImageBitmap(it)
        }

        binding.btnPrev.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_firstProductPhotoImageFragment_to_secondPhotoProductFragment)
        }
    }
}
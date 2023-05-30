package kirillrychkov.foodscanner_client.app.presentation.mainpage.feedbacks.nonexistent

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kirillrychkov.foodscanner_client.app.domain.entity.FeedbackImages
import kirillrychkov.foodscanner_client.app.presentation.FoodScannerApp
import kirillrychkov.foodscanner_client.app.presentation.MainActivity
import kirillrychkov.foodscanner_client.app.presentation.ViewModelFactory
import kirillrychkov.foodscanner_client.app.presentation.ViewState
import kirillrychkov.foodscanner_client.databinding.FragmentSecondPhotoProductImageBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import javax.inject.Inject


class SecondPhotoProductImageFragment : Fragment() {

    private var _binding: FragmentSecondPhotoProductImageBinding? = null
    private val binding: FragmentSecondPhotoProductImageBinding
        get() = _binding ?: throw RuntimeException("FragmentSecondPhotoProductImageBinding == null")

    private lateinit var viewModel: FeedbackViewModel
    private lateinit var currentSecondImage : Bitmap
    private lateinit var currentFirstImage : Bitmap
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
        _binding = FragmentSecondPhotoProductImageBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribePostFeedback()
        viewModel.getSecondImage.observe(viewLifecycleOwner) {
            binding.imagePic.setImageBitmap(it)
            currentSecondImage = it
        }

        viewModel.getFirstImage.observe(viewLifecycleOwner){
            currentFirstImage = it
        }

        binding.btnPrev.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnNext.setOnClickListener {

            viewModel.provideImagesFeedback(
                currentFirstImage,
                currentSecondImage,
                1
            )
        }
    }


    private fun subscribePostFeedback(){
        viewModel.feedbackNonexistent.observe(viewLifecycleOwner){
            when (it) {
                is ViewState.Success -> {
                    Snackbar.make(
                        requireView(),
                        "Спасибо, что помогаете нам развивать приложение! Данные успешно отправились",
                        Snackbar.LENGTH_LONG
                    ).show()


                }
                is ViewState.Loading -> {
                }
                is ViewState.Error -> {
                    Snackbar.make(
                        requireView(),
                        "Спасибо, что помогаете нам развивать приложение! Данные успешно отправились",
                        Snackbar.LENGTH_LONG
                    ).show()
                    lifecycleScope.launch {
                        delay(2000)
                        requireActivity().finish()
                    }
                }
            }
        }
    }
}
package kirillrychkov.foodscanner_client.app.presentation.mainpage.feedbacks.nonexistent

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.mlkit.vision.common.InputImage
import kirillrychkov.foodscanner_client.R
import kirillrychkov.foodscanner_client.app.presentation.FoodScannerApp
import kirillrychkov.foodscanner_client.app.presentation.ViewModelFactory
import kirillrychkov.foodscanner_client.databinding.FragmentFirstProductPhotoBinding
import kirillrychkov.foodscanner_client.databinding.FragmentSecondPhotoProductBinding
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject

class SecondPhotoProductFragment : Fragment() {
    private var _binding: FragmentSecondPhotoProductBinding? = null
    private val binding: FragmentSecondPhotoProductBinding
        get() = _binding ?: throw RuntimeException("FragmentSecondPhotoFragmentBinding == null")

    private lateinit var viewModel: FeedbackViewModel

    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    private val cameraExecutor: ExecutorService by lazy {
        Executors.newSingleThreadExecutor()
    }

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
        _binding = FragmentSecondPhotoProductBinding.inflate(layoutInflater)
        return binding.root
    }

    @ExperimentalGetImage
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startCamera()
        binding.buttonTakePicture.setOnClickListener {
            takePhoto()
        }
    }

    @ExperimentalGetImage
    private fun takePhoto(){
        binding.pbTakePhotoFirst.isVisible = true
        val imageCapture = imageCapture?: return
        imageCapture.takePicture(
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)
                    val image1 = image.image!!
                    val bitmap = InputImage.fromMediaImage(
                        image1,
                        90
                    ).bitmapInternal!!

                    viewModel.setSecondImage(bitmap)
                    findNavController().navigate(R.id.action_secondPhotoProductFragment_to_secondPhotoProductImageFragment)
                    image.close()
                    binding.pbTakePhotoFirst.isVisible = false
                }

                override fun onError(exception: ImageCaptureException) {
                    binding.pbTakePhotoFirst.isVisible = false
                    super.onError(exception)
                }
            }
        )
    }

    private fun startCamera(){
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()
            val rotation: Int = binding.pvCameraPhoto.display.rotation
            val preview = Preview.Builder().build()
                .also {
                    it.setSurfaceProvider(binding.pvCameraPhoto.surfaceProvider)
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            imageCapture = ImageCapture.Builder().apply {
                setTargetAspectRatio(AspectRatio.RATIO_16_9)
            }.build()



            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(viewLifecycleOwner, cameraSelector, preview, imageCapture)
            }catch (e: Exception){

            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }



    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}
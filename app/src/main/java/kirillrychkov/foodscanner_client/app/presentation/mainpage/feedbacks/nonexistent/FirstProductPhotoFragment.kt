package kirillrychkov.foodscanner_client.app.presentation.mainpage.feedbacks.nonexistent

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.mlkit.vision.common.InputImage
import kirillrychkov.foodscanner_client.R
import kirillrychkov.foodscanner_client.app.presentation.FoodScannerApp
import kirillrychkov.foodscanner_client.app.presentation.ViewModelFactory
import kirillrychkov.foodscanner_client.databinding.FragmentFirstProductPhotoBinding
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject


class FirstProductPhotoFragment : Fragment() {

    private var _binding: FragmentFirstProductPhotoBinding? = null
    private val binding: FragmentFirstProductPhotoBinding
        get() = _binding ?: throw RuntimeException("FragmentFirstProductPhotoBinding == null")

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
        _binding = FragmentFirstProductPhotoBinding.inflate(layoutInflater)
        return binding.root
    }

    @ExperimentalGetImage
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startCamera()
        val barcode = requireArguments().getLong("BARCODE")
        viewModel.setCurrentBarcode(barcode)
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

                    viewModel.setFirstImage(bitmap)
                    findNavController().navigate(R.id.action_firstProductPhotoFragment_to_firstProductPhotoImageFragment)
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
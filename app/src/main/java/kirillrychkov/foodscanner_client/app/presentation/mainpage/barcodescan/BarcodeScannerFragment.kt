package kirillrychkov.foodscanner_client.app.presentation.mainpage.barcodescan

import android.Manifest
import android.annotation.SuppressLint
import android.app.Instrumentation
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.squareup.picasso.Picasso
import kirillrychkov.foodscanner_client.R
import kirillrychkov.foodscanner_client.app.domain.entity.Product
import kirillrychkov.foodscanner_client.app.domain.entity.ProductRestriction
import kirillrychkov.foodscanner_client.app.domain.repository.ProductsRepository
import kirillrychkov.foodscanner_client.app.presentation.FoodScannerApp
import kirillrychkov.foodscanner_client.app.presentation.ViewModelFactory
import kirillrychkov.foodscanner_client.app.presentation.ViewState
import kirillrychkov.foodscanner_client.app.presentation.mainpage.products.ProductsListViewModel
import kirillrychkov.foodscanner_client.databinding.FragmentBarcodeScannerBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject


class BarcodeScannerFragment : Fragment() {

    private var _binding: FragmentBarcodeScannerBinding? = null
    private val binding: FragmentBarcodeScannerBinding
        get() = _binding ?: throw RuntimeException("FragmentBarcodeScannerBinding == null")

    private lateinit var cameraExecutor: ExecutorService
    private lateinit var viewModel: BarcodeScannerViewModel
    private lateinit var viewModelProductDetails: ProductsListViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelFactory


    @Inject
    lateinit var productsRepository: ProductsRepository

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
        _binding = FragmentBarcodeScannerBinding.inflate(layoutInflater)
        viewModelProductDetails = ViewModelProvider(requireActivity(), viewModelFactory)[ProductsListViewModel::class.java]
        viewModel = ViewModelProvider(this, viewModelFactory)[BarcodeScannerViewModel::class.java]
        cameraExecutor = Executors.newSingleThreadExecutor()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeGetProductDetails()
        subscribeGetProductRestrictionsDetails()
        val barcode : Long = 46079604901081
        viewModel.getProductDetails(barcode)
        if(allPermissionGranted()){
            startCamera()
        }else{
            permReqLauncher.launch(PERMISSION)
        }
        binding.buttonHistory.setOnClickListener {
            findNavController().navigate(R.id.action_barcodeScannerFragment_to_barcodeScannerHistoryFragment)
        }

    }

    override fun onPause() {
        super.onPause()
    }



    private fun subscribeGetProductDetails(){
        viewModel.productDetails.observe(viewLifecycleOwner){
            when(it){
                is ViewState.Success -> {
                    viewModelProductDetails.sendProductDetails(it.result)
                    showBottomSheetProductDetails()
                    viewModel.getProductRestrictionsDetails(it.result.id)
                }
                is ViewState.Error -> {
                    if(it.result == "Not Found"){
                        val bottomSheetRoot = binding.bottomSheetProductDetailsError.bottomSheetRoot
                        bottomSheetRoot.visibility = View.VISIBLE
                        val mBottomBehavior =
                            BottomSheetBehavior.from(bottomSheetRoot)
                        mBottomBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                        findNavController().navigate(R.id.action_barcodeScannerFragment_to_firstProductPhotoFragment)
                    }
                }
                is ViewState.Loading -> {
                    Log.d(TAG, "Loading")
                }
            }
        }
    }

    private fun showBottomSheetProductDetails(){
        val bottomSheetRoot = binding.bottomSheet.bottomSheetRoot
        bottomSheetRoot.visibility = View.VISIBLE
        val mBottomBehavior =
            BottomSheetBehavior.from(bottomSheetRoot)
        mBottomBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }


    private fun subscribeGetProductRestrictionsDetails(){
        viewModel.productRestrictionDetails.observe(viewLifecycleOwner){
            when (it) {
                is ViewState.Success -> {
                    binding.pbBarcodeDetails.isVisible = false
                }
                is ViewState.Loading -> {
                    binding.pbBarcodeDetails.isVisible = true
                }
                is ViewState.Error -> {
                    binding.pbBarcodeDetails.isVisible = false
                    Snackbar.make(
                        requireView(),
                        it.result.toString(),
                        Snackbar.LENGTH_LONG
                    ).setAction("OK") {
                    }.show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cameraExecutor.shutdown()
    }




    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build()
                .also {
                    it.setSurfaceProvider(binding.pvCamera.surfaceProvider)
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            val imageCapture = ImageCapture.Builder().build()
            val imageAnalysis = ImageAnalysis.Builder()
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, object : ImageAnalysis.Analyzer {
                        override fun analyze(image: ImageProxy) {
                                scanBarcode(image)
                        }

                        private var firstCall = true

                        private fun scanBarcode(image: ImageProxy) {
                            @SuppressLint("UnsafeOptInUsageError")
                            val image1 = image.image
                            if(image1 != null){
                                val inputImage = InputImage.fromMediaImage(
                                    image1,
                                    image.imageInfo.rotationDegrees
                                )
                                val scannerOptions = BarcodeScannerOptions.Builder()
                                    .setBarcodeFormats(Barcode.FORMAT_EAN_13)
                                    .build()
                                val scanner = BarcodeScanning.getClient(scannerOptions)

                                val barcodeResult = scanner.process(inputImage)
                                    .addOnSuccessListener { barcodes ->
                                        val barcode = barcodes.getOrNull(0)
                                        if (barcode != null) {
                                            if (firstCall) {
                                                firstCall = false
                                                readBarcodeData(barcode)
                                            }

                                        }
                                    }
                                    .addOnFailureListener {
                                    }
                                    .addOnCompleteListener{
                                        image1.close()
                                        image.close()
                                    }
                            }
                        }

                        private fun readBarcodeData(barcode: Barcode) {
                            if(barcode.rawValue != null){
                                viewModel.getProductDetails(barcode.rawValue!!.toLong())
                            }
                        }
                    })
                }


            try {
                cameraProvider.bindToLifecycle(
                    viewLifecycleOwner,
                    cameraSelector,
                    preview,
                    imageCapture,
                    imageAnalysis
                )
            }catch (e: Exception){
                Log.d(TAG, e.message.toString())
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun allPermissionGranted() = PERMISSION.all{
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    private val permReqLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.entries.all {
                it.value == true
            }
            if (granted) {
                startCamera()
            }else{
                Toast.makeText(requireContext(), "Permissions error", Toast.LENGTH_LONG)
            }
        }

    companion object{
        private const val TAG = "CameraX"
        private val PERMISSION = arrayOf(Manifest.permission.CAMERA)
    }

}
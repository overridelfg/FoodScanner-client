package kirillrychkov.foodscanner_client.app.presentation.mainpage.barcodescan

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.util.Size
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kirillrychkov.foodscanner_client.R
import kirillrychkov.foodscanner_client.databinding.FragmentBarcodeScannerBinding
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class BarcodeScannerFragment : Fragment() {

    private var _binding: FragmentBarcodeScannerBinding? = null
    private val binding: FragmentBarcodeScannerBinding
        get() = _binding ?: throw RuntimeException("FragmentBarcodeScannerBinding == null")

    private lateinit var cameraExecutor: ExecutorService
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBarcodeScannerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(allPermissionGranted()){
            startCamera()
        }else{
            permReqLauncher.launch(PERMISSION)
        }

        cameraExecutor = Executors.newSingleThreadExecutor()
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
                                        readBarcodeData(barcodes)
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(requireContext(), "WTF", Toast.LENGTH_LONG).show()
                                    }
                                    .addOnCompleteListener{
                                        image.close()
                                    }
                            }
                        }

                        private fun readBarcodeData(barcodes: List<Barcode>) {
                            for(barcode in barcodes){
                                val bound = barcode.boundingBox
                                val corners = barcode.cornerPoints
                                Log.d("CameraX", barcode.rawValue.toString())
                            }
                        }
                    })
                }


            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    requireActivity(),
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
        private const val FILE_FORMAT = "yyyy-MM-HH-ss-SSS"
        private const val PERMISSION_CODE = 10
        private val PERMISSION = arrayOf(Manifest.permission.CAMERA)
    }

}
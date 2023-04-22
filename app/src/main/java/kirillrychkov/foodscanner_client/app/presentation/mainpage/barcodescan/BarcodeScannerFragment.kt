package kirillrychkov.foodscanner_client.app.presentation.mainpage.barcodescan

import android.Manifest
import android.annotation.SuppressLint
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
        viewModel = ViewModelProvider(this, viewModelFactory)[BarcodeScannerViewModel::class.java]
        cameraExecutor = Executors.newSingleThreadExecutor()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeGetProductDetails()
        subscribeGetProductRestrictionsDetails()
        val barcode : Long = 4607960490108
        viewModel.getProductDetails(barcode)
        if(allPermissionGranted()){
            startCamera()
        }else{
            permReqLauncher.launch(PERMISSION)
        }

    }



    private fun subscribeGetProductDetails(){
        viewModel.productDetails.observe(viewLifecycleOwner){
            when(it){
                is ViewState.Success -> {
                    bindProductData(it.result)
                    showBottomSheetProductDetails()
                    viewModel.getProductRestrictionsDetails(it.result.id)
                }
                is ViewState.Error -> {
                    Log.d(TAG, it.toString())
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

    private fun fillProductRestrictionUI(productRestriction: ProductRestriction){
        if(productRestriction.status == false){
            val productRestrictionsSet = mutableSetOf<String>()
            for(i in 0 until productRestriction.answer.size){
                val el = productRestriction.answer[i].split(':')[1].split(',')
                for(j in 0 until el.size){
                    productRestrictionsSet.add(el[j])
                }
            }

            binding.bottomSheet.tvRestrictedIngredients.text = "Запрещенные игредиенты: " +
                    productRestrictionsSet.joinToString(", ")
            binding.bottomSheet.tvRestrictedIngredients.isVisible = true
            binding.bottomSheet.tvIsRestricted.text = "Продукт не подходит под ваши ограничения"
            binding.bottomSheet.ivInfoIcon.isVisible = true
            binding.bottomSheet.ivIsRestrictedIcon.setBackgroundResource(R.drawable.ic_restricted)
            binding.bottomSheet.layoutIsRestricted.setBackgroundResource(R.drawable.background_restricted_layout)
            binding.bottomSheet.ivInfoIcon.setOnClickListener {
                val bundle = Bundle()
                val answer = ArrayList(productRestriction.answer)
                bundle.putStringArrayList("ANSWER", answer)
                findNavController().navigate(R.id.action_barcodeScannerFragment_to_productRestrictionsDetailsFragment, bundle)
            }
        }else{
            binding.bottomSheet.tvRestrictedIngredients.isVisible = false
            binding.bottomSheet.tvIsRestricted.text = "Продукт подходит под ваши ограничения"
            binding.bottomSheet.ivIsRestrictedIcon.setBackgroundResource(R.drawable.ic_check_circle)
            binding.bottomSheet.layoutIsRestricted.setBackgroundResource(R.drawable.background_not_restricted_layout)
        }

    }

    private fun subscribeGetProductRestrictionsDetails(){
        viewModel.productRestrictionDetails.observe(viewLifecycleOwner){
            when (it) {
                is ViewState.Success -> {
                    binding.pbBarcodeDetails.isVisible = false
                    fillProductRestrictionUI(it.result)
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

    @SuppressLint("SetTextI18n")
    private fun bindProductData(product: Product){
        val productWeight = product.Weight.replace("\\s".toRegex(), "")
        binding.bottomSheet.tvProductTitle.text = product.Name + " " + productWeight
        binding.bottomSheet.tvProductIngredients.text =
            "Ингредиенты:" + " " + product.Description
        binding.bottomSheet.tvProteins.text =
            "Белки " + "\n" + product.Proteins
        binding.bottomSheet.tvFats.text =
            "Жиры" + "\n" + product.Fats
        binding.bottomSheet.tvCarbohydrates.text =
            "Углеводы" + "\n" + product.Carbohydrates
        if(product.Jpg.isBlank()){
            Picasso.get().load(R.drawable.no_pictures).into(binding.bottomSheet.ivProductImg);
        }else{
            try{
                Picasso.get().isLoggingEnabled = true
                Picasso.get().load(product.Jpg)
                    .placeholder(R.color.white)
                    .error(R.drawable.no_pictures)
                    .fit()
                    .centerInside().
                    into(binding.bottomSheet.ivProductImg);
            }catch (e: Exception){
                Picasso.get().load(R.drawable.no_pictures).into(binding.bottomSheet.ivProductImg)
            }
        }
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
                                Log.d("CameraX", barcode.rawValue.toString())
                                if(barcode.rawValue != null){
                                    viewModel.getProductDetails(barcode.rawValue!!.toLong())
                                }
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
package kirillrychkov.foodscanner_client.app.presentation.mainpage.feedbacks.nonexistent

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.storage.FirebaseStorage
import kirillrychkov.foodscanner_client.app.presentation.FoodScannerApp
import kirillrychkov.foodscanner_client.app.presentation.ViewModelFactory
import kirillrychkov.foodscanner_client.app.presentation.ViewState
import kirillrychkov.foodscanner_client.databinding.FragmentSecondPhotoProductImageBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okio.IOException
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class SecondPhotoProductImageFragment : Fragment() {

    private var _binding: FragmentSecondPhotoProductImageBinding? = null
    private val binding: FragmentSecondPhotoProductImageBinding
        get() = _binding ?: throw RuntimeException("FragmentSecondPhotoProductImageBinding == null")

    private lateinit var viewModel: FeedbackViewModel
    private lateinit var currentSecondImage: Bitmap
    private lateinit var currentFirstImage: Bitmap
    private var barcode: Long = 0

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
        subscribeGetCurrentBarcode()
        viewModel.getSecondImage.observe(viewLifecycleOwner) {
            binding.imagePic.setImageBitmap(it)
            currentSecondImage = it
        }


        viewModel.getFirstImage.observe(viewLifecycleOwner) {
            currentFirstImage = it
        }



        binding.btnPrev.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnNext.setOnClickListener {
            saveFeedbackToFirebase(currentFirstImage, currentSecondImage)
        }
    }


    private fun subscribeGetCurrentBarcode() {
        viewModel.currentBarcode.observe(viewLifecycleOwner){
            barcode = it
        }
    }

    private fun saveFeedbackToFirebase(image1: Bitmap, image2: Bitmap) {
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val filename = sdf.format(Date())
        val file1 = convertToFile(image1, filename, "first")
        val file2 = convertToFile(image2, filename, "second")
        val imageRef = FirebaseStorage.getInstance().reference.child("image/${file1.name}")
        val imageRef2 = FirebaseStorage.getInstance().reference.child("image/${file2.name}")
        val uri1 = Uri.fromFile(file1)
        val uri2 = Uri.fromFile(file2)
        imageRef.putFile(uri1).addOnSuccessListener {
            imageRef2.putFile(uri2).addOnSuccessListener {
                binding.pbUploadFeedback.isVisible = false
                requireActivity().finish()
            }.addOnFailureListener {
                binding.pbUploadFeedback.isVisible = false
                binding.layoutButtons.isVisible = true
            }
        }.addOnFailureListener {
            binding.pbUploadFeedback.isVisible = false
            binding.layoutButtons.isVisible = true

        }.addOnProgressListener {
            binding.pbUploadFeedback.isVisible = true
            binding.layoutButtons.isVisible = false
            it.error
        }

    }

    private fun convertToFile(image: Bitmap, filename: String, number: String): File {
        val currentBarcode = barcode.toString()
        val f = File(requireContext().cacheDir, "$filename $number $currentBarcode" + ".jpg")
        f.parentFile?.mkdirs()
        f.createNewFile()
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        Log.d("CameraZX", image.toString())
        val byteArray = baos.toByteArray()
        val fos = FileOutputStream(f)
        fos.write(byteArray)
        fos.flush()
        fos.close()
        return f
    }

}
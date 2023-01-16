package kirillrychkov.foodscanner_client.presentation.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import kirillrychkov.foodscanner_client.presentation.presentation.ViewModelFactory
import kirillrychkov.foodscanner_client.presentation.presentation.auth.AuthViewModel

abstract class BaseFragment<VB : ViewBinding, VM: ViewModel>(
    private val bindingInflater: (inflater: LayoutInflater) -> VB
) : Fragment() {

    private var _binding: VB? = null
    val binding: VB
    get() = _binding ?: throw RuntimeException("ViewBinding == null")

    lateinit var viewModel: VM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater.invoke(inflater)
        val factory  = ViewModelFactory()
        viewModel = ViewModelProvider(this, factory)[getViewModel()]
        return binding.root
    }

    abstract fun getViewModel(): Class<VM>
}
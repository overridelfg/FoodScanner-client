package kirillrychkov.foodscanner_client.presentation.presentation.auth

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kirillrychkov.foodscanner_client.R
import kirillrychkov.foodscanner_client.databinding.FragmentLoginBinding
import kirillrychkov.foodscanner_client.presentation.presentation.FoodScannerApp
import kirillrychkov.foodscanner_client.presentation.presentation.ViewModelFactory
import kirillrychkov.foodscanner_client.presentation.presentation.ViewState
import kirillrychkov.foodscanner_client.presentation.presentation.restrictions.ChooseRestrictionsActivity.Companion.newIntentChooseRestrictions
import javax.inject.Inject


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding:  FragmentLoginBinding
        get() = _binding ?: throw RuntimeException("FragmentLoginBinding == null")
    private lateinit var viewModel: AuthViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy{
        FoodScannerApp.appComponent
    }

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this, viewModelFactory)[AuthViewModel::class.java]
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnTextChange()
        subscribeError()
        launchRegisterFragment()
        subscribeLoginResult()
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            viewModel.login(email, password)
        }
    }

    private fun launchRegisterFragment(){
        binding.registerButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun subscribeError() {
        viewModel.errorInputEmail.observe(viewLifecycleOwner) {
            val errorMessage = if (it == AuthFormErrorState.EMPTY_EMAIL) {
                "Заполните поле Email"
            } else
                null
            binding.emailContainer.error = errorMessage
        }

        viewModel.errorInputPassword.observe(viewLifecycleOwner) {
            val errorMessage = when (it) {
                AuthFormErrorState.INVALID_PASSWORD -> {
                    "Длина пароля должна быть не меньше 4 и не больше 32"
                }
                AuthFormErrorState.EMPTY_PASSWORD -> {
                    "Заполните поле Password"
                }
                else -> {
                    null
                }
            }
            binding.passwordContainer.error = errorMessage
        }
    }

    private fun subscribeLoginResult(){
        viewModel.loginResult.observe(viewLifecycleOwner){
            when (it) {
                is ViewState.Loading -> {
//                    binding.progressBar.isVisible = true
                }
                is ViewState.Error -> {
//                  binding.progressBar.isVisible = false
//                    showError(it.result ?: "Unknown login error")
                }
                is ViewState.Success -> {
//                  binding.progressBar.isVisible = false
                    startActivity(newIntentChooseRestrictions(requireContext()))
                    requireActivity().finish()
                }
            }
        }
    }
    private fun setOnTextChange() {
        binding.emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputEmail()
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        binding.passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputPassword()
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }

}
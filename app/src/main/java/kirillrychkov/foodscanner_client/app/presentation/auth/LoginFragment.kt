package kirillrychkov.foodscanner_client.app.presentation.auth

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kirillrychkov.foodscanner_client.R
import kirillrychkov.foodscanner_client.databinding.FragmentLoginBinding
import kirillrychkov.foodscanner_client.app.presentation.FoodScannerApp
import kirillrychkov.foodscanner_client.app.presentation.MainActivity
import kirillrychkov.foodscanner_client.app.presentation.ViewModelFactory
import kirillrychkov.foodscanner_client.app.presentation.ViewState
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
        Log.d("ff", savedInstanceState.toString())
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
            findNavController().navigate(R.id.action_loginFragment_to_chooseDietsFragment)
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
                    binding.pbLogin.isVisible = true
                }
                is ViewState.Error -> {
                    binding.pbLogin.isVisible = false
                    Snackbar.make(
                        requireView(),
                        it.result.toString(),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
                is ViewState.Success -> {
                    binding.pbLogin.isVisible = true
                    startActivity(MainActivity.newIntentMainActivity(requireContext()))
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
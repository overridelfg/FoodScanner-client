package kirillrychkov.foodscanner_client.presentation.presentation.auth

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kirillrychkov.foodscanner_client.databinding.FragmentLoginBinding
import kirillrychkov.foodscanner_client.databinding.FragmentRegisterBinding
import kirillrychkov.foodscanner_client.presentation.presentation.FoodScannerApp
import kirillrychkov.foodscanner_client.presentation.presentation.ViewModelFactory
import kirillrychkov.foodscanner_client.presentation.presentation.ViewState
import kirillrychkov.foodscanner_client.presentation.presentation.restrictions.ChooseRestrictionsActivity
import javax.inject.Inject

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding: FragmentRegisterBinding
        get() = _binding ?: throw RuntimeException("FragmentRegisterBinding == null")
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
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnTextChange()
        subscribeError()
        subscribeRegisterResult()
        launchLoginFragment()
        binding.registerButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val username = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            viewModel.register(email, password, username)
        }
    }

    private fun launchLoginFragment(){
        binding.signInButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun subscribeRegisterResult(){
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
                    startActivity(
                        ChooseRestrictionsActivity.newIntentChooseRestrictions(
                            requireContext()
                        )
                    )
                    requireActivity().finish()
                }
            }
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
        viewModel.errorInputUsername.observe(viewLifecycleOwner) {
            val errorMessage = when (it) {
                AuthFormErrorState.INVALID_USERNAME -> {
                    "Длина имени должна быть не меньше 2 и не больше 18"
                }
                AuthFormErrorState.EMPTY_USERNAME -> {
                    "Заполните поле Username"
                }
                else -> {
                    null
                }
            }
            binding.usernameContainer.error = errorMessage
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

        binding.usernameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputUsername()
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }
}
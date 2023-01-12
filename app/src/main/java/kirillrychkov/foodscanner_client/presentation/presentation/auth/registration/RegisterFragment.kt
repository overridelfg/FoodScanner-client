package kirillrychkov.foodscanner_client.presentation.presentation.auth.registration

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kirillrychkov.foodscanner_client.R
import kirillrychkov.foodscanner_client.databinding.FragmentRegisterBinding
import kirillrychkov.foodscanner_client.presentation.presentation.restrictions.ChooseRestrictionsActivity

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding: FragmentRegisterBinding
        get() = _binding ?: throw RuntimeException("FragmentWelcomeBinding == null")

    private val registerViewModel by lazy {
        ViewModelProvider(this)[RegisterViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnTextChange()
        subscribeError()
        subscribeFinishLogin()
        launchLoginFragment()
        binding.registerButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val username = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            registerViewModel.register(email, password, username)
        }
    }

    private fun launchLoginFragment(){
        binding.signInButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun subscribeFinishLogin(){
        registerViewModel.shouldFinishRegister.observe(viewLifecycleOwner){
            if(it){
                startActivity(ChooseRestrictionsActivity.newIntentChooseRestrictions(requireContext()))
                requireActivity().finish()
            }
        }
    }

    private fun subscribeError() {
        registerViewModel.errorInputEmail.observe(viewLifecycleOwner) {
            val errorMessage = if (it == RegisterViewModel.RegisterFormErrors.EMPTY_EMAIL) {
                "Заполните поле Email"
            } else
                null
            binding.emailContainer.error = errorMessage
        }

        registerViewModel.errorInputPassword.observe(viewLifecycleOwner) {
            val errorMessage = when (it) {
                RegisterViewModel.RegisterFormErrors.INVALID_PASSWORD -> {
                    "Длина пароля должна быть не меньше 4 и не больше 32"
                }
                RegisterViewModel.RegisterFormErrors.EMPTY_PASSWORD -> {
                    "Заполните поле Password"
                }
                else -> {
                    null
                }
            }
            binding.passwordContainer.error = errorMessage
        }
        registerViewModel.errorInputUsername.observe(viewLifecycleOwner) {
            val errorMessage = when (it) {
                RegisterViewModel.RegisterFormErrors.INVALID_USERNAME -> {
                    "Длина имени должна быть не меньше 2 и не больше 18"
                }
                RegisterViewModel.RegisterFormErrors.EMPTY_USERNAME -> {
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
                registerViewModel.resetErrorInputEmail()
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        binding.passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                registerViewModel.resetErrorInputPassword()
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        binding.usernameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                registerViewModel.resetErrorInputUsername()
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }
}
package kirillrychkov.foodscanner_client.presentation.presentation.auth

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kirillrychkov.foodscanner_client.R
import kirillrychkov.foodscanner_client.databinding.FragmentLoginBinding
import kirillrychkov.foodscanner_client.presentation.presentation.MainActivity.Companion.newIntentMainActivity
import kirillrychkov.foodscanner_client.presentation.presentation.ViewState
import kirillrychkov.foodscanner_client.presentation.presentation.base.BaseFragment
import kirillrychkov.foodscanner_client.presentation.presentation.restrictions.ChooseRestrictionsActivity.Companion.newIntentChooseRestrictions


class LoginFragment : BaseFragment<FragmentLoginBinding, AuthViewModel>(
    FragmentLoginBinding::inflate
) {

    override fun getViewModel() = AuthViewModel::class.java

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
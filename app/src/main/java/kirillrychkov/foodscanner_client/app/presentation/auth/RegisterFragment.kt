package kirillrychkov.foodscanner_client.app.presentation.auth

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kirillrychkov.foodscanner_client.app.domain.repository.ChooseRestrictionsRepository
import kirillrychkov.foodscanner_client.databinding.FragmentRegisterBinding
import kirillrychkov.foodscanner_client.app.presentation.FoodScannerApp
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kirillrychkov.foodscanner_client.R
import kirillrychkov.foodscanner_client.app.presentation.MainActivity
import kirillrychkov.foodscanner_client.app.presentation.ViewModelFactory
import kirillrychkov.foodscanner_client.app.presentation.ViewState
import javax.inject.Inject

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding: FragmentRegisterBinding
        get() = _binding ?: throw RuntimeException("FragmentRegisterBinding == null")
    private lateinit var viewModel: AuthViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var chooseRestrictionsRepository: ChooseRestrictionsRepository

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
        bindRegisterButton()
    }

    private fun bindRegisterButton(){
        binding.registerButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val username = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val diets = chooseRestrictionsRepository.getSelectedDiets()
            val allergens = chooseRestrictionsRepository.getSelectedAllergens()
            viewModel.register(email, password, username, diets!!, allergens!!)
        }
    }

    private fun launchLoginFragment(){
        binding.signInButton.setOnClickListener {
            chooseRestrictionsRepository.removeSelectedRestrictions()
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    private fun subscribeRegisterResult(){
        viewModel.registerResult.observe(viewLifecycleOwner){
            when (it) {
                is ViewState.Loading -> {
                    binding.pbRegister.isVisible = true
                }
                is ViewState.Error -> {
                    binding.pbRegister.isVisible = false
                    Snackbar.make(
                        requireView(),
                        it.result.toString(),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
                is ViewState.Success -> {
                    binding.pbRegister.isVisible = false
                    startActivity(MainActivity.newIntentMainActivity(requireContext()))
                    requireActivity().finish()
                }
            }
        }
    }

    private fun subscribeError() {
        viewModel.errorInputEmail.observe(viewLifecycleOwner) {
            val errorMessage = when (it) {
                AuthFormErrorState.EMPTY_EMAIL -> {
                    "Заполните поле Email"
                }
                AuthFormErrorState.INVALID_EMAIL -> {
                    "Неверная почта"
                }
                else -> null
            }
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
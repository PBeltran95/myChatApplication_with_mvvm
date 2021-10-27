package ar.com.example.chatExample.ui.auth.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import ar.com.example.chatExample.R
import ar.com.example.chatExample.core.Response
import ar.com.example.chatExample.databinding.FragmentLoginBinding
import ar.com.example.chatExample.presentation.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding : FragmentLoginBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)
        setButtonActions()
        isUserLogged()
        onBackPressed()
    }

    private fun onBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        })
    }


    private fun isUserLogged() {
        if (viewModel.isUserLoggedIn()){
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
        }
    }

    private fun setButtonActions() {
        binding.btnSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }

        binding.btnSignIn.setOnClickListener {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
            if (checkFields(email, password)){
                logIn(email,password)
            }else{
                Toast.makeText(requireContext(), "Complete the fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun logIn(email: String, password: String) {
        viewModel.logIn(email, password).observe(viewLifecycleOwner, Observer {
            when(it){
                is Response.Loading -> {
                    //progressBar
                }
                is Response.Success -> {
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                }
                is Response.Failure -> {
                    //Toast
                }
            }

        })
    }

    private fun checkFields(email: String, password: String): Boolean {
        return !(email.isEmpty() || password.isEmpty())
    }

}
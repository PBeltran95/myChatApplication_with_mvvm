package ar.com.example.chatExample.ui.auth.signup

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import ar.com.example.chatExample.R
import ar.com.example.chatExample.core.Response
import ar.com.example.chatExample.databinding.FragmentSignUpBinding
import ar.com.example.chatExample.presentation.auth.AuthViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private val viewModel: AuthViewModel by viewModels()
    private lateinit var binding: FragmentSignUpBinding
    private var bitmap: Bitmap? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSignUpBinding.bind(view)
        setActionButton()
        takeUserImage()
    }

    private fun setActionButton() {
        binding.btnSignUp.setOnClickListener {

            val userName = binding.etUserName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val confirmPassword = binding.etConfirmPassword.text.toString().trim()
            val profileImage: Bitmap? = bitmap

            if (checkFields(userName, email, password, confirmPassword, profileImage)) {
                viewModel.signUp(email, password, userName, profileImage)
                    .observe(viewLifecycleOwner, Observer {
                        when (it) {
                            is Response.Loading -> {
                                binding.progressBar.isVisible = true
                            }
                            is Response.Success -> {
                                binding.progressBar.isVisible = false
                                findNavController().navigate(R.id.action_signUpFragment_to_homeFragment)
                            }
                            is Response.Failure -> { Toast.makeText(requireContext(), "Error: ${it.exception}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
            } else { Toast.makeText(requireContext(), "Fields not completed", Toast.LENGTH_SHORT).show() }
        }
    }

    private fun checkFields(userName: String, email: String, password: String, confirmPassword: String, profileImage: Bitmap?
    ): Boolean {
        if (userName.isEmpty()) return false
        if (email.isEmpty()) return false
        if (password.isEmpty()) return false
        if (confirmPassword.isEmpty()) return false
        if (profileImage == null) return false
        return true
    }

    private fun takeUserImage() {
        binding.btnPicture.setOnClickListener {
            materialAlertDialog()
        }
    }

    private fun useCamera() {
        val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            openActivityForResult(takePicture)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openActivityForResult(takePicture: Intent) {
        startForResult.launch(takePicture)
    }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                when(result.data?.data){
                    is Uri -> {
                        val image = result.data?.data
                        if(Build.VERSION.SDK_INT < 28){
                            bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), image)
                        } else{
                            val source: ImageDecoder.Source = ImageDecoder.createSource(requireActivity().getContentResolver(), image!!)
                            bitmap = ImageDecoder.decodeBitmap(source)
                        }
                        binding.btnPicture.setImageBitmap(bitmap)
                    }
                    else -> {
                        val imageBitmap = result.data?.extras?.get("data") as Bitmap
                        binding.btnPicture.setImageBitmap(imageBitmap)
                        bitmap = imageBitmap
                    }
                }


            }
        }

    private fun materialAlertDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Alert")
            .setMessage("From where would you like to take your profile photo?")
            .setNeutralButton("Cancel") { dialog, witch ->
                Toast.makeText(
                    requireContext(),
                    "You will need a profile photo.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .setNegativeButton("Camera") { dialog, witch ->
                useCamera()
            }
            .setPositiveButton("Files") { _, _ ->
                val checkSelfPermission = ContextCompat.checkSelfPermission(requireContext(),
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                if (checkSelfPermission != PackageManager.PERMISSION_GRANTED){
                    //Requests permissions to be granted to this application at runtime
                    ActivityCompat.requestPermissions(requireActivity(),
                        arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
                }
                else{
                    takeImageFromFiles()
                }

            }
            .show()
    }
    private fun takeImageFromFiles(){
        val photoFile = Intent(Intent.ACTION_GET_CONTENT)
        photoFile.also {
            it.type = "image/*"
        }
        startForResult.launch(photoFile)
    }


}

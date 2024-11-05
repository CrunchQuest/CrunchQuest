package com.crunchquest.android.presentation.ui.register

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.crunchquest.shared.R
import com.crunchquest.shared.databinding.FragmentRegisterStepThreeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterStepThreeFragment : Fragment(R.layout.fragment_register_step_three) {

    private val viewModel: RegisterViewModel by activityViewModels()
    private var _binding: FragmentRegisterStepThreeBinding? = null
    private val binding get() = _binding!!

    private val pickImageRequestCode = 1001
    private var currentImageType: ImageType = ImageType.ID

    enum class ImageType {
        ID, SELFIE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterStepThreeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            buttonUploadID.setOnClickListener {
                currentImageType = ImageType.ID
                checkPermissionAndPickImage()
            }

            buttonUploadSelfie.setOnClickListener {
                currentImageType = ImageType.SELFIE
                checkPermissionAndPickImage()
            }

            textInfo.setOnClickListener {
                showInfoBottomSheet()
            }
        }

        observeViewModel()
    }

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            when (currentImageType) {
                ImageType.ID -> {
                    viewModel.idImageUri.value = it
                    binding.imageViewID.setImageURI(it)
                }
                ImageType.SELFIE -> {
                    viewModel.selfieImageUri.value = it
                    binding.imageViewSelfie.setImageURI(it)
                }
            }
            viewModel.validateStepThree()
        } ?: run {
            // Optionally handle case where no image was selected
            Toast.makeText(requireContext(), "No image selected", Toast.LENGTH_SHORT).show()
            Log.e("RegisterStepThreeFragment", "No image selected:")
        }
    }


    private fun pickImage() {
        pickImageLauncher.launch("image/*")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == pickImageRequestCode && resultCode == Activity.RESULT_OK) {
            val imageUri: Uri? = data?.data
            imageUri?.let {
                when (currentImageType) {
                    ImageType.ID -> {
                        viewModel.idImageUri.value = it
                        binding.imageViewID.setImageURI(it)
                    }
                    ImageType.SELFIE -> {
                        viewModel.selfieImageUri.value = it
                        binding.imageViewSelfie.setImageURI(it)
                    }
                }
                // Call validateStepThree after image is uploaded
                viewModel.validateStepThree()
            }
        }
    }

    private fun showInfoBottomSheet() {
        val bottomSheet = InfoBottomSheetFragment()
        bottomSheet.show(parentFragmentManager, bottomSheet.tag)
    }

    private fun observeViewModel() {
        viewModel.idImageUri.observe(viewLifecycleOwner) { uri ->
            if (uri != null) {
                binding.imageViewID.setImageURI(uri)
            } else {
                binding.imageViewID.setImageResource(com.denzcoskun.imageslider.R.drawable.placeholder)
            }
        }

        viewModel.selfieImageUri.observe(viewLifecycleOwner) { uri ->
            if (uri != null) {
                binding.imageViewSelfie.setImageURI(uri)
            } else {
                binding.imageViewSelfie.setImageResource(com.denzcoskun.imageslider.R.drawable.placeholder)
            }
        }

    }

    private fun checkPermissionAndPickImage() {
        when {
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED -> {
                pickImage()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                Toast.makeText(requireContext(), "Permission is needed to pick an image", Toast.LENGTH_SHORT).show()
            }
            else -> {
                // User has denied the permission permanently, show dialog to navigate to settings
                showPermissionDeniedDialog()
            }
        }
    }

    private fun showPermissionDeniedDialog() {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setTitle("Permission Required")
            .setMessage("Storage access is required to pick an image. Please allow it in the app settings.")
            .setPositiveButton("Go to Settings") { dialog, _ ->
                dialog.dismiss()
                openAppSettings()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", requireActivity().packageName, null)
        }
        startActivity(intent)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

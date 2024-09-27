package com.crunchquest.android.presentation.ui.register

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            pickImage()
        } else {
            Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
        }
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

    private fun pickImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, pickImageRequestCode)
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
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

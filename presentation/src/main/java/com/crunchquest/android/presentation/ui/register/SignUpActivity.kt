package com.crunchquest.android.presentation.ui.register

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.crunchquest.android.domain.entities.User
import com.crunchquest.android.presentation.ui.dialogs.LoadingDialog
import com.crunchquest.android.presentation.ui.login.LoginActivity
import com.crunchquest.shared.databinding.ActivitySignUpPageBinding
import com.google.firebase.auth.FirebaseAuth
import java.io.ByteArrayOutputStream
import java.io.IOException

class SignUpActivity : AppCompatActivity() {
    private var _binding: ActivitySignUpPageBinding? = null
    private val binding get() = _binding!!

    private lateinit var loadingDialog: LoadingDialog
    private var capturedIdUri: Uri? = null

    companion object {
        const val IMAGE_PICK_CODE: Int = 1000
        const val DEFAULT_IMG_URL: String = "https://firebasestorage.googleapis.com/v0/b/course-project-88fec.appspot.com/o/default-images%2Fprofile_image_default.webp?alt=media&token=652ce853-0e3a-49ff-9fd7-980d02c6bb32"
        const val CAMERA_REQUEST_CODE = 1002
        const val CAMERA_PERMISSION_CODE = 1003
        private val REQUEST_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySignUpPageBinding.inflate(layoutInflater)
        setContentView(binding.root)


        loadingDialog = LoadingDialog(this, "Creating Account...")

        binding.btnSignup.setOnClickListener {
            if (checkAndRequestPermissions()) {
            }
        }

        binding.tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.imgCaptureId.setOnClickListener {
            captureId()
        }
    }


    private fun captureId() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
        } else {
            openCamera()
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera()
                } else {
                    Toast.makeText(this, "Camera Permission is Required to Use Camera.", Toast.LENGTH_SHORT).show()
                }
            }
            REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                } else {
                    Toast.makeText(this, "Permissions are required to complete registration.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val photo = data?.extras?.get("data") as Bitmap
            capturedIdUri = getImageUri(this, photo)
            binding.imgCaptureId.setImageBitmap(photo)
        }
    }

    private fun getImageUri(context: Context, image: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, image, "picture", null)
        return Uri.parse(path.toString())
    }

    private fun checkAndRequestPermissions(): Boolean {
        val permissionsToCheck = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
        val permissionsNeeded = permissionsToCheck.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()

        if (permissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsNeeded, REQUEST_CODE)
            return false
        }
        return true
    }

    private fun uploadIdToFirebaseDatabase() {
        capturedIdUri?.let { uri ->
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                val byteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                val byteArray = byteArrayOutputStream.toByteArray()
                val base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT)
                saveUserToFirebaseDatabase(base64Image)
            } catch (e: IOException) {
                Log.e("Upload ID Image", "Error converting image to Base64: $e")
                saveUserToFirebaseDatabase(DEFAULT_IMG_URL)
            }
        } ?: run {
            Log.d("Upload ID Image", "ID image URI is null")
            saveUserToFirebaseDatabase("")
        }
    }

    private fun saveUserToFirebaseDatabase(idImageUrl: String) {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let { currentUser ->
            val uid = currentUser.uid
            val user = User(
                uid,
                binding.etFirstName.text.toString(),
                binding.etLastName.text.toString(),
                binding.etEmail.text.toString(),
                idImageUrl,
                5.0,
                "14 Mei 2008",
                "Male",
                "08xxxx",
                "this is picture",
                "this is id",
                "this is preference",
                "10 Sept",
                "10 Sept New",
                "12:00:00",
                "Active",
                "CLient",
                "Verified",
                "this is work experience"
            )
            loadingDialog.dismissDialog()
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
        } ?: run {
            Log.e("Current User", "Current user is null")
            loadingDialog.dismissDialog()
        }
    }

}




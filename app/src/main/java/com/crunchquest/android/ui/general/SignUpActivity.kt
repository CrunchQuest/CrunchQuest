package com.crunchquest.android.ui.general


import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.util.Patterns
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.crunchquest.android.data.model.User
import com.crunchquest.android.data.model.UserSellerInfo
import com.crunchquest.android.databinding.ActivitySignUpPageBinding
import com.crunchquest.android.ui.dialogs.LoadingDialog
import com.crunchquest.android.utility.handlers.UserHandler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayOutputStream
import java.io.IOException


class SignUpActivity : AppCompatActivity() {
    private var _binding: ActivitySignUpPageBinding? = null
    private val binding get() = _binding!!

    lateinit var tvSignupHeading: TextView
    lateinit var tvSignupSubHeading: TextView
    lateinit var fname: EditText
    lateinit var lname: EditText
    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var confirmPass: EditText
    private lateinit var auth: FirebaseAuth
    private var user: FirebaseUser? = null
    lateinit var userHandler: UserHandler
    private lateinit var loadingDialog: LoadingDialog

    companion object {
        const val IMAGE_PICK_CODE: Int = 1000
        private const val PERMISSION_CODE = 1004
        const val DEFAULT_IMG_URL: String =
            "https://firebasestorage.googleapis.com/v0/b/course-project-88fec.appspot.com/o/default-images%2Fprofile_image_default.webp?alt=media&token=652ce853-0e3a-49ff-9fd7-980d02c6bb32"
        const val CAMERA_REQUEST_CODE = 1002
        const val CAMERA_PERMISSION_CODE = 1003
        private val REQUEST_CODE = 1001
    }

    private var selectedPhotoUri: Uri? = null
    private var capturedIdUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySignUpPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userHandler = UserHandler()
        auth = FirebaseAuth.getInstance()
        loadingDialog = LoadingDialog(this, "Creating Account...")

        tvSignupHeading = binding.tvSignupHeading
        tvSignupSubHeading = binding.tvSignupSubHeading
        fname = binding.etFirstName
        lname = binding.etLastName
        email = binding.etEmail
        password = binding.etPassword
        confirmPass = binding.etConfirmPassword
        val signUpBtn = binding.btnSignup
        val loginTextView = binding.tvLogin
        val captureIdImgView = binding.imgCaptureId

        signUpBtn.setOnClickListener {
            if (checkAndRequestPermissions()) {
                startRegistration()
            }
        }
        loginTextView.setOnClickListener {
            btnLoginAlready()
        }
        captureIdImgView.setOnClickListener {
            captureId()
        }
    }

    private fun btnLoginAlready() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
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
                if (grantResults.isNotEmpty()) {
                    grantResults.forEachIndexed { index, result ->
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Log.d("Permission Result", "Permission ${permissions[index]} not granted")
                        }
                    }
                    if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                        Log.d("Permission Result", "All permissions granted")
                        startRegistration()
                    } else {
                        Toast.makeText(this, "Permissions are required to complete registration.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            IMAGE_PICK_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    data?.data?.let {
                        // Handle image selection (existing code)
                    }
                } else {
                    Log.e("Image Pick", "Failed to pick image")
                }
            }

            CAMERA_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val photo = data?.extras?.get("data") as Bitmap
                    capturedIdUri = getImageUri(this, photo)

                    // Display captured ID image in ImageView
                    val captureIdImgView = binding.imgCaptureId
                    captureIdImgView.setImageBitmap(photo)

                    Log.d("Capture ID Image", "ID image was successfully captured: $capturedIdUri")
                } else {
                    Toast.makeText(this, "Failed to Capture Image", Toast.LENGTH_SHORT).show()
                }
            }
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
            val permissionGranted = ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
            Log.d("Permission Check", "Permission $it is granted: $permissionGranted")
            !permissionGranted
        }.toTypedArray()

        if (permissionsNeeded.isNotEmpty()) {
            // Show rationale if necessary
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show a dialog explaining why the permission is needed, then request the permission again
                // Replace this with your own dialog or method of displaying a message to the user
                AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because...")
                    .setPositiveButton("OK") { _, _ ->
                        ActivityCompat.requestPermissions(this, permissionsNeeded, REQUEST_CODE)
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create().show()
            } else {
                ActivityCompat.requestPermissions(this, permissionsNeeded, REQUEST_CODE)
            }
            return false
        } else {
            return true
        }
    }

    private fun startRegistration() {
        if (!validateInput()) return

        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("Please make sure that the Email is still active.")
            .setCancelable(true)
            .setPositiveButton("Create Account") { _, _ ->
                loadingDialog.startLoadingAnimation()
                auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            user = auth.currentUser
                            user?.sendEmailVerification()
                                ?.addOnCompleteListener { emailTask ->
                                    if (emailTask.isSuccessful) {
                                        uploadIdToFirebaseDatabase()
                                    } else {
                                        loadingDialog.dismissDialog()
                                        Toast.makeText(this, "Failed to send verification email.", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        } else {
                            val message = task.exception?.message ?: "Registration failed, please try again later."
                            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                            loadingDialog.dismissDialog()
                        }
                    }
            }
            .setNegativeButton("Edit") { dialog, _ ->
                email.requestFocus()
                dialog.cancel()
            }
        val alert = dialogBuilder.create()
        alert.setTitle("Continue?")
        alert.show()
    }

    private fun validateInput(): Boolean {
        if (fname.text.toString().isEmpty()) {
            fname.error = "Please fill this up"
            fname.requestFocus()
            return false
        }
        if (lname.text.toString().isEmpty()) {
            lname.error = "Please fill this up"
            lname.requestFocus()
            return false
        }
        if (email.text.toString().isEmpty()) {
            email.error = "Please enter email"
            email.requestFocus()
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()) {
            email.error = "Please enter valid email"
            email.requestFocus()
            return false
        }
        if (password.text.toString().isEmpty()) {
            password.error = "Please enter password"
            password.requestFocus()
            return false
        }
        if ((password.text.toString().isNotEmpty()) && (password.text.toString() != confirmPass.text.toString())) {
            confirmPass.error = "Please retype password"
            confirmPass.requestFocus()
            return false
        }
        return true
    }

    private fun uploadIdToFirebaseDatabase() {
        capturedIdUri?.let { uri ->
            try {
                // Convert image to bitmap
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)

                // Convert bitmap to Base64 string
                val byteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                val byteArray = byteArrayOutputStream.toByteArray()
                val base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT)

                // Save the Base64 string to Firebase Database
                saveUserToFirebaseDatabase(base64Image)
            } catch (e: IOException) {
                Log.e("Upload ID Image", "Error converting image to Base64: $e")
                saveUserToFirebaseDatabase(DEFAULT_IMG_URL)
            }
        } ?: run {
            // If capturedIdUri is null, log the error and proceed with default image URL
            Log.d("Upload ID Image", "ID image URI is null")
            saveUserToFirebaseDatabase(DEFAULT_IMG_URL)
        }
    }


    private fun saveUserToFirebaseDatabase(idImageUrl: String) {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let { currentUser ->
            val uid = currentUser.uid
            val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

            val user = User(
                uid = currentUser.uid,
                emailAddress = email.text.toString(),
                firstName = fname.text.toString(),
                lastName = lname.text.toString(),
                profileImageUrl = DEFAULT_IMG_URL,
                idImageUrl = idImageUrl
            )
            ref.setValue(user)

            val anotherRef = FirebaseDatabase.getInstance().getReference("user_seller_info/${user.uid}")
            val userSellerInfo = UserSellerInfo(
                uid = currentUser.uid
            )
            anotherRef.setValue(userSellerInfo)

            val notificationsRef = FirebaseDatabase.getInstance().getReference("notifications/")
            notificationsRef.child("${user.uid}").setValue(0)

            loadingDialog.dismissDialog()
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
        } ?: run {
            Log.e("Current User", "Current user is null")
            loadingDialog.dismissDialog()
        }
    }

    private fun getRealPathFromURI(context: Context, contentUri: Uri): String? {
        val cursor = context.contentResolver.query(contentUri, null, null, null, null)
        return if (cursor == null) {
            contentUri.path
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            cursor.getString(idx).also { cursor.close() }
        }
    }
}
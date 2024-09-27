@file:Suppress("DEPRECATION")

package com.crunchquest.android.presentation.ui.general

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.crunchquest.shared.R
import com.crunchquest.android.domain.entities.User
import com.crunchquest.android.presentation.ui.MainActivity
import com.crunchquest.android.presentation.ui.dialogs.LoadingDialog
import com.crunchquest.android.presentation.ui.register.SignUpActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream
import java.io.IOException

class SendRequirementActivity : AppCompatActivity() {

    private lateinit var tbar: Toolbar
    private lateinit var submitButton: Button
    private lateinit var id: CardView
    private lateinit var idImageView: FloatingActionButton

    private var capturedIdUri: Uri? = null

    private lateinit var loadingDialog: LoadingDialog

    companion object {
        const val ID = 1
        const val TAG: String = "SAMPLETAG"
        const val REQUEST_CAMERA_PERMISSION = 100
        const val CAMERA_PERMISSION_CODE = 101
        const val IMAGE_PICK_CODE = 102
        const val CAMERA_REQUEST_CODE = 103
        const val DEFAULT_IMG_URL = "default_img_url"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_requirement)

        val mode = intent.getStringExtra(com.crunchquest.android.presentation.ui.general.ChooseActivity.Companion.TAG)

        loadingDialog = LoadingDialog(this, "Submitting requirements...")

        tbar = findViewById(R.id.toolbar_sendRequirementsActivity)
        submitButton = findViewById(R.id.submitButton_activitySendRequirements)
        id = findViewById(R.id.validId_activitySendRequirements)
        idImageView = findViewById(R.id.validIdImageView_activitySendRequiresments)

        tbar.setNavigationOnClickListener {
            finish()
        }

        id.setOnClickListener {
            takePicture()
        }

        submitButton.setOnClickListener {
            if (checkAndRequestPermissions()) {
                checkImages()
            }
        }
    }

    private fun takePicture() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),
                com.crunchquest.android.presentation.ui.general.SendRequirementActivity.Companion.REQUEST_CAMERA_PERMISSION
            )
        } else {
            openCamera()
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent,
            com.crunchquest.android.presentation.ui.general.SendRequirementActivity.Companion.CAMERA_REQUEST_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            com.crunchquest.android.presentation.ui.general.SendRequirementActivity.Companion.IMAGE_PICK_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    data?.data?.let {
                        // Handle image selection (existing code)
                    }
                } else {
                    Log.e("Image Pick", "Failed to pick image")
                }
            }

            com.crunchquest.android.presentation.ui.general.SendRequirementActivity.Companion.CAMERA_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val photo = data?.extras?.get("data") as Bitmap
                    capturedIdUri = getImageUri(this, photo)

                    changeColors()

                    Log.d("Capture ID Image", "ID image was successfully captured: $capturedIdUri")
                } else {
                    Toast.makeText(this, "Failed to Capture Image", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun changeColors() {
        val color = this.resources.getColor(R.color.done)

        if (capturedIdUri != null) {
            idImageView.backgroundTintList = ColorStateList.valueOf(color)
        }
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
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create().show()
            } else {
            }
            return false
        } else {
            return true
        }
    }


    private fun checkImages() {
        if (capturedIdUri != null) {
            uploadIdToFirebaseDatabase()
        } else {
            Toast.makeText(this, "Submit the requirements needed.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadImage(ref: StorageReference, imageData: ByteArray, type: Int) {
        Log.d(com.crunchquest.android.presentation.ui.general.SendRequirementActivity.Companion.TAG, "Attempting to upload image of type $type")
        loadingDialog.startLoadingAnimation()
        ref.putBytes(imageData)
            .addOnSuccessListener {
                Log.d(com.crunchquest.android.presentation.ui.general.SendRequirementActivity.Companion.TAG, "Image upload successful")
                ref.downloadUrl.addOnSuccessListener { uri ->
                    Log.d(com.crunchquest.android.presentation.ui.general.SendRequirementActivity.Companion.TAG, "Download URL retrieved: $uri")
                    saveToDatabase(uri.toString(), type)
                    uploadIdToFirebaseDatabase()
                }
            }
            .addOnFailureListener { exception ->
                Log.e(com.crunchquest.android.presentation.ui.general.SendRequirementActivity.Companion.TAG, "Error uploading image", exception)
                loadingDialog.dismissDialog()
                Toast.makeText(this, "Error uploading image", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveToDatabase(url: String, type: Int) {
        Log.d(com.crunchquest.android.presentation.ui.general.SendRequirementActivity.Companion.TAG, "saveToDatabase called with url: $url, type: $type")
        val currentUserUid = FirebaseAuth.getInstance().currentUser!!.uid
        val ref = FirebaseDatabase.getInstance().getReference("user_seller_info/$currentUserUid")
        val accountRef = FirebaseDatabase.getInstance().getReference("/users/$currentUserUid")

        accountRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)!!
                ref.child("idImageUrl").setValue(user.idCard)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        when (type) {
            com.crunchquest.android.presentation.ui.general.SendRequirementActivity.Companion.ID -> ref.child("idImageUrl").setValue(url)
        }

        loadingDialog.dismissDialog()
        finish()
        Toast.makeText(this, "Requirement submitted. Wait for account to be verified by the admin. Thank you.", Toast.LENGTH_LONG).show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            com.crunchquest.android.presentation.ui.general.SendRequirementActivity.Companion.CAMERA_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera()
                } else {
                    Toast.makeText(this, "Camera Permission is Required to Use Camera.", Toast.LENGTH_SHORT).show()
                }
            }
        }
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
                saveUserToFirebaseDatabase(SignUpActivity.Companion.DEFAULT_IMG_URL)
            }
        } ?: run {
            // If capturedIdUri is null, log the error and proceed with default image URL
            Log.d("Upload ID Image", "ID image URI is null")
            saveUserToFirebaseDatabase("")
        }
    }

    private fun getImageUri(context: Context, image: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, image, "picture", null)
        return Uri.parse(path.toString())
    }

    private fun saveUserToFirebaseDatabase(idImageUrl: String) {

        val user = FirebaseAuth.getInstance().currentUser
        user?.let { currentUser ->
            val uid = currentUser.uid
            val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

            val childUpdates = hashMapOf<String, Any>("idImageUrl" to idImageUrl)
            ref.updateChildren(childUpdates)

            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        } ?: run {
            Log.e("Current User", "Current user is null")
        }
    }
}



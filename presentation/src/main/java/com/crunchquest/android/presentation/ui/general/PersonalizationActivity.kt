package com.crunchquest.android.presentation.ui.general

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.CheckBox
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.crunchquest.shared.R
import com.crunchquest.shared.databinding.ActivityPersonalizationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class PersonalizationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPersonalizationBinding
    private lateinit var servicesCategories: Array<String>
    private lateinit var checkboxes: MutableList<CheckBox>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalizationBinding.inflate(layoutInflater)
//        enableEdgeToEdge()
        setContentView(binding.root)

        servicesCategories = resources.getStringArray(R.array.category)
        createCheckboxes()

        // Set onClickListener for the next button
        binding.btnNext.setOnClickListener {
            savePreferences()
            // Proceed to the next screen or perform any desired action
            val intent = Intent(this@PersonalizationActivity, SplashScreenActivity::class.java)
            startActivity(intent)
        }
    }

    private fun createCheckboxes() {
        checkboxes = mutableListOf()
        for (category in servicesCategories) {
            val customCheckboxLayout = layoutInflater.inflate(R.layout.custom_checkbox, null) as CheckBox
            customCheckboxLayout.text = category

            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(0, 0, 0, 24)
            customCheckboxLayout.layoutParams = layoutParams

            checkboxes.add(customCheckboxLayout)

            binding.checkboxContainer.addView(customCheckboxLayout)
        }
    }

    // saving to firebase realtime database
    private fun savePreferences() {
        val selectedPreferences = mutableListOf<Int>()
        for ((index, checkbox) in checkboxes.withIndex()) {
            if (checkbox.isChecked) {
                selectedPreferences.add(index)
            }
        }

        // Convert the list of preferences to a map with index as key and value as 1 if checked, 0 otherwise
        val preferencesMap = mutableMapOf<String, Int>()
        for (i in 0 until servicesCategories.size) {
            preferencesMap["$i"] = if (selectedPreferences.contains(i)) 1 else 0
        }

        // Update Firebase with the selected preferences
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUserUid != null) {
            val preferencesRef = FirebaseDatabase.getInstance().reference
                .child("users")
                .child(currentUserUid)
                .child("preferences")

            preferencesRef.setValue(preferencesMap)
                .addOnSuccessListener {
                    // Preferences saved successfully
                }
                .addOnFailureListener { exception ->
                    // Handle any errors
                    Log.e("TAG", "Error saving preferences: $exception")
                }
        }
    }
}

package com.crunchquest.android.ui.buyer.buyer_activities

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.crunchquest.android.R
import com.crunchquest.android.data.model.User
import com.crunchquest.android.data.model.UserSellerInfo
import com.crunchquest.android.data.model.UserSkills
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BottomFragmentShowProfile : BottomSheetDialogFragment() {


    //user as seller info -> this is for the first card view where all informations are from the model UserSellerInfo
    lateinit var sellingDescriptionTextView: TextView
    lateinit var previousSchoolTextView: TextView
    lateinit var educationalAttainmentTextView: TextView

    //List view for the skills
    lateinit var skillsListView: ListView
    var skillsArrayList: ArrayList<UserSkills> = ArrayList()
    lateinit var skillsArrayAdapter: ArrayAdapter<UserSkills>

    //user info
    lateinit var bio: TextView
    lateinit var number: TextView

    override fun getTheme(): Int = R.style.AppBottomSheetDialogTheme

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_bottom_showprofile, container, false)
        //Map the views for the user seller info
        sellingDescriptionTextView = v.findViewById(R.id.sellingDescriptionTextView_fragmentBottomShowProfile)
        previousSchoolTextView = v.findViewById(R.id.previousSchool_fragmentBottomShowProfile)
        educationalAttainmentTextView = v.findViewById(R.id.educationalAttainment_fragmentBottomShowProfile)
        bio = v.findViewById(R.id.bio_fragmentBottomShowProfile)
        number = v.findViewById(R.id.number_fragmentBottomShowProfile)


        //Skills Array List
        skillsListView = v.findViewById(R.id.skillsListView_fragmentBottomShowProfile)
        //Makes the list view scrollable once the user touched it. To view all the skills included by the User.
        skillsListView.setOnTouchListener { _, _ ->
            // Setting on Touch Listener for handling the touch inside ScrollView
            // Disallow the touch request for parent scroll on touch of child view
            v.parent.requestDisallowInterceptTouchEvent(true)
            false
        }
        fetchUserSellerInfoData()
        fetchSkills(v.context)
        fetchUserData()
        return v


    }

    private fun fetchSkills(v: Context) {
        val ref = FirebaseDatabase.getInstance().getReference("/user_skills")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                skillsArrayList.clear()
                p0.children.forEach {
                    val userSkills = it.getValue(UserSkills::class.java)
                    if (userSkills!!.userUid == DisplaySpecificServiceActivity.userUidForFragment) {
                        skillsArrayList.add(userSkills)
                    }
                }
                skillsArrayAdapter = ArrayAdapter(v, android.R.layout.simple_list_item_1, skillsArrayList)
                skillsListView.adapter = skillsArrayAdapter

            }

            override fun onCancelled(p0: DatabaseError) {

            }


        })
    }

    private fun fetchUserSellerInfoData() {
        val ref = FirebaseDatabase.getInstance().getReference("user_seller_info/${DisplaySpecificServiceActivity.userUidForFragment}")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userSellerInfo = snapshot.getValue(UserSellerInfo::class.java)
                if (userSellerInfo != null) {
                    sellingDescriptionTextView.text = "${userSellerInfo.description}"
                    previousSchoolTextView.text = "${userSellerInfo.previousSchool}"
                    educationalAttainmentTextView.text = userSellerInfo.educationalAttainment
                } else {
                    sellingDescriptionTextView.text = "No description available"
                    previousSchoolTextView.text = "No previous school information available"
                    educationalAttainmentTextView.text = "No educational attainment information available"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Log the error
                Log.e("DatabaseError", error.message)
                // Show a toast message to the user
                Toast.makeText(context, "Failed to fetch user seller info: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchUserData() {
        val ref = FirebaseDatabase.getInstance().getReference("/users/${DisplaySpecificServiceActivity.userUidForFragment}")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val user = p0.getValue(User::class.java)
                if (user != null) {
                    bio.text = user.bio
                    number.text = user.mobileNumber
                } else {
                    bio.text = "No bio available"
                    number.text = "No number available"
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                // Log the error
                Log.e("DatabaseError", p0.message)
                // Show a toast message to the user
                Toast.makeText(context, "Failed to fetch user data: ${p0.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        DisplaySpecificServiceActivity.userUidForFragment = null
    }


}
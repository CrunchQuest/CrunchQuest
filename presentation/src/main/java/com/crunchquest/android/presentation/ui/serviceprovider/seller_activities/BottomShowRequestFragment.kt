package com.crunchquest.android.presentation.ui.serviceprovider.seller_activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.crunchquest.shared.R
import com.crunchquest.android.domain.model.ServiceRequest
import com.crunchquest.android.domain.entities.User
import com.crunchquest.android.presentation.ui.messages.RequestChatLogActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class BottomShowRequestFragment : BottomSheetDialogFragment() {

    lateinit var v: View
    private lateinit var imageView: ImageView
    private lateinit var textView: TextView
    private lateinit var floatButton: FloatingActionButton
    override fun getTheme(): Int = R.style.AppBottomSheetDialogTheme

    var request: com.crunchquest.android.domain.model.ServiceRequest = BuyersRequestActivity.serviceRequestToBeViewed!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_bottom_show_request, container, false)
        imageView = v.findViewById(R.id.imageVIew_fragmentBottomShowRequest)
        textView = v.findViewById(R.id.textView_fragmentBottomShowRequest)
        floatButton = v.findViewById(R.id.floatingActionButton_fragmentBottomShowRequest)

        val ref = FirebaseDatabase.getInstance().getReference("users/${BuyersRequestActivity.serviceRequestToBeViewed!!.userUid}")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userData = snapshot.getValue(User::class.java)
                Picasso.get().load(userData!!.profilePicture).into(imageView)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        textView.text = "${BuyersRequestActivity.serviceRequestToBeViewed!!.description}"

        floatButton.setOnClickListener {
            goToRequestChatLog()


        }






        return v
    }

    private fun goToRequestChatLog() {
        val intent = Intent(v.context, RequestChatLogActivity::class.java)
        intent.putExtra("user", BuyersRequestActivity.serviceRequestToBeViewed!!.userUid)
        startActivity(intent)
    }

//    private fun fetchTheUserWhoPostedTheRequest() {
//        val ref = FirebaseDatabase.getInstance().getReference("users/${BuyersRequestActivity.serviceRequestToBeViewed!!.userUid}")
//        ref.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val toUser = snapshot.getValue(User::class.java)
//                val intent = Intent(v.context, ChatLogActivity::class.java)
//                intent.putExtra("user", toUser)
//                startActivity(intent)
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//
//            }
//
//        })
//
//    }


}


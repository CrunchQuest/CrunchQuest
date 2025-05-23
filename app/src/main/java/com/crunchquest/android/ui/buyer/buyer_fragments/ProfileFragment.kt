package com.crunchquest.android.ui.buyer.buyer_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.crunchquest.android.R
import com.crunchquest.android.data.model.User
import com.crunchquest.android.ui.buyer.BuyerActivity
import com.crunchquest.android.ui.buyer.bottomNavigationBuyer
import com.crunchquest.android.ui.buyer.buyer_activities.ManageRequestActivity
import com.crunchquest.android.ui.buyer.buyer_activities.RequestActivity
import com.crunchquest.android.utility.handlers.UserHandler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import android.content.Intent as Intent1


class ProfileFragment : Fragment() {



    //Used to fetch data
    var userHandler = UserHandler()
    var userArrayList: ArrayList<User> = ArrayList()
    var userData: User? = null
    var uid = FirebaseAuth.getInstance().uid

    //until here
    lateinit var v: View
    lateinit var postRequestCardView: CardView
    lateinit var manageRequestCardView: CardView
    var auth = FirebaseAuth.getInstance().currentUser
    lateinit var profileImage: ImageView
    lateinit var name: TextView

    companion object {

        var isBuyerMode: Boolean = true
    }


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_profile, container, false)
        //map the views of the layout file
        profileImage = v.findViewById(R.id.profileImage)
        name = v.findViewById(R.id.tvUsername)

        postRequestCardView = v.findViewById(R.id.cvPostRequest)
        manageRequestCardView = v.findViewById(R.id.cvManageRequest)
        //Apply listeners
        //Post Request
        postRequestCardView.setOnClickListener {
            val intent = Intent1(v.context, RequestActivity::class.java)
            startActivity(intent)
        }
        //Manage Request
        manageRequestCardView.setOnClickListener {
            val intent = Intent1(v.context, ManageRequestActivity::class.java)
            startActivity(intent)

        }
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            fetchUserData()
        }

        return v
    }




    override fun onResume() {
        super.onResume()
        (activity as BuyerActivity?)?.setActionBarTitle("Profile")
        bottomNavigationBuyer.menu.findItem(R.id.profilePage).isChecked = true
    }


    private fun fetchUserData() {
        if (FirebaseAuth.getInstance().currentUser != null) {
            val uid = FirebaseAuth.getInstance().currentUser!!.uid
            val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
            ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    val user = p0.getValue(User::class.java)
                    Picasso.get().load(user!!.profileImageUrl).into(profileImage)
                    name.text = "${user.firstName} ${user.lastName}"
                }

                override fun onCancelled(p0: DatabaseError) {
                    //TODO("Not yet implemented")
                }
            })

        }

    }
}
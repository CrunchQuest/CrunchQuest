package com.crunchquest.android.presentation.ui.serviceprovider.seller_fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.crunchquest.shared.R
import com.crunchquest.android.domain.entities.User
import com.crunchquest.android.presentation.ui.serviceprovider.SellerActivity
import com.crunchquest.android.presentation.ui.serviceprovider.bottomNavigationSeller
import com.crunchquest.android.presentation.ui.serviceprovider.seller_activities.AboutMeAsSellerActivity
import com.crunchquest.android.presentation.ui.serviceprovider.seller_activities.BuyersRequestActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class SellerProfileFragment : Fragment() {

    val sellerServices = SellerServicesFragment()
    lateinit var profileImage: ImageView
    lateinit var nameTextView: TextView
    lateinit var services: CardView
    lateinit var buyersRequestCardView: CardView
    lateinit var aboutMeAsASellerCardView: CardView
    lateinit var v: View
    var uid = FirebaseAuth.getInstance().uid

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_seller_profile, container, false)

        //map the views of the layout file
        aboutMeAsASellerCardView = v.findViewById(R.id.cvAboutMe)
        services = v.findViewById<CardView>(R.id.cvMyServices)
        profileImage = v.findViewById(R.id.sellerProfileImage)
        nameTextView = v.findViewById(R.id.tvSellerUsername)
        buyersRequestCardView = v.findViewById(R.id.cvClientRequest)

        //Fetch user data
        fetchUserData()

        //Add listeners
        //Switch button view

        //Services card view
        services.setOnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.wrapper, sellerServices)
            transaction?.disallowAddToBackStack()
            transaction?.commit()
        }
        //Buyers Request
        buyersRequestCardView.setOnClickListener {
            val intent = Intent(v.context, BuyersRequestActivity::class.java)
            startActivity(intent)
        }
        //About me as a seller
        aboutMeAsASellerCardView.setOnClickListener {
            val intent = Intent(v.context, AboutMeAsSellerActivity::class.java)
            startActivity(intent)

        }



        return v
    }

    private fun fetchUserData() {
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val user = p0.getValue(User::class.java)
                Picasso.get().load(user!!.profilePicture).into(profileImage)
                nameTextView.text = "${user.firstName} ${user.lastName}"
            }

            override fun onCancelled(p0: DatabaseError) {
                //TODO("Not yet implemented")
            }
        })
    }



    companion object;

    override fun onResume() {
        super.onResume()
        // Set title bar
        (activity as SellerActivity?)?.setActionBarTitle("Profile Page")
        (activity as SellerActivity).menuItem =
                bottomNavigationSeller.menu.findItem(R.id.Seller_profilePage).setChecked(true)

    }


}
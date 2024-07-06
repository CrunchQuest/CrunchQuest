package com.crunchquest.android.ui.dialogs

import android.app.AlertDialog
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.crunchquest.android.R
import com.crunchquest.android.data.model.Order
import com.crunchquest.android.data.model.UserPerformance
import com.crunchquest.android.data.model.UserReview
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ReviewDialog(fragment: Fragment, order: Order, private val servicesCategories: Array<String>) {
    private val fragment: Fragment = fragment
    private val order: Order = order
    private var dialog: AlertDialog? = null
    private lateinit var ratingBar: RatingBar
    private lateinit var confirmButton: Button
    private lateinit var laterButton: Button
    private lateinit var editText: EditText
    lateinit var v: View


    fun showReviewDialog() {
        val builder = AlertDialog.Builder(fragment.context)
        val inflater = fragment.layoutInflater
        v = inflater.inflate(R.layout.dialog_review, null)
        builder.setView(v)
        builder.setCancelable(false)

        ratingBar = v.findViewById(R.id.ratingBar_dialogReview)
        confirmButton = v.findViewById(R.id.confirmButton_dialogReview)
        laterButton = v.findViewById(R.id.laterButton_dialogReview)
        editText = v.findViewById(R.id.editText_dialogReview)

        laterButton.setOnClickListener {
            dismissDialog()
        }

        confirmButton.setOnClickListener {
            submitReview()
        }


        dialog = builder.create()
        dialog!!.show()

    }

    private fun submitReview() {
        val userBeingReviewed = order.bookedTo!!
        val currentUserUid = FirebaseAuth.getInstance().currentUser!!.uid
        val ref = FirebaseDatabase.getInstance().getReference("reviews/$userBeingReviewed")
        val id = ref.push().key!!
        val review = UserReview(
                uid = id,
                userUid = currentUserUid,
                review = editText.text.toString(),
                rating = ratingBar.rating.toInt(),
                categoryId = servicesCategories.indexOf(order.category)
        )
        ref.child(id).setValue(review)

        val bookedByRef = FirebaseDatabase.getInstance().getReference("booked_by/${order.bookedBy}/${order.service_booked_uid}/${order.bookedBy}")
        bookedByRef.child("/reviewed").setValue(true)

        val bookedToInByRef = FirebaseDatabase.getInstance().getReference("booked_by/${order.bookedBy}/${order.service_booked_uid}/${order.bookedTo}")
        bookedToInByRef.child("/reviewed").setValue(true)
        
        val bookedToRef = FirebaseDatabase.getInstance().getReference("booked_to/${order.bookedTo}/${order.service_booked_uid}")
        bookedToRef.child("/reviewed").setValue(true)

        // Set buyer review
        val bookedTo = order.bookedTo
        val bookingUid = order.service_booked_uid
        val refReviewBuyer = FirebaseDatabase.getInstance().getReference("booked_to/$bookedTo/$bookingUid")
        refReviewBuyer.child("buyerReview").setValue(review.review)

        // Update user performance
        val performanceRef = FirebaseDatabase.getInstance().getReference("user_performance/${order.bookedTo}/${review.categoryId}")
        performanceRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val performance = snapshot.getValue(UserPerformance::class.java) ?: UserPerformance()
                performance.rating += ratingBar.rating.toInt()
                performance.total += 1
                performanceRef.setValue(performance)
            }

            override fun onCancelled(error: DatabaseError) {}
        })


        Toast.makeText(v.context, "Thank you for the review!", Toast.LENGTH_SHORT).show()
        dismissDialog()

    }

    fun dismissDialog() {
        dialog!!.dismiss()
    }

}
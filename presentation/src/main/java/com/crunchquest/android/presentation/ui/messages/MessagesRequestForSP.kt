package com.crunchquest.android.presentation.ui.messages

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.crunchquest.shared.R
import com.crunchquest.android.domain.model.RequestMessage
import com.crunchquest.android.domain.model.ServiceRequest
import com.crunchquest.android.presentation.ui.components.groupie_views.LatestMessagesRowRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder


class MessagesRequestForSP : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var rv: RecyclerView
    val adapter = GroupAdapter<ViewHolder>()

    companion object{
        const val TAG = "ASDASDASDASDASDDSADAV"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages_request_for_s_p)
        toolbar = findViewById(R.id.toolbar5)
        rv = findViewById(R.id.rv)
        rv.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        rv.adapter = adapter

        clickListeners()
        listenAgain()

    }

    private fun clickListeners() {
        toolbar.setNavigationOnClickListener {
            finish()
        }
        adapter.setOnItemClickListener { item, _ ->
            val intent = Intent(this, RequestChatLogActivity::class.java)
            val row = item as LatestMessagesRowRequest

            val ref = FirebaseDatabase.getInstance().getReference("service_requests/${row.message.requestUid}")
    }

    }


    val latestMessagesMap = HashMap<String, com.crunchquest.android.domain.model.RequestMessage>()
    private fun refreshRecyclerView() {
        adapter.clear()
        latestMessagesMap.values.forEach { message ->
            adapter.add(LatestMessagesRowRequest(message))
        }

    }


    fun listenAgain(){
        val fromId = FirebaseAuth.getInstance().currentUser!!.uid
        val ref = FirebaseDatabase.getInstance().getReference("/latest_messages_request/$fromId/")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach { requestUid ->
                    requestUid.children.forEach { latestMessage ->
                        val latestMessageValue = latestMessage.getValue(com.crunchquest.android.domain.model.RequestMessage::class.java)
                        Log.i(TAG, latestMessageValue!!.uid!!)
                        val message = latestMessage.getValue(com.crunchquest.android.domain.model.RequestMessage::class.java) ?: return
                        latestMessagesMap[latestMessageValue.uid.toString()] = message

                    }


                }
                refreshRecyclerView()

            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }


}
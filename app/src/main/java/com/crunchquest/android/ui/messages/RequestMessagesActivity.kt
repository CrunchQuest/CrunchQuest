package com.crunchquest.android.ui.messages

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.crunchquest.android.R
import com.crunchquest.android.data.model.Message
import com.crunchquest.android.data.model.ServiceRequest
import com.crunchquest.android.ui.buyer.buyer_activities.ManageRequestActivity
import com.crunchquest.android.ui.components.groupie_views.LatestMessageRow
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder

class RequestMessagesActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    private lateinit var toolBar: androidx.appcompat.widget.Toolbar
    val adapter = GroupAdapter<ViewHolder>()
    var request: ServiceRequest? = null
    
    companion object {
        const val TAG = "Imoutoftags"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_messages)

        request = intent.getParcelableExtra(ManageRequestActivity.TAG)!!

        recyclerView = findViewById(R.id.recycelrView_requests)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.adapter = adapter

        toolBar = findViewById(R.id.toolbar4)
        toolBar.setNavigationOnClickListener {
            finish()
        }
        toolBar.title = request!!.title

        listenForLatestMessages()

        init()
    }

    private fun init() {
        adapter.setOnItemClickListener { item, _ ->
            val intent = Intent(this, RequestChatLogActivity::class.java)
            val row = item as LatestMessageRow
            intent.putExtra(RequestChatLogActivity.USER_EXTRA_TAG, row.chatPartnerUser)
            intent.putExtra(RequestChatLogActivity.REQUEST_EXTRA_TAG, request)
            startActivity(intent)
        }
    }


    val latestMessagesMap = HashMap<String, Message>()
    private fun refreshRecyclerView() {
        adapter.clear()
        latestMessagesMap.values.forEach { message ->
            adapter.add(LatestMessageRow(message))
        }

    }

    fun listenForLatestMessages() {
        val fromId = FirebaseAuth.getInstance().currentUser!!.uid
        val ref = FirebaseDatabase.getInstance().getReference("/latest_messages_request/$fromId/${request!!.uid}")
        ref.addChildEventListener(object : ChildEventListener {
            //New user messages us
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(Message::class.java) ?: return
                latestMessagesMap[snapshot.key!!] = message
                refreshRecyclerView()

            }

            //old user messages us again with a new message.
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(Message::class.java) ?: return
                latestMessagesMap[snapshot.key!!] = message
                refreshRecyclerView()

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }


}
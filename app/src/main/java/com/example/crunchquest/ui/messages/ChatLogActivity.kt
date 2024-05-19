@file:Suppress("DEPRECATION")

package com.example.crunchquest.ui.messages

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.example.crunchquest.R
import com.example.crunchquest.data.model.Message
import com.example.crunchquest.data.model.User
import com.example.crunchquest.ui.buyer.BuyerActivity
import com.example.crunchquest.ui.components.groupie_views.ChatFromItem
import com.example.crunchquest.ui.components.groupie_views.ChatToItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder

class ChatLogActivity : AppCompatActivity() {

    companion object {
        val TAG = "ChatLog"
    }

    lateinit var toolbar: Toolbar
    lateinit var recyclerView: RecyclerView
    lateinit var button: Button
    lateinit var editText: EditText
    val adapter = GroupAdapter<ViewHolder>()


    var toUser: User? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "ChatLogActivity started.")
        setContentView(R.layout.activity_chat_log)

        toUser = intent.getParcelableExtra<User>("user")
        //Map everything here
        toolbar = findViewById(R.id.toolbar_activityChatLog)
        recyclerView = findViewById(R.id.recyclerView_activityChatLog)
        button = findViewById(R.id.button_activityChatLog)
        editText = findViewById(R.id.editText_activityChatLog)
        //Attach the adapter to recycler view
        recyclerView.adapter = adapter

        //Toolbar
        toolbar.setNavigationOnClickListener {
            finish()
        }
        if (toUser != null) {
            toolbar.title = toUser!!.firstName + " " + toUser!!.lastName
            Log.d(TAG, "Debug Title: ${toolbar.title}")
        } else {
            // Handle the case where toUser is null
            // For example, you can set a default title for the toolbar
            toolbar.title = "Debug Chat"
            Log.d(TAG, "toUser is null")
        }

        //Handle the recycler view here
        //setUpDummyData()
        listenForMessages()

        //button
        button.setOnClickListener {
            Log.d(TAG, "Attempted to send message.")
            performSendMessage()

        }


        Log.d(TAG, "ChatLogActivity finished.")
    }


    private fun listenForMessages() {
        Log.d(TAG, "Listening for messages.")
        val fromId = FirebaseAuth.getInstance().currentUser!!.uid
        val toId = toUser!!.uid
        // val messageHandler = MessageHandler()
        val ref = FirebaseDatabase.getInstance().getReference("/user_messages/$fromId/$toId")
        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(Message::class.java)
                if (message != null && !message.text.isNullOrEmpty()) {
                    Log.d(TAG, "Message data: ${message.text}, from: ${message.fromId}, to: ${message.toId}, timestamp: ${message.timeStamp}")
                    if (message.fromId == FirebaseAuth.getInstance().currentUser!!.uid) {
                        val currentUser = BuyerActivity.currentUser ?: return
                        adapter.add(ChatFromItem(message.text!!, currentUser, message.timeStamp!!))
                    } else {
                        adapter.add(ChatToItem(message.text!!, toUser!!, message.timeStamp!!))
                    }
                    adapter.notifyDataSetChanged()
                    recyclerView.scrollToPosition(adapter.itemCount - 1)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "onCancelled called with Error: ${error.message}")
            }

        })
        Log.d(TAG, "Finished listening for messages.")
    }

    private fun performSendMessage() {
        Log.d(TAG, "performSendMessage started.")
        val currentUser = FirebaseAuth.getInstance().currentUser!!.uid
        val getNameRef = FirebaseDatabase.getInstance().getReference("users/$currentUser")
        getNameRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d(TAG, "onDataChange called.")
                val user = snapshot.getValue(User::class.java)
                val name = "${user!!.firstName} ${user.lastName}"

                if (editText.text.toString().isEmpty()) return
                val toId = toUser!!.uid
                val fromId = FirebaseAuth.getInstance().currentUser!!.uid
                val text = editText.text.toString()
                val ref = FirebaseDatabase.getInstance().getReference("/user_messages/$fromId/$toId").push()
                val toRef = FirebaseDatabase.getInstance().getReference("/user_messages/$toId/$fromId").push()
                val message = Message(
                    uid = ref.key,
                    text = text,
                    fromId = fromId,
                    toId = toId,
                    timeStamp = System.currentTimeMillis(),
                    senderName = name)
                ref.setValue(message)
                        .addOnSuccessListener {
                            editText.text.clear()
                            recyclerView.scrollToPosition(adapter.itemCount - 1)
                        }
                toRef.setValue(message)

                val latestMessageRef = FirebaseDatabase.getInstance().getReference("/latest_messages/$fromId/$toId")
                latestMessageRef.setValue(message)

                val latestMessageToRef = FirebaseDatabase.getInstance().getReference("/latest_messages/$toId/$fromId")
                latestMessageToRef.setValue(message)

                Toast.makeText(this@ChatLogActivity, "Message sent", Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "onCancelled called with Error: ${error.message}")
            }

        })
        Log.d(TAG, "performSendMessage finished.")

    }


}




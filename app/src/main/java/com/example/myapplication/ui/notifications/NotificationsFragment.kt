package com.example.myapplication.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import android.R.string.no
import android.R.attr.name
import android.R



class NotificationsFragment : Fragment() {

    private lateinit var notifyView: View
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mFirebaseDatabase: FirebaseDatabase
    private lateinit var mReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        notifyView = inflater.inflate(R.layout.fragment_notifications, container, false)

                //RecyclerView
                mRecyclerView = notifyView.findViewById(R.id.recyclerViewNotif)
                mRecyclerView.setHasFixedSize(true)

                //set layout as Linear Layout
                mRecyclerView.layoutManager = LinearLayoutManager(context)

                //send Query to FirebaseDatabase
                mFirebaseDatabase = FirebaseDatabase.getInstance()
                mReference = mFirebaseDatabase.reference.child("Notification")

        return notifyView
     }

    //load data into recycler view onStart
    override fun onStart() {
        super.onStart()

        var options = FirebaseRecyclerOptions.Builder<Notification>().setQuery(mReference, Notification::class.java).build()
        var firebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<Notification, NotificationViewHolder>(options){

            override fun onBindViewHolder(holder: NotificationViewHolder, position: Int, notification: Notification) {
                holder.setNotification(context, notification.notifyTitle, notification.notifyVenue, notification.notifyDate)
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
                val inflater: LayoutInflater = LayoutInflater.from(parent.context)
                val itemView = inflater.inflate(R.layout.notification_row, parent, false)
                return NotificationViewHolder(itemView)
            }
        }
        //set adapter to recycler view
        mRecyclerView.adapter = firebaseRecyclerAdapter
        firebaseRecyclerAdapter.startListening()
    }

    val mIth = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: ViewHolder, target: ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
                // remove from adapter
            }
        })
}
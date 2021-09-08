package com.example.btbeaconlab

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class CustomAdapter(private val mList: List<ItemsViewModel>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_design, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = mList[position]

        // sets the text to the textview and color from our itemHolder class
        if(ItemsViewModel.isConnectable) {
            holder.deviceName.setTextColor(Color.parseColor("#000000"))
            holder.deviceAddress.setTextColor(Color.parseColor("#000000"))
            holder.deviceRssi.setTextColor(Color.parseColor("#000000"))

            holder.deviceName.text = ItemsViewModel.deviceName
            holder.deviceAddress.text = ItemsViewModel.deviceAddress
            holder.deviceRssi.text = ItemsViewModel.deviceRssi
        } else {
            holder.deviceName.setTextColor(Color.parseColor("#999696"))
            holder.deviceAddress.setTextColor(Color.parseColor("#999696"))
            holder.deviceRssi.setTextColor(Color.parseColor("#999696"))

            holder.deviceName.text = ItemsViewModel.deviceName
            holder.deviceAddress.text = ItemsViewModel.deviceAddress
            holder.deviceRssi.text = ItemsViewModel.deviceRssi
        }


    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val deviceName: TextView = itemView.findViewById(R.id.deviceName)
        val deviceAddress: TextView = itemView.findViewById(R.id.deviceAddress)
        val deviceRssi: TextView = itemView.findViewById(R.id.deviceRssi)

    }
}

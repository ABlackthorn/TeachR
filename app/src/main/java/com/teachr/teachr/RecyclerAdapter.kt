package com.teachr.teachr

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import org.w3c.dom.Text
import java.util.ArrayList

/**
 * Created by Parsania Hardik on 26-Jun-17.
 */
class RecyclerAdapter(ctx: Context, private val list: MutableList<Entry>) : RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {

    private val inflater: LayoutInflater


    init {

        inflater = LayoutInflater.from(ctx)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.MyViewHolder {

        val view = inflater.inflate(R.layout.cardview, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.MyViewHolder, position: Int) {
        holder.course.text = list[position].subject
        holder.address.text = list[position].latitude.toString()
        holder.duration.text = list[position].duration.toString()
        holder.date.text = list[position].date
        holder.name.text = list[position].user
        holder.price.text = list[position].price.toString()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var course: TextView
        var address: TextView
        var duration: TextView
        var date: TextView
        var name: TextView
        var price: TextView

        init {
            course = itemView.findViewById(R.id.course) as TextView
            address = itemView.findViewById(R.id.address) as TextView
            duration = itemView.findViewById(R.id.duration) as TextView
            date = itemView.findViewById(R.id.date) as TextView
            name = itemView.findViewById(R.id.name) as TextView
            price = itemView.findViewById(R.id.price) as TextView
        }

    }
}
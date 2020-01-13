package com.example.moviecatalogue_made_s2.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moviecatalogue_made_s2.Model.Show
import com.example.moviecatalogue_made_s2.R
import com.example.moviecatalogue_made_s2.ui.Activity.DetailShowActivity
import kotlinx.android.synthetic.main.item_show.view.*

class ListShowAdapter(private val listShow: ArrayList<Show>) : RecyclerView.Adapter<ListShowAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_show, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = listShow.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listShow[position])
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(show: Show) {
            with(itemView){
                com.bumptech.glide.Glide.with(itemView.context)
                    .load(show.getPortraitPhoto())
                    .into(img_item_photo)
                tv_item_name.text = show.name?.substring(0, Math.min(show.name!!.length, 50))
                val overview = show.overview?.substring(0, Math.min(show.overview!!.length, 120))+"..."
                tv_item_overview.text = overview

                itemView.setOnClickListener {
                    val detailIntent = Intent(context,
                        DetailShowActivity::class.java)
                    detailIntent.putExtra("detailShow",show)
                    context.startActivity(detailIntent)
                }
            }
        }
    }
}
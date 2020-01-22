package com.example.moviecatalogue_made_s2.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moviecatalogue_made_s2.R
import com.example.moviecatalogue_made_s2.model.Show
import com.example.moviecatalogue_made_s2.ui.activity.DetailShowActivity
import kotlinx.android.synthetic.main.item_show.view.*

class ListShowAdapter :
    RecyclerView.Adapter<ListShowAdapter.ListViewHolder>() {

    private val listShow = ArrayList<Show>()

    fun setData(items: ArrayList<Show>) {
        listShow.clear()
        listShow.addAll(items)
        notifyDataSetChanged()
    }

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
            with(itemView) {
                com.bumptech.glide.Glide.with(itemView.context)
                    .load(show.getPortraitPhoto())
                    .into(img_item_photo)
                show.name?.let {
                    tv_item_name.text = show.name?.substring(0, it.length.coerceAtMost(50))
                }
                show.overview?.let {
                    tv_item_overview.text = context.getString(
                        R.string.overview_list,
                        show.overview?.substring(0, it.length.coerceAtMost(120))
                    )
                }

                itemView.setOnClickListener {
                    val detailIntent = Intent(
                        context,
                        DetailShowActivity::class.java
                    )
                    detailIntent.putExtra("detailShow", show)
                    context.startActivity(detailIntent)
                }
            }
        }
    }
}
package com.example.moviecatalogue_made_s2.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.moviecatalogue_made_s2.R
import com.example.moviecatalogue_made_s2.model.Show
import com.example.moviecatalogue_made_s2.ui.CustomOnItemClickListener
import com.example.moviecatalogue_made_s2.ui.activity.DetailShowActivity
import com.example.moviecatalogue_made_s2.ui.activity.DetailShowActivity.Companion.DETAIL_SHOW
import com.example.moviecatalogue_made_s2.ui.activity.DetailShowActivity.Companion.EXTRA_POSITION
import com.example.moviecatalogue_made_s2.ui.activity.DetailShowActivity.Companion.EXTRA_TYPE
import kotlinx.android.synthetic.main.item_show.view.*

class ListShowAdapter(private val fragment: Fragment, showType: String) :
    RecyclerView.Adapter<ListShowAdapter.ListViewHolder>() {

    private val showsType = showType

    private var listShow = ArrayList<Show>()
        set(listShow) {
            if (listShow.size > 0) {
                this.listShow.clear()
            }
            this.listShow.addAll(listShow)
            notifyDataSetChanged()
        }


    fun setData(items: ArrayList<Show>) {
        listShow.clear()
        listShow.addAll(items)
        notifyDataSetChanged()
    }

    fun addItem(show: Show) {
        this.listShow.add(show)
        notifyItemInserted(this.listShow.size - 1)
    }

    fun removeItem(position: Int) {
        this.listShow.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listShow.size)
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

                itemView.setOnClickListener(
                    CustomOnItemClickListener(
                        adapterPosition,
                        object :
                            CustomOnItemClickListener.OnItemClickCallback {
                            override fun onItemClicked(view: View, position: Int) {
                                val detailIntent =
                                    Intent(fragment.context, DetailShowActivity::class.java)
                                Log.d("show type", showsType)
                                detailIntent.putExtra(DETAIL_SHOW, show)
                                detailIntent.putExtra(EXTRA_TYPE, showsType)
                                detailIntent.putExtra(EXTRA_POSITION, position)
                                fragment.startActivityForResult(
                                    detailIntent,
                                    DetailShowActivity.REQUEST_VIEW
                                )
                            }
                        })
                )
            }
        }
    }
}
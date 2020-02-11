package com.example.moviecatalogue_made_s2.adapter

import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moviecatalogue_made_s2.R
import com.example.moviecatalogue_made_s2.model.Show
import com.example.moviecatalogue_made_s2.ui.activity.DetailShowActivity
import com.example.moviecatalogue_made_s2.ui.activity.DetailShowActivity.Companion.DETAIL_SHOW
import com.example.moviecatalogue_made_s2.ui.activity.DetailShowActivity.Companion.EXTRA_POSITION
import com.example.moviecatalogue_made_s2.ui.activity.DetailShowActivity.Companion.EXTRA_TYPE
import com.example.moviecatalogue_made_s2.ui.listener.CustomOnItemClickListener
import com.example.moviecatalogue_made_s2.utils.Constant
import kotlinx.android.synthetic.main.item_show_grid.view.*

class SearchShowAdapter(private val activity: Activity, showType: String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val showsType = showType

    private var listShow = ArrayList<Show?>()


    fun setData(items: ArrayList<Show>) {
        listShow.clear()
        listShow.addAll(items)
        notifyDataSetChanged()
    }

    fun getData(): ArrayList<Show?> {
        return listShow
    }


    override fun getItemViewType(position: Int): Int {
        return if (listShow[position] == null) {
            Constant.VIEW_TYPE_LOADING
        } else {
            Constant.VIEW_TYPE_ITEM
        }
    }

    fun addLoadingView() {
        Handler().post {
            listShow.add(null)
            notifyItemInserted(listShow.size - 1)
        }
    }

    fun removeLoadingView() {
        if (listShow.size != 0) {
            listShow.removeAt(listShow.size - 1)
            notifyItemRemoved(listShow.size)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            Constant.VIEW_TYPE_ITEM -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_show_grid, parent, false)
                GridViewHolder(view)
            }
            else -> {
                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.item_load, parent, false)
                LoadingViewHolder(view)
            }
        }
    }

    fun getItemAtPosition(position: Int): Show? {
        return listShow[position]
    }

    override fun getItemCount(): Int = listShow.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == Constant.VIEW_TYPE_ITEM) {
            listShow[position]?.let { (holder as GridViewHolder).bind(it) }
        }
    }

    inner class GridViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(show: Show) {
            with(itemView) {
                if (show.imgPath == null) {
                    com.bumptech.glide.Glide.with(itemView.context)
                        .load(R.raw.image_not_available)
                        .override(200, 200)
                        .fitCenter()
                        .into(img_item_photo_grid)
                } else {
                    com.bumptech.glide.Glide.with(itemView.context)
                        .load(show.getPortraitPhoto())
                        .into(img_item_photo_grid)
                }
                show.name?.let {
                    tv_item_name_grid.text = show.name?.substring(0, it.length.coerceAtMost(50))
                }
                itemView.setOnClickListener(
                    CustomOnItemClickListener(
                        adapterPosition,
                        object :
                            CustomOnItemClickListener.OnItemClickCallback {
                            override fun onItemClicked(view: View, position: Int) {
                                val detailIntent =
                                    Intent(context, DetailShowActivity::class.java)
                                Log.d("show type", showsType)
                                detailIntent.putExtra(DETAIL_SHOW, show)
                                detailIntent.putExtra(EXTRA_TYPE, showsType)
                                detailIntent.putExtra(EXTRA_POSITION, position)
                                activity.startActivityForResult(
                                    detailIntent,
                                    DetailShowActivity.REQUEST_VIEW
                                )
                            }
                        })
                )
            }
        }
    }

    inner class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}
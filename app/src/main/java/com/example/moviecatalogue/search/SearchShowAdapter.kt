package com.example.moviecatalogue.search

import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.example.moviecatalogue.R
import com.example.moviecatalogue.data.source.local.entity.ShowEntity
import com.example.moviecatalogue.detail.DetailShowActivity
import com.example.moviecatalogue.detail.DetailShowActivity.Companion.DETAIL_SHOW
import com.example.moviecatalogue.detail.DetailShowActivity.Companion.EXTRA_POSITION
import com.example.moviecatalogue.detail.DetailShowActivity.Companion.EXTRA_TYPE
import com.example.moviecatalogue.listener.CustomOnItemClickListener
import com.example.moviecatalogue.utils.Constant
import com.example.moviecatalogue.utils.GlideApp
import kotlinx.android.synthetic.main.item_show_grid.view.*

class SearchShowAdapter(private val activity: Activity, showType: String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val showsType = showType

    private var listShow = ArrayList<ShowEntity?>()


    fun setData(items: List<ShowEntity>) {
        listShow.clear()
        listShow.addAll(items)
        notifyDataSetChanged()
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

    override fun getItemCount(): Int = listShow.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == Constant.VIEW_TYPE_ITEM) {
            listShow[position]?.let { (holder as GridViewHolder).bind(it) }
        }
    }

    inner class GridViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(show: ShowEntity) {
            with(itemView) {
                GlideApp.with(itemView.context)
                    .load(show.getPortraitPhoto())
                    .placeholder(R.drawable.ic_image_black)
                    .error(R.drawable.ic_image_error_black)
//                    .apply(
//                        RequestOptions.placeholderOf(R.drawable.ic_image_black)
//                            .error(R.drawable.ic_image_error_black)
//                    )
                    .into(img_item_photo_grid)
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
                                detailIntent.putExtra(DETAIL_SHOW, show.movieDbId)
                                detailIntent.putExtra(EXTRA_TYPE, showsType)
                                detailIntent.putExtra(EXTRA_POSITION, position)
                                activity.startActivity(detailIntent)
                            }
                        })
                )
            }
        }
    }

    inner class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}
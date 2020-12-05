package com.example.moviecatalogue.shows

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.moviecatalogue.R
import com.example.moviecatalogue.data.source.local.entity.ShowEntity
import com.example.moviecatalogue.detail.DetailShowActivity
import com.example.moviecatalogue.detail.DetailShowActivity.Companion.DETAIL_SHOW
import com.example.moviecatalogue.detail.DetailShowActivity.Companion.EXTRA_POSITION
import com.example.moviecatalogue.detail.DetailShowActivity.Companion.EXTRA_TYPE
import com.example.moviecatalogue.listener.CustomOnItemClickListener
import com.example.moviecatalogue.utils.Constant
import kotlinx.android.synthetic.main.item_show.view.*

class ListShowAdapter(private val fragment: Fragment, showType: String) :
    PagedListAdapter<ShowEntity, ListShowAdapter.ListViewHolder>(DIFF_CALLBACK) {

    private val type = showType

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ShowEntity>() {
            override fun areItemsTheSame(oldItem: ShowEntity, newItem: ShowEntity): Boolean {
                return oldItem.movieDbId == newItem.movieDbId
            }

            override fun areContentsTheSame(oldItem: ShowEntity, newItem: ShowEntity): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_show, parent, false)
        return ListViewHolder(view)
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(show: ShowEntity) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(show.getPortraitPhoto())
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_image_black)
                            .error(R.drawable.ic_image_error_black)
                    )
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
                                Log.d("show type", type)
                                detailIntent.putExtra(DETAIL_SHOW, show)
                                detailIntent.putExtra(EXTRA_TYPE, type)
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

    inner class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val show = getItem(position)
        if (show != null) {
            holder.bind(show)
        }
    }

}
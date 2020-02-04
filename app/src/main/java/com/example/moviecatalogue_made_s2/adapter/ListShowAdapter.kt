package com.example.moviecatalogue_made_s2.adapter

import android.content.Intent
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.moviecatalogue_made_s2.R
import com.example.moviecatalogue_made_s2.model.Show
import com.example.moviecatalogue_made_s2.ui.listener.CustomOnItemClickListener
import com.example.moviecatalogue_made_s2.ui.activity.DetailShowActivity
import com.example.moviecatalogue_made_s2.ui.activity.DetailShowActivity.Companion.DETAIL_SHOW
import com.example.moviecatalogue_made_s2.ui.activity.DetailShowActivity.Companion.EXTRA_POSITION
import com.example.moviecatalogue_made_s2.ui.activity.DetailShowActivity.Companion.EXTRA_TYPE
import com.example.moviecatalogue_made_s2.utils.Constant
import kotlinx.android.synthetic.main.item_show.view.*

class ListShowAdapter(private val fragment: Fragment, showType: String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val showsType = showType

    private var listShow = ArrayList<Show?>()


    fun setData(items: ArrayList<Show>) {
        listShow.clear()
        listShow.addAll(items)
        notifyDataSetChanged()
    }

    fun getData(): ArrayList<Show?>{
        return listShow
    }

    fun addData( data : ArrayList<Show>){
        listShow.addAll(data)
        notifyDataSetChanged()
    }

    fun addItem(show: Show?) {
        this.listShow.add(show)
        notifyItemInserted(this.listShow.size - 1)
    }

    fun removeItem(position: Int) {
        this.listShow.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listShow.size)
    }

    override fun getItemViewType(position: Int): Int {
        return if (listShow[position] == null) {
            Constant.VIEW_TYPE_LOADING
        } else {
            Constant.VIEW_TYPE_ITEM
        }
    }

    fun addLoadingView() {
        //Add loading item
        Handler().post {
            listShow.add(null)
            notifyItemInserted(listShow.size - 1)
        }
    }

    fun removeLoadingView() {
        //Remove loading item
        if (listShow.size != 0) {
            listShow.removeAt(listShow.size - 1)
            notifyItemRemoved(listShow.size)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            Constant.VIEW_TYPE_ITEM -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_show, parent, false)
                ListViewHolder(view)
            }
            else ->{
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_load, parent, false)
                LoadingViewHolder(view)
            }
        }
    }

    fun getItemAtPosition(position: Int): Show? {
        return listShow[position]
    }

    override fun getItemCount(): Int = listShow.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == Constant.VIEW_TYPE_ITEM){
            listShow[position]?.let { (holder as ListViewHolder).bind(it) }
        }
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

    inner class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}
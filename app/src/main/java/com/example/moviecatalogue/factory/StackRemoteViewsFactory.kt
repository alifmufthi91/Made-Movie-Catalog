package com.example.moviecatalogue.factory

import android.content.Context
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.bumptech.glide.Glide
import com.example.moviecatalogue.R
import com.example.moviecatalogue.db.FavouriteHelper
import com.example.moviecatalogue.model.Show

internal class StackRemoteViewsFactory(private val mContext: Context) :
    RemoteViewsService.RemoteViewsFactory {

    private val mWidgetItems = ArrayList<Show>()

    override fun onCreate() {
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getItemId(p0: Int): Long = 0

    override fun onDataSetChanged() {
        val favouriteHelper = FavouriteHelper.getInstance(mContext)
        favouriteHelper.open()
        mWidgetItems.addAll(favouriteHelper.getAllData())
        favouriteHelper.close()
    }

    override fun hasStableIds(): Boolean = false

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(
            mContext.packageName,
            R.layout.widget_item
        )
        val imgBitmap = Glide.with(mContext)
            .asBitmap()
            .load(mWidgetItems[position].getLandscapePhoto())
            .submit(300, 300)
            .get()
        rv.setImageViewBitmap(R.id.iv_widgetItem, imgBitmap)
        rv.setTextViewText(R.id.tv_widgetItem, mWidgetItems[position].name)

        return rv
    }

    override fun getCount(): Int = mWidgetItems.size

    override fun getViewTypeCount(): Int = 1

    override fun onDestroy() {
    }
}
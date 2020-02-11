package com.example.moviecatalogue_made_s2.factory

import android.content.Context
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.bumptech.glide.Glide
import com.example.moviecatalogue_made_s2.R
import com.example.moviecatalogue_made_s2.db.FavoritesHelper
import com.example.moviecatalogue_made_s2.model.Show

internal class StackRemoteViewsFactory(private val mContext: Context) :
    RemoteViewsService.RemoteViewsFactory {

    private val mWidgetItems = ArrayList<Show>()

    override fun onCreate() {
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getItemId(p0: Int): Long = 0

    override fun onDataSetChanged() {
        val favoritesHelper = FavoritesHelper.getInstance(mContext)
        favoritesHelper.open()
        mWidgetItems.addAll(favoritesHelper.getAllData())
        favoritesHelper.close()
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
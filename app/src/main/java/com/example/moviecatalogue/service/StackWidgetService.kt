package com.example.moviecatalogue.service

import android.content.Intent
import android.widget.RemoteViewsService
import com.example.moviecatalogue.remoteviews.StackRemoteViewsFactory

class StackWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory =
        StackRemoteViewsFactory(this.applicationContext)
}
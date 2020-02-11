package com.example.moviecatalogue_made_s2.service

import android.content.Intent
import android.widget.RemoteViewsService
import com.example.moviecatalogue_made_s2.factory.StackRemoteViewsFactory

class StackWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory =
        StackRemoteViewsFactory(this.applicationContext)
}
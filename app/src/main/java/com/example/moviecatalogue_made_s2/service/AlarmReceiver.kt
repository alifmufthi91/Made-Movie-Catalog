package com.example.moviecatalogue_made_s2.service

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import com.example.moviecatalogue_made_s2.BuildConfig
import com.example.moviecatalogue_made_s2.R
import com.example.moviecatalogue_made_s2.model.ShowList
import com.example.moviecatalogue_made_s2.ui.fragment.MovieFragment
import com.example.moviecatalogue_made_s2.utils.MovieDB
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        const val TYPE_REMINDER = "Reminder"
        const val TYPE_RELEASETODAY = "ReleaseToday"
        const val EXTRA_TYPE = "type"

        const val ID_REMINDER = 100
        const val ID_RELEASETODAY = 101
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.d("onReceive", "Boot Completed")
            val sh = PreferenceManager.getDefaultSharedPreferences(context)
            if (sh.getBoolean(
                    context.resources.getString(R.string.key_reminder),
                    false
                )
            ) setReminderAlarm(context)
            if (sh.getBoolean(
                    context.resources.getString(R.string.key_release),
                    false
                )
            ) setTodayReleaseAlarm(context)
        } else {
            when (intent.getStringExtra(EXTRA_TYPE)) {
                TYPE_RELEASETODAY -> {
                    val retrofit = Retrofit.Builder()
                        .baseUrl("https://api.themoviedb.org")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                    val movieDBClient = retrofit.create(MovieDB::class.java)
                    val date = Calendar.getInstance().time
                    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val currentDate = formatter.format(date)
                    val call = movieDBClient.getTodayReleases(
                        MovieFragment.SHOW_MOVIE.toLowerCase(Locale.getDefault()),
                        BuildConfig.API_KEY, currentDate, currentDate
                    )
                    call.enqueue(object : Callback<ShowList> {
                        override fun onResponse(
                            call: Call<ShowList>,
                            response: Response<ShowList>
                        ) {
                            Log.d("getReleaseToday", "success..")
                            val showList = response.body()
                            if (showList != null) {
                                var notifId = 100
                                for (show in showList.list) {
                                    showNotification(
                                        context,
                                        context.resources.getString(
                                            R.string.today_release_title,
                                            show.name
                                        ),
                                        context.resources.getString(R.string.today_release_subtitle),
                                        notifId++
                                    )
                                }
                            }
                        }

                        override fun onFailure(call: Call<ShowList>, t: Throwable) {
                            Log.d("getReleaseToday", "failed..")
                        }

                    })
                }
                TYPE_REMINDER -> {
                    val notifId = 99
                    showNotification(
                        context,
                        context.resources.getString(R.string.app_name),
                        context.resources.getString(R.string.reminder),
                        notifId
                    )
                }
            }
        }
    }

    private fun showNotification(context: Context, title: String, message: String, notifId: Int) {
        val CHANNEL_ID = "Channel_1"
        val CHANNEL_NAME = "Job scheduler channel"
        val notificationManagerCompat =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(title)
            .setSmallIcon(R.drawable.ic_movie_foreground)
            .setContentText(message)
            .setColor(
                ContextCompat.getColor(
                    context,
                    R.color.colorAccent
                )
            )
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            builder.setChannelId(CHANNEL_ID)
            notificationManagerCompat.createNotificationChannel(channel)
        }
        val notification = builder.build()
        notificationManagerCompat.notify(notifId, notification)
    }

    fun setTodayReleaseAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(
            EXTRA_TYPE,
            TYPE_RELEASETODAY
        )
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 8)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            ID_RELEASETODAY, intent, 0
        )
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
        Toast.makeText(context, context.getString(R.string.toast_alert_set), Toast.LENGTH_SHORT)
            .show()
    }

    fun setReminderAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(
            EXTRA_TYPE,
            TYPE_REMINDER
        )
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 7)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            ID_REMINDER, intent, 0
        )
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
        Toast.makeText(context, context.getString(R.string.toast_reminder_set), Toast.LENGTH_SHORT)
            .show()
    }

    fun cancelTodayReleaseAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(
            EXTRA_TYPE,
            TYPE_RELEASETODAY
        )
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 8)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            ID_RELEASETODAY, intent, 0
        )
        alarmManager.cancel(pendingIntent)
    }

    fun cancelReminderAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(
            EXTRA_TYPE,
            TYPE_REMINDER
        )
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 7)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            ID_REMINDER, intent, 0
        )
        alarmManager.cancel(pendingIntent)
    }

}

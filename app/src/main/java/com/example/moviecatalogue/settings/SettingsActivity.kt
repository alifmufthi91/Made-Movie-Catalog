package com.example.moviecatalogue.settings

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.example.moviecatalogue.R
import dagger.android.support.DaggerAppCompatActivity

class SettingsActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.settings)
        supportFragmentManager
            .beginTransaction()
            .add(
                R.id.settings_container,
                SettingsPreferenceFragment()
            )
            .commit()


    }

//    fun switchTheme() = object : SwitchPreferenceCompat(this@SettingsActivity){
//        override fun onClick() {
//            super.onClick()
//        }
//    }

    class SettingsPreferenceFragment : PreferenceFragmentCompat(),
        SharedPreferences.OnSharedPreferenceChangeListener {

        private lateinit var REMINDER: String
        private lateinit var RELEASE_TODAY: String
        private lateinit var NIGHT_MODE: String
        private lateinit var CHANGE_LANGUAGE: String
        private lateinit var DELETE_FAVORITES: String

//        private lateinit var alarmReceiver: AlarmReceiver

        private lateinit var reminderPreference: SwitchPreferenceCompat
        private lateinit var releaseTodayPreference: SwitchPreferenceCompat
        private lateinit var changeLanguagePreference: Preference
        private lateinit var deleteFavoritesPreference: Preference
        private lateinit var changeNightMode: SwitchPreferenceCompat

//        private lateinit var favsHelper: FavouriteHelper

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.settings, rootKey)
            init()
            setSummaries()
        }

        override fun onResume() {
            super.onResume()
            preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        }

        override fun onPause() {
            super.onPause()
            preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        }

        private fun setSummaries() {
            val sh = preferenceManager.sharedPreferences
            reminderPreference.isChecked = sh.getBoolean(REMINDER, false)
            releaseTodayPreference.isChecked = sh.getBoolean(RELEASE_TODAY, false)
        }

        private fun init() {
//            favsHelper = FavouriteHelper(requireContext())
            REMINDER = resources.getString(R.string.key_reminder)
            RELEASE_TODAY = resources.getString(R.string.key_release)
            CHANGE_LANGUAGE = resources.getString(R.string.key_change_language)
            DELETE_FAVORITES = resources.getString(R.string.key_delete_favorites)
//            NIGHT_MODE = resources.getString(R.string.key_night_mode)
//            alarmReceiver =
//                AlarmReceiver()

            reminderPreference =
                findPreference<SwitchPreferenceCompat>(REMINDER) as SwitchPreferenceCompat
            releaseTodayPreference =
                findPreference<SwitchPreferenceCompat>(RELEASE_TODAY) as SwitchPreferenceCompat
//            changeNightMode = findPreference<SwitchPreferenceCompat>(NIGHT_MODE) as SwitchPreferenceCompat
            changeLanguagePreference = findPreference<Preference>(CHANGE_LANGUAGE) as Preference

            changeLanguagePreference.onPreferenceClickListener =
                Preference.OnPreferenceClickListener {
                    val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                    startActivity(mIntent)
                    true
                }

            deleteFavoritesPreference = findPreference<Preference>(DELETE_FAVORITES) as Preference
//            deleteFavoritesPreference.onPreferenceClickListener =
//                Preference.OnPreferenceClickListener {
//                    val dialogBuilder = AlertDialog.Builder(requireContext())
//                        .setTitle(getString(R.string.are_you_sure))
//                        .setMessage(getString(R.string.will_be_deleted))
//                        .setNegativeButton(getString(R.string.no)) { _, _ ->
//
//                        }
//                        .setPositiveButton(getString(R.string.yes)) { _, _ ->
//                            favsHelper.open()
//                            val count = favsHelper.deleteAll()
//                            favsHelper.close()
//                            Toast.makeText(
//                                context,
//                                getString(R.string.count_deleted, count.toString()),
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                        .setCancelable(false)
//                        .create()
//                    dialogBuilder.show()
//                    true
//                }

        }

        override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
            when (key) {
                REMINDER -> {
                    reminderPreference.isChecked = sharedPreferences.getBoolean(REMINDER, false)
//                    if (reminderPreference.isChecked) {
//                        alarmReceiver.setReminderAlarm(requireContext())
//                    } else {
//                        alarmReceiver.cancelReminderAlarm(requireContext())
//                    }
                }
                RELEASE_TODAY -> {
                    releaseTodayPreference.isChecked =
                        sharedPreferences.getBoolean(RELEASE_TODAY, false)
//                    if (releaseTodayPreference.isChecked) {
//                        alarmReceiver.setTodayReleaseAlarm(requireContext())
//                    } else {
//                        alarmReceiver.cancelTodayReleaseAlarm(requireContext())
//                    }
                }
                NIGHT_MODE -> {
                    changeNightMode.isChecked = sharedPreferences.getBoolean(NIGHT_MODE, false)
                    if (changeNightMode.isChecked) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }
                }
            }

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}

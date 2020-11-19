package com.gradient.starfruit

import android.Manifest
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.SplashTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val CHANNEL_ID = "starfruit"

        fun createNotificationChannel() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = "Starfruit motivational quotes"
                val descriptionText = "Starfruit delivers fresh motivational quotes via notifications."
                val importance = NotificationManager.IMPORTANCE_HIGH

                val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                    description = descriptionText
                    lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                }
                val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
        }

        createNotificationChannel()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val homeFragment = HomeFragment()
        val aboutFragment = AboutFragment()
        val settingsFragment = SettingsFragment()

        makeCurrentFragment(homeFragment)

        val navbar = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        navbar.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.ic_home -> {
                    makeCurrentFragment(homeFragment)
                    supportActionBar?.setTitle("Starfruit")
                }
                R.id.ic_settings -> {
                    makeCurrentFragment(settingsFragment)
                    supportActionBar?.setTitle("Settings")
                }
                R.id.ic_about -> {
                    makeCurrentFragment(aboutFragment)
                    supportActionBar?.setTitle("About")
                }
            }
            true
        }

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_star)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //todo: setup AlarmManager

        // Get AlarmManager instance
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Intent part
        val intent = Intent(this, AlarmReceiver::class.java)
        intent.action = "FOO_ACTION"
        intent.putExtra("KEY_FOO_STRING", "Starfruit quote sent!")

        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)

        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 7)
            set(Calendar.MINUTE, 10)
        }

        if (calendar.timeInMillis < System.currentTimeMillis()) { // checks if alarm time is earlier than system time
            calendar.add(Calendar.DAY_OF_YEAR, 1) // goes to next day
        }

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )

        //todo: get sms permissions [WORKING]

        @Suppress("DEPRECATED_IDENTITY_EQUALS")

        // this code checks for sendsms permission and asks for it the app doesn't have it
        if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.SEND_SMS
            ) !== PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@MainActivity,
                    Manifest.permission.SEND_SMS
                )
            ) {
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(Manifest.permission.SEND_SMS),
                    1
                )
            } else {
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(Manifest.permission.SEND_SMS),
                    1
                )
            }
        }
    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }

    // code below runs when alarm is triggered
    class AlarmReceiver : BroadcastReceiver() {
        @Suppress("DEPRECATION")
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == "FOO_ACTION") {
                val fooString = intent.getStringExtra("KEY_FOO_STRING")
                Toast.makeText(context, fooString, Toast.LENGTH_LONG).show()

                // quote picker below --------------------
                val quoteNum = (0..200).random()
                val quote = context.resources.openRawResource(R.raw.quotes)
                    .bufferedReader().useLines { it.elementAtOrNull(quoteNum) ?: "" }

                // send notification ---------------------

                val CHANNEL_ID = "starfruit"
                val notificationId = 6275

                val intent = Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

                val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.ic_star)
                val bitmapLargeIcon = BitmapFactory.decodeResource(context.resources, R.drawable.ic_quotes)

                val builder = context.let {
                    NotificationCompat.Builder(it, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_star)
                        .setLargeIcon(bitmapLargeIcon)
                        .setContentTitle("Daily quote!")
                        .setContentText(quote)
                        .setStyle(NotificationCompat.BigTextStyle())
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent)
                }

                with(context.let { NotificationManagerCompat.from(it) }) {
                    if (builder != null) {
                        this.notify(notificationId, builder.build())
                    }
                }

                // set daily quote -------------------------

                val preferences: SharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(context)
                preferences.edit().putString("dailyQuote", quote).apply()
            }
        }
    }
    // this part marks the end of the OnCreate, just in mainactivity now
}

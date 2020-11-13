package com.gradient.starfruit

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.os.Bundle
import android.os.Vibrator
import android.preference.PreferenceManager
import android.telephony.SmsManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.SplashTheme);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

        // Set with system Alarm Service
        // Other possible functions: setExact() / setRepeating() / setWindow(), etc

        //todo: set up NumberPicker

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

                // todo: call auto quote picker
                // quote picker below --------------------
                val quoteNum = (0..90).random()
                val quote = context.resources.openRawResource(R.raw.quotes)
                    .bufferedReader().useLines { it.elementAtOrNull(quoteNum) ?: "" }
                // quote picker above --------------------

                //gets phone number saved earlier from preferences
                val preferences: SharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(context)
                val num = (preferences.getString("phoneNumber", "").toString())

                // check if quote has more than 70 characters, and split if needed
                if (quote.count() > 70) {
                    val smsManager = SmsManager.getDefault()
                    var quoteParts = smsManager.divideMessage(quote)
                    // send split quote portions separately with a for loop
                    for (quote in quoteParts) {
                        smsManager.sendTextMessage(num, null, quote, null, null)
                    }
                }
                //send everything in one piece if quote is 70 chars or under
                else {
                    val smsManager = SmsManager.getDefault()
                    smsManager.sendTextMessage(num, null, quote, null, null)
                    val vibrator =
                        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator; vibrator.vibrate(
                        400
                    )
                }
            }
        }
    }
    // this part marks the end of the OnCreate, just in mainactivity now
}

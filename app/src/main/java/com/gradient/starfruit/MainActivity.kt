package com.gradient.starfruit

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.os.Bundle
import android.os.Vibrator
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
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        this.supportActionBar?.hide()
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

//        val smsbutton: Button = findViewById(R.id.smsButton)
//        smsbutton.setOnClickListener{
//            sendMessage("If you got this, SMS seems to be working!")
//            Toast.makeText(this, "SMS test message sent!", Toast.LENGTH_SHORT).show()
//        }

        //todo: TimePicker

        //todo: setup AlarmManager

        // Get AlarmManager instance
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Intent part
        val intent = Intent(this, AlarmReceiver::class.java)
        intent.action = "FOO_ACTION"
        intent.putExtra("KEY_FOO_STRING", "Alarm triggered!")

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

        //todo: send the message [WORKING]

//        if (ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
//            sendMessage("Hello from Starfruit! \uD83C\uDF20")
//        }
    }

    fun sendMessage(text: String) {
        val smsManager = SmsManager.getDefault()
        smsManager.sendTextMessage("+12672747668", null, text, null, null)
    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }

    class AlarmReceiver : BroadcastReceiver() {
        @Suppress("DEPRECATION")
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == "FOO_ACTION") {
                val fooString = intent.getStringExtra("KEY_FOO_STRING")
                Toast.makeText(context, fooString, Toast.LENGTH_LONG).show()

                // quote that is being sent. Will be read from a json in the future, but is solid for now
                var quote =
                    "“At any given moment you have the power to say: this is not how the story is going to end.” – Unknown"

                // check if quote has more than 70 characters, and split if neede
                if (quote.count() > 70) {
                    val smsManager = SmsManager.getDefault()
                    var quoteParts = smsManager.divideMessage(quote)
                    // send split quote portions separately with a for loop
                    for (quote in quoteParts) {
                        smsManager.sendTextMessage("+12672747668", null, "$quote ", null, null)
                    }
                }
                //send everything in one piece if quote is 70 chars or under
                else {
                    val smsManager = SmsManager.getDefault()
                    smsManager.sendTextMessage("+12672747668", null, quote, null, null)
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

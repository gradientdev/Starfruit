package com.gradient.starfruit

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SmsManager
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
            when (it.itemId){
                R.id.ic_home -> {
                    makeCurrentFragment(homeFragment)
                    getSupportActionBar()?.setTitle("Starfruit")
                }
                R.id.ic_settings -> {
                    makeCurrentFragment(settingsFragment)
                    getSupportActionBar()?.setTitle("Settings")
                }
                R.id.ic_about -> {
                    makeCurrentFragment(aboutFragment)
                    getSupportActionBar()?.setTitle("About")
                }
            }
            true
        }

        //todo: start AlarmManger to trigger message

        //todo: set up NumberPicker

        //todo: get sms permissions [WORKING]

        @Suppress("DEPRECATED_IDENTITY_EQUALS")

        // this code checks for sendsms permission and asks for it the app doesn't have it
        if (ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.SEND_SMS) !== PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity, Manifest.permission.SEND_SMS)) {
                ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.SEND_SMS), 1)
            } else {
                ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.SEND_SMS), 1)
            }
        }

        //todo: send the message [WORKING]

        if (ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            sendMessage("Hello from Starfruit! \uD83C\uDF20")
        }
}
    fun sendMessage(text: String) {
        val smsManager: SmsManager = SmsManager.getDefault()
        smsManager.sendTextMessage("+12672747668", null, text, null, null);
    }

    private fun makeCurrentFragment(fragment: Fragment) =
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fl_wrapper, fragment)
                    commit()
                }
}

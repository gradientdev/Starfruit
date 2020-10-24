package com.gradient.starfruit

import android.os.Bundle
import android.telephony.SmsManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val homeFragment = HomeFragment()
        val aboutFragment = AboutFragment()
        val settingsFragment = SettingsFragment()

        makeCurrentFragment(homeFragment)

        val navbar = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        navbar.setOnNavigationItemSelectedListener {
            when (it.itemId){
                R.id.ic_home -> makeCurrentFragment(homeFragment)
                R.id.ic_settings -> makeCurrentFragment(settingsFragment)
                R.id.ic_about -> makeCurrentFragment(aboutFragment)
            }
            true
        }

        //todo: add text function here



        val smsManager: SmsManager = SmsManager.getDefault()

        smsManager.sendTextMessage("+12672747668", null, "hello!", null, null);

}
        private fun makeCurrentFragment(fragment: Fragment) =
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fl_wrapper, fragment)
                    commit()
                }
}

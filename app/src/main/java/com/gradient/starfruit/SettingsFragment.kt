package com.gradient.starfruit

import android.content.Context
import android.content.SharedPreferences
import android.net.ParseException
import android.os.Bundle
import android.os.Vibrator
import android.preference.PreferenceManager
import android.telephony.SmsManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_settings.view.*

@Suppress("DEPRECATION")
class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_settings, container, false)

        val phonePicker = view.phonePicker

        try {
            val preferences: SharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context)
            val num: String = (preferences.getString("phoneNumber", "").toString())

            if (num != null) {
                view.phonePicker.setText(num)
            }
        } catch (e: ParseException) {
        }

        view.saveButton.setOnClickListener { view ->
            var phoneNum = phonePicker.text.toString()
            Log.d("smsSave", "Selected")
            Log.d("smsSave", phoneNum)

            val preferences: SharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context)
            preferences.edit().putString("phoneNumber", phoneNum).apply();

            Toast.makeText(context, "Number saved!", Toast.LENGTH_LONG).show()
            val vibrator =
                context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator; vibrator.vibrate(
            100)
        }

        view.smsButton.setOnClickListener { view ->
            Log.d("smsSend", "Selected")
            val smsManager = SmsManager.getDefault()

            //gets phone number saved earlier from preferences
            val preferences: SharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context)
            val num = (preferences.getString("phoneNumber", "").toString())

            smsManager.sendTextMessage(
                "${num}",
                null,
                "This is an SMS test message from Starfruit! \uD83C\uDF86",
                null,
                null
            )
            Toast.makeText(context, "Message sent!", Toast.LENGTH_LONG).show()
            val vibrator =
                context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator; vibrator.vibrate(
            100)
        }

        // Return the fragment view/layout
        return view
    }
}
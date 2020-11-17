package com.gradient.starfruit

import android.app.TimePickerDialog
import android.content.SharedPreferences
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.net.ParseException
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_home.view.*

@Suppress("DEPRECATION")
class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)

        try {
            val preferences: SharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context)
            val simpleDateFormat = SimpleDateFormat("HH:mm")

            if (preferences.getString("mydate", "") != "") {
                val date = simpleDateFormat.parse(preferences.getString("mydate", ""))
                val formattedDate = simpleDateFormat.format(date)
                view.alarmText.text = ("Text at " + formattedDate)
            }
        } catch (e: ParseException) {
        }

        view.timeButton.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)

                val simpleDateFormat = SimpleDateFormat("HH:mm")
                val date = simpleDateFormat.format(cal.time)
                view.alarmText.text = ("Texts at " + date)

                val preferences: SharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(context)
                preferences.edit().putString("mydate", date).apply();
            }
            TimePickerDialog(
                context,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                false
            ).show()
        }

        // Return the fragment view/layout
        return view
    }
}
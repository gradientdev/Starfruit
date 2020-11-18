package com.gradient.starfruit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

@Suppress("DEPRECATION")
class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)

//        view.timeButton.setOnClickListener {
//            val cal = Calendar.getInstance()
//            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
//                cal.set(Calendar.HOUR_OF_DAY, hour)
//                cal.set(Calendar.MINUTE, minute)
//
//                val simpleDateFormat = SimpleDateFormat("HH:mm")
//                val date = simpleDateFormat.format(cal.time)
//                view.alarmText.text = ("Texts at " + date)
//
//                val preferences: SharedPreferences =
//                    PreferenceManager.getDefaultSharedPreferences(context)
//                preferences.edit().putString("mydate", date).apply();
//            }
//            TimePickerDialog(
//                context,
//                timeSetListener,
//                cal.get(Calendar.HOUR_OF_DAY),
//                cal.get(Calendar.MINUTE),
//                false
//            ).show()
//        }

        // Return the fragment view/layout
        return view
    }
}
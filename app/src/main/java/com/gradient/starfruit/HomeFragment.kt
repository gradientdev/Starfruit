package com.gradient.starfruit

import android.app.TimePickerDialog
import android.content.SharedPreferences
import android.net.ParseException
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@Suppress("DEPRECATION")
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

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
            val date = simpleDateFormat.parse(preferences.getString("mydate", ""))
            val formattedDate = simpleDateFormat.format(date)

            view.alarmText.text = ("Text at " + formattedDate)
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

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }
}
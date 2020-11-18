package com.gradient.starfruit

import android.content.SharedPreferences
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

//        val preferences: SharedPreferences =
//            PreferenceManager.getDefaultSharedPreferences(context)
//        preferences.edit().putString("dailyQuote", "Lawrenceville").apply()

        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)

        try {
            val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            if (preferences.getString("dailyQuote", "") != "") {
                val quote = (preferences.getString("dailyQuote", "")).toString()
                view.quoteText.text = (quote)
            }
        } catch (e: ParseException) {
        }

        // Return the fragment view/layout
        return view
    }
}
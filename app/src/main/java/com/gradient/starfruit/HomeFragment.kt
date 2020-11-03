package com.gradient.starfruit

import android.content.Context
import android.os.Bundle
import android.os.Vibrator
import android.telephony.SmsManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_home.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)

        view.smsButton.setOnClickListener { view ->
            Log.d("btnSetup", "Selected")
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage("+12672747668", null, "This is an SMS test message from Starfruit!", null, null)
            val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator; vibrator.vibrate(50)
            Toast.makeText(context, "Message sent!", Toast.LENGTH_LONG).show()
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
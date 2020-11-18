package com.gradient.starfruit

import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.net.ParseException
import android.os.Bundle
import android.os.Vibrator
import android.preference.PreferenceManager
import android.telephony.SmsManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
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

        val CHANNEL_ID = "starfruit"
        val notificationId = 6275

        fun sendNotificationTest() {
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_star)
            val bitmapLargeIcon = BitmapFactory.decodeResource(resources, R.drawable.ic_quotes)

            val builder = context?.let {
                NotificationCompat.Builder(it, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_star)
                    .setLargeIcon(bitmapLargeIcon)
                    .setContentTitle("Starfruit notification test! \uD83C\uDF20")
                    .setContentText("A notification test message. ")
                    .setStyle(NotificationCompat.BigTextStyle())
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
            }

            with(context?.let { NotificationManagerCompat.from(it) }) {
                if (builder != null) {
                    this?.notify(notificationId, builder.build())
                }
            }
        }

        try {
            val preferences: SharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context)
            val simpleDateFormat = SimpleDateFormat("HH:mm")

            if (preferences.getString("mydate", "") != "") {
                val date = simpleDateFormat.parse(preferences.getString("mydate", ""))
                val formattedDate = simpleDateFormat.format(date)
                view.alarmText.text = (formattedDate + " AM")
            }
        } catch (e: ParseException) {
        }

        view.timeButton.setOnClickListener {
            val vibrator =
                context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator; vibrator.vibrate(
            100
        )
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)

                val simpleDateFormat = SimpleDateFormat("HH:mm")
                val date = simpleDateFormat.format(cal.time)
                view.alarmText.text = ("date" + " AM")

                val preferences: SharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(context)
                preferences.edit().putString("mydate", date).apply()
            }
            TimePickerDialog(
                context,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                false
            ).show()
        }

        view.notificationButton.setOnClickListener { view ->
            val vibrator =
                context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator; vibrator.vibrate(
            100
        )
            val smsManager = SmsManager.getDefault()

            sendNotificationTest()

            //gets phone number saved earlier from preferences
            Toast.makeText(context, "Notification sent!", Toast.LENGTH_LONG).show()
        }

        // Return the fragment view/layout
        return view
    }
}
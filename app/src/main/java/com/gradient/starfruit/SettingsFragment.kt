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
                view.alarmText.text = ("Sends at " + formattedDate)
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
                view.alarmText.text = ("Sends at " + date)

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
            sendNotificationTest()
            Toast.makeText(context, "Fresh quote added!", Toast.LENGTH_LONG).show()
        }

        view.newQuoteButton.setOnClickListener { view ->
            val vibrator =
                context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator; vibrator.vibrate(
            100
        )
            // quote picker below --------------------
            val quoteNum = (0..200).random()
            val quote = this.resources.openRawResource(R.raw.quotes)
                .bufferedReader().useLines { it.elementAtOrNull(quoteNum) ?: "" }

            // send notification ---------------------

            val CHANNEL_ID = "starfruit"
            val notificationId = 6275

            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_star)
            val bitmapLargeIcon = BitmapFactory.decodeResource(resources, R.drawable.ic_quotes)

            val builder = context.let {
                it?.let { it1 ->
                    NotificationCompat.Builder(it1, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_star)
                        .setLargeIcon(bitmapLargeIcon)
                        .setContentTitle("Fresh quote!")
                        .setContentText(quote)
                        .setStyle(NotificationCompat.BigTextStyle())
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent)
                }
            }

            with(context.let { it?.let { it1 -> NotificationManagerCompat.from(it1) } }) {
                if (builder != null) {
                    this?.notify(notificationId, builder.build())
                }
            }

            // set daily quote -------------------------

            val preferences: SharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context)
            preferences.edit().putString("dailyQuote", quote).apply()

            Toast.makeText(context, "New quote set!", Toast.LENGTH_LONG).show()
        }

        // Return the fragment view/layout
        return view
    }
}
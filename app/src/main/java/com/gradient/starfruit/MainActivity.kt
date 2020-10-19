package com.gradient.starfruit

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.testbutton).setOnClickListener {
            val toast = Toast.makeText(this, "Text Sent!", Toast.LENGTH_SHORT)
            toast.show()
        }

//        Twilio.init("sid", "auth"
//        )
//
//
//        val message = Message.creator(
//                PhoneNumber("+12672747668"),
//                PhoneNumber("+16504192614"),
//                "Hello World!"
//        ).create()
//
//        print(message.sid)
    }
}
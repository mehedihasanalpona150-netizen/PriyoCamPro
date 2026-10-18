package com.example.priyocampro

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // লেআউট ডিজাইন তৈরি
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 80, 50, 50)
        }

        // টাইটেল টেক্সট
        val titleText = TextView(this).apply {
            text = "PriyoCamPro Virtual Studio"
            textSize = 22f
            setPadding(0, 0, 0, 40)
        }
        layout.addView(titleText)

        // স্ট্যাটাস টেক্সট
        val statusText = TextView(this).apply {
            text = "Status: Virtual Camera Ready"
            textSize = 16f
            setPadding(0, 0, 0, 40)
        }
        layout.addView(statusText)

        // ভার্চুয়াল ক্যামেরা স্টার্ট বাটন
        val startButton = Button(this).apply {
            text = "Start Virtual Camera"
            setOnClickListener {
                val serviceIntent = Intent(this@MainActivity, MediaStreamService::class.java)
                startService(serviceIntent)
                statusText.text = "Status: Virtual Stream Running..."
            }
        }
        layout.addView(startButton)

        setContentView(layout)
    }
}

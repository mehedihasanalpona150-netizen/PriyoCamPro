package com.priyocam

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var tvStatus: TextView
    private lateinit var btnSelectMedia: Button
    private lateinit var btnToggleService: Button

    private var selectedMediaUri: Uri? = null
    private var isServiceRunning = false

    private val selectMediaLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedMediaUri = it
            tvStatus.text = "Media Selected: ${it.lastPathSegment}"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvStatus = findViewById(R.id.tvStatus)
        btnSelectMedia = findViewById(R.id.btnSelectMedia)
        btnToggleService = findViewById(R.id.btnToggleService)

        btnSelectMedia.setOnClickListener {
            selectMediaLauncher.launch("video/*")
        }

        btnToggleService.setOnClickListener {
            if (isServiceRunning) {
                stopMediaStreamService()
            } else {
                startMediaStreamService()
            }
        }
    }

    private fun startMediaStreamService() {
        val intent = Intent(this, MediaStreamService::class.java).apply {
            putExtra("MEDIA_URI", selectedMediaUri?.toString())
        }
        startService(intent)
        isServiceRunning = true
        btnToggleService.text = "Stop Virtual Camera"
        tvStatus.text = "Virtual Camera Active"
    }

    private fun stopMediaStreamService() {
        val intent = Intent(this, MediaStreamService::class.java)
        stopService(intent)
        isServiceRunning = false
        btnToggleService.text = "Start Virtual Camera"
        tvStatus.text = "Virtual Camera Stopped"
    }
}

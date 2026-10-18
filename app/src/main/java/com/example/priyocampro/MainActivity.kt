package com.example.priyocampro

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var selectedVideoUri: Uri? = null
    private var currentRotation = 0
    private var isFlipped = false

    private val selectVideoLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            selectedVideoUri = result.data?.data
            Toast.makeText(this, "Media Selected Successfully!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(60, 80, 60, 60)
        }

        val title = TextView(this).apply {
            text = "PriyoCamPro Virtual Studio"
            textSize = 22f
            setPadding(0, 0, 0, 30)
        }
        layout.addView(title)

        val status = TextView(this).apply {
            text = "Status: Idle"
            textSize = 16f
            setPadding(0, 0, 0, 30)
        }
        layout.addView(status)

        // Select Media Button
        val btnSelect = Button(this).apply {
            text = "Select Video/Image Media"
            setOnClickListener {
                val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                    type = "video/*"
                }
                selectVideoLauncher.launch(intent)
            }
        }
        layout.addView(btnSelect)

        // Rotate Frame Button
        val btnRotate = Button(this).apply {
            text = "Rotate Frame (90°)"
            setOnClickListener {
                currentRotation = (currentRotation + 90) % 360
                Toast.makeText(this@MainActivity, "Rotation: $currentRotation°", Toast.LENGTH_SHORT).show()
            }
        }
        layout.addView(btnRotate)

        // Flip Frame Button
        val btnFlip = Button(this).apply {
            text = "Flip Horizontal / Mirror"
            setOnClickListener {
                isFlipped = !isFlipped
                Toast.makeText(this@MainActivity, "Flip: $isFlipped", Toast.LENGTH_SHORT).show()
            }
        }
        layout.addView(btnFlip)

        // Start Stream Button
        val btnStart = Button(this).apply {
            text = "Start Virtual Camera Stream"
            setOnClickListener {
                if (selectedVideoUri == null) {
                    Toast.makeText(this@MainActivity, "Please select a media file first", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val intent = Intent(this@MainActivity, MediaStreamService::class.java).apply {
                    action = "ACTION_START_STREAM"
                    putExtra("VIDEO_URI", selectedVideoUri.toString())
                    putExtra("ROTATION", currentRotation)
                    putExtra("FLIP", isFlipped)
                }
                startService(intent)
                status.text = "Status: Virtual Camera Streaming..."
            }
        }
        layout.addView(btnStart)

        // Stop Stream Button
        val btnStop = Button(this).apply {
            text = "Stop Stream"
            setOnClickListener {
                val intent = Intent(this@MainActivity, MediaStreamService::class.java).apply {
                    action = "ACTION_STOP_STREAM"
                }
                startService(intent)
                status.text = "Status: Stopped"
            }
        }
        layout.addView(btnStop)

        setContentView(layout)
    }
}

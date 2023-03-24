package com.example.clapapp

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.SeekBar
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var seekBar: SeekBar
    private var mediaPlayer : MediaPlayer? = null
    private lateinit var runnable: Runnable
    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        seekBar = findViewById(R.id.musicSeekBar)
        handler = Handler(Looper.getMainLooper())

        val playButton = findViewById<FloatingActionButton>(R.id.playButton)
        playButton.setOnClickListener {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(this, R.raw.aty)
                initializeSeekBar()
            }
            mediaPlayer?.start()
        }

        val pauseButton = findViewById<FloatingActionButton>(R.id.pauseButton)
        pauseButton.setOnClickListener {
            mediaPlayer?.pause()
        }

        val stopButton = findViewById<FloatingActionButton>(R.id.stopButton)
        stopButton.setOnClickListener {
            mediaPlayer?.stop()
            mediaPlayer?.reset()
            mediaPlayer?.release()
            mediaPlayer = null
            handler.removeCallbacks(runnable)
            seekBar.progress = 0
        }
    }
//    private fun initializeSeekBar(seekBar: SeekBar, mediaPlayer: MediaPlayer?)
    private fun initializeSeekBar(){
        seekBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (fromUser) mediaPlayer?.seekTo(progress)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {

                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }

            }
        )
    val currentPlayingPosition = findViewById<TextView>(R.id.currentPlayingPosition)
    val totalDuration = findViewById<TextView>(R.id.timeRemeaningOrTotalTime)

    seekBar.max = mediaPlayer!!.duration
    runnable = Runnable {

        val playingPosition = mediaPlayer!!.currentPosition / 1000
        currentPlayingPosition.text = "${playingPosition}s"

        val duration = mediaPlayer!!.duration / 100
        totalDuration.text = "${duration - playingPosition}s" // remaining time

        seekBar.progress = mediaPlayer!!.currentPosition
        handler.postDelayed(runnable, 1000)
    }
    handler.postDelayed(runnable, 1000)
    }
}
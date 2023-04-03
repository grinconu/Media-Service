package com.ziecorp.mediaservicetest

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player

class AudioPlayerViewModel : ViewModel() {
    private lateinit var exoPlayer: ExoPlayer
    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var mediaNotificationManager: MediaNotificationManager

    var isPlaying by mutableStateOf(false)
        private set

    var currentPosition by mutableStateOf(0L)
        private set

    var duration by mutableStateOf(0L)
        private set

    val progress: Float
        get() = if (duration != 0L) currentPosition.toFloat() / duration.toFloat() else 0f

    fun initializePlayer(context: Context, audioUri: Uri) {
        exoPlayer = ExoPlayer.Builder(context).build().apply {
            addListener(object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    this@AudioPlayerViewModel.isPlaying = isPlaying
                }

                override fun onPositionDiscontinuity(reason: Int) {
                    updateProgress()
                }

                override fun onPositionDiscontinuity(oldPosition: Player.PositionInfo, newPosition: Player.PositionInfo, reason: Int) {
                    updateProgress()
                }

                override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
                    updateProgress()
                }
            })
        }

        mediaSession = MediaSessionCompat(context, "AudioPlayerViewModel").apply {
            isActive = true
            setPlaybackState(
                PlaybackStateCompat.Builder()
                    .setState(PlaybackStateCompat.STATE_NONE, 0, 1f)
                    .build(),
            )
        }

        mediaNotificationManager = MediaNotificationManager(
            context = context,
            notificationChannelId = "audio_player_channel",
            notificationId = 456,
        )

        exoPlayer.addListener(object : Player.Listener {
            override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
                updateNotification()
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                updateNotification()
            }
        })

        val mediaItem = MediaItem.fromUri(audioUri)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
    }

    private fun updateNotification() {
        val title = "Título del audio"
        val description = "Descripción del audio"
        val icon: Bitmap? = null

        if (exoPlayer.isPlaying) {
            mediaNotificationManager.showNotification(mediaSession, title, description, icon)
        } else {
            mediaNotificationManager.cancelNotification()
        }
    }

    private fun updateProgress() {
        currentPosition = exoPlayer.currentPosition
        duration = exoPlayer.duration
    }

    fun play() {
        exoPlayer.playWhenReady = true
    }

    fun pause() {
        exoPlayer.playWhenReady = false
    }

    fun seekTo(position: Long) {
        exoPlayer.seekTo(position)
    }

    fun setVolume(volume: Float) {
        exoPlayer.volume = volume
    }

    fun fastForward(millis: Long) {
        val newPosition = currentPosition + millis
        exoPlayer.seekTo(newPosition.coerceIn(0, duration))
    }

    fun rewind(millis: Long) {
        val newPosition = currentPosition - millis
        exoPlayer.seekTo(newPosition.coerceIn(0, duration))
    }

    override fun onCleared() {
        super.onCleared()
        exoPlayer.release()
        mediaSession.release()
        mediaNotificationManager.cancelNotification()
    }
}

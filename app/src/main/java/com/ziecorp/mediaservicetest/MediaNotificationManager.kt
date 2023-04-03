package com.ziecorp.mediaservicetest

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.media.session.MediaButtonReceiver

class MediaNotificationManager(
    private val context: Context,
    private val notificationChannelId: String,
    private val notificationId: Int,
) {
    private val notificationManager = NotificationManagerCompat.from(context)

    init {
        createNotificationChannel()
    }

    fun showNotification(
        mediaSession: MediaSessionCompat,
        title: String,
        description: String,
        icon: Bitmap?,
    ) {
        val notification = buildNotification(mediaSession, title, description, icon)
        notificationManager.notify(notificationId, notification)
    }

    fun cancelNotification() {
        notificationManager.cancel(notificationId)
    }

    private fun buildNotification(
        mediaSession: MediaSessionCompat,
        title: String,
        description: String,
        icon: Bitmap?,
    ): Notification {
        val mediaStyle = androidx.media.app.NotificationCompat.MediaStyle()
            .setMediaSession(mediaSession.sessionToken)
            .setShowActionsInCompactView(0)

        val isPlaying = mediaSession.controller.playbackState.state == PlaybackStateCompat.STATE_PLAYING

        val playPauseAction = if (isPlaying) {
            NotificationCompat.Action(
                R.drawable.ic_pause,
                "Pause",
                MediaButtonReceiver.buildMediaButtonPendingIntent(
                    context,
                    PlaybackStateCompat.ACTION_PAUSE,
                ),
            )
        } else {
            NotificationCompat.Action(
                R.drawable.ic_play,
                "Play",
                MediaButtonReceiver.buildMediaButtonPendingIntent(
                    context,
                    PlaybackStateCompat.ACTION_PLAY,
                ),
            )
        }

        return NotificationCompat.Builder(context, notificationChannelId)
            .setContentTitle(title)
            .setContentText(description)
            .setSmallIcon(androidx.core.R.drawable.notification_bg)
            .setLargeIcon(icon)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)
            .addAction(playPauseAction)
            .setStyle(mediaStyle)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.channel_name)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(notificationChannelId, name, importance)
            notificationManager.createNotificationChannel(channel)
        }
    }
}

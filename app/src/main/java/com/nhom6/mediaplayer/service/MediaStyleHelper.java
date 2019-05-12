package com.nhom6.mediaplayer.service;


import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.provider.SyncStateContract;
import android.support.v4.app.NotificationCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.app.NotificationCompat.MediaStyle;
import android.view.KeyEvent;

import com.nhom6.mediaplayer.R;

/**
 * Helper APIs for constructing MediaStyle notifications
 */
public class MediaStyleHelper {
    /**
     * Build a notification using the information from the given media session.
     * @param context Context used to construct the notification.
     * @param mediaSession Media session to get information.
     * @return A pre-built notification with information from the given media session.
     */
    public static NotificationCompat.Builder from(Context context,
                                                  MediaSessionCompat mediaSession,
                                                  String channel) {

        MediaControllerCompat controller = mediaSession.getController();
        MediaMetadataCompat mediaMetadata = controller.getMetadata();
        MediaDescriptionCompat description = mediaMetadata.getDescription();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channel);

        builder
                .setColorized(true)
                .setColor(Color.parseColor("#ff42959a"))
                .setSmallIcon(R.drawable.icmusicplay)
                .setContentTitle(description.getTitle())
                .setContentText(description.getSubtitle())
                .setSubText(description.getDescription())
                .setLargeIcon(description.getIconBitmap())
                .setContentIntent(controller.getSessionActivity())
                .setDeleteIntent(MediaButtonReceiver.buildMediaButtonPendingIntent(context, PlaybackStateCompat.ACTION_STOP))
                .setContentIntent(controller.getSessionActivity())
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setWhen(0)
                .setShowWhen(false);

        if (description.getIconBitmap() == null || description.getIconBitmap().isRecycled()) {
            builder.setLargeIcon(
                    BitmapFactory.decodeResource(context.getResources(), R.drawable.adele));
        } else {
            builder.setLargeIcon(description.getIconBitmap());
        }

        return builder;
    }

    /**
     * Create a {@link PendingIntent} appropriate for a MediaStyle notification's action. Assumes
     * you are using a media button receiver.
     * @param context Context used to construct the pending intent.
     * @param mediaKeyEvent KeyEvent code to send to your media button receiver.
     * @return An appropriate pending intent for sending a media button to your media button
     *      receiver.
     */
    public static PendingIntent getActionIntent(Context context, int mediaKeyEvent) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_BUTTON);
        intent.setPackage(context.getPackageName());
        intent.putExtra(Intent.EXTRA_KEY_EVENT,
                new KeyEvent(KeyEvent.ACTION_DOWN, mediaKeyEvent));
        return PendingIntent.getBroadcast(context, mediaKeyEvent, intent, 0);
    }
}

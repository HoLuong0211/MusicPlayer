package utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import models.Song;

/**
 * Created by Ran on 4/26/2016.
 */
public class Config {

    public static final int backwardTime = 5000;
    public static final int forwardTime = 5000;
    public static final String KEY_SONG_POSITION = "songPosition";
    public static final String KEY_CURRENT_TIME = "currentTime";
    public static final String KEY_PREVIOUS_OR_NEXT = "previousOrNext";
    public static final String KEY_IS_PLAYING = "isPlaying";

    public static final String ACTION_UPATE_SONG_TIME = "com.example.administrator.playmusic.action.UPATE_SONG_TIME";
    public static final String ACTION_PLAY_MUSIC = "com.example.administrator.playmusic.action.PLAY_MUSIC";
    public static final String ACTION_BACKWARD_OR_FORWARD = "com.example.administrator.playmusic.action.BACKWARD_OR_FORWARD";

    public static int OPEN_ACTIVITY_PLAY_SONG = 1;

    public static String getTextFormat(long timeInMilliseconds) {
        long minutes = TimeUnit.MILLISECONDS.toMinutes((long) timeInMilliseconds);
        long seconds = TimeUnit.MILLISECONDS.toSeconds((long) timeInMilliseconds)
                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeInMilliseconds));
        String min = minutes < 10 ? ("0" + minutes) : String.valueOf(minutes);
        String sec = seconds < 10 ? ("0" + seconds) : String.valueOf(seconds);
        return min + ":" + sec;
    }

    public static List<Song> getListSongs(Context context) {
        List<Song> mListSongs = new ArrayList<Song>();
        Uri uri;
        uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String[] m_data = {MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA};

        Cursor c = context.getContentResolver().query(uri, m_data, MediaStore.Audio.Media.IS_MUSIC + "=1", null,
                MediaStore.Audio.Media.TITLE + " ASC");

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {

            String id, name, title, album, artist, path;
            int duration;
            id = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
            name = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
            title = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
            album = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
            artist = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
            duration = (int) (c.getInt(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
            path = c.getString(c.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
            Song song = new Song(id, name, title, album, artist, path, duration);
            mListSongs.add(song);

        }
        return mListSongs;
    }

}
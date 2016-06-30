package services;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.util.List;

import models.Song;
import utils.Config;

/**
 * Created by Administrator on 4/22/2016.
 */
public class MusicService extends Service {

    private MediaPlayer mediaPlayer;
    private List<Song> mSongs;
    private int songPosition;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        songPosition = intent.getIntExtra(Config.KEY_SONG_POSITION, 0);
        mSongs = Config.getListSongs(getApplicationContext());
        try {
            mediaPlayer.setDataSource(mSongs.get(songPosition).getPath());
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
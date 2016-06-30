package services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
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

    private static final long delayMillis = 100;

    private MediaPlayer mediaPlayer;
    private List<Song> mSongs;
    private int songPosition;

    private Handler mHandler = new Handler();
    private Runnable updateSongTime = new Runnable() {
        @Override
        public void run() {
            if(mediaPlayer!=null && mediaPlayer.isPlaying()){
                Intent updateSongIntent = new Intent(Config.ACTION_UPATE_SONG_TIME);
                updateSongIntent.putExtra(Config.KEY_CURRENT_TIME, mediaPlayer.getCurrentPosition());
                sendBroadcast(updateSongIntent);
                mHandler.postDelayed(this, delayMillis );
            }
        }
    };

    private BroadcastReceiver playMediaPlayerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int position = intent.getIntExtra(Config.KEY_PREVIOUS_OR_NEXT, -1);
            if (position != -1) {
                songPosition = position;
                // play next song or previous song
                playNewSong();
                return;
            }

            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            } else {
                mediaPlayer.start();
                mHandler.postDelayed(updateSongTime, delayMillis);
            }

        }
    };

    private BroadcastReceiver seekMediaPlayerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int seekBarProcess = intent.getIntExtra(Config.KEY_SEEKBAR_PROGRESS,-1);
            if(seekBarProcess == -1){
                // backward or forward
                if (intent.getBooleanExtra(Config.KEY_BACKWARD_OR_FORWARD, false))
                    forward();
                else
                    backward();
            }else{
                // seek Media Player to seekBarProcess
                mediaPlayer.seekTo(seekBarProcess);
            }

        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        registerReceiver(playMediaPlayerReceiver, new IntentFilter(Config.ACTION_PLAY_MUSIC));
        registerReceiver(seekMediaPlayerReceiver, new IntentFilter(Config.ACTION_SEEK_MEDIA_PLAYER));
    }

    @Override
    public void onDestroy() {
        mediaPlayer.release();
        unregisterReceiver(playMediaPlayerReceiver);
        unregisterReceiver(seekMediaPlayerReceiver);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        songPosition = intent.getIntExtra(Config.KEY_SONG_POSITION, 0);
        mSongs = Config.getListSongs(getApplicationContext());
        if(!mediaPlayer.isPlaying()){
            // Service starts for the first time
            try {
                mediaPlayer.setDataSource(mSongs.get(songPosition).getPath());
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.prepare();
                mediaPlayer.start();
                mHandler.postDelayed(updateSongTime, delayMillis);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            // Service is running => play selected song
            playNewSong();
        }
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void playNewSong() {
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(mSongs.get(songPosition).getPath());
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepare();
            mediaPlayer.start();
            mHandler.postDelayed(updateSongTime, delayMillis);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void backward() {
        if (mediaPlayer.getCurrentPosition() / 1000 - Config.BACKWARD_TIME / 1000 >= 0) {
            mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - Config.BACKWARD_TIME);
        }
    }

    private void forward() {
        if (mediaPlayer.getCurrentPosition() / 1000 + Config.FORWARD_TIME / 1000 <= mediaPlayer.getDuration() / 1000) {
            mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + Config.FORWARD_TIME);
        }
    }
}
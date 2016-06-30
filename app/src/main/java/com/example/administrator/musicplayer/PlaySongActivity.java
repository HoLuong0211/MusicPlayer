package com.example.administrator.musicplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.List;

import models.Song;
import services.MusicService;
import utils.Config;

public class PlaySongActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener{

    private SeekBar seekBar;
    private TextView tvCurrentTime, tvTotalTime, tvSongName;
    private ImageButton btnPlay;

    private Song selectedSong;
    private int songPosition;
    private int songDuration;
    private List<Song> mSongs;

    private boolean isPlaying;

    private BroadcastReceiver updateSongReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int currentTime = intent.getIntExtra(Config.KEY_CURRENT_TIME, 0);
            if (currentTime / 1000 == songDuration / 1000) {
                next(btnPlay);
            }
            tvCurrentTime.setText(Config.getTextFormat(currentTime));
            seekBar.setProgress(currentTime);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        tvCurrentTime = (TextView) findViewById(R.id.tvCurrentTime);
        tvTotalTime = (TextView) findViewById(R.id.tvTotalTime);
        tvSongName = (TextView) findViewById(R.id.tvSongName);
        btnPlay = (ImageButton) findViewById(R.id.btnPlay);

        songPosition = getIntent().getIntExtra(Config.KEY_SONG_POSITION, 0);
        mSongs = Config.getListSongs(this);
        selectedSong = mSongs.get(songPosition);
        isPlaying = true;
        setDefaultInfo();

        // playing music
        Intent playSongIntent = new Intent(PlaySongActivity.this, MusicService.class);
        playSongIntent.putExtra(Config.KEY_SONG_POSITION, songPosition);
        startService(playSongIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(updateSongReceiver, new IntentFilter(Config.ACTION_UPATE_SONG_TIME));
    }

    @Override
    protected void onPause() {
        unregisterReceiver(updateSongReceiver);
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if(!isPlaying){
            // stop music
            Intent playSongIntent = new Intent(PlaySongActivity.this, MusicService.class);
            stopService(playSongIntent);
        }
        this.finish();
    }

    public void play(View v) {
        isPlaying = !isPlaying;
        if (isPlaying) {
            btnPlay.setImageResource(android.R.drawable.ic_media_pause);
        } else {
            btnPlay.setImageResource(android.R.drawable.ic_media_play);
        }
        Intent playIntent = new Intent(Config.ACTION_PLAY_MUSIC);
        sendBroadcast(playIntent);
    }

    public void previous(View v) {
    }

    public void next(View v) {
    }

    public void backward(View v) {
        if(seekBar.getProgress() - Config.BACKWARD_TIME >= 0){
            Intent backwardIntent = new Intent(Config.ACTION_SEEK_MEDIA_PLAYER);
            backwardIntent.putExtra(Config.KEY_BACKWARD_OR_FORWARD, false); // false for backward
            sendBroadcast(backwardIntent);
            tvCurrentTime.setText(Config.getTextFormat(seekBar.getProgress() - Config.BACKWARD_TIME));
        }
    }

    public void forward(View v) {
        if(seekBar.getProgress() + Config.FORWARD_TIME <= seekBar.getMax()){
            Intent forwardIntent = new Intent(Config.ACTION_SEEK_MEDIA_PLAYER);
            forwardIntent.putExtra(Config.KEY_BACKWARD_OR_FORWARD, true); // true for forward
            sendBroadcast(forwardIntent);
            tvCurrentTime.setText(Config.getTextFormat(seekBar.getProgress() + Config.FORWARD_TIME));
        }
    }

    private void setDefaultInfo() {
        String songTitle = selectedSong.getTitle();
        songDuration = selectedSong.getDuration();

        if (isPlaying)
            btnPlay.setImageResource(android.R.drawable.ic_media_pause);
        else
            btnPlay.setImageResource(android.R.drawable.ic_media_play);
        tvSongName.setText(songTitle);
        tvCurrentTime.setText(Config.getTextFormat(0));
        tvTotalTime.setText(Config.getTextFormat(songDuration));
        seekBar.setMax(songDuration);
        seekBar.setProgress(0);
        seekBar.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int progress = seekBar.getProgress();
        // if seek to the end of song
        if (progress / 1000 == songDuration / 1000) {
            if (isPlaying)
                next(btnPlay);
            else{
                tvCurrentTime.setText(Config.getTextFormat(0));
                seekBar.setProgress(0);
            }
            return;
        }
        Intent seekMediaIntent = new Intent(Config.ACTION_SEEK_MEDIA_PLAYER);
        seekMediaIntent.putExtra(Config.KEY_SEEKBAR_PROGRESS, progress);
        sendBroadcast(seekMediaIntent);
    }
}
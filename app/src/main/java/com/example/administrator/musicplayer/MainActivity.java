package com.example.administrator.musicplayer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import adapters.ListSongsAdapter;
import models.Song;
import utils.Config;

public class MainActivity extends AppCompatActivity {

    private List<Song> mListSongs = new ArrayList<Song>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListSongs.addAll(Config.getListSongs(this));
        ListView lvListSongs = (ListView)findViewById(R.id.lvListSongs);
        ListSongsAdapter adapter = new ListSongsAdapter(this,R.layout.song_row, mListSongs);
        lvListSongs.setAdapter(adapter);

        lvListSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intentPlaySong = new Intent(MainActivity.this, PlaySongActivity.class);
                intentPlaySong.putExtra(Config.KEY_SONG_POSITION,position);
                startActivityForResult(intentPlaySong,Config.OPEN_ACTIVITY_PLAY_SONG);
            }
        });
    }
}

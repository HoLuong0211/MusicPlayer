package com.example.administrator.musicplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    }
}

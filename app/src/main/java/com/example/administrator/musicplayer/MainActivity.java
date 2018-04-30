package com.example.administrator.musicplayer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
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

    private static final int REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 100;
    private List<Song> mListSongs = new ArrayList<>();
    private ListView lvListSongs;
    private ListSongsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvListSongs = (ListView)findViewById(R.id.lvListSongs);
        adapter = new ListSongsAdapter(this,R.layout.song_row, mListSongs);
        lvListSongs.setAdapter(adapter);

        lvListSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intentPlaySong = new Intent(MainActivity.this, PlaySongActivity.class);
                intentPlaySong.putExtra(Config.KEY_SONG_POSITION,position);
                startActivityForResult(intentPlaySong,Config.OPEN_ACTIVITY_PLAY_SONG);
            }
        });

        if (checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            mListSongs.addAll(Config.getListSongs(this));
            adapter.notifyDataSetChanged();
        } else {
            //Register permission
            requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION_READ_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mListSongs.addAll(Config.getListSongs(this));
                adapter.notifyDataSetChanged();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private boolean checkPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {

                //Permission don't granted
                if (shouldShowRequestPermissionRationale(permission)) {
                    // Permission isn't granted
                    return false;
                } else {
                    // Permission don't granted and don't show dialog again.
                    return false;
                }
            } else
                return true;
        } else {
            return true;
        }
    }
}

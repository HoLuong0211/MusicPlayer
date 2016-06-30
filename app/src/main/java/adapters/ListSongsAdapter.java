package adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.administrator.musicplayer.R;

import java.util.List;

import models.Song;

/**
 * Created by Ran on 4/26/2016.
 */
public class ListSongsAdapter extends ArrayAdapter<Song> {

    private List<Song> mListSongs;
    private int mResourceID;
    private Context mContext;

    public ListSongsAdapter(Context context, int resource, List<Song> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResourceID = resource;
        this.mListSongs = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = View.inflate(mContext,mResourceID,null);

        TextView tvOrder,tvSongName;
        tvOrder = (TextView)convertView.findViewById(R.id.tvOrder);
        tvSongName = (TextView)convertView.findViewById(R.id.tvSongName);

        tvOrder.setText(String.valueOf(position+1));
        tvSongName.setText(mListSongs.get(position).getTitle());

        return convertView;
    }
}
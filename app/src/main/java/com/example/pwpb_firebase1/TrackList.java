package com.example.pwpb_firebase1;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class TrackList extends ArrayAdapter<Track> {
    private Activity context;
    private List<Track> tracks;

    public TrackList(Activity context, List<Track> tracks){
        super(context, R.layout.track_list_layout, tracks);
        this.context = context;
        this.tracks = tracks;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.track_list_layout, null, true);

        TextView txtName1 = (TextView) listViewItem.findViewById(R.id.textviewname);
        TextView txtrating = (TextView) listViewItem.findViewById(R.id.textviewrating);

        Track track = tracks.get(position);

        txtName1.setText(track.getTrackName());
        txtrating.setText(String.valueOf(track.getTrackRating()));


        return listViewItem;
    }
}

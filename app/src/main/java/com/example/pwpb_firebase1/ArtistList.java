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

public class ArtistList extends ArrayAdapter <Artist> {

    private Activity context;
    private List<Artist> artistList;

    public ArtistList(Activity context, List<Artist> artistList){
        super(context, R.layout.artist_list_layout, artistList);
        this.context = context;
        this.artistList = artistList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.artist_list_layout, null, true);

        TextView txtName = (TextView) listViewItem.findViewById(R.id.textviewname);
        TextView txtGenre = (TextView) listViewItem.findViewById(R.id.textviewgenre);

        Artist artist = artistList.get(position);

        txtName.setText(artist.getArtistName());
        txtGenre.setText(artist.getArtistGenre());


        return listViewItem;
    }
}

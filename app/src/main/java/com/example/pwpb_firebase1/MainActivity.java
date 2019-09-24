package com.example.pwpb_firebase1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String ARTIST_NAME = "artistname";
    public static final String ARTIST_ID = "artistid";

    EditText edtTextName;
    Button btnAdd;
    Spinner spinnerGenres;

    DatabaseReference db_artist;

    ListView listViewArtists;

    List<Artist> artistList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db_artist = FirebaseDatabase.getInstance().getReference("Artists");

        edtTextName = findViewById(R.id.editText_Name);
        btnAdd = findViewById(R.id.btn_add_artist);
        spinnerGenres = findViewById(R.id.spinner_genres);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addArtist();
            }
        });

        listViewArtists = findViewById(R.id.listViewArtists);
        artistList = new ArrayList<>();

        listViewArtists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Artist artist = artistList.get(i);

                Intent intent = new Intent(getApplicationContext(), AddTrackActivity.class);

                intent.putExtra(ARTIST_ID, artist.getArtisId());
                intent.putExtra(ARTIST_NAME, artist.getArtistName());

                startActivity(intent);
            }
        });
        listViewArtists.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                Artist artist = artistList.get(i);

                showUpdateDialog(artist.getArtisId(), artist.getArtistName());

                return false;
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        db_artist.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                artistList.clear();

                for (DataSnapshot artistsnapshot: dataSnapshot.getChildren()){
                    Artist artist = artistsnapshot.getValue(Artist.class);

                    artistList.add(artist);
                }

                ArtistList adapter = new ArtistList(MainActivity.this, artistList);
                listViewArtists.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showUpdateDialog(final String artisId, final String artistName){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.update_dialog, null);

        dialogBuilder.setView(dialogView);

        final EditText edttxtname = (EditText) dialogView.findViewById(R.id.edttxt_name);
        final Button btnUpdate = (Button) dialogView.findViewById(R.id.btn_update);
        final Button btnDelete = (Button) dialogView.findViewById(R.id.btn_delete);
        final Spinner spinnerGenres = (Spinner) dialogView.findViewById(R.id.spinner_genres2);


        dialogBuilder.setTitle("Updating Artist" +artistName);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edttxtname.getText().toString().trim();
                String genre = spinnerGenres.getSelectedItem().toString();

                if (!TextUtils.isEmpty(name)){
                    edttxtname.setError("Name required");
                }

                updateArtist(artisId,name,genre);

                alertDialog.dismiss();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteArtist(artisId);
            }


        });

    }

    private void deleteArtist(String artisId) {
        DatabaseReference db_delete_Artist = FirebaseDatabase.getInstance().getReference("Artists")
                .child(artisId);
        DatabaseReference db_delete_Track = FirebaseDatabase.getInstance().getReference("tracks")
                .child(artisId);

        db_delete_Artist.removeValue();
        db_delete_Track.removeValue();

        Toast.makeText(this, "Artists is deleted", Toast.LENGTH_SHORT).show();

    }

    private boolean updateArtist(String id, String name, String genre){
        DatabaseReference dbreference = FirebaseDatabase.getInstance().getReference("Artists")
                .child(id);

        Artist artist = new Artist(id,name,genre);

        dbreference.setValue(artist);

        Toast.makeText(this, "Artist Update Success ", Toast.LENGTH_SHORT).show();

        return true;
    }

    private void addArtist(){
        String name = edtTextName.getText().toString().trim();
        String genres = spinnerGenres.getSelectedItem().toString();

        if (!TextUtils.isEmpty(name)){
            String id = db_artist.push().getKey();

            Artist artist = new Artist(id,name,genres);

            db_artist.child(id).setValue(artist);

            Toast.makeText(this, "artist added", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "you should enter name", Toast.LENGTH_SHORT).show();
        }
    }
}

package com.example.pwpb_firebase1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddTrackActivity extends AppCompatActivity {

    TextView textViewArtistName;
    EditText editTextTrackName;
    SeekBar seekBarRating;

    Button btnAddTrack;

    ListView listViewTracks;

    DatabaseReference dbTrack;

    List<Track> tracks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_track);

        textViewArtistName = findViewById(R.id.textViewArtistName);
        editTextTrackName = findViewById(R.id.editText_TrackName);
        seekBarRating = findViewById(R.id.seekBarRating);

        btnAddTrack = findViewById(R.id.btn_add_track);
        listViewTracks = findViewById(R.id.listViewTrack);

        Intent intent = getIntent();

        tracks = new ArrayList<>();

        String id = intent.getStringExtra(MainActivity.ARTIST_ID);
        String name = intent.getStringExtra(MainActivity.ARTIST_NAME);

        textViewArtistName.setText(name);

        dbTrack = FirebaseDatabase.getInstance().getReference("tracks").child(id);

        btnAddTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveTrack();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        dbTrack.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tracks.clear();

                for (DataSnapshot trackSnapshot : dataSnapshot.getChildren()){
                    Track track = trackSnapshot.getValue(Track.class);
                    tracks.add(track);
                }

                TrackList trackListAdapter = new TrackList(AddTrackActivity.this, tracks);
                listViewTracks.setAdapter(trackListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void SaveTrack() {
        String trackName = editTextTrackName.getText().toString().trim();
        int rating = seekBarRating.getProgress();

        if (!TextUtils.isEmpty(trackName)){
            String id = dbTrack.push().getKey();

            Track track = new Track(id, trackName, rating);
            dbTrack.child(id).setValue(track);

            Toast.makeText(this, "Track Saved successfully", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Track name should not be empty", Toast.LENGTH_SHORT).show();
        }
    }
}

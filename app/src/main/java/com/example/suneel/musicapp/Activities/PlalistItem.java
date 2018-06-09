package com.example.suneel.musicapp.Activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suneel.musicapp.Adapters.PlayListItemAdapter;
import com.example.suneel.musicapp.Database.DatabaseHelper;
import com.example.suneel.musicapp.Database.GetSongData;
import com.example.suneel.musicapp.models.PlayListStore;
import com.example.suneel.musicapp.R;
import com.example.suneel.musicapp.models.SongModel;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by suneel on 11/4/18.
 */

public class PlalistItem extends Activity {
    private ArrayList<SongModel> songList;
    private RecyclerView recyclerView;
    private ImageView playlistImage;
    private TextView playlistName, playlistCount;
    DatabaseHelper helper;
    PlayListItemAdapter adapter;
    private String Playlistname;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlistsonglist);
        recyclerView = (RecyclerView) findViewById(R.id.playlistItemSong);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        playlistImage = (ImageView) findViewById(R.id.playimage);
        playlistName = (TextView) findViewById(R.id.playTitle);
        playlistCount = (TextView) findViewById(R.id.playcount);
        Intent i = getIntent();
        String songname = i.getStringExtra("songname");
        String count = i.getStringExtra("songcount");
        byte[] byteArray = getIntent().getByteArrayExtra("image");
//        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        //       setSelectedSong(songname,count,bmp);
        setSelectedSong(songname, count);
        adapter = new PlayListItemAdapter(this, songList, Playlistname);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    public void setSelectedSong(String s, String s1) {
        playlistImage.setImageResource(R.mipmap.ic_launcher);
        playlistName.setText(s);
        playlistCount.setText(s1);
        Playlistname = s;
        showSongList(s);

    }

    private void showSongList(String s) {
        songList = new ArrayList<>();
        helper = new DatabaseHelper(this);
        // Select All Query
        String selectQuery = "SELECT * FROM " + PlayListStore.TABLE_NAME + " WHERE " +
                PlayListStore.PLAYLIST_ID + "='" + s + "'";
        Log.e("QUERY_____", selectQuery);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor == null)
            Toast.makeText(this, cursor.getCount() + "Cursor values", Toast.LENGTH_SHORT).show();
            // looping through all rows and adding to list
        else {
            if (cursor.moveToFirst()) {
                do {
                    byte[] blob = cursor.getBlob(5);
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(blob);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    songList.add(new SongModel(cursor.getString(1), cursor.getString(2), cursor.getString(3), Uri.parse(cursor.getString(4)), bitmap));
                } while (cursor.moveToNext());
            }

            // close db connection
            db.close();
        }
    }


    public void setSongList(String playlistame, int position) {
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("name", playlistame);
        intent.putExtra("value", position);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
       /* MainActivity.instance.getPlaylist(songList,position);*/
    }
}
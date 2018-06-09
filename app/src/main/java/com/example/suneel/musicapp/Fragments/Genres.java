package com.example.suneel.musicapp.Fragments;


import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.suneel.musicapp.Activities.GenresItem;
import com.example.suneel.musicapp.Activities.SongPlay;
import com.example.suneel.musicapp.Activities.splashscreen;
import com.example.suneel.musicapp.Adapters.GenresAdapter;
import com.example.suneel.musicapp.Adapters.GridAdapter;
import com.example.suneel.musicapp.Database.Getmusic;
import com.example.suneel.musicapp.R;
import com.example.suneel.musicapp.models.SongList;
import com.example.suneel.musicapp.models.SongModel;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by suneel on 27/3/18.
 */

public class Genres extends BaseFragment {

    RecyclerView recyclerView;
    GenresAdapter adapter;
    List<SongModel> sList;
    MediaMetadataRetriever metaRetriver;
    byte[] art;
    Bitmap songImage;
    Getmusic help;
    SQLiteDatabase db;
    private boolean isLoading = true;
    private int pastVisibleItems, VisibleItemCount, total_itemCount, Prevvious_total;
    private int view_threshold = 10;
    private int OFFSET = 0;
    private int LIMIT = 10;
    private GridLayoutManager layout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.genres, container, false);
        layout = new GridLayoutManager(getContext(), 2);
        help = new Getmusic(getContext());
        db = help.getWritableDatabase();
        pastVisibleItems = 0;
        VisibleItemCount = 0;
        total_itemCount = 0;
        Prevvious_total = 0;
        OFFSET = 0;
        sList = databasemusic(LIMIT, OFFSET);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerviewgenres);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layout);
        adapter = new GenresAdapter(getContext(), sList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                VisibleItemCount = layout.getChildCount();
                total_itemCount = layout.getItemCount();
                pastVisibleItems = layout.findFirstVisibleItemPosition();

                if (isLoading) {
                    if (total_itemCount > Prevvious_total) {
                        isLoading = false;
                        Prevvious_total = total_itemCount;
                    }
                }
                if (!isLoading && (total_itemCount - VisibleItemCount) <= (pastVisibleItems + view_threshold)) {
                    performPagination();
                    isLoading = true;
                }
            }
        });
        return view;
    }

    private void performPagination() {
        List<SongModel> smodel = databasemusic(LIMIT, OFFSET);
        adapter.addSongs(smodel);
    }

    public List<SongModel> databasemusic(int limit, int offset) {
        sList = new ArrayList<>();
        String selectQuery = "SELECT DISTINCT genres" +
                " FROM " + SongList.TABLE_NAME + " LIMIT " + limit + " OFFSET " + offset;

        Log.e("QUERY_____", selectQuery);
        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    String genres = cursor.getString(cursor.getColumnIndex(SongList.GENRES));
                    Bitmap image = genresImage(genres);
                    sList.add(new SongModel(genres, image));
                } while (cursor.moveToNext());
            }
            OFFSET = OFFSET + LIMIT;
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        return sList;
    }

    public Bitmap genresImage(String genres) {
        ByteArrayInputStream inputStream = null;
        Bitmap bitmap = null;
        String selectQuery = "SELECT song_image FROM " + SongList.TABLE_NAME + " WHERE genres ='" + genres + "'";
        Log.e("QUERY_____", selectQuery);
        try {
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                byte[] blob = cursor.getBlob(cursor.getColumnIndex(SongList.SONG_IMAGE));
                inputStream = new ByteArrayInputStream(blob);
                bitmap = BitmapFactory.decodeStream(inputStream);
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    public void setSongList(int position) {
        Intent intent = new Intent(getContext(), SongPlay.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    public void setGenresList(String genres) {
        Intent intent = new Intent(getContext(), GenresItem.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("genres", genres);
        startActivity(intent);
    }
}

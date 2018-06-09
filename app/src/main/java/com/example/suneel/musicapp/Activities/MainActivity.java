package com.example.suneel.musicapp.Activities;


import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suneel.musicapp.Database.GetSongData;
import com.example.suneel.musicapp.Utils.Utilities;
import com.example.suneel.musicapp.models.SongDataModel;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;
import com.example.suneel.musicapp.Adapters.SearchAdapter;
import com.example.suneel.musicapp.Adapters.SelectSongAdapter;
import com.example.suneel.musicapp.Database.DatabaseHelper;
import com.example.suneel.musicapp.Database.Getmusic;
import com.example.suneel.musicapp.Fragments.Album;
import com.example.suneel.musicapp.Fragments.Artist;
import com.example.suneel.musicapp.Fragments.Genres;
import com.example.suneel.musicapp.Fragments.MyDialog;
import com.example.suneel.musicapp.Fragments.Playlist;
import com.example.suneel.musicapp.Fragments.SelectPlaylist;
import com.example.suneel.musicapp.Fragments.Songs;
import com.example.suneel.musicapp.R;
import com.example.suneel.musicapp.Services.MusicService;
import com.example.suneel.musicapp.models.PlayListStore;
import com.example.suneel.musicapp.models.SongList;
import com.example.suneel.musicapp.models.SongModel;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import me.itangqi.waveloadingview.WaveLoadingView;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, MediaPlayer.OnCompletionListener {
    private static String LOG_TAG = MainActivity.class.getName();
    private static final int MY_PERMISSION_REQUEST = 1;
    private RecyclerView recyclerView;
    private MediaMetadataRetriever metaRetriver;
    byte[] art;
    Bitmap songImage;
    SearchAdapter adapter;
    SearchView searchView;
    private TextView contentText, dname;
    private boolean checkstate;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ActionModeCallback actionModeCallback;
    private ActionMode actionMode;
    LinearLayout frame;
    Bundle base;
    Menu menu;
    FragmentManager mMangaer;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private final String voiceValue = "";
    FragmentTransaction myfragmentTransaction;
    EditText songType;
    String playname;
    private String playlistname;
    SelectSongAdapter songadapter;
    List<SongModel> selectedSong;
    Intent playIntent;
    private MyDialog mydailog;
    private boolean mActionModeIsActive = false;
    android.app.FragmentManager manger;
    private KeyListener originalKeyListener;
    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    private ViewPagerAdapter viewPagerAdapter;
    PlalistItem item = new PlalistItem();
    Getmusic help;
    SQLiteDatabase db;
    private ArrayList<String> tester;
    private ImageView contentviewImage;
    private SelectPlaylist selectplaylist;
    private RecyclerView selectsongrecycle;
    private boolean stateSelection = false;
    private int count = 0;
    private SongModel smodel;
    private LinearLayout layoutmain, dragView, listview;
    private LinearLayout layoutselectsong;
    private SongModel songModel;
    private int addnewplaylistsongposition;
    private RelativeLayout searchbarview;
    public SlidingUpPanelLayout slidingUpPanelLayout;
    private static final String ACTION_VOICE_SEARCH = "com.google.android.gms.actions.SEARCH_ACTION";
    TextView stitle, sartist, starttime, endtime;
    CircleImageView simage;
    Button play, backbtn, fwdbtn, btnRepeat, btnShuffle;
    SeekBar updater;
    Handler hand;
    Utilities utils;
    DatabaseHelper helper;
    private int position;
    private boolean isShuffle = false;
    private boolean isRepeat = false;
    public static MainActivity instance;
    private MusicService player;
    boolean serviceBound = false;
    private String s;
    private String category;
    private ArrayList<SongModel> sList;
    private WaveLoadingView waveLoadingView;
    private GetSongData getSongData;
    private ArrayList<SongModel> totalsongList;
    private int requestCode;
    private String totalprogress;
    private String currentprogress;
    private String oldsongname;
    public int progress=0;
    private Intent intent;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.base = savedInstanceState;
        instance = this;
        setContentView(R.layout.dummy);
        slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        if (getIntent() != null) {
            requestCode = getIntent().getIntExtra("requestcode", 0);
           /* if(requestCode!=0){
                setSongView();
               slidingUpPanelLayout.setPanelState(PanelState.EXPANDED);
            }*/
            category = getIntent().getStringExtra("category");
            playlistname = getIntent().getStringExtra("name");
            position = getIntent().getIntExtra("value", 0);
        }
        help = new Getmusic(this);
        getSongData = new GetSongData(this);
        db = help.getWritableDatabase();
        tester = new ArrayList<>();
        dl = (DrawerLayout) findViewById(R.id.dl);

        dragView = (LinearLayout) findViewById(R.id.dragView);
        listview = (LinearLayout) findViewById(R.id.list);
        contentText = (TextView) findViewById(R.id.contentviewtext);
        dname = (TextView) findViewById(R.id.dname);
        contentviewImage = (ImageView) findViewById(R.id.contentviewimage);
        contentText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "contentText", Toast.LENGTH_SHORT).show();
            }
        });
        dname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Hello", Toast.LENGTH_SHORT).show();
            }
        });
        layoutmain = (LinearLayout) findViewById(R.id.lineartab);
        layoutselectsong = (LinearLayout) findViewById(R.id.removetab);
        selectsongrecycle = (RecyclerView) findViewById(R.id.rsongViewaction);
        layoutselectsong.setVisibility(View.GONE);
        layoutmain.setVisibility(View.VISIBLE);
        recyclerView = (RecyclerView) findViewById(R.id.rsongView);
        playIntent = getIntent();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        actionModeCallback = new ActionModeCallback();
        frame = (LinearLayout) findViewById(R.id.container);
        mMangaer = getSupportFragmentManager();
        count = 0;
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            }
        } else {
            getMusic();
        }
        OpenNavigation();
        final NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.song1) {
                    viewPager.setCurrentItem(0);
                    dl.closeDrawer(Gravity.LEFT);
                }
                if (id == R.id.playlist1) {
                    viewPager.setCurrentItem(4);
                    dl.closeDrawer(Gravity.LEFT);
                }
                if (id == R.id.artists1) {
                    viewPager.setCurrentItem(3);
                    dl.closeDrawer(Gravity.LEFT);
                }
                if (id == R.id.genres1) {
                    viewPager.setCurrentItem(1);
                    dl.closeDrawer(Gravity.LEFT);
                }
                if (id == R.id.albums1) {
                    viewPager.setCurrentItem(2);
                    dl.closeDrawer(Gravity.LEFT);
                }
                if (id == R.id.youtubeid) {
                    dl.closeDrawer(Gravity.LEFT);
                    Intent intent = new Intent(MainActivity.this, Youtube.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                return true;
            }
        });

        waveLoadingView = (WaveLoadingView) findViewById(R.id.waveloading);
        updater = (SeekBar) findViewById(R.id.Songrun);
        stitle = (TextView) findViewById(R.id.dname);
        sartist = (TextView) findViewById(R.id.dartist);
        simage = (CircleImageView) findViewById(R.id.SongImage);
        starttime = (TextView) findViewById(R.id.start);
        backbtn = (Button) findViewById(R.id.BackBtn);
        fwdbtn = (Button) findViewById(R.id.FwdBtn);
        endtime = (TextView) findViewById(R.id.end);
        play = (Button) findViewById(R.id.PlayBtn);
        updater.setOnSeekBarChangeListener(this);
        play.setBackgroundResource(R.drawable.ic_pause);
        btnRepeat = (Button) findViewById(R.id.Repeat);
        btnShuffle = (Button) findViewById(R.id.Shuffle);
        btnRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (btnRepeat.isSelected()) {
                    btnRepeat.setSelected(false);
                } else
                    btnRepeat.setSelected(true);
                if (isRepeat) {
                    if (player != null) {
                        isRepeat = false;
                        player.getRepeat(isRepeat);
                        Toast.makeText(getApplicationContext(), "Repeat is OFF", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // make repeat to true
                    if (player != null) {
                        isRepeat = true;
                        player.getRepeat(isRepeat);
                        isShuffle = false;
                        btnShuffle.setSelected(false);
                        player.getShuffle(isShuffle);
                        Toast.makeText(getApplicationContext(), "Repeat is ON", Toast.LENGTH_SHORT).show();
                        // make shuffle to false
                    }
                }
            }
        });


        btnShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (btnShuffle.isSelected()) {
                    btnShuffle.setSelected(false);
                } else
                    btnShuffle.setSelected(true);
                if (isShuffle) {
                    if (player != null) {
                        isShuffle = false;
                        player.getShuffle(isShuffle);
                        Toast.makeText(getApplicationContext(), "Shuffle is OFF", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // make repeat to true
                    if (player != null) {
                        isShuffle = true;
                        player.getShuffle(isShuffle);
                        Toast.makeText(getApplicationContext(), "Shuffle is ON", Toast.LENGTH_SHORT).show();
                        isRepeat = false;
                        btnRepeat.setSelected(false);
                        player.getRepeat(isRepeat);
                        // make shuffle to false
                    }
                }
            }
        });


        fwdbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // check if next song is there or not
                nextSong();
            }
        });

        play.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // check for already playing
                playpause();
            }
        });

        /**
         * Back button click event
         * Plays previous song by currentSongIndex - 1
         * */
        backbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                previousSong();
            }
        });
    }

    public static boolean isServiceRunning(String serviceClassName) {
        final ActivityManager activityManager = (ActivityManager) getActivityContext().getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);

        for (ActivityManager.RunningServiceInfo runningServiceInfo : services) {
            if (runningServiceInfo.service.getClassName().equals(serviceClassName)) {
                return true;
            }
        }
        return false;
    }

    public void addSongintonewplaylist(int getUri, String playname) {
        DatabaseHelper help = new DatabaseHelper(this);
        SQLiteDatabase db = help.getWritableDatabase();
        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(PlayListStore.PLAYLIST_ID, playname);
        values.put(PlayListStore.SONG_NAME, sList.get(getUri).getTitle());
        values.put(PlayListStore.SONG_ARTIST, sList.get(getUri).getArtist());
        values.put(PlayListStore.SONG_LOCATION, sList.get(getUri).getLocation());
        values.put(PlayListStore.SONG_URI, String.valueOf(sList.get(getUri).getUri()));
        values.put(PlayListStore.SONG_IMAGE, getBitmapAsByteArray(sList.get(getUri).getImage()));
        // insert row
        db.insert(PlayListStore.TABLE_NAME, null, values);
        // close db connection
        db.close();
        Toast.makeText(this, "Playlist Created", Toast.LENGTH_SHORT).show();

    }

    public void OpenSliding() {
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
    }

    private void OpenNavigation() {
        abdt = new ActionBarDrawerToggle(this, dl, toolbar, R.string.Open, R.string.Close);
        abdt.setDrawerIndicatorEnabled(true);
        dl.addDrawerListener(abdt);
        abdt.syncState();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        abdt.syncState();
    }

    public void getMusic() {
        sList = new ArrayList<>();
        totalsongList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + SongList.TABLE_NAME;
        Log.e("QUERY_____", selectQuery);
        try {
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    if (cursor == null)
                        Toast.makeText(MainActivity.this, cursor.getCount() + "Cursor values", Toast.LENGTH_SHORT).show();
                        // looping through all rows and adding to list
                    else {
                        if (cursor.moveToFirst()) {
                            do {
                                String id = cursor.getString(cursor.getColumnIndex(SongList.SONG_ID));
                                String title = cursor.getString(cursor.getColumnIndex(SongList.SONG_NAME));
                                String location = cursor.getString(cursor.getColumnIndex(SongList.SONG_LOCATION));
                                Uri uri = Uri.parse(cursor.getString(cursor.getColumnIndex(SongList.SONG_URI)));
                                byte[] blob = cursor.getBlob(cursor.getColumnIndex(SongList.SONG_IMAGE));
                                ByteArrayInputStream inputStream = new ByteArrayInputStream(blob);
                                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                String artistid = cursor.getString(cursor.getColumnIndex(SongList.ARTIST_ID));
                                String artist = cursor.getString(cursor.getColumnIndex(SongList.ARTIST));
                                String albumid = cursor.getString(cursor.getColumnIndex(SongList.ALBUM_ID));
                                String album = cursor.getString(cursor.getColumnIndex(SongList.ALBUM));
                                String genres = cursor.getString(cursor.getColumnIndex(SongList.GENRES));
                                sList.add(new SongModel(id, title, location, uri, bitmap, artistid, artist, albumid, album, genres, false));
                                totalsongList.add(new SongModel(id, title, location, uri, bitmap, artistid, artist, albumid, album, genres, false));
                            } while (cursor.moveToNext());
                        }
                        // close db connection
                        db.close();
                    }
                } while (cursor.moveToNext());
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void openpopup(final SongModel smodel, final int position, View view) {
        final PopupMenu popup = new PopupMenu(this, view);
        //Inflating the Popup using xml file
        popup.getMenuInflater()
                .inflate(R.menu.popup, popup.getMenu());
        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.share:
                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                        sharingIntent.putExtra(Intent.EXTRA_STREAM, sList.get(position).getUri());
                        sharingIntent.setType("audio/mp3");
                        sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(Intent.createChooser(sharingIntent, "Share via"));
                        popup.dismiss();
                        break;
                    case R.id.addtoplaylist:
                        Bundle bundle = new Bundle();
                        bundle.putInt("songuri", position);
                        manger = getFragmentManager();
                        selectplaylist = new SelectPlaylist();
                        selectplaylist.show(manger, "MyPlaylist");
                        selectplaylist.setArguments(bundle);
                        popup.dismiss();
                        break;
                    case R.id.remove:
                        deleteSong(smodel);
                        popup.dismiss();
                        break;
                }
                return true;
            }
        });
        popup.show();
    }

    private void deleteSong(SongModel smodel) {
        Uri uri = smodel.getUri();
        getContentResolver().delete(uri, null, null);
        sList.remove(smodel);
        // checkdatafromPlaylist(uri);
        Toast.makeText(this, "Song Deleted", Toast.LENGTH_SHORT).show();
    }

    private void checkdatafromPlaylist(Uri uri) {

    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (!checkstate) {
            getMenuInflater().inflate(R.menu.menu, menu);
            this.menu = menu;
            final MenuItem search = menu.findItem(R.id.search).setIcon(getResources().getDrawable(R.drawable.ic_search));
            searchView = (SearchView) MenuItemCompat.getActionView(search);
            songType = ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text));
            songType.setHintTextColor(getResources().getColor(R.color.white));
            searchView.setOnSearchClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    frame.setVisibility(View.VISIBLE);
                    tabLayout.setVisibility(View.GONE);
                    viewPager.setVisibility(View.GONE);
                   /* searchbarview.setVisibility(View.GONE);*/
                    setItemsVisibility(menu, search, false);
                }
            });
            // Detect SearchView close
            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    tabLayout.setVisibility(View.VISIBLE);
                    viewPager.setVisibility(View.VISIBLE);
                    layoutmain.setVisibility(View.VISIBLE);
                    /*searchbarview.setVisibility(View.VISIBLE);*/
                    frame.setVisibility(View.GONE);
                    setItemsVisibility(menu, search, true);
                    return false;
                }
            });

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    if (!searchView.isIconified()) {
                        searchView.setIconified(true);
                    }
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    final List<SongModel> filteredModelList = filter(sList, newText);
                    if (newText.isEmpty()) {
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                        adapter = new SearchAdapter(MainActivity.this, sList);
                        adapter.setFilter(filteredModelList);
                        recyclerView.setAdapter(adapter);
                    }
                    return false;
                }
            });
            return true;
        } else
            return false;

    }


    private List<SongModel> filter(List<SongModel> models, String query) {
        query = query.toLowerCase();
        final List<SongModel> filteredModelList = new ArrayList<>();
        for (SongModel model : models) {
            final String text = model.getTitle().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    private void setItemsVisibility(Menu menu, MenuItem exception, boolean visible) {
        for (int i = 0; i < menu.size(); ++i) {
            MenuItem item = menu.getItem(i);
            if (item != exception) item.setVisible(visible);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    final List<SongModel> VoiceModelList = filter(sList, result.get(0).toString());
                    frame.setVisibility(View.VISIBLE);
                    tabLayout.setVisibility(View.GONE);
                    viewPager.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    adapter.setFilter(VoiceModelList);
                    recyclerView.setAdapter(adapter);
                }
                break;
            }
        }
    }

    private void getPlaylistItems(String s) {
        sList = new ArrayList<>();
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
                    sList.add(new SongModel(cursor.getString(1), cursor.getString(2), cursor.getString(3), Uri.parse(cursor.getString(4)), bitmap));
                } while (cursor.moveToNext());
            }
            // close db connection
            sList.size();
        }
    }

    public void getPlaylist(ArrayList<SongModel> sList, int location) {
        this.sList = sList;
        this.position = location;
        player.setPlaylist(sList, position);
        songPlay();
        slidingUpPanelLayout.setPanelState(PanelState.EXPANDED);
    }

    public void previousSong() {
        if (player != null && sList.size() > 0) {
            player.getPreviousSong();
            setSongView();
        }
    }

    public void nextSong() {
        if (player != null && sList.size() > 0) {
            player.sList=this.sList;
            player.getNextSong();
            setSongView();
        }
    }


    public void playpause() {

        if (player.mp != null) {
            if (player.mp.isPlaying()) {

                player.getPause();
                // Changing button image to play button
                play.setBackgroundResource(R.drawable.ic_play);
            } else {
                // Resume song
                if (player.mp != null) {
                    player.getPlay();
                    // Changing button image to pause button
                    play.setBackgroundResource(R.drawable.ic_pause);
                }
            }
        }
    }

    public void stopSong() {
        if (player.mp != null) {
            player.stopSong();
            play.setBackgroundResource(R.drawable.ic_play);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MusicService.LocalBinder binder = (MusicService.LocalBinder) service;
            player = binder.getService();
            serviceBound = true;
            if (requestCode != 0) {
                //finish();
                setSongView();
               // songView();
                slidingUpPanelLayout.setPanelState(PanelState.EXPANDED);
            }
            if (playlistname != null) {
                if (category != null) {
                    getCategorydata(category, playlistname);
                    getPlaylist(sList, position);
                } else {
                    getPlaylistItems(playlistname);
                    getPlaylist(sList, position);
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };


    private void updatesongData() {
        SQLiteDatabase db = getSongData.getReadableDatabase();
        String count = "SELECT count(*) FROM " + SongDataModel.TABLE_NAME;
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        if (icount > 0) {
            if (s != null) {
                if (category != null) {
                    updateCurrentSongData(category, s);
                } else {
                    updateCurrentSongData(playlistname);
                }
            } else {
                updateCurrentSongData("fullSongList");
            }
        } else {
            if (s != null) {
                if (category != null) {
                    insertCurrentSongData(category, s);
                } else {
                    insertCurrentSongData(playlistname);
                }
            } else {
                insertCurrentSongData("fullSongList");
            }
        }
    }

    private void updateCurrentSongData(String playlistname) {
        SQLiteDatabase db = getSongData.getWritableDatabase();
        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(SongDataModel.SONG_NAME, sList.get(position).getTitle());
        values.put(SongDataModel.SONG_ARTIST, sList.get(position).getArtist());
        values.put(SongDataModel.SONG_LOCATION, sList.get(position).getLocation());
        values.put(SongDataModel.SONG_URI, String.valueOf(sList.get(position).getUri()));
        values.put(SongDataModel.SONG_IMAGE, getBitmapAsByteArray(sList.get(position).getImage()));
        values.put(SongDataModel.CURRENT_SONG_DURATION, currentprogress);
        values.put(SongDataModel.TOTAL_SONG_DURATION, totalprogress);
        values.put(SongDataModel.CATEGORY_TYPE, "playlist");
        values.put(SongDataModel.CATEGORY_NAME, playlistname);
        // insert row
        db.update(SongDataModel.TABLE_NAME, values, SongDataModel.SONG_NAME + "='" + oldsongname + "'", null);
        // close db connection
        db.close();
    }

    private void updateCurrentSongData(String category, String s) {
        SQLiteDatabase db = getSongData.getWritableDatabase();
        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(SongDataModel.SONG_NAME, sList.get(position).getTitle());
        values.put(SongDataModel.SONG_ARTIST, sList.get(position).getArtist());
        values.put(SongDataModel.SONG_LOCATION, sList.get(position).getLocation());
        values.put(SongDataModel.SONG_URI, String.valueOf(sList.get(position).getUri()));
        values.put(SongDataModel.SONG_IMAGE, getBitmapAsByteArray(sList.get(position).getImage()));
        values.put(SongDataModel.CURRENT_SONG_DURATION, currentprogress);
        values.put(SongDataModel.TOTAL_SONG_DURATION, totalprogress);
        values.put(SongDataModel.CATEGORY_TYPE, category);
        values.put(SongDataModel.CATEGORY_NAME, s);
        // insert row
        db.update(SongDataModel.TABLE_NAME, values, SongDataModel.SONG_NAME + "= '" + oldsongname + "'", null);
        // close db connection
        db.close();
    }



    private void insertCurrentSongData(String playlistname) {
        SQLiteDatabase db = getSongData.getWritableDatabase();
        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(SongDataModel.SONG_NAME, sList.get(position).getTitle());
        values.put(SongDataModel.SONG_ARTIST, sList.get(position).getArtist());
        values.put(SongDataModel.SONG_LOCATION, sList.get(position).getLocation());
        values.put(SongDataModel.SONG_URI, String.valueOf(sList.get(position).getUri()));
        values.put(SongDataModel.SONG_IMAGE, getBitmapAsByteArray(sList.get(position).getImage()));
        values.put(SongDataModel.CURRENT_SONG_DURATION, currentprogress);
        values.put(SongDataModel.TOTAL_SONG_DURATION, totalprogress);
        values.put(SongDataModel.CATEGORY_TYPE, "playlist");
        values.put(SongDataModel.CATEGORY_NAME, playlistname);
        // insert row
        db.insert(SongDataModel.TABLE_NAME, null, values);
        // close db connection
        db.close();
    }

    private void insertCurrentSongData(String category, String s) {
        SQLiteDatabase db = getSongData.getWritableDatabase();
        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(SongDataModel.SONG_NAME, sList.get(position).getTitle());
        values.put(SongDataModel.SONG_ARTIST, sList.get(position).getArtist());
        values.put(SongDataModel.SONG_LOCATION, sList.get(position).getLocation());
        values.put(SongDataModel.SONG_URI, String.valueOf(sList.get(position).getUri()));
        values.put(SongDataModel.SONG_IMAGE, getBitmapAsByteArray(sList.get(position).getImage()));
        values.put(SongDataModel.CURRENT_SONG_DURATION, currentprogress);
        values.put(SongDataModel.TOTAL_SONG_DURATION, totalprogress);
        values.put(SongDataModel.CATEGORY_TYPE, category);
        values.put(SongDataModel.CATEGORY_NAME, s);
        // insert row
        db.insert(SongDataModel.TABLE_NAME, null, values);
        // close db connection
        db.close();
        // Toast.makeText(this, "Playlist Created", Toast.LENGTH_SHORT).show();
    }


    private void getCategorydata(String category, String name) {
        sList = new ArrayList<>();
        Getmusic helper = new Getmusic(this);
        // Select All Query
        String selectQuery = "SELECT * FROM " + SongList.TABLE_NAME + " WHERE " +
                category + "='" + name + "'";
        Log.e("QUERY_____", selectQuery);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor == null)
            Toast.makeText(this, cursor.getCount() + "Cursor values", Toast.LENGTH_SHORT).show();
            // looping through all rows and adding to list
        else {
            if (cursor.moveToFirst()) {
                do {
                    byte[] blob = cursor.getBlob(4);
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(blob);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    sList.add(new SongModel(cursor.getString(1), s, cursor.getString(2), Uri.parse(cursor.getString(3)), bitmap));
                } while (cursor.moveToNext());
            }
            // close db connection
            sList.size();
        }
    }





    private void setSongView() {
        if(player!=null && sList.size()>0 && sList!=null) {
            try {
                player.sList=this.sList;
                stitle.setText(player.sList.get(player.position).getTitle());
                sartist.setText(player.sList.get(player.position).getArtist());
                simage.setImageBitmap(player.sList.get(player.position).getImage());
               // updateProgressBar();
                setdataToView(player.sList.get(player.position).getTitle(), player.sList.get(player.position).getImage());
               /* stitle.setText(sList.get(position).getTitle());
                sartist.setText(sList.get(position).getArtist());
                simage.setImageBitmap(sList.get(position).getImage());
                // updateProgressBar();
                setdataToView(sList.get(position).getTitle(), sList.get(position).getImage());*/
            }catch (ArrayIndexOutOfBoundsException e){}
        }
        else{
            getcurrentSongData();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
            //bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        getcurrentSongData();
    }

    @Override
    protected void onPause() {
        updatesongData();
        super.onPause();
    }

    private void getcurrentSongData() {
        SQLiteDatabase datab = getSongData.getReadableDatabase();
        // Select All Query
        String selectQuery = "SELECT * FROM " + SongDataModel.TABLE_NAME;
        Log.e("QUERY_____", selectQuery);
        Cursor cursor = datab.rawQuery(selectQuery, null);
        tester.size();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String title=cursor.getString(cursor.getColumnIndex(SongDataModel.SONG_NAME));
                String artist=cursor.getString(1);
                String location=cursor.getString(2);
                String uri=cursor.getString(3);
                byte[] blob = cursor.getBlob(4);
                ByteArrayInputStream inputStream = new ByteArrayInputStream(blob);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                String currentsong=cursor.getString(5);
                String totalsong=cursor.getString(6);
                String category=cursor.getString(7);
                String categoryname=cursor.getString(8);
                stitle.setText(title);
                oldsongname=title;
                sartist.setText(artist);
                simage.setImageBitmap(bitmap);
                starttime.setText(currentsong);
                endtime.setText(totalsong);
                currentprogress=currentsong;
                totalprogress=totalsong;
                setdataToView(title, bitmap);
               // updateProgressBar();
            } while (cursor.moveToNext());
        }// looping through all rows and adding to list
        else
            {
            Toast.makeText(this, cursor.getCount() + "Cursor values", Toast.LENGTH_SHORT).show();
            }
        cursor.close();
        db.close();

    }


    private void songPlay() {
        utils = new Utilities();
        hand = new Handler();
        try {
            // set Progress bar values
            if(player!=null) {
                player.progress=0;
                updater.setProgress(0);
                waveLoadingView.setProgressValue(0);
                updater.setMax(100);
                player.getStart(position);
                // Updating progress bar
                /*updatesongData();
                setSongView();*/
               updateProgressBar();
               player.updateProgressBar();
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void updateProgressBar() {
        hand = new Handler();
        hand.postDelayed(mUpdateTimeTask, 100);
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            try {
                if(player!=null) {
                   /* long totalDuration = player.getDuration();
                    long currentDuration = player.gettotalDuration();
                    // Displaying Total Duration time
                    endtime.setText("" + utils.milliSecondsToTimer(totalDuration));
                    // Displaying time completed playing
                    starttime.setText("" + utils.milliSecondsToTimer(currentDuration));
                    // Updating progress bar
                    progress = (int) (utils.getProgressPercentage(currentDuration, totalDuration));
                   // int progress = player.progress;
                    currentprogress = ""+utils.milliSecondsToTimer(progress);
                    totalprogress= "" + utils.milliSecondsToTimer(totalDuration);*/
                    //Log.d("Progress", ""+progress);
                    /*updater.setProgress(progress);
                    waveLoadingView.setProgressValue(progress);*/
                   // updateui(progress,"" + utils.milliSecondsToTimer(currentDuration),"" + utils.milliSecondsToTimer(totalDuration));
                    //updatesongData();
                    //setSongView();
                    // Running this thread after 100 milliseconds
                    hand.postDelayed(this, 100);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
       }

    };

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
        if(fromTouch){
            this.progress=progress;
            //player.setProgress();
            updater.setProgress(progress);
            waveLoadingView.setProgressValue(progress);
           // player.getSeekBardata(progress);
            //player.mp.seekTo(progress);
            Toast.makeText(this, String.valueOf(progress),Toast.LENGTH_LONG).show();
            player.onProgressChanged(seekBar,progress,fromTouch);
            updateui(progress,currentprogress,totalprogress);
        }

    }

    /**
     * When user starts moving the progress handler
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // remove message Handler from updating progress bar
        if(hand!=null)
        hand.removeCallbacks(mUpdateTimeTask);
        //player.onStartTrackingTouch(seekBar);
    }

    /**
     * When user stops moving the progress hanlder
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if(hand!=null && player!=null) {
            seekBar=updater;
            hand.removeCallbacks(mUpdateTimeTask);
            int totalDuration = (int) player.getDuration();
            progress=utils.progressToTimer(seekBar.getProgress(), totalDuration);
            //int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);
            // forward or backward to certain seconds
           // player.getSeekBardata(currentPosition);
           //------------------------------------------------ player.getSeekBardata(progress);
            player.mp.seekTo(progress);
            updateui(progress,currentprogress,String.valueOf(totalprogress));
            // update timer progress again
           // updateProgressBar();
            player.updateProgressBar();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        item.expandActionView();
        switch (item.getItemId()) {
            case R.id.addplaylist:
                showDailog();
                break;
            case R.id.voice:
                Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("com.google.android.youtube");
                startActivity( LaunchIntent );
                //promptSpeechInput();
                break;
            case R.id.search:
                songType.requestFocus();
                songType.setFocusableInTouchMode(true);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(songType, InputMethodManager.SHOW_FORCED);
                break;

        }
        return abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }


    public void open(ArrayList<SongModel> smodel, int position) {
        if (player != null)
        {
            this.position = position;
            this.sList = smodel;
            player.resetSong();
            player.setPlaylist(smodel, position);
            songPlay();
           /* updatesongData();*/
            setSongView();
        }

    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void showDailog() {
        Bundle bundle = new Bundle();
        bundle.putInt("songuri", -1);
        manger = getFragmentManager();
        mydailog = new MyDialog();
        mydailog.show(manger, "MyDialog");
        mydailog.setArguments(bundle);
    }

    public void showDailog(int songposition) {
        Bundle bundle = new Bundle();
        bundle.putInt("songuri", songposition);
        manger = getFragmentManager();
        mydailog = new MyDialog();
        mydailog.show(manger, "MyDialog");
        mydailog.setArguments(bundle);
    }

    private void setupViewPager(ViewPager viewPager) {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFrag(new Songs(), "SONGS");
        viewPagerAdapter.addFrag(new Genres(), "GENRES");
        viewPagerAdapter.addFrag(new Album(), "ALBUM");
        viewPagerAdapter.addFrag(new Artist(), "ARTIST");
        viewPagerAdapter.addFrag(new Playlist(), "PLAYLIST");
        viewPager.setAdapter(viewPagerAdapter);
    }
    public void toolbarchange(int i) {
        enableActionMode(i);
    }

    private void enableActionMode(int position) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
            mActionModeIsActive = true;

        }
        toggleSelection(position);
    }

    private void toggleSelection(int position) {
        if (position == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(position));
            actionMode.invalidate();
        }
    }

    @Override
    public void onBackPressed() {
        if (!(searchView.isIconified()) && searchView != null) {
            searchView.setIconified(true);
        } else {
            super.onBackPressed();
            this.finish();
        }
    }

    public void setSelectedSong(String s, String s1, Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        Intent in1 = new Intent(this, PlalistItem.class);
        in1.putExtra("songname", s);
        in1.putExtra("songcount", s1);
        in1.putExtra("image", byteArray);
        startActivity(in1);
    }

    public void openSong(SongModel smodel, int position) {
        for (int i = 0; i < sList.size(); i++) {
            if (smodel.getTitle() == sList.get(i).getTitle()) {
                position = i;
            }
        }
        Intent intent = new Intent(this, SongPlay.class);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        smodel.getImage().compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        intent.putExtra("title", smodel.getTitle());
        intent.putExtra("artist", smodel.getArtist());
        intent.putExtra("location", smodel.getLocation());
        intent.putExtra("position", position);
        intent.putExtra("image", byteArray);
        startActivity(intent);
    }

    public void dismisspopup() {
        selectplaylist.dismiss();
    }

    public void mydialogdismisspopup() {
        mydailog.dismiss();
    }


    public void changelayout(String playlistname) {
        if (actionMode == null) {
            this.playlistname = playlistname;
           /* layoutmain.setVisibility(View.GONE);*/
            layoutselectsong.setVisibility(View.VISIBLE);
            actionMode = startSupportActionMode(actionModeCallback);
            mActionModeIsActive = true;
            selectedSong = new ArrayList<>();
            selectsongrecycle.setHasFixedSize(true);
            selectsongrecycle.setLayoutManager(new LinearLayoutManager(this));
            songadapter = new SelectSongAdapter(this, totalsongList);
            selectsongrecycle.setAdapter(songadapter);
        }
    }

    public static Context getActivityContext() {
        return instance;
    }

    public  void setdataToView(String title, Bitmap image) {
        contentText.setText(title);
        contentviewImage.setImageBitmap(image);

    }

    public void updateui(int progress,String current,String total) {
            this.progress=progress;
            currentprogress=current;
            totalprogress=total;
            endtime.setText("" + total);
            // Displaying time completed playing
            starttime.setText("" + current);
            updater.setProgress(this.progress);
            waveLoadingView.setProgressValue(this.progress);
    }


    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.selectmode, menu);
            // disable swipe refresh if action mode is enabled
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            toolbar.setVisibility(View.GONE);
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.okbtn:
                    if (playlistname != null && selectedSong.size() > 0)
                        insertSong(playlistname);
                    if (mode != null) mode.finish();
                    layoutselectsong.setVisibility(View.GONE);
                   /* layoutmain.setVisibility(View.VISIBLE);*/
                    return true;
                case R.id.canbtn:/*selectedSong.clear();*/
                    if (mode != null) mode.finish();
                    layoutselectsong.setVisibility(View.GONE);
                   /* layoutmain.setVisibility(View.VISIBLE);*/
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
            toolbar.setVisibility(View.VISIBLE);
            layoutselectsong.setVisibility(View.GONE);
           /* if(!player.mp.isPlaying()){
                unbindService(serviceConnection);
                stopService(intent);
            }*/
           /* layoutmain.setVisibility(View.VISIBLE);*/
        }
    }


    public void insertSong(String note) {
        // get writable database as we want to write data
        DatabaseHelper help = new DatabaseHelper(this);
        SQLiteDatabase db = help.getWritableDatabase();
        int countsongs = 0;
        for (int i = 0; i < selectedSong.size(); i++) {
            ContentValues values = new ContentValues();
            // `id` and `timestamp` will be inserted automatically.
            // no need to add them
            values.put(PlayListStore.PLAYLIST_ID, note);
            values.put(PlayListStore.SONG_NAME, selectedSong.get(i).getTitle());
            values.put(PlayListStore.SONG_ARTIST, selectedSong.get(i).getArtist());
            values.put(PlayListStore.SONG_LOCATION, selectedSong.get(i).getLocation());
            values.put(PlayListStore.SONG_URI, String.valueOf(selectedSong.get(i).getUri()));
            values.put(PlayListStore.SONG_IMAGE, getBitmapAsByteArray(selectedSong.get(i).getImage()));
            // insert row
            db.insert(PlayListStore.TABLE_NAME, null, values);
            countsongs++;
        }
        // close db connection
        db.close();
        Toast.makeText(this, "Playlist created", Toast.LENGTH_SHORT).show();
    }


    private byte[] getBitmapAsByteArray(Bitmap image) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }


    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.dummy);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();
       /* if (serviceBound){
            unbindService(serviceConnection);
            serviceBound = false;
        }
        Toast.makeText(this,"stop",Toast.LENGTH_SHORT).show();*/
    }

    @Override
    protected void onStart() {
       if(intent==null) {
           intent = new Intent(this, MusicService.class);
       }
        //startService(intent);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        super.onStart();
    }

    public void setCheck(boolean state, SongModel model) {
        if (state) {
            count++;
            this.toolbarchange(count);
            selectedSong.add(model);
        } else {
            --count;
            this.toolbarchange(count);
            selectedSong.remove(model);
        }
    }
}
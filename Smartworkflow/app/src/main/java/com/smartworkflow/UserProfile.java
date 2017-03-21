package com.smartworkflow;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class UserProfile extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    /*Map
    * onCreate()
    * onBackPressed()
    * boolean onCreateOptionsMenu()
    * boolean onOptionsItemSelected()
    * boolean onNavigationItemSelected()
    * onActivityResult()
    * */
    //variables and objects
    Long TimefromDB;
    String userID, UserDisplayName;
    TextView UserName;
    Chronometer ProfileChrono;
    ImageView Profile_Image;
    Storage_Management storageManagement = new Storage_Management();// handle picture
    DB_Managment dbmanager = new DB_Managment(); // handle data
    boolean isChronometerRunning = false;
    long timeWhenStopped = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        //get extras
        Intent intent = getIntent();
        userID = intent.getStringExtra("USERID");
        UserDisplayName = intent.getStringExtra("USERNAME");
        //Chrono creation
        ProfileChrono = (Chronometer) findViewById(R.id.chronometer1);
        //toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //as it says below floating button, when pressed it starts or stops the Chrono
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isChronometerRunning)  // condition on which you check whether it's start or stop
                {
                    ProfileChrono.start();
                    Log.d("Clock", "IN");
                    dbmanager.GetTime(userID);
                    /*I have to wait for it to get time so I decided to check every second until it finishes the retrieve*/
                    final ScheduledExecutorService scheduler =
                            Executors.newSingleThreadScheduledExecutor();
                    scheduler.scheduleAtFixedRate
                            (new Runnable() {
                                public void run() {
                                    // Do something here on the main thread
                                    if (dbmanager.DownloadFinish){
                                        TimefromDB = dbmanager.Time;
                                        ProfileChrono.setBase(SystemClock.elapsedRealtime() + TimefromDB);
                                        Log.d("Time from DB", TimefromDB.toString());
                                        ProfileChrono.start();
                                        scheduler.shutdown();
                                    }
                                }
                            }, 0, 1, TimeUnit.SECONDS); // or .MINUTES, .HOURS etc.
                    ProfileChrono.start();
                    isChronometerRunning  = true;
                }
                else
                {
                    Log.d("Clock", "Out");
                    ProfileChrono.stop();
                    timeWhenStopped = ProfileChrono.getBase() - SystemClock.elapsedRealtime();
                    dbmanager.SaveTime(userID, timeWhenStopped);
                    isChronometerRunning  = false;
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        assert drawer != null;
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        //Navigation view or slider
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);
        //this to change slide profile name
        View header=navigationView.getHeaderView(0);
        UserName = (TextView)header.findViewById(R.id.textViewUserName);
        Profile_Image = (ImageView)header.findViewById(R.id.UserProfileImage);
        UserName.setText(UserDisplayName);
        //loading image from database for profile
        Profile_Image.setImageResource(R.drawable.genericperson);
        storageManagement.RetrievePicture(userID);
        /*I have to wait for it to download so I decided to check every second until it finishes the retrieve
        Create a schedule timer to check if picture finish downloading every second and once it does set it as profile picture*/
        final ScheduledExecutorService scheduler =
                Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate
                (new Runnable() {
                    public void run() {
                        // call
                        Log.d("Second", "+1");
                        // Do something here on the main thread
                        if (storageManagement.Download_finish){
                            Log.d("Download ended", "true");
                            Profile_Image.setImageBitmap(storageManagement.UserPictureFromDB);
                            scheduler.shutdown();
                        }
                    }
                }, 0, 1, TimeUnit.SECONDS); // or .MINUTES, .HOURS etc.
    }
    //ask first exit second time pressed
    private Boolean exit = false;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (exit) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(UserProfile.this, MainActivity.class);
                //start intent
                startActivity(intent);
            } else {
                Toast.makeText(this, "Press Back again to Sing Out.",
                        Toast.LENGTH_SHORT).show();
                exit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        exit = false;
                    }
                }, 3 * 1000);

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action continue with Camera
            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //sent the data to activity result, i have the user crop down there.
            startActivityForResult(takePicture, 0);//zero can be replaced with any action code
        } else if (id == R.id.nav_gallery) {
            // continue with gallery
            Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            //crop before setting the image
            try {
                pickPhoto.putExtra("crop", "true");
                pickPhoto.putExtra("aspectX", 1);
                pickPhoto.putExtra("aspectY", 1);
                pickPhoto.putExtra("outputX", 1920);
                pickPhoto.putExtra("outputY", 1080);
                pickPhoto.putExtra("return-data", "true");
                startActivityForResult(pickPhoto, 1);//one can be replaced with any action code
            } catch (Exception e) {
                // display an error message
                String errorMessage = "Whoops - your device doesn't support the crop action!";
                Toast toast = Toast.makeText(UserProfile.this, errorMessage, Toast.LENGTH_SHORT);
                toast.show();
            }
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    //On activity result + crop
    //result
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    // get the Uri for the captured image
                    final Bitmap CameraPicture = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    //set the image
                    try{
                        Profile_Image.setImageBitmap(CameraPicture);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    //save to database
                    //if greater than honeycomb run in the backgroud else on main thread
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                //TODO your background code
                                try{
                                    storageManagement.SavePicture(CameraPicture, userID);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        });
                    }else{
                        try{
                            storageManagement.SavePicture(CameraPicture, userID);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }

                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    // get the returned data
                    Bundle extras = imageReturnedIntent.getExtras();
                    // get the cropped bitmap
                    final Bitmap selectedBitmap = extras.getParcelable("data");
                    //set the image
                    try{
                        Profile_Image.setImageBitmap(selectedBitmap);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    //save to database
                    //if greater than honeycomb run in the backgroud else on main thread
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                //TODO your background code
                                try{
                                    storageManagement.SavePicture(selectedBitmap, userID);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        });
                    }else{
                        try{
                            storageManagement.SavePicture(selectedBitmap, userID);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
                break;
        }
    }
}

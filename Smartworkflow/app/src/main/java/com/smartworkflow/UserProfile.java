package com.smartworkflow;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class UserProfile extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    /*Map
    * onCreate() creates all variables plus graphics.
    * refreshValues() it delays the set pay function for 2 seconds, does the final calc and updates GUI, it can be called from anywhere.
    * StopTimer() stops the Chronometer
    * StartTimer() starts the Chronometer
    * onBackPressed() sends you back outside after warning you
    * boolean onCreateOptionsMenu() creates the menu of the left, left panel
    * boolean onOptionsItemSelected() Handle action bar item clicks
    * boolean onNavigationItemSelected() registers when an item is clicked and does something, left panel
    * useUIThreat() update graphics on UI thread, need it because Timer Threat doesn't allows it.
    * onActivityResult() // handles picture Intents and setting profile picture
    * */

    //variables
    private static final String TAG = "Image File";
    Locale local;
    String userID;
    static String UserDisplayName;
    public static Double valueDBPayRate = 0.0;
    Double netMoney = 0.0;
    long delayInMillis = 2000;
    private double m_Text = 0.0;
    boolean isChronometerRunning = false;
    long timeWhenStopped = 0;
    int ToDay;


    //and objects
    private AdView mAdView;
    Timer timer = new Timer();
    TextView textviewTotalHours;
    static TextView UserName, DayMonday, DayTuesday, DayWednesday;
    static TextView DayThursday, DayFriday, DaySaturday, DaySunday;
    static TextView lastWeekEarnings;
    static TextView priorOne, priorTwo;
    TextView payRateFromDB;
    static TextView netPay;
    static Chronometer ProfileChrono;
    static ImageView Profile_Image;
    ImageButton ClockStatus;
    Calendar cal = Calendar.getInstance();
    Storage_Management storageManagement = new Storage_Management();// handle picture
    DB_Managment dbmanager = new DB_Managment(); // handle data
    ProfileHelper helper = new ProfileHelper();
    public ProgressDialog pd;
    final int weekOfYear = cal.get(Calendar.WEEK_OF_YEAR);

    //creates most things I would say ______________________________________________________________
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        //ad
        MobileAds.initialize(this, "ca-app-pub-1762917079825621/4741555998");
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //loading
        pd = new ProgressDialog(UserProfile.this);
        pd.setTitle("Loading...");
        pd.setMessage("Please wait.");
        pd.show();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                pd.dismiss();
            }
        }, delayInMillis);

        //get permission if it's not there yet
        ActivityCompat.requestPermissions(UserProfile.this,
                new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                1);

        //calendar
        ToDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
        Log.d("Today", String.valueOf(ToDay));
        final String Day = helper.DaysOfWeek[ToDay];
        Log.d("Today", Day);
        //get extras
        Intent intent = getIntent();
        userID = intent.getStringExtra("USERID");
        //days of week values from textView
        DayMonday = (TextView) findViewById(R.id.textViewMondayValue);
        DayTuesday = (TextView) findViewById(R.id.textViewTuesdayValue);
        DayWednesday = (TextView) findViewById(R.id.textViewWednesdayValue);
        DayThursday = (TextView) findViewById(R.id.textViewThursdayValue);
        DayFriday = (TextView) findViewById(R.id.textViewFridayValue);
        DaySaturday = (TextView) findViewById(R.id.textViewSaturdayValue);
        DaySunday = (TextView) findViewById(R.id.textViewSundayValue);
        textviewTotalHours = (TextView) findViewById(R.id.textViewTotalHours);
        payRateFromDB = (TextView) findViewById(R.id.textViewCurrentPayRate);
        //display stuff
        dbmanager.PopulateDaysAtGlance(userID, weekOfYear); //loads time ASAP for Days
        dbmanager.GetTime(userID, weekOfYear, Day);//loads time ASAP
        UserDisplayName = intent.getStringExtra("USERNAME");
        //get stuff
        dbmanager.getPayRate(userID);
        //Chrono creation
        ProfileChrono = (Chronometer) findViewById(R.id.chronometer1);
        //clock status init
        ClockStatus = (ImageButton) findViewById(R.id.imageButtonClockStatus);
        //textviews
        netPay = (TextView) findViewById(R.id.textViewNetPay);
        lastWeekEarnings = (TextView) findViewById(R.id.textViewLastWeekEarnings);
        priorOne = (TextView) findViewById(R.id.textViewPriorOne);
        priorTwo = (TextView) findViewById(R.id.textViewPriorTwo);
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
                    StartTimer(weekOfYear, Day);
                }
                else
                {
                    StopTimer(weekOfYear, Day);
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
        refreshValues();

        DaySunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileHelper.timePicker(UserProfile.this, helper.DaysOfWeek[0], userID, weekOfYear);
                refreshValues();}
        });
        DayMonday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileHelper.timePicker(UserProfile.this, helper.DaysOfWeek[1], userID, weekOfYear);
                refreshValues();}
        });
        DayTuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileHelper.timePicker(UserProfile.this, helper.DaysOfWeek[2], userID, weekOfYear);
                refreshValues();       }
        });
        DayWednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileHelper.timePicker(UserProfile.this, helper.DaysOfWeek[3], userID, weekOfYear);
                refreshValues();            }
        });
        DayThursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileHelper.timePicker(UserProfile.this, helper.DaysOfWeek[4], userID, weekOfYear);
                refreshValues();           }
        });
        DayFriday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileHelper.timePicker(UserProfile.this, helper.DaysOfWeek[5], userID, weekOfYear);
                refreshValues();           }
        });
        DaySaturday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileHelper.timePicker(UserProfile.this, helper.DaysOfWeek[6], userID, weekOfYear);
                refreshValues();}
        });

    }

    //Refresh  _____________________________________________________________________________________
    private void refreshValues() {
        dbmanager.getWeekEarnings(userID, weekOfYear, 1);
        dbmanager.getWeekEarnings(userID, weekOfYear - 1, 2);
        dbmanager.getWeekEarnings(userID, weekOfYear - 2, 3);
        dbmanager.getWeekEarnings(userID, weekOfYear - 3, 4);
        //it takes some time for graphics to load, after load get the values
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                helper.setPayNet();
                netMoney = valueDBPayRate * helper.DayHours;
                //save total to the given week
                dbmanager.saveWeekEarnings(userID, weekOfYear, netMoney);
                useUIThread();
            }
        }, 5000);
    }
    //Stop Timer ___________________________________________________________________________________
    private void StopTimer(int weekOfYear, String Day){
        ProfileChrono.stop();
        timeWhenStopped = UserProfile.ProfileChrono.getBase() - SystemClock.elapsedRealtime();
        Log.d("Time when Stopped", String.valueOf(timeWhenStopped));
        dbmanager.SaveTime(userID, timeWhenStopped, weekOfYear, Day);
        ClockStatus.setImageResource(R.drawable.clockout);
        dbmanager.PopulateDaysAtGlance(userID, weekOfYear); //loads time ASAP for Days
        refreshValues();
        isChronometerRunning  = false;
    }
    //Start Timer __________________________________________________________________________________
    private void StartTimer(int weekOfYear, String Day){
        dbmanager.GetTime(userID, weekOfYear, Day);
        Log.d("Clock ", "IN");
        ProfileChrono.start();
        ClockStatus.setImageResource(R.drawable.clockin);
        isChronometerRunning  = true;
    }

    //ask first, exit second time pressed ___________________________________________________________
    private Boolean exit = false;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (exit) {
                //FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                //Intent intent = new Intent(UserProfile.this, MainActivity.class);
                //start intent
                //startActivity(intent);
            } else {
                Toast.makeText(this, "Press Back again to Exit.",
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
    //Create Options _______________________________________________________________________________
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_profile, menu);
        return true;
    }
    //Options Select _______________________________________________________________________________
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent UserRegistration = new Intent(UserProfile.this, SettingsActivity.class);
            startActivity(UserRegistration);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //Navigation Item Select _______________________________________________________________________
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        ToDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
        Log.d("Today 2", String.valueOf(ToDay));
        final String Day = helper.DaysOfWeek[ToDay];
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
        } else if (id == R.id.nav_refresh) {
            //reload everything
            dbmanager.PopulateDaysAtGlance(userID, weekOfYear); //loads time ASAP for Days
            storageManagement.RetrievePicture(userID);
            refreshValues();


        } else if (id == R.id.nav_resetclock) {
            //reset time to 0 and start the Chronometer
            //also delete the time value on DB
            dbmanager.DeleteTime(userID, weekOfYear, Day);
            ProfileChrono.setBase(SystemClock.elapsedRealtime());
            ProfileChrono.start();
            ClockStatus.setImageResource(R.drawable.clockin);
            isChronometerRunning  = true;

        } else if (id == R.id.nav_sethrate) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Enter pay rate");

            // Set up the input
            final EditText inputpayrate = new EditText(this);
            // Specify the type of input expected; sets the input as a password, and will mask the text
            inputpayrate.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            builder.setView(inputpayrate);

            // Set up the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    m_Text = Double.parseDouble(inputpayrate.getText().toString());
                    dbmanager.setPayRate(userID, m_Text);
                    valueDBPayRate = m_Text;
                    refreshValues();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();

        } else if (id == R.id.nav_share) {
            //close the drawer
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            assert drawer != null;
            drawer.closeDrawer(GravityCompat.START);
            //wait for it to close
            pd.setTitle("Loading...");
            pd.setMessage("Please wait.");
            pd.show();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    //take the screenshot
                    final View rootView = getWindow().getDecorView().
                            findViewById(android.R.id.content);
                    //saves the screenshot
                    getScreenShot(rootView);
                    //get the screenshot
                    Bitmap b = loadBitmap(UserProfile.this, "Screenshot.jpeg");
                    //Bitmap c = Bitmap.createScaledBitmap(b, 150, 150, true);
                    Uri uriToImage = ProfileHelper.getImageUri(UserProfile.this, b);
                    //share intent
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, uriToImage);
                    shareIntent.setType("image/jpeg");
                    try {
                        startActivity(Intent.createChooser(shareIntent, "Share via"));
                    } catch (Exception ex) {
                        Log.d("ERROR", ex.getMessage());
                    }
                    pd.dismiss();
                }
            }, 500);



        } else if(id == R.id.nav_LogoutProfile){
            if(isChronometerRunning){
                StopTimer(weekOfYear, Day);
            }
            //i need to stop clock and register time on DB
            LoginManager.getInstance().logOut(); //clear button try 1
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(UserProfile.this, MainActivity.class);
            //start intent
            startActivity(intent);

        } else if (id == R.id.nav_help){
            // set title
            String title = this.getString(R.string.Help);
            // set dialog message
            String message = this.getString(R.string.HelpMessage);

            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Presentation);
            } else {
                builder = new AlertDialog.Builder(this);
            }
            builder.setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //update graphics on n UI thread________________________________________________________________
    private void useUIThread() {
        new Thread() {
            public void run() {
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dbmanager.getPayRate(userID);
                            payRateFromDB.setText(String.format(local, valueDBPayRate.toString()));
                            Double roundOff = Math.round(helper.DayHours * 100.0) / 100.0;
                            textviewTotalHours.setText(String.format(local, roundOff.toString()));
                            payRateFromDB.setText(String.format(local, valueDBPayRate.toString()));
                            helper.DayHours = 0.0D;
                        }
                    });
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    //take screenshot of the app (working)__________________________________________________________
    public void getScreenShot(View view) {
        View screenView = view.getRootView();
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        if (bitmap != null){
            Log.d(TAG, "Screenshoted");
        }
        saveFile(UserProfile.this, bitmap, "Screenshot.jpeg");
    }
    //store the screenshot so that I can use it and send it (not working)___________________________
    public static void saveFile(Context context, Bitmap b, String picName){
        FileOutputStream fos;
        try {
            fos = context.openFileOutput(picName, Context.MODE_PRIVATE);
            b.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
            Log.d(TAG, "Save Success");
        }
        catch (FileNotFoundException e) {
            Log.d(TAG, "Write file not found");
            e.printStackTrace();
        }
        catch (IOException e) {
            Log.d(TAG, "io exception");
            e.printStackTrace();
        }

    }
    //Get Image ____________________________________________________________________________________
    public static Bitmap loadBitmap(Context context, String picName){
        Bitmap b = null;
        FileInputStream fis;
        try {
            fis = context.openFileInput(picName);
            b = BitmapFactory.decodeStream(fis);
            fis.close();

        }
        catch (FileNotFoundException e) {
            Log.d(TAG, "file not found");
            e.printStackTrace();
        }
        catch (IOException e) {
            Log.d(TAG, "io exception");
            e.printStackTrace();
        }
        return b;
    }

    //On activity result + crop ____________________________________________________________________
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
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                storageManagement.SavePicture(CameraPicture, userID);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    });
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
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                storageManagement.SavePicture(selectedBitmap, userID);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    });
                }
                break;
        }
    }

    //request return _______________________________________________________________________________
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(UserProfile.this, "Permission denied to read your External " +
                            "storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}

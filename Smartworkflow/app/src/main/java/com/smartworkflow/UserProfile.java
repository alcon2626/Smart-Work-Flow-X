package com.smartworkflow;

import android.app.ProgressDialog;
import android.content.DialogInterface;
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

import com.google.firebase.auth.FirebaseAuth;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class UserProfile extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    /*Map
    * onCreate()
    * StopTimer()
    * StartTimer()
    * onBackPressed()
    * boolean onCreateOptionsMenu()
    * boolean onOptionsItemSelected()
    * boolean onNavigationItemSelected()
    * onActivityResult()
    * */
    //variables and objects
    Locale local;
    String userID;
    String UserDisplayName;
    Double valuePayRate;
    static TextView UserName, DayMonday, DayTuesday, DayWednesday;
    static TextView DayThursday, DayFriday, DaySaturday, DaySunday;
    TextView netPay;
    static Chronometer ProfileChrono;
    static ImageView Profile_Image;
    private double m_Text = 0.0;
    private double PayRateDBValue;
    ImageButton ClockStatus;
    History history = new History();
    Date date = new Date();
    Calendar cal = Calendar.getInstance();
    Storage_Management storageManagement = new Storage_Management();// handle picture
    DB_Managment dbmanager = new DB_Managment(); // handle data
    boolean isChronometerRunning = false;
    long timeWhenStopped = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        //two seconds to load information
        final ProgressDialog pd = new ProgressDialog(UserProfile.this);
        pd.setTitle("Loading...");
        pd.setMessage("Please wait.");
        pd.show();

        long delayInMillis = 2000;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                pd.dismiss();
            }
        }, delayInMillis);
        //time
        int ToDay;
        //calendar
        cal.setTime(date);
        final int weekOfYear = cal.get(Calendar.WEEK_OF_YEAR);
        ToDay = date.getDay()-1;
        final String Day = history.DaysOfWeek[ToDay];
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
        //display stuff
        dbmanager.PopulateDaysAtGlance(userID, weekOfYear); //loads time ASAP for Days
        dbmanager.GetTime(userID, weekOfYear, Day);//loads time ASAP
        UserDisplayName = intent.getStringExtra("USERNAME");
        //Chrono creation
        ProfileChrono = (Chronometer) findViewById(R.id.chronometer1);
        //clock status init
        ClockStatus = (ImageButton) findViewById(R.id.imageButtonClockStatus);
        //textview netpay
        netPay = (TextView) findViewById(R.id.textViewNetPay);
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
    }

    public void setPayNet() {

        Double DayHours = 0.0;
        String dateTest = "";
                //pay rate
        dateTest = DayMonday.getText().toString();
        Log.d("Monday", dateTest);
        DayHours += daysToDouble(dateTest);
        dateTest = DayTuesday.getText().toString();
        Log.d("Tuesday", dateTest);
        DayHours += daysToDouble(dateTest);
        dateTest = DayWednesday.getText().toString();
        Log.d("Wednesday", dateTest);
        DayHours += daysToDouble(dateTest);
        dateTest = DayThursday.getText().toString();
        Log.d("Thursday", dateTest);
        DayHours += daysToDouble(dateTest);
        dateTest = DayFriday.getText().toString();
        Log.d("Friday", dateTest);
        DayHours += daysToDouble(dateTest);
        dateTest = DaySaturday.getText().toString();
        Log.d("Saturday", dateTest);
        DayHours += daysToDouble(dateTest);
        dateTest = DaySunday.getText().toString();
        Log.d("Sunday", dateTest);
        DayHours += daysToDouble(dateTest);
        Log.d("Total hours", DayHours.toString());

        valuePayRate = dbmanager.getPayRate(userID);
        netPay.setText(String.format(local, DayHours.toString()));

    }
    private Double daysToDouble(String day){
        Double total = 0.0;
        String[] tokens = day.split(":");
        int hours = Integer.parseInt(tokens[0]);
        int minutes = Integer.parseInt(tokens[1]);
        day = hours + "." + minutes;
        total = Double.parseDouble(day);
        return total;
    }

    private void StopTimer(int weekOfYear, String Day){
        ProfileChrono.stop();
        timeWhenStopped = UserProfile.ProfileChrono.getBase() - SystemClock.elapsedRealtime();
        Log.d("Time when Stopped", String.valueOf(timeWhenStopped));
        dbmanager.SaveTime(userID, timeWhenStopped, weekOfYear, Day);
        ClockStatus.setImageResource(R.drawable.clockout);
        dbmanager.PopulateDaysAtGlance(userID, weekOfYear); //loads time ASAP for Days
        isChronometerRunning  = false;
    }
    private void StartTimer(int weekOfYear, String Day){
        dbmanager.GetTime(userID, weekOfYear, Day);
        Log.d("Clock ", "IN");
        ProfileChrono.start();
        ClockStatus.setImageResource(R.drawable.clockin);
        isChronometerRunning  = true;
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
        final int weekOfYear = cal.get(Calendar.WEEK_OF_YEAR);
        int ToDay = date.getDay()-1;
        final String Day = history.DaysOfWeek[ToDay];
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
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            inputpayrate.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            builder.setView(inputpayrate);

            // Set up the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    m_Text = Double.parseDouble(inputpayrate.getText().toString());
                    dbmanager.setPayRate(userID, m_Text);
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

        } else if (id == R.id.nav_send) {

        } else if(id == R.id.nav_LogoutProfile){
            StopTimer(weekOfYear, Day);
            //i need to stop clock and register time on DB
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(UserProfile.this, MainActivity.class);
            //start intent
            startActivity(intent);

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

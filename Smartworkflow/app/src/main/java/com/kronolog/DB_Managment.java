package com.kronolog;

import android.app.Activity;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by LeoAizen on 3/21/2017.
 */

public class DB_Managment  extends Activity {

    //track time
    Long Time;
    Double earnings = 0.0D;
    ProfileHelper helper = new ProfileHelper();
    private Double valuePayRate;
    Locale local;
    //objects
    private static final String TAG = DB_Managment.class.getSimpleName();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("users");
    //array of Textview
    int i;
    int textVuewSize = 7;
    TextView[] arraytextView = new TextView[textVuewSize];
    //save data
    public void SaveTime(String userid, Long timeWhenStopped, int weekOfYear, String Day){
        DatabaseReference UserReference = myRef.child(userid+"/"+weekOfYear+"/"+Day+"/Time");
        UserReference.setValue(timeWhenStopped);
    }


    //get data
    public void GetTime(String userid, int weekOfYear, String Day){
        DatabaseReference UserReference = myRef.child(userid+"/"+weekOfYear+"/"+Day+"/Time");
        UserReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Time = dataSnapshot.getValue(Long.class);
                if (Time == null){
                    Time = 0L;
                    UserProfile.ProfileChrono.setBase(SystemClock.elapsedRealtime()+ Time);
                }else{
                    UserProfile.ProfileChrono.setBase(SystemClock.elapsedRealtime()+ Time);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }
    public void setPayRate(String userid, Double payrate){
        DatabaseReference UserReference = myRef.child(userid+"/"+"PayRate");
        UserReference.setValue(payrate);
    }
    public void getPayRate(String userid){
        DatabaseReference UserReference = myRef.child(userid+"/"+"PayRate");
        UserReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                valuePayRate = dataSnapshot.getValue(Double.class);
                if (valuePayRate == null){
                    valuePayRate = 0.0D;
                }else{
                    Log.d("valuePayRate ", String.valueOf(valuePayRate));
                    UserProfile.valueDBPayRate = valuePayRate;
                    Log.d("valuePayRate UP ", String.valueOf(UserProfile.valueDBPayRate));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }
    //delete and reset the clock
    public void DeleteTime(String userid, int weekOfYear, String Day){
        DatabaseReference UserReference = myRef.child(userid+"/"+weekOfYear+"/"+Day+"/Time");
        UserReference.removeValue();
    }
    //populates days wiht proper info
    public void PopulateDaysAtGlance(String userid, int weekOfYear){
        for (i = 0; i < 7; i++){
            String Day = helper.DaysOfWeek[i];
            GetTimePerDay(userid, weekOfYear, Day);
        }
    }

    public void GetTimePerDay(String userid, int weekOfYear, final String Day){
        DatabaseReference UserReference = myRef.child(userid+"/"+weekOfYear+"/"+Day+"/Time");
        UserReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long value = dataSnapshot.getValue(Long.class);
                if (value != null){
                    value = value  * -1;
                    String time = String.format(local, "%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(value),
                            TimeUnit.MILLISECONDS.toMinutes(value) % TimeUnit.HOURS.toMinutes(1),
                            TimeUnit.MILLISECONDS.toSeconds(value) % TimeUnit.MINUTES.toSeconds(1));
                    switch (Day){
                        //setters
                        case "Monday":
                            UserProfile.DayMonday.setText(time);
                            break;
                        case "Tuesday":
                            UserProfile.DayTuesday.setText(time);
                            break;
                        case "Wednesday":
                            UserProfile.DayWednesday.setText(time);
                            break;

                        case "Thursday":
                            UserProfile.DayThursday.setText(time);
                            break;

                        case "Friday":
                            UserProfile.DayFriday.setText(time);
                            break;

                        case "Saturday":
                            UserProfile.DaySaturday.setText(time);
                            break;

                        case "Sunday":
                            UserProfile.DaySunday.setText(time);
                            break;
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }


    //save earnings for a given week
    public void saveWeekEarnings(String userid, int weekOfYear, Double earnings) {
        DatabaseReference UserReference = myRef.child(userid+"/"+weekOfYear+"/"+"Earnings");
        UserReference.setValue(earnings);
    }
    //get earnings for a given week
    void getWeekEarnings(String userid, int weekOfYear, final int type) {
        DatabaseReference UserReference = myRef.child(userid+"/"+weekOfYear+"/"+"Earnings");
        UserReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                earnings = dataSnapshot.getValue(Double.class);
                if(earnings == null){
                    earnings = 0.0D;
                }
                Double roundOff = Math.round(earnings * 100.0) / 100.0;
                Log.d("Reading Earn", "Sucess");
                switch (type){
                    case 1:
                        //current week
                        UserProfile.netPay.setText(String.format(local, roundOff.toString()));
                        break;
                    case 2:
                        //do something
                        UserProfile.lastWeekEarnings.setText(String.format(local, roundOff.toString()));
                        break;
                    case 3:
                        //do something
                        UserProfile.priorOne.setText(String.format(local, roundOff.toString()));
                        break;
                    case 4:
                        //do something
                        UserProfile.priorTwo.setText(String.format(local, roundOff.toString()));
                        break;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                earnings = 0.0D;
                // Failed to read value
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }
}
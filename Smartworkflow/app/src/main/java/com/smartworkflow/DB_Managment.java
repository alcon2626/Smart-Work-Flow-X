package com.smartworkflow;

import android.app.Activity;
import android.util.Log;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by LeoAizen on 3/21/2017.
 */

public class DB_Managment  extends Activity {

    //keep track of day
    String Day;
    private int ToDay;
    Date date = new Date();
    History history = new History();
    //track time
    public Long Time, Long_Monday, Long_Tuesday, Long_Wednesday, Long_Thursday, Long_Friday, Long_Saturday, Long_Sunday;
    public boolean DownloadFinish;
    //objects
    private static final String TAG = DB_Managment.class.getSimpleName();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("users");


    //save data (works)
    public void SaveTime(String userid, Long time){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int weekOfYear = cal.get(Calendar.WEEK_OF_YEAR);
        Log.d("Week of Year", String.valueOf(weekOfYear)); // great it does work
        ToDay = date.getDay()-1;
        Day = history.DaysOfWeek[ToDay];
        DatabaseReference UserReference = myRef.child(userid+"/"+weekOfYear+"/"+Day+"/Time");//need to add day
        UserReference.setValue(time);
    }


    //get data (works)
    public void GetTime(String userid){
        DownloadFinish = false;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int weekOfYear = cal.get(Calendar.WEEK_OF_YEAR);
        ToDay = date.getDay()-1;
        Day = history.DaysOfWeek[ToDay];
        DatabaseReference UserReference = myRef.child(userid+"/"+weekOfYear+"/"+Day+"/Time");
        UserReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Time = dataSnapshot.getValue(Long.class);
                //check if empty
                if (Time == null){
                    Log.d("Time Empty:", "True");
                }else{
                    Log.d("Time Empty:", "False");
                    DownloadFinish = true;
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
    public void DeleteTime(String userid){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int weekOfYear = cal.get(Calendar.WEEK_OF_YEAR);
        ToDay = date.getDay()-1;
        Day = history.DaysOfWeek[ToDay];
        DatabaseReference UserReference = myRef.child(userid+"/"+weekOfYear+"/"+Day+"/Time");
        UserReference.removeValue();
    }

    /*public void PopulateInformationAtGlance(String userid){
        ToDay = date.getDay()-1;
        Day = history.DaysOfWeek[ToDay];
        switch (Day){
            case "Monday":


        }
    }*/
}
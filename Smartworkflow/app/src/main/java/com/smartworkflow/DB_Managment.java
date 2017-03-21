package com.smartworkflow;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by LeoAizen on 3/21/2017.
 */

public class DB_Managment {
    public Long Time;
    public boolean DownloadFinish;
    private static final String TAG = DB_Managment.class.getSimpleName();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("users");
    //save data (works)
    public void SaveTime(String userid, Long time){
        DatabaseReference UserReference = myRef.child(userid+"/Time");
        UserReference.setValue(time);
    }


    //get data (testing)
    public void GetTime(String userid){
        DownloadFinish = false;
        DatabaseReference UserReference = myRef.child(userid+"/Time");
        UserReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Time = dataSnapshot.getValue(Long.class);
                DownloadFinish = true;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }
}

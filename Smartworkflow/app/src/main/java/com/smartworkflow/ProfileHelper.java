package com.smartworkflow;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

/**
 * Created by SurfaceUser on 6/12/2017.
 */
/*
 * daysToDouble() takes the hours and minutes and make them useful, string to double, also turns minutes to money equivalent value.
 * setPayNet() takes the values from the days on display, adds them up.
 */

class ProfileHelper{
    static int userHour = 0;
    static int userMinute = 0;
    private Locale local;
    static long duration = 0;
    Double DayHours = 0.0;
    String DaysOfWeek[] = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

    private Double daysToDouble(String day){
        Double minutescoverted = 0.0;
        Double total = 0.0;
        String[] tokens = day.split(":");
        int hours = Integer.parseInt(tokens[0]);
        Double minutes = Double.parseDouble(tokens[1]);
        minutescoverted = minutes / 100 * 1.666666666;
        day = String.valueOf(hours);
        total = Double.parseDouble(day);
        total += minutescoverted;
        return total;
    }

    void setPayNet() {
        String dateTest;
        dateTest = UserProfile.DayMonday.getText().toString();
        DayHours += daysToDouble(dateTest);

        dateTest = UserProfile.DayTuesday.getText().toString();
        DayHours += daysToDouble(dateTest);

        dateTest = UserProfile.DayWednesday.getText().toString();
        DayHours += daysToDouble(dateTest);

        dateTest = UserProfile.DayThursday.getText().toString();
        DayHours += daysToDouble(dateTest);

        dateTest = UserProfile.DayFriday.getText().toString();
        DayHours += daysToDouble(dateTest);

        dateTest = UserProfile.DaySaturday.getText().toString();
        DayHours += daysToDouble(dateTest);

        dateTest = UserProfile.DaySunday.getText().toString();

        DayHours += daysToDouble(dateTest);

        Log.d("Total hours", DayHours.toString());
    }
    //dialogBox as a time picker____________________________________________________________________
    static void timePicker(final Context context, final String day, final String userID, final int weekOfYear){
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                userHour = selectedHour;
                userMinute = selectedMinute;
                Log.d("MINUTES", String.valueOf(userMinute));
                Log.d("HOURS", String.valueOf(userHour));
                String valueHoursDouble = String.valueOf(userHour);
                String valueMinutesDouble = String.valueOf(userMinute);
                Log.d("V MINUTES", valueMinutesDouble);
                Log.d("V HOURS", valueHoursDouble);
                String allTogether;
                if(userMinute < 10){
                    allTogether = valueHoursDouble +'.'+'0'+ valueMinutesDouble;
                }else{
                    allTogether = valueHoursDouble +'.'+ valueMinutesDouble;
                }
                Log.d("All", allTogether);

                DB_Managment dbmanager = new DB_Managment();
                long timeWhenStopped = 0;
                Double m_Text = Double.parseDouble(allTogether);
                Log.d("m_Text Double", m_Text.toString());

                String time = doubleToTimeFormat(m_Text);
                Log.d("finalResult", time);

                timeWhenStopped = duration * -1;
                Log.d("timeWhenStopped", String.valueOf(timeWhenStopped));
                switch(day){
                    //setters
                    case "Monday":
                        UserProfile.DayMonday.setText(time);
                        dbmanager.SaveTime(userID, timeWhenStopped, weekOfYear, day);
                        break;
                    case "Tuesday":
                        UserProfile.DayTuesday.setText(time);
                        dbmanager.SaveTime(userID, timeWhenStopped, weekOfYear, day);
                        break;
                    case "Wednesday":
                        UserProfile.DayWednesday.setText(time);
                        dbmanager.SaveTime(userID, timeWhenStopped, weekOfYear, day);
                        break;

                    case "Thursday":
                        UserProfile.DayThursday.setText(time);
                        dbmanager.SaveTime(userID, timeWhenStopped, weekOfYear, day);
                        break;

                    case "Friday":
                        UserProfile.DayFriday.setText(time);
                        dbmanager.SaveTime(userID, timeWhenStopped, weekOfYear, day);
                        break;

                    case "Saturday":
                        UserProfile.DaySaturday.setText(time);
                        dbmanager.SaveTime(userID, timeWhenStopped, weekOfYear, day);
                        break;

                    case "Sunday":
                        UserProfile.DaySunday.setText(time);
                        dbmanager.SaveTime(userID, timeWhenStopped, weekOfYear, day);
                        break;
                }


            }
        }, 0, 0, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    static private String doubleToTimeFormat(Double value){
        Log.d("m_Text D to Time", value.toString());
        String splitter;
        int hoursH = 0, minutesM = 0, secondS = 0;
        splitter = value.toString();
        Log.d("splitter", splitter);
        StringTokenizer defaultTokenizer = new StringTokenizer(splitter,".");


        String hours = defaultTokenizer.nextToken();
        Log.d("SV HOURS", hours);
        if (hours.length() == 1){
            hours = "0" + hours;
            hoursH = Integer.parseInt(hours);
            Log.d("int hours", String.valueOf(hoursH));
        }else{
            hoursH = Integer.parseInt(hours);
        }
        String minutes = defaultTokenizer.nextToken();
        Log.d("SV MINUTES", minutes);
        if (minutes.length()== 1){
            minutes =  minutes  + "0";
            minutesM = Integer.parseInt(minutes);
            Log.d("int min", String.valueOf(minutesM));
        }else{
            minutesM = Integer.parseInt(minutes);
        }
        Log.d("Duration", String.valueOf(hoursH+"."+minutesM));
        duration = 3600 * hoursH + 60 * minutesM + secondS;
        duration = TimeUnit.MILLISECONDS.convert(duration, TimeUnit.SECONDS);
        System.out.println("time in millis = " + duration);
        return hours+":"+ minutes + ":" + "00";
    }


    static public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}

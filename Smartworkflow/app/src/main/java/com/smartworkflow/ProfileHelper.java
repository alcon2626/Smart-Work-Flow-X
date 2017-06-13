package com.smartworkflow;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    static void MyAlertBox(String title, String mymessage, Context context, final String day, final String userID, final int weekOfYear)
    {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(mymessage);

        // Set up the input
        final EditText inputpayrate = new EditText(context);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        inputpayrate.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        builder.setView(inputpayrate);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DB_Managment dbmanager = new DB_Managment();
                long timeWhenStopped = 0;
                Double m_Text = Double.parseDouble(inputpayrate.getText().toString());
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
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    static private String doubleToTimeFormat(Double value){
        Log.d("m_Text D to Time", value.toString());
        String splitter;
        int hoursH = 0, minutesM = 0, secondS = 0;
        splitter = value.toString();
        Log.d("splitter", splitter);
        StringTokenizer defaultTokenizer = new StringTokenizer(splitter,".");

        String hours = defaultTokenizer.nextToken();
        if (hours.length() == 1){
            hours = "0" + hours;
            hoursH = Integer.parseInt(hours);
        }
        String minutes = defaultTokenizer.nextToken();
        if (minutes.length()== 1){
            minutes = "0" + minutes;
            hoursH = Integer.parseInt(hours);
        }else{
            minutes = minutes.substring(0, 2);
            minutesM = Integer.parseInt(minutes);
        }
        duration = 3600 * hoursH + 60 * minutesM + secondS;
        duration = TimeUnit.MILLISECONDS.convert(duration, TimeUnit.SECONDS);
        System.out.println("time in millis = " + duration);
        return hours+":"+ minutes + ":" + "00";
    }
}

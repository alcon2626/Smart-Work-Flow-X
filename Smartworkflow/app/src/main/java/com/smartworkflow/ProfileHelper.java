package com.smartworkflow;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;

/**
 * Created by SurfaceUser on 6/12/2017.
 */
/*
 * daysToDouble() takes the hours and minutes and make them useful, string to double, also turns minutes to money equivalent value.
 * setPayNet() takes the values from the days on display, adds them up.
 */

class ProfileHelper{
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

    static void MyAlertBox(String title, String mymessage,  Context context, String day)
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
                Double m_Text = Double.parseDouble(inputpayrate.getText().toString());
                Log.d("m_Text", m_Text.toString());
                //String test = doubleToTimeFormat(m_Text);

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
    private static String doubleToTimeFormat(Double value){
        String splitter;
        splitter = String.valueOf(value);
        String[] tokens = splitter.split(".");
        String hours = tokens[0];
        String minutes = tokens[1];
        String finalResult = hours+":"+ minutes + ":" + "00";
        Log.d("finalResult", finalResult);
        return finalResult;
    }
}

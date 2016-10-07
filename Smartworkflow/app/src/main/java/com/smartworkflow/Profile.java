package com.smartworkflow;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Profile extends AppCompatActivity implements View.OnClickListener {
    FloatingActionButton fab, fab_1, fab_2, fab_3;
    //fab_1 is the group/network, fab_2 is messages and fab_3 is clock.
    Animation fab_open, fab_close, rotate_forward, rotate_backward;
    private Boolean isFabOpen = false;
    ImageView ProfilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab_1 = (FloatingActionButton) findViewById(R.id.fab_1);
        fab_2 = (FloatingActionButton) findViewById(R.id.fab_2);
        fab_3 = (FloatingActionButton) findViewById(R.id.fab_3);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);
        ProfilePicture = (ImageView) findViewById(R.id.Profile_Picture);
        //listeners
        fab.setOnClickListener(this);
        fab_1.setOnClickListener(this);
        fab_2.setOnClickListener(this);
        fab_3.setOnClickListener(this);
        //picture selector and set
        SetProfilePicture();
        //action when buttons are click bellow


    }

    private void SetProfilePicture() {
        ProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Profile.this)
                        .setTitle("Image Option")
                        .setMessage("Select a source for you Image")
                        .setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with Camera
                                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(takePicture, 0);//zero can be replaced with any action code
                            }
                        })
                        .setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with gallery
                                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(pickPhoto , 1);//one can be replaced with any action code
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.fab:

                animateFAB();
                break;
            case R.id.fab_1:

                Log.d("Raj", "Fab 1");
                break;
            case R.id.fab_2:

                Log.d("Raj", "Fab 2");
                break;
            case R.id.fab_3:

                Log.d("Raj", "Fab 3");
                break;
        }
    }
    public void animateFAB(){

        if(isFabOpen){

            fab.startAnimation(rotate_backward);
            fab_1.startAnimation(fab_close);
            fab_2.startAnimation(fab_close);
            fab_3.startAnimation(fab_close);
            fab_1.setClickable(false);
            fab_2.setClickable(false);
            fab_3.setClickable(false);
            isFabOpen = false;
            Log.d("Raj", "close");

        } else {

            fab.startAnimation(rotate_forward);
            fab_1.startAnimation(fab_open);
            fab_2.startAnimation(fab_open);
            fab_3.startAnimation(fab_open);
            fab_1.setClickable(true);
            fab_2.setClickable(true);
            fab_3.setClickable(true);
            isFabOpen = true;
            Log.d("Raj","open");

        }
    }

    //result
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    ProfilePicture.setImageURI(selectedImage);
                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    ProfilePicture.setImageURI(selectedImage);
                }
                break;
        }
    }
}

package com.smartworkflow;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class Profile extends AppCompatActivity implements View.OnClickListener {
    FloatingActionButton fab, fab_1, fab_2, fab_3;
    //fab_1 is the group/network, fab_2 is messages and fab_3 is clock.
    Animation fab_open, fab_close, rotate_forward, rotate_backward;
    private Boolean isFabOpen = false;
    ImageView ProfilePicture, CompanyLogo;
    private Uri picUri;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://smart-workflow-144316.appspot.com");
    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //get extras
        Intent intent = getIntent();
        userID = intent.getStringExtra("USERID");
        Log.d("UserID", userID);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       //Animated Buttoms
        fab_1 = (FloatingActionButton) findViewById(R.id.fab_1);
        fab_2 = (FloatingActionButton) findViewById(R.id.fab_2);
        fab_3 = (FloatingActionButton) findViewById(R.id.fab_3);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_backward);
        ProfilePicture = (ImageView) findViewById(R.id.Profile_Picture);
        CompanyLogo = (ImageView) findViewById(R.id.CompanyLogo);
        //listeners
        fab.setOnClickListener(this);
        fab_1.setOnClickListener(this);
        fab_2.setOnClickListener(this);
        fab_3.setOnClickListener(this);
        //load basic info
        LoadParameters();
        //picture selector and set
        SetProfilePicture();
        SetCompanyLogo();
        //action when buttons are click bellow


    }
    private Boolean exit = false;
    @Override
    public void onBackPressed(){
        if (exit) {
            Intent intent = new Intent(Profile.this, MainActivity.class);
            //start intent
            startActivity(intent);
        } else {
            FirebaseAuth.getInstance().signOut();
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
                                //sent the data to activity result, i have the user crop down there.
                                startActivityForResult(takePicture, 0);//zero can be replaced with any action code
                            }
                        })
                        .setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
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
                                    Toast toast = Toast.makeText(Profile.this, errorMessage, Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            }
                        })
                        .setIcon(android.R.drawable.gallery_thumb)
                        .show();
            }
        });
    }

    //animated buttons click listeners
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
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

    //animated buttons
    public void animateFAB() {

        if (isFabOpen) {

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
            Log.d("Raj", "open");

        }
    }

    //result
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    // get the Uri for the captured image
                    picUri = imageReturnedIntent.getData();
                    //Have the user crop the image taken from cam
                    performCrop();
                }

                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    // get the returned data
                    Bundle extras = imageReturnedIntent.getExtras();
                    // get the cropped bitmap
                    Bitmap selectedBitmap = extras.getParcelable("data");
                    //set the image
                    ProfilePicture.setImageBitmap(selectedBitmap);
                    SaveImageProfile(selectedBitmap);

                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    // get the returned data
                    Bundle extras = imageReturnedIntent.getExtras();
                    // get the cropped bitmap
                    Bitmap selectedBitmap = extras.getParcelable("data");
                    //set the image
                    ProfilePicture.setImageBitmap(selectedBitmap);
                    SaveImageProfile(selectedBitmap);
                }
            case 3:
                if (resultCode == RESULT_OK) {
                    // get the returned data
                    Bundle extras = imageReturnedIntent.getExtras();
                    // get the cropped bitmap
                    Bitmap selectedBitmap = extras.getParcelable("data");
                    //set the image
                    CompanyLogo.setImageBitmap(selectedBitmap);
                    SaveCompanyLogo(selectedBitmap);
                }

                break;
        }
    }

    //camera crop function
    private void performCrop() {
        // take care of exceptions
        try {
            // call the standard crop action intent (the user device may not
            // support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 1920);
            cropIntent.putExtra("outputY", 1080);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, 2);
        }
        // respond to users whose devices do not support the crop action
        catch (Exception e) {
            Toast toast = Toast
                    .makeText(this, "This device doesn't support the crop action!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    //get a company logo and crop to my dimensions =P
    public void SetCompanyLogo() {
        CompanyLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // continue with gallery
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //crop before setting the image
                try {
                    pickPhoto.putExtra("crop", "true");
                    pickPhoto.putExtra("aspectX", 1);
                    pickPhoto.putExtra("aspectY", 1);
                    pickPhoto.putExtra("outputX", 160);
                    pickPhoto.putExtra("outputY", 70);
                    pickPhoto.putExtra("return-data", "true");
                    startActivityForResult(pickPhoto, 3);//one can be replaced with any action code
                } catch (Exception e) {
                    // display an error message
                    String errorMessage = "Whoops - your device doesn't support the crop action!";
                    Toast toast = Toast.makeText(Profile.this, errorMessage, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    //load images etc
    public void LoadParameters() {
        //loading image from database for profile
            StorageReference UserImageProfileReference = storageRef.child("users/" + userID + "/ProfilePhoto.jpeg");
            final long ONE_MEGABYTE = 1024 * 1024;
            UserImageProfileReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    // Data for "images/island.jpg" is returns, use this as needed
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    ProfilePicture.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    //get image from local
                    ProfilePicture.setImageDrawable(getResources().getDrawable(R.drawable.genericperson));
                }
            });
        //loading image from database for company logo
        StorageReference CompanyLogoReference = storageRef.child("users/" + userID + "/CompanyLogo.jpeg");
        CompanyLogoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                CompanyLogo.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                //get image from local
                CompanyLogo.setImageDrawable(getResources().getDrawable(R.drawable.genericlogo));
            }
        });
    }


    //save profile picture
    public void SaveImageProfile(Bitmap bitmap){
        //Saving Image for Profile to database
        StorageReference UserImageProfileReference = storageRef.child("users/"+userID+"/ProfilePhoto.jpeg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        assert bitmap != null;
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = UserImageProfileReference.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
            }
        });
    }

    //save Company Logo
    public void SaveCompanyLogo(Bitmap bitmap){
        //Saving Image for Profile to database
        StorageReference CompanyLogoReference = storageRef.child("users/"+userID+"/CompanyLogo.jpeg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        assert bitmap != null;
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = CompanyLogoReference.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
            }
        });
    }

}

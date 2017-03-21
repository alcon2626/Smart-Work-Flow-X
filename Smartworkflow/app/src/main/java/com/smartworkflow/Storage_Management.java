package com.smartworkflow;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

/**
 * Created by LeoAizen on 3/16/2017.
 */

public class Storage_Management {
    public Bitmap UserPictureFromDB; // to store the image
    public boolean Download_finish; //to check if it finish the download
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReferenceFromUrl("gs://smart-workflow-144316.appspot.com");

    public void SavePicture(Bitmap bitmap, String UserID){
        //Saving Image for Profile to database
        StorageReference UserImageProfileReference = storageRef.child("users/"+UserID+"/ProfilePhoto.jpeg");
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
    //Retrieve the image and the we GEt it.
    public void RetrievePicture(String UserID){
        Download_finish = false;
        //loading image from database for profile
        final StorageReference[] UserImageProfileReference = {storageRef.child("users/" + UserID + "/ProfilePhoto.jpeg")};
        final long ONE_MEGABYTE = 1024 * 1024;
        UserImageProfileReference[0].getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                UserPictureFromDB = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                //check if empty
                if (UserPictureFromDB == null){
                    Log.d("Bitmap Empty:", "True");
                }else{
                    Log.d("Bitmap Empty:", "False");
                    Download_finish = true;
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                //display error

            }
        });
    }

}

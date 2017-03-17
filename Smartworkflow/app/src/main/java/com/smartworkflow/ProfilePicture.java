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

public class ProfilePicture {
    Bitmap bitmap;
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
    //change the return to the PassImage and change the name
    public Bitmap GetPicture (String UserID){

        //loading image from database for profile
        final StorageReference[] UserImageProfileReference = {storageRef.child("users/" + UserID + "/ProfilePhoto.jpeg")};
        final long ONE_MEGABYTE = 1024 * 1024;
        UserImageProfileReference[0].getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                Bitmap bitmapDB = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                PassImage(bitmapDB);
                //set it
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                //display error

            }
        });
        //check if empty
        if (bitmap == null){
            Log.d("BitmapDb Empty:", "True");
        }else{
            Log.d("BitmapDb Empty:", "False");
        }
        return bitmap;
    }
    private void PassImage(Bitmap image){
        bitmap = image;
    }

}

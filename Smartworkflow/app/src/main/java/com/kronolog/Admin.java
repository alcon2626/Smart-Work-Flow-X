package com.kronolog;

import android.content.pm.PackageInfo;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static android.content.pm.PackageManager.*;

public class Admin extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


        try {
            TextView Mykeyhash = findViewById(R.id.Mykeyhash);
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.smartworkflow",
                    GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Mykeyhash.setText(String.format("KeyHash: %s", Base64.encodeToString(md.digest(), Base64.DEFAULT)));
            }
        } catch (NameNotFoundException e) {
            Log.d("Error A", e.getMessage());

        } catch (NoSuchAlgorithmException e) {
            Log.d("Error B", e.getMessage());
        }
    }
}

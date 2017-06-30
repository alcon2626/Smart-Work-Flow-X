package com.smartworkflow;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class RegisterUser extends AppCompatActivity {
    EditText Useremail, Userpassword, Username;
    Button SignUp;
    String Email, Password, UserName;
    ProgressDialog progressbdialog;
    FirebaseAuth authenticator;
    FirebaseUser CurrentUser;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setTheme(R.style.MyAppThemeCustome);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        progressbdialog = new ProgressDialog(this);
        authenticator = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //init GUI
        Useremail = (EditText) findViewById(R.id.UserEmail);
        Userpassword = (EditText) findViewById(R.id.UserPassword);
        Username = (EditText) findViewById(R.id.DisplayName);
        SignUp = (Button) findViewById(R.id.Register_button);
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //after getting info set the registration proccess
                Email = Useremail.getText().toString().trim();
                Password = Userpassword.getText().toString().trim();
                UserName = Username.getText().toString().trim();
                if (TextUtils.isEmpty(Email)){
                    Toast.makeText(RegisterUser.this, "Please enter email", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(Password)){
                    Toast.makeText(RegisterUser.this, "Please enter password", Toast.LENGTH_SHORT).show();
                }
                RegistrationPoccess(Email, Password);
            }
        });



    }

    private void RegistrationPoccess(String email, String password) {
        progressbdialog.setMessage("Registering User...");
        progressbdialog.show();
        authenticator.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //user is now register and logged in
                            //start profile activity
                            // for now only display toast
                            Toast.makeText(RegisterUser.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                            CurrentUser = authenticator.getCurrentUser();
                            assert CurrentUser != null;
                            CurrentUser.sendEmailVerification();
                            setProfileInformation(UserName);
                            progressbdialog.cancel();
                            FirebaseAuth.getInstance().signOut();
                            Intent BackMain = new Intent(RegisterUser.this, com.smartworkflow.MainActivity.class);
                            startActivity(BackMain);

                        }else{
                            task.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressbdialog.cancel();
                                    Toast.makeText(RegisterUser.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                    }
                });


    }
    private void setProfileInformation(String displayname){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(displayname)
                //.setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("User profile updated.", "True");
                        }
                    }
                });
    }
}

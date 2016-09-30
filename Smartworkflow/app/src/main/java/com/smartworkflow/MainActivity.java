package com.smartworkflow;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    private static final int RC_SIGN_IN = 1;
    TextView Register_User;
    ImageButton UserSignIn, GooglesignIn;
    FirebaseAuth aunthenticator;
    EditText UserEmail;
    EditText Userpassword;
    String Email ="", Password="";
    String ClienID = "390858261168-u6ju4oioebajagm6ht4kb0v40o5dq14k.apps.googleusercontent.com";
    GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            super.setTheme(R.style.MyAppThemeCustome);
        }
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        //init adds
        AdRequest request = new AdRequest.Builder()
                .addTestDevice("D599B82756D2C99F4C2326F0BFF81C67")  // An example device ID
                .setGender(AdRequest.GENDER_MALE)
                .build();
        AdView mAdView = (AdView) findViewById(R.id.adView);
        mAdView.loadAd(request);
        //init user authenticator
        aunthenticator = FirebaseAuth.getInstance();
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(ClienID)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        findViewById(R.id.imageButtonGoogleSignIn).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Register_User = (TextView) findViewById(R.id.NewUser);
        UserSignIn = (ImageButton) findViewById(R.id.imageButtonSignIn);
        GooglesignIn=(ImageButton)findViewById(R.id.imageButtonGoogleSignIn);
        UserEmail = (EditText) findViewById(R.id.SignInEmail);
        Userpassword = (EditText) findViewById(R.id.SignInPassword);



        //listeners
        //registration
        Register_User.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent UserRegistration = new Intent(MainActivity.this, RegisterUser.class);
                startActivity(UserRegistration);

            }
        });
        //sign in
        UserSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    v.isActivated();
                }
                if (UserEmail.getText().toString().equals("") || Userpassword.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, "You did not enter a username or password", Toast.LENGTH_SHORT).show();
                    return;
                }

                Email = UserEmail.getText().toString().trim();
                Password = Userpassword.getText().toString().trim();

                // Create a handler to handle the result of the authentication
                Firebase.AuthResultHandler authResultHandler = new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        Intent UserProfile = new Intent(MainActivity.this, UserProfile.class);
                        startActivity(UserProfile);
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        Toast.makeText(MainActivity.this, firebaseError.toString(), Toast.LENGTH_SHORT).show();

                    }
                };

                aunthenticator.signInWithEmailAndPassword(Email, Password)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Intent UserProfile = new Intent(MainActivity.this, UserProfile.class);
                                    startActivity(UserProfile);
                                    UserEmail.setText("");
                                    Userpassword.setText("");
                                }else{
                                    task.addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        });

            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    private Boolean exit = false;
    @Override
    public void onBackPressed(){
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageButtonGoogleSignIn:
                signIn();
                break;
            // ...
        }

    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                Log.d("Error", "No google Account");
            }
        }
    }

    //firebase authenticate
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("firebaseAuthWithGoogle:",  acct.getId());
        try{
            acct.getIdToken();
            Log.d("Token:", acct.getIdToken());
        }catch (Exception e){
            e.printStackTrace();
        }

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        aunthenticator.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("signInnComplete:", String.valueOf(task.isSuccessful()));

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        Intent UserProfile = new Intent(MainActivity.this, UserProfile.class);
                        startActivity(UserProfile);
                        UserEmail.setText("");
                        Userpassword.setText("");
                        if (!task.isSuccessful()) {
                            Log.w("signInWithCredential", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
    }
}
        
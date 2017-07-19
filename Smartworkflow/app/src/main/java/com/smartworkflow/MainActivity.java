package com.smartworkflow;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
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
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    private static final int RC_SIGN_IN = 1;
    private AdView mAdView;
    ImageButton userSingUp;
    ImageButton UserSignIn;
    LoginButton facebookSignin;
    FirebaseAuth aunthenticator;
    ImageButton GooglesignIn;
    EditText UserEmail;
    EditText Userpassword;
    String DisplayName;
    String Email ="", Password="";
    String ClienID = "390858261168-u6ju4oioebajagm6ht4kb0v40o5dq14k.apps.googleusercontent.com";
    GoogleApiClient mGoogleApiClient;
    private FirebaseAuth.AuthStateListener mAuthListener;
    CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setTheme(R.style.MyAppThemeCustome);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        //ad
        MobileAds.initialize(this, "ca-app-pub-1762917079825621/4741555998");
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        //init user authenticator
        aunthenticator = FirebaseAuth.getInstance();
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        //user sign in listener
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = aunthenticator.getCurrentUser();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("MainDisplayNamen:", user.getDisplayName());
                    DisplayName = user.getDisplayName();
                    Log.d("MainUserIDn:", user.getUid());
                    String ID = String.valueOf(user.getUid());
                    Intent UserProfile = new Intent(MainActivity.this, UserProfile.class);
                    UserProfile.putExtra("USERID", ID);
                    UserProfile.putExtra("USERNAME", DisplayName);
                    startActivity(UserProfile);
                    UserEmail.setText("");
                    Userpassword.setText("");
                } else {
                    // User is signed out
                    Log.d("onAuth:signed_out", "TRUE");
                }
                // ...
            }
        };

        findViewById(R.id.imageButtonGooglesignin).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        aunthenticator.addAuthStateListener(mAuthListener);
        userSingUp = (ImageButton) findViewById(R.id.ImageButtonRegister);
        UserSignIn = (ImageButton) findViewById(R.id.imageButtonSignIn);
        GooglesignIn = (ImageButton)findViewById(R.id.imageButtonGooglesignin);
        UserEmail = (EditText) findViewById(R.id.SignInEmail);
        Userpassword = (EditText) findViewById(R.id.SignInPassword);



        //listeners
        //registration
        userSingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent UserRegistration = new Intent(MainActivity.this, RegisterUser.class);
                startActivity(UserRegistration);
            }
        });
        //sign in with facebook
        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        facebookSignin = (LoginButton) findViewById(R.id.login_button);
        facebookSignin.setReadPermissions("email", "public_profile");
        facebookSignin.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("facebook:onSuccess:", loginResult.toString());
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("facebook", "onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("facebook:onError", error.getMessage());
                // ...
            }
        });

        //sign in from user button not google
        UserSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.isActivated();
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
                                    //say something
                                    Log.d("Sign_IN_Email:", "TRUE");
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
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            aunthenticator.removeAuthStateListener(mAuthListener);
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageButtonGooglesignin:
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

                // Google Sign In failed, update UI appropriately
                // ...
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Google Sign In failed");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        }

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
    // [START auth_with_facebook]
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("handleFacebookAccT:", token.toString());
        // [START_EXCLUDE silent]
        // [END_EXCLUDE]

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        aunthenticator.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("signInWithCredential:", "success");
                            //FirebaseUser user = aunthenticator.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("signIn:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    // [END auth_with_facebook]

    //firebase authenticate
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        aunthenticator.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        UserEmail.setText("");
                        Userpassword.setText("");
                        if (!task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
    }
}
        
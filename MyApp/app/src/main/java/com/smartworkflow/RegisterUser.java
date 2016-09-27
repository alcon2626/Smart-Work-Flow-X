package com.smartworkflow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.amazonaws.cognitoidentityprovider.CognitoUser;
import com.amazonaws.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.cognitoidentityprovider.handlers.SignUpHandler;
import com.smartworkflow.Cognito.CognitoUserAttributes;

public class RegisterUser extends AppCompatActivity {
    EditText UserName, UserEmail, UserPhone, UserPassword;
    Button SignUp;
    String Name, Email, Password, Phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        UserName = (EditText) findViewById(R.id.GetUserName);
        UserEmail = (EditText) findViewById(R.id.GetUserEmail);
        UserPhone = (EditText) findViewById(R.id.GetUserPhone);
        UserPassword = (EditText) findViewById(R.id.GetUserPassword);
        SignUp = (Button) findViewById(R.id.Sign_UP);

        Register();

    }

    private void Register() {
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //get info
                Name = UserName.getText().toString();
                Phone = UserPhone.getText().toString();
                Email = UserEmail.getText().toString();
                // Create a CognitoUserAttributes object and add user attributes
                CognitoUserAttributes userAttributes = new CognitoUserAttributes();

                // Add the user attributes. Attributes are added as key-value pairs
                // Adding user's given name.
                // Note that the key is "given_name" which is the OIDC claim for given name
                userAttributes.addAttribute("given_name", Name);

                // Adding user's phone number
                userAttributes.addAttribute("phone_number", Phone);

                // Adding user's email address
                userAttributes.addAttribute("email", Email);

            }
        });
    }

    SignUpHandler signupCallback = new SignUpHandler() {

        @Override
        public void onSuccess(CognitoUser cognitoUser, boolean userConfirmed, CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
            // Sign-up was successful

            // Check if this user (cognitoUser) needs to be confirmed
            if(!userConfirmed) {
                // This user must be confirmed and a confirmation code was sent to the user
                // cognitoUserCodeDeliveryDetails will indicate where the confirmation code was sent
                // Get the confirmation code from user
            }
            else {
                // The user has already been confirmed
            }
        }

        @Override
        public void onFailure(Exception exception) {
            // Sign-up failed, check exception for the cause
        }
    };
}

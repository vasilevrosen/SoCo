package com.soco.ebusiness.soco;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import java.util.Arrays;
import java.util.List;


public class LoginActivity extends MainActivity {
    private EditText mUsernameField;
    private EditText mPasswordField;
    private TextView mErrorField;
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private Button loginButton;
    private Dialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButton = (Button) findViewById(R.id.facebook_loginbtn);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFacebookloginButtonClicked();
            }
        });
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items); // load
        // titles
        // from
        // strings.xml

        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);// load icons from
        // strings.xml

        set(navMenuTitles, navMenuIcons);

        mUsernameField = (EditText) findViewById(R.id.login_username);
        mPasswordField = (EditText) findViewById(R.id.login_password);
        mErrorField = (TextView) findViewById(R.id.error_messages);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    public void signIn(final View v) {
        v.setEnabled(false);
        ParseUser.logInInBackground(mUsernameField.getText().toString(), mPasswordField.getText().toString(), new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    Intent intent = new Intent(LoginActivity.this, FirstActivity.class);
                    startActivity(intent);
                    App.setloginstate(true);
                    finish();
                } else {
                    // Signup failed. Look at the ParseException to see what happened.
                    switch (e.getCode()) {
                        case ParseException.USERNAME_TAKEN:
                            mErrorField.setText("Sorry, this username has already been taken.");
                            break;
                        case ParseException.USERNAME_MISSING:
                            mErrorField.setText("Sorry, you must supply a username to register.");
                            break;
                        case ParseException.PASSWORD_MISSING:
                            mErrorField.setText("Sorry, you must supply a password to register.");
                            break;
                        case ParseException.OBJECT_NOT_FOUND:
                            mErrorField.setText("Sorry, those credentials were invalid.");
                            break;
                        default:
                            mErrorField.setText(e.getLocalizedMessage());
                            break;
                    }
                    v.setEnabled(true);
                }
            }
        });
    }

    public void showRegistration(View v) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);

    }

    private void onFacebookloginButtonClicked() {
        LoginActivity.this.progressDialog = ProgressDialog.show(
                LoginActivity.this, "", "Logging in...", true);
        List<String> permissions = Arrays.asList( "user_about_me",
                "user_relationships", "user_birthday", "user_location");
        ParseFacebookUtils.logInWithReadPermissionsInBackground( this,permissions, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                LoginActivity.this.progressDialog.dismiss();
                if (user == null) {
                    Toast.makeText(getApplicationContext(), "Uh oh. The user cancelled the Facebook login.",
                            Toast.LENGTH_SHORT).show();

                } else if (user.isNew()) {
                    Toast.makeText(getApplicationContext(), "User signed up and logged in through Facebook.",
                            Toast.LENGTH_SHORT).show();
                    App.setloginstate(true);


                    showFirstActivity();
                } else {
                    Toast.makeText(getApplicationContext(), "User logged in through Facebook!",
                            Toast.LENGTH_SHORT).show();
                    App.setloginstate(true);


                    showFirstActivity();
                }
            }
        });
    }

    private void showUserDetailsActivity() {
     //   Intent intent = new Intent(this, UserDetailsActivity.class);
     //   startActivity(intent);
    }
    private void showFirstActivity() {
          Intent intent = new Intent(this, FirstActivity.class);
          startActivity(intent);
    }

}
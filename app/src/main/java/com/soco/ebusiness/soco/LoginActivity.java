package com.soco.ebusiness.soco;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class LoginActivity extends ActionBarActivity {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_index, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    private EditText username;
    private EditText password;
    private Button login;
    private TextView loginLockedTV;
    private TextView attemptsLeftTV;
    private TextView numberOfRemainingLoginAttemptsTV;
    int numberOfRemainingLoginAttempts = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        setupVariables();
    }

//    public void authenticateLogin(View view) {
//        if (username.getText().toString().equals("admin") &&
//                password.getText().toString().equals("admin")) {
//            Toast.makeText(getApplicationContext(), "Hello admin!",
//                    Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(getApplicationContext(), "Seems like you 're not admin!",
//                    Toast.LENGTH_SHORT).show();
//            numberOfRemainingLoginAttempts--;
//            attemptsLeftTV.setVisibility(View.VISIBLE);
//            numberOfRemainingLoginAttemptsTV.setVisibility(View.VISIBLE);
//            numberOfRemainingLoginAttemptsTV.setText(Integer.toString(numberOfRemainingLoginAttempts));
//
//            if (numberOfRemainingLoginAttempts == 0) {
//                login.setEnabled(false);
//                loginLockedTV.setVisibility(View.VISIBLE);
//                loginLockedTV.setBackgroundColor(Color.RED);
//                loginLockedTV.setText("LOGIN LOCKED!!!");
//            }
//        }
//    }

//    private void setupVariables() {
//        username = (EditText) findViewById(R.id.TextEdit_username);
//        password = (EditText) findViewById(R.id.TextEdit_pass);
//        login = (Button) findViewById(R.id.btn_login);
//        loginLockedTV = (TextView) findViewById(R.id.loginLockedTV);
//        attemptsLeftTV = (TextView) findViewById(R.id.numberOfRemainingLoginAttemptsTV);
//        numberOfRemainingLoginAttemptsTV = (TextView) findViewById(R.id.numberOfRemainingLoginAttemptsTV);
//        numberOfRemainingLoginAttemptsTV.setText(Integer.toString(numberOfRemainingLoginAttempts));
//    }


    public void login(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}

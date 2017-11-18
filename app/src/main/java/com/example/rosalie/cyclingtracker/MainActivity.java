package com.example.rosalie.cyclingtracker;

/**
 * Created by Rose on 07-11-2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button buttonSignIn, buttonSignUp, buttonSubmitSignIn, buttonSubmitSignOn;
    LinearLayout fragmentMain, fragmentSignIn, fragmentSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setButtonsListener();
    }

    public void setButtonsListener(){
        fragmentMain = (LinearLayout) findViewById(R.id.layout_fragment_main);
        fragmentSignIn = (LinearLayout) findViewById(R.id.layout_fragment_sign_in);
        fragmentSignUp = (LinearLayout) findViewById(R.id.layout_fragment_sign_up);
        //Set up the visibility to only display the main layout, aka the two first buttons to sign in and sign out
        fragmentMain.setVisibility(View.VISIBLE);
        fragmentSignUp.setVisibility(View.GONE);
        fragmentSignIn.setVisibility(View.GONE);

        //Find all the buttons on the layout and add listeners to them
        buttonSignIn = (Button)findViewById(R.id.button_sign_in);
        buttonSignUp = (Button)findViewById(R.id.button_sign_up);
        buttonSubmitSignIn = (Button)findViewById(R.id.submit_button_sign_in);
        buttonSubmitSignOn = (Button)findViewById(R.id.submit_button_sign_on);
        buttonSignIn.setOnClickListener(this);
        buttonSignUp.setOnClickListener(this);
        buttonSubmitSignIn.setOnClickListener(this);
        buttonSubmitSignOn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.button_sign_in:
                //Hide the previous buttons and display the new buttons
                fragmentMain.setVisibility(View.GONE);
                fragmentSignIn.setVisibility(View.VISIBLE);
                break;
            case R.id.button_sign_up:
                fragmentMain.setVisibility(View.GONE);
                fragmentSignUp.setVisibility(View.VISIBLE);
                break;
            case R.id.submit_button_sign_in:
                Toast.makeText(this, "Check the info in the database", Toast.LENGTH_SHORT).show();
                /* Insert database code here (Greg) */
                Intent intent = new Intent(this, WhatsUp.class);
                startActivity(intent);
                break;
            case R.id.submit_button_sign_on:
                Toast.makeText(this,"Add the info in the database",Toast.LENGTH_SHORT).show();
                /* Insert database code here (Greg) */
                Intent intents = new Intent(this, About.class);
                startActivity(intents);
                break;
        }
    }

    /* Those two functions are used to handle the back key press event, to not have it go out of the activity */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Log.d("CDA", "onKeyDown Called");
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        fragmentSignIn.setVisibility(View.GONE);
        fragmentSignUp.setVisibility(View.GONE);
        fragmentMain.setVisibility(View.VISIBLE);
    }
}





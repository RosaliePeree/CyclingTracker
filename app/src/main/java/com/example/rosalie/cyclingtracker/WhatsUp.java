package com.example.rosalie.cyclingtracker;

/**
 * Created by Rose on 07-11-2017.
 */

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public class WhatsUp extends NavDrawActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_whats_up, null, false);
        drawer.addView(contentView, 0);

        toolbar.setTitle(R.string.nav_whats_up);
    }
}

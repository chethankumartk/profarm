package com.example.chethan.industrain.Acheck;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.chethan.industrain.R;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;

public class Acheck1 extends AppCompatActivity {

    PlacesClient placesClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acheck1);

        String key="AIzaSyDPoqKD7KzaZp1IfFvUZiuu4olJkhFPYAA";

        if(!Places.isInitialized())
        {
            Places.initialize(getApplicationContext(),key);
        }

        placesClient=Places.createClient(this);

        //final AutocompleteSupportFragment autocompleteSupportFragment
    }
}

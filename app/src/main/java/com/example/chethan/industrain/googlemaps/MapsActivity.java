package com.example.chethan.industrain.googlemaps;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.chethan.industrain.R;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.PlaceDetectionClient;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    double latitude,longitude;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        latitude=getIntent().getDoubleExtra("latitude",0);
        longitude=getIntent().getDoubleExtra("longitude",0);



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(true);

        Marker marker = null;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(latitude, longitude);

        LatLng tumk = new LatLng(14, 79);

        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        //mMap.addMarker(new MarkerOptions().position(tumk).title("Tumkur"));

       /* LatLng coordinate = new LatLng(lat, lng); //Store these lat lng values somewhere. These should be constant.
        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                coordinate, 15);
        mMap.animateCamera(location);*/


        // mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        //to:

        float zoomLevel = 12.0f; //This goes up to 21
        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                sydney, 12);
        mMap.animateCamera(location);

        // mMap.moveCamera(CameraUpdateFactory.newLatLng(tumk));
        Circle circle=mMap.addCircle(new CircleOptions().center(sydney).radius(15000).strokeWidth(1).strokeColor((Color.parseColor("#500084d3"))).fillColor(Color.parseColor("#500084d3")));
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sit,Tumkur")).showInfoWindow();

    }
}

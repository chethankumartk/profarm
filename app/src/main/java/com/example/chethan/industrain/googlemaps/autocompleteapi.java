package com.example.chethan.industrain.googlemaps;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chethan.industrain.R;
import com.example.chethan.industrain.usermai;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.io.IOException;
import java.util.List;


public class autocompleteapi extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

    private static final int GOOGLE_API_CLIENT_ID = 0;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    String city="";
    SharedPreferences sharedPref;
    private AutoCompleteTextView mAutocompleteTextView;
    private TextView mNameView;
    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private Button confirmplace;
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {

                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();
            Geocoder geocoder = new Geocoder(autocompleteapi.this);
            try
            {
                List<Address> addresses = geocoder.getFromLocation(place.getLatLng().latitude,place.getLatLng().longitude, 1);
               /* if (addresses != null && addresses.size() > 0) {
                        for (Address adr : addresses) {
                            if (adr.getAdminArea() != null && adr.getAdminArea().length() > 0) {
                                 city = adr.getSubAdminArea();
                                // city=adr.getAdminArea();
                                 break;
                            }
                        }
                }*/

                city = addresses.get(0).getSubAdminArea();
                if(city!=null)
                {
                    mNameView.setText(Html.fromHtml(city + ""));

                }else {
                     addresses = geocoder.getFromLocation(place.getLatLng().latitude,place.getLatLng().longitude, 2);
                     city=addresses.get(1).getSubAdminArea();
                    mNameView.setText(Html.fromHtml(city + ""));

                }



            } catch (IOException e){
                e.printStackTrace();
            }



        }
    };
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autocompleteapi);

        mAutocompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        mAutocompleteTextView.setThreshold(3);
        mNameView = (TextView) findViewById(R.id.name);
        confirmplace=findViewById(R.id.confirmplace);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Select Locality/city");


        confirmplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmplace();

            }
        });



        mGoogleApiClient = new GoogleApiClient.Builder(autocompleteapi.this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                .setCountry("IN")
                .build();

        mAutocompleteTextView.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, typeFilter);
        mAutocompleteTextView.setAdapter(mPlaceArrayAdapter);

    }

    public void confirmplace()
    {
        String confirmplace=mNameView.getText().toString();
        Toast.makeText(this, confirmplace, Toast.LENGTH_SHORT).show();
        if(!TextUtils.isEmpty(confirmplace))
        {

        //    new locationpreference(this).writepreference(confirmplace);


            sharedPref = getSharedPreferences("myPref", MODE_PRIVATE);

            // save your string in SharedPreferences
            sharedPref.edit().putString("location", confirmplace).commit();




             startActivity(new Intent(autocompleteapi.this,usermai.class));
            finish();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);

    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {


        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onBackPressed() {

        startActivity(new Intent(autocompleteapi.this,usermai.class));
        finish();
    }

}



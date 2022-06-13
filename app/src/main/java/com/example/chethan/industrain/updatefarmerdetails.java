package com.example.chethan.industrain;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chethan.industrain.googlemaps.PlaceArrayAdapter;
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
import java.util.Locale;


public class updatefarmerdetails extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, LocationListener {

    private static final int GOOGLE_API_CLIENT_ID = 0;
    private AutoCompleteTextView mAutocompleteTextView;
    private GoogleApiClient mGoogleApiClient;
    private TextView state, district, locality, address;
    private Button update;
    LocationManager locationManager;
    private ImageView getgpsbut;
    ProgressDialog PD;

    String city = "", statestring = "", districtstr = "", addressstr = "";
    String s1, d1, l1, a1;


    double longitude, latitude;
    SharedPreferences sharedPref;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatefarmerdetails);
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }


        mAutocompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        mAutocompleteTextView.setThreshold(3);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Update Location");
        state = findViewById(R.id.state);
        district = findViewById(R.id.district);
        locality = findViewById(R.id.locality);
        address = findViewById(R.id.address);
        getgpsbut = findViewById(R.id.gpsbutton);
        update = findViewById(R.id.update);
        PD = new ProgressDialog(this);
        PD.setMessage("Loading...");
        PD.setCancelable(true);
        PD.setCanceledOnTouchOutside(false);
        getgpsbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getgps();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatee();
            }
        });
        sharedPref = getSharedPreferences("myPref", MODE_PRIVATE);
        a1 = sharedPref.getString("address", "--Select Address");
        s1 = sharedPref.getString("state", "--Select State");
        d1 = sharedPref.getString("district", "--Select District");
        l1 = sharedPref.getString("locality", "--Select Locality");
        address.setText(a1);
        state.setText(s1);
        locality.setText(l1);
        district.setText(d1);
        mGoogleApiClient = new GoogleApiClient.Builder(updatefarmerdetails.this)
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
            Geocoder geocoder = new Geocoder(updatefarmerdetails.this);
            try {
                List<Address> addresses = geocoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1);
                districtstr = addresses.get(0).getSubAdminArea();
                city = addresses.get(0).getLocality();
                statestring = addresses.get(0).getAdminArea();
                if (city != null && statestring != null && districtstr != null) {
                    state.setText(statestring);
                    locality.setText(city);
                    district.setText(districtstr);

                } else {
                    addresses = geocoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 2);
                    districtstr = addresses.get(1).getSubAdminArea();
                    city = addresses.get(1).getLocality();
                    statestring = addresses.get(1).getAdminArea();
                    state.setText(statestring);
                    locality.setText(city);
                    district.setText(districtstr);

                }


            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    };

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

    public void getgps() {
        if (district.getText().toString().isEmpty() || state.getText().toString().isEmpty()
                || locality.getText().toString().isEmpty()
                ) {
            Toast.makeText(this, "Please Select the above details first", Toast.LENGTH_SHORT).show();
        } else {
            PD.show();
            try {
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 5, this);
            } catch (SecurityException e) {
                PD.dismiss();
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        try {

            latitude = location.getLatitude();
            longitude = location.getLongitude();
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            long time = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getTime();
            

            List<Address> addressess = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            Toast.makeText(this, "success"+latitude+"       "+longitude+"     "+time
                    , Toast.LENGTH_SHORT).show();

            addressstr=addressess.get(0).getAddressLine(0).toString();
            address.setText(addressstr);


            PD.dismiss();
        }catch(Exception e)
        {

        }


    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {


    }

    @Override
    public void onProviderDisabled(String s) {
        Toast.makeText(this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    public void updatee()
    {
        try
        {
            sharedPref = getSharedPreferences("myPref",MODE_PRIVATE);

            // save your string in SharedPreferences
            sharedPref.edit().putString("latitude", String.valueOf(latitude)).commit();
            sharedPref.edit().putString("longitude",String.valueOf(longitude)).commit();
            sharedPref.edit().putString("state",state.getText().toString().trim()).commit();
            sharedPref.edit().putString("district",district.getText().toString().trim()).commit();
            sharedPref.edit().putString("locality",locality.getText().toString().trim()).commit();
            sharedPref.edit().putString("address",address.getText().toString().trim()).commit();

        }catch (Exception e)
        {

        }

    }

}





/*
try{
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response = httpclient.execute(new HttpGet("https://google.com/"));
        StatusLine statusLine = response.getStatusLine();
        if(statusLine.getStatusCode() == HttpStatus.SC_OK){
            String dateStr = response.getFirstHeader("Date").getValue();
            //Here I do something with the Date String
            System.out.println(dateStr);

        } else{
            //Closes the connection.
            response.getEntity().getContent().close();
            throw new IOException(statusLine.getReasonPhrase());
        }
    }catch (ClientProtocolException e) {
        Log.d("Response", e.getMessage());
    }catch (IOException e) {
        Log.d("Response", e.getMessage());
    }

 */



package com.unal.gpsmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.unal.gpsmap.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker currentUsrLocatMkr;
    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private ActivityMapsBinding binding;
    private final static int INTERVAL_NUMBER = 1100;
    private static final int RQSTR_USR_LOC_CODE = 99;
    private double latitude, longitude;
    private int proximityRadius = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            checkUserLocationPermission();

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

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            createGoogleAPIClient();
            mMap.setMyLocationEnabled(true);
        }

    }

    protected synchronized void createGoogleAPIClient(){
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        googleApiClient.connect();


    }


    @Override
    public void onLocationChanged(@NonNull Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        lastLocation = location;

        if (currentUsrLocatMkr != null)
        {
            currentUsrLocatMkr.remove();
        }

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Ubicación actual");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));


        currentUsrLocatMkr = mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(12));

        if (googleApiClient != null)
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(INTERVAL_NUMBER);
        locationRequest.setFastestInterval(INTERVAL_NUMBER);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public boolean checkUserLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, RQSTR_USR_LOC_CODE);
            else
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, RQSTR_USR_LOC_CODE);
            return false;
        }
        else
            return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case RQSTR_USR_LOC_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    {
                        if (googleApiClient == null)
                        {
                            createGoogleAPIClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }
                else
                {
                    Toast.makeText(this, "Permiso denegado!", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    public void onClick(View v) {

        EditText radius = findViewById(R.id.editRadiusText);
        proximityRadius = Integer.parseInt(radius.getText().toString());

        String hospital = "hospital", school = "school", restaurant = "restaurant", hotel = "hotel";
        Object transferData[] = new Object[2];
        GetNearbyPlaces getNearbyPlaces = new GetNearbyPlaces();

        int id = v.getId();
        if (id == R.id.hospital_img) {
            mMap.clear();
            String url = getUrl(latitude, longitude, hospital);
            transferData[0] = mMap;
            transferData[1] = url;

            getNearbyPlaces.execute(transferData);
            Toast.makeText(this, "Buscando los hospitales más cercanos...", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Mostrando los hospitales más cercanos...", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.school_img) {
            String url;
            mMap.clear();
            url = getUrl(latitude, longitude, school);
            transferData[0] = mMap;
            transferData[1] = url;

            getNearbyPlaces.execute(transferData);
            Toast.makeText(this, "Buscando los colegios más cercanos...", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Mostrando los colegios más cercanos...", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.restaurant_img) {
            String url;
            mMap.clear();
            url = getUrl(latitude, longitude, restaurant);
            transferData[0] = mMap;
            transferData[1] = url;

            getNearbyPlaces.execute(transferData);
            Toast.makeText(this, "Buscando los restaurantes más cercanos...", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Mostrando los restaurantes más cercanos...", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.hotel_img) {
            String url;
            mMap.clear();
            url = getUrl(latitude, longitude, hotel);
            transferData[0] = mMap;
            transferData[1] = url;

            getNearbyPlaces.execute(transferData);
            Toast.makeText(this, "Buscando los hoteles más cercanos...", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Mostrando los hoteles más cercanos...", Toast.LENGTH_SHORT).show();
        }
    }

    private String getUrl(double latitude, double longitude, String nearbyPlace)
    {
        StringBuilder googleURL = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googleURL.append("location=" + latitude + "," + longitude);
        googleURL.append("&radius=" + proximityRadius);
        googleURL.append("&type=" + nearbyPlace);
        googleURL.append("&sensor=true");
        googleURL.append("&key=" + "AIzaSyAR_U_0LM71I5N1Mf52wdGHogaEiGOls9M");

        Log.d("MapsActivity", "url = " + googleURL.toString());

        return googleURL.toString();
    }
}
package findhosp.com.findhosp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class NearActivity extends FragmentActivity implements OnMapReadyCallback {

    //Explicit
    private GoogleMap mMap;
    private int indexAnInt;
    private String tag = "9Aprilv2";
    private double latADouble, lngADouble;
    private LocationManager locationManager;
    private Criteria criteria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near);

        //Get Value From Intent
        getValueFromIntent();

        //Setup
        mySetup();


        // Create Map Fragment
        createFragment();

    }   //Main Method


    @Override
    protected void onResume() {
        super.onResume();


        locationManager.removeUpdates(locationListener);

        latADouble = 13.964143;
        lngADouble = 100.585723;
        //For Find Location by Network
        Location networkLocation = myFindLocation(LocationManager.NETWORK_PROVIDER);
        if (networkLocation!=null) {
            latADouble = networkLocation.getLatitude();
            lngADouble = networkLocation.getLongitude();
            //For Find Location my GPS card
            Location gpsLocation = myFindLocation(LocationManager.GPS_PROVIDER);
            if (gpsLocation!=null) {
                latADouble = gpsLocation.getLatitude();
                lngADouble = gpsLocation.getLongitude();
            }

            Log.d(tag, "lat==>" + latADouble);
            Log.d(tag, "lng==>" + lngADouble);

            createCenterMap();
        }
    }   //onResume

    @Override
    protected void onStop() {
        super.onStop();


        locationManager.removeUpdates(locationListener);
    }

    public Location myFindLocation(String strProvider) {

        Location location = null;

        if (locationManager.isProviderEnabled(strProvider)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return null;
            }
            locationManager.requestLocationUpdates(strProvider, 1000, 10, locationListener);
            location=locationManager.getLastKnownLocation(strProvider);
        }

        return location;
    }

    public LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            latADouble = location.getLatitude();
            lngADouble = location.getLongitude();

        }   //onLocationChange

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    private void mySetup() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        criteria=new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
    }

    private void getValueFromIntent() {
        indexAnInt = getIntent().getIntExtra("Index", 0);
        Log.d(tag, "Index==>" + indexAnInt);
    }

    private void createFragment() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //Create Center Map and Marker User
        createCenterMap();

        //Syncronize Location
        syncronizeLocation();

    }   //onMap

    private void syncronizeLocation() {
    }

    private void createCenterMap() {
        try {
            //Create Map
            LatLng latLng = new LatLng(latADouble, lngADouble);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,16));

            //ClearMarker
            mMap.clear();

            //Create Marker
            createMarker(latLng,R.mipmap.ic_user,"คุณอยู่ที่นี่","ในขณะนี้คุณอยู่ที่นี่");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createMarker(LatLng latLng,
                              int intIcon,
                              String strTitle,
                              String strDetial) {
        mMap.addMarker(new MarkerOptions()
        .position(latLng)
        .icon(BitmapDescriptorFactory.fromResource(intIcon))
        .title(strTitle)
        .snippet(strDetial));
    }
}   //Main Class

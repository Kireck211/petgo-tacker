package mx.iteso.petgo;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import mx.iteso.petgo.beans.MyLocation;

import static mx.iteso.petgo.utils.Constants.PARCELABLE_TRIP;
import static mx.iteso.petgo.utils.Constants.TOKEN_TRIP;
import static mx.iteso.petgo.utils.Constants.TOKEN_USER;

public class ServiceLocation extends Service {
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback mLocationCallback;
    private DatabaseReference mReference;
    private String trip;
    private String user;

    protected int minimumDistanceBetweenUpdates = 2;

    public ServiceLocation() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        trip = intent.getStringExtra(TOKEN_TRIP);
        user = intent.getStringExtra(TOKEN_USER);
        mReference = FirebaseDatabase.getInstance().getReference();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setSmallestDisplacement(minimumDistanceBetweenUpdates);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                sendLocation(locationResult.getLastLocation());
            }
        };
        try {
            mFusedLocationClient.requestLocationUpdates(locationRequest, mLocationCallback, null);
        } catch (SecurityException e) {

        }

        return Service.START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    private void sendLocation(Location location) {
        String locationId = mReference.child("users/" + user + "/trips/" + trip + "/locations").push().getKey();
        mReference.child("users/" + user + "/trips/" + trip + "/locations").child(locationId).setValue(new MyLocation(location.getLatitude(), location.getLongitude()));
    }
}

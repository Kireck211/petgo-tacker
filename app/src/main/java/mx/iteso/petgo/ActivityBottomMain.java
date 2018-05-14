package mx.iteso.petgo;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import mx.iteso.petgo.beans.Phone;
import mx.iteso.petgo.beans.Trip;
import mx.iteso.petgo.beans.User;
import mx.iteso.petgo.fragments.DashboardFragment;
import mx.iteso.petgo.fragments.HomeFragment;
import mx.iteso.petgo.fragments.NotificationFragment;
import mx.iteso.petgo.fragments.ProfileFragment;

import static mx.iteso.petgo.utils.Constants.PARCELABLE_USER;

public class ActivityBottomMain extends ActivityBase {
    public User userProfile;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String userId;
    Fragment homeFragment;
    Fragment notificationFragment;
    Fragment profileFragment;
    Bundle bundle = new Bundle();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:              //home fragment
                    if(homeFragment == null){
                        homeFragment = new HomeFragment();
                    }
                    bundle.putParcelable(PARCELABLE_USER, userProfile);
                    homeFragment.setArguments(bundle);
                    selectedFragment = homeFragment;
                    break;
                case R.id.navigation_notifications:     //notification fragment
                    if(notificationFragment == null){
                        notificationFragment = NotificationFragment.newInstance(userProfile);
                    }
                    bundle.putParcelable(PARCELABLE_USER, userProfile);
                    notificationFragment.setArguments(bundle);
                    selectedFragment = notificationFragment;
                    break;
                case R.id.navigation_profile:           //profile fragment
                    if(profileFragment == null){
                        profileFragment = new ProfileFragment();
                    }
                    bundle.putParcelable(PARCELABLE_USER,userProfile);
                    profileFragment.setArguments(bundle);
                    selectedFragment = profileFragment;
                    break;
            }
            loadFragment(selectedFragment);
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_nav);
        mAuth = FirebaseAuth.getInstance();
        userProfile = getIntent().getParcelableExtra(PARCELABLE_USER);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        loadFragment(new HomeFragment());
    }

    @Override
    protected void onResume() {
        super.onResume();
        //setTripListener();
    }

    private void setTripListener() {
        final StringBuilder clients = new StringBuilder();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(userProfile.getKeyDatabase()).child("trips");
        ValueEventListener tripListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        userId = snapshot.getKey();
                        Trip trip = snapshot.getValue(Trip.class);
                        User cliente;
                        cliente = trip.getUser();
                        clients.append(cliente.getName()+" "); // cargar nombres de clientes que piden servicio
                    }
                    localNotification();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabase.child("users").child(userProfile.getKeyDatabase()).child("trips").addValueEventListener(tripListener);
    }

    private boolean loadFragment (Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            return true;
        }
        return false;
    }

    private void localNotification() {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "123456");

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_warning)
                .setTicker("Pet & Go")
                .setPriority(Notification.PRIORITY_MAX) // this is deprecated in API 26 but you can still use for below 26. check below update for 26 API
                .setContentTitle("Nuevas solicitudes")
                .setContentText("Solicitud de paseo..")
                .setContentInfo("Puedes tener nuevas solicitudes");

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notificationBuilder.build());
    }

}

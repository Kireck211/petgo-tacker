package mx.iteso.petgo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.widget.TextView;

import mx.iteso.petgo.beans.User;
import mx.iteso.petgo.fragments.DashboardFragment;
import mx.iteso.petgo.fragments.HomeFragment;
import mx.iteso.petgo.fragments.NotificationFragment;
import mx.iteso.petgo.fragments.ProfileFragment;

import static mx.iteso.petgo.utils.Constants.PARCELABLE_USER;

public class ActivityBottomNav extends ActivityBase {
    public User userProfile;
    private TextView mTextMessage;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    selectedFragment = new DashboardFragment();
                    break;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    selectedFragment = new NotificationFragment();
                    break;
                case R.id.navigation_profile:
                    mTextMessage.setText(R.string.title_profile);
                    Fragment profile = new ProfileFragment();
                    /*Bundle bundle = new Bundle();
                    bundle.putParcelable(PARCELABLE_USER,userProfile);
                    profile.setArguments(bundle);*/
                    selectedFragment = profile;
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

        userProfile = getIntent().getParcelableExtra(PARCELABLE_USER);
        mTextMessage = findViewById(R.id.message);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        loadFragment(new HomeFragment());
    }

    private boolean loadFragment (Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            return true;
        }
        return false;
    }

}

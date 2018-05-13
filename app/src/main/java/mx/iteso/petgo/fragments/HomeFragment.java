package mx.iteso.petgo.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import mx.iteso.petgo.R;
import mx.iteso.petgo.beans.Trip;
import mx.iteso.petgo.beans.User;

import static mx.iteso.petgo.utils.Constants.PARCELABLE_USER;

public class HomeFragment extends Fragment {

    private DatabaseReference mReference;
    private User mUser;
    private FirebaseAuth mAuth;
    private TextView money;
    private TextView stars;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        mAuth = FirebaseAuth.getInstance();

        money = view.findViewById(R.id.paseador_ganancia);
        stars = view.findViewById(R.id.paseador_calificacion);
        cargaDatos();
        return view;
    }

    public void cargaDatos() {

        mReference = FirebaseDatabase.getInstance().getReference();
        final FirebaseUser user = mAuth.getCurrentUser();

        Query query = mReference.child("users")
                .orderByChild("tokenId")
                .limitToFirst(1)
                .equalTo(user.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String cash,grade;
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Log.d("SNAPSHOT: ", "onDataChange: "+snapshot);
                        mUser = snapshot.getValue(User.class);
                        cash = String.valueOf(mUser.getBalance());
                        grade = String.valueOf(mUser.getRating());

                        money.setText("$"+cash);
                        stars.setText(grade);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

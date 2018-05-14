package mx.iteso.petgo.fragments;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PrimitiveIterator;

import mx.iteso.petgo.ActivityCliente;
import mx.iteso.petgo.R;
import mx.iteso.petgo.ServiceLocation;
import mx.iteso.petgo.beans.Alert;
import mx.iteso.petgo.beans.MyLocation;
import mx.iteso.petgo.beans.Phone;
import mx.iteso.petgo.beans.Solicitud;
import mx.iteso.petgo.beans.Trip;
import mx.iteso.petgo.beans.User;
import mx.iteso.petgo.recycler.RecyclerAdapter;

import static mx.iteso.petgo.utils.Constants.PARCELABLE_USER;
import static mx.iteso.petgo.utils.Constants.TOKEN_TRIP;
import static mx.iteso.petgo.utils.Constants.TOKEN_USER;

public class NotificationFragment extends Fragment {
    private DatabaseReference mReference;
    private FirebaseAuth mAuth;
    private User mUser;
    private String userId;
    private FirebaseDatabase mDataBase;
    private ArrayList<Trip> trackerTrips;
    private ArrayList<Alert> trackerAlerts;
    private ArrayList<MyLocation> trackerLocs;
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private ArrayList<Solicitud> solicitudes;
    private Bundle bundle = new Bundle();
    private Map<String,MyLocation> clientLocation;

    public static NotificationFragment newInstance(User user) {
        NotificationFragment notificationFragment = new NotificationFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(PARCELABLE_USER, user);
        notificationFragment.setArguments(bundle);

        return notificationFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications,container,false);
        //INSERTAR DATOS AL FRAGMENTO
        mAuth = FirebaseAuth.getInstance();
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mReference = FirebaseDatabase.getInstance().getReference();
        mUser = getArguments().getParcelable(PARCELABLE_USER);
        putSolicitud();
        buildRecyclerView();
        return view;
    }

    private void buildRecyclerView() {

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        solicitudes = new ArrayList<>();
        mAdapter = new RecyclerAdapter(solicitudes);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final int position) {
                solicitudes.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Ver solicitud de paseo?")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent serviceIntent = new Intent(getContext(), ServiceLocation.class);
                                serviceIntent.putExtra(TOKEN_USER, mUser.getKeyDatabase());
                                serviceIntent.putExtra(TOKEN_TRIP, solicitudes.get(position).getTokenId());
                                getActivity().startService(serviceIntent);
                                Intent intent = new Intent(getActivity(), ActivityCliente.class);
                                intent.putExtra("PICTURE",(solicitudes.get(position).getmImageResource().toString()));
                                intent.putExtra("NAME",solicitudes.get(position).getClientName());
                                intent.putExtra("ADDRESS",solicitudes.get(position).getClientAddress());
                                intent.putExtra("PET",solicitudes.get(position).getmPetResource().toString());
                                intent.putExtra("TRIP", solicitudes.get(position).getTokenId());
                                intent.putExtra(PARCELABLE_USER, solicitudes.get(position).getUser());
                                //intent.putExtra("PHONE",solicitudes.get(position).get)
                                startActivity(intent);
                                mReference.child("users").child(mUser.getKeyDatabase()).child("trips").child(solicitudes.get(position).getTokenId()).child("status").setValue("active");

                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(getActivity(), "Aceptar solicitud para ver datos de cliente", Toast.LENGTH_SHORT).show();
                            }
                        });
                builder.create().show();
            }

            @Override
            public void onCancelClick(final int position) {
                removeItem(position);
            }
        });
    }

    private void putSolicitud() { // leer de la BD desde 0 y llenar el Recycler con una ArrayLsit
        mReference = FirebaseDatabase.getInstance().getReference();
        mReference.child("users").child(mUser.getKeyDatabase()).child("trips");

        ValueEventListener tripListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ArrayList<Trip> tripArrayList = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Trip trip = snapshot.getValue(Trip.class);
                        trip.setTokenId(snapshot.getKey());
                        tripArrayList.add(trip);
                    }
                    tripSolicitudAdapter(tripArrayList);
                    mAdapter.setTrips(solicitudes);
                    //localNotification();
                } else {
                    mAdapter.setTrips(new ArrayList<Solicitud>());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mReference.child("users").child(mUser.getKeyDatabase()).child("trips").orderByChild("status").equalTo("notification").addValueEventListener(tripListener);


    }
    private void localNotification() {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getContext(), "123456");

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_warning)
                .setTicker("Pet & Go")
                .setPriority(Notification.PRIORITY_MAX) // this is deprecated in API 26 but you can still use for below 26. check below update for 26 API
                .setContentTitle("Nuevas solicitudes")
                .setContentText("Solicitud de paseo..")
                .setContentInfo("Puedes tener nuevas solicitudes");

        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notificationBuilder.build());
    }

    private void tripSolicitudAdapter(ArrayList<Trip> tripArraylist) {
        Solicitud solicitud;
        User cliente;
        String[] date_hour;
        String fecha;
        String hora;
        solicitudes = new ArrayList<>();
        for (Trip t: tripArraylist) {
            cliente = t.getUser();
            solicitud = new Solicitud();
            solicitud.setClientName(cliente.getName()); //name
            solicitud.setmImageResource(Uri.parse(cliente.getPicture())); //picture
            solicitud.setTokenId(t.getTokenId());
            date_hour = t.getDate_hour().split(" ");
            fecha = date_hour[0];
            hora = date_hour[1];
            solicitud.setWalkDate(fecha); //fecha
            solicitud.setWalkTime(hora); //hora
            solicitud.setmPetResource(Uri.parse(t.getPet())); //pet image
            solicitud.setUser(mUser);
            clientLocation = t.getAddress(); // location
            for (String key:clientLocation.keySet()) { //lat lon
                MyLocation value = clientLocation.get(key);
                String address = value.getLatitude()+","+value.getLongitude();
                Log.w("ADDRESS", "tripSolicitudAdapter: "+address );
                solicitud.setClientAddress(address);
                break;
            }
            //for ()
            solicitudes.add(solicitud); // agrega a solicitudes para recycler
        }

    }

    public void removeItem(final int position) { // debe actualizar la BD

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final Solicitud solicitud = solicitudes.get(position);
        builder.setMessage("Eliminar solicitud de paseo?")
                .setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference()
                                .child("users").child(mUser.getKeyDatabase()).child("trips")
                                .child(solicitud.getTokenId());
                        mDatabase.removeValue();

                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Toast.makeText(getActivity(), "Borrado de solicitud cancelado", Toast.LENGTH_SHORT).show();
                    }
                });
        builder.create().show();

    }


}

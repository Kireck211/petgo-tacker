package mx.iteso.petgo.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.PrimitiveIterator;

import mx.iteso.petgo.R;
import mx.iteso.petgo.beans.Solicitud;
import mx.iteso.petgo.recycler.RecyclerAdapter;

public class NotificationFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Solicitud> solicitudes;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_notifications,container,false);
        //INSERTAR DATOS AL FRAGMENTO
        insertarSolicitud();
        mRecyclerView = view.findViewById(R.id.recyclerView);
        buildRecyclerView();
        return view;
    }

    private void buildRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity()); // getContext??
        mAdapter = new RecyclerAdapter(solicitudes);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                solicitudes.get(position).invocarActivityCliente(getActivity());
            }

            @Override
            public void onCancelClick(int position) {
                removeItem(position);
            }
        });
    }

    private void insertarSolicitud() { // leer de la BD desde 0 y llenar el Recycler con una ArrayLsit
        solicitudes = new ArrayList<>();
        for () { // for each elemento del array de Solicitud en la base de datos, agregarlo
            solicitudes.add(new Solicitud(elemento.getImagen,elemento.getNombre,elemento.getFecha,elemento.getTiempo,elemento.getDireccion));
        }
    }

    public void insertItem(int position) { // debe actualizar la BD
        solicitudes.add(position, new Solicitud(elemento.getImagen,elemento.getNombre,elemento.getFecha,elemento.getTiempo,elemento.getDireccion));
        mAdapter.notifyItemInserted(position);
    }

    public void removeItem(int position) { // debe actualizar la BD
        solicitudes.remove(position);
        mAdapter.notifyItemRemoved(position);
    }
}

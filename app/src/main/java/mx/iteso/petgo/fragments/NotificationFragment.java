package mx.iteso.petgo.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.PrimitiveIterator;

import mx.iteso.petgo.ActivityCliente;
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

        View view = inflater.inflate(R.layout.fragment_notifications,container,false);
        //INSERTAR DATOS AL FRAGMENTO
        mRecyclerView = view.findViewById(R.id.recyclerView);
        exampleList(); // temporal hasta tener DB
        putSolicitud();
        buildRecyclerView();
        return view;
    }

    private void buildRecyclerView() {

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new RecyclerAdapter(solicitudes);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                solicitudes.get(position);
                Intent intent = new Intent(getActivity(), ActivityCliente.class);
                intent.putExtra("PICTURE",(solicitudes.get(position).getmImageResource().toString()));
                intent.putExtra("NAME",solicitudes.get(position).getClientName());
                intent.putExtra("ADDRESS",solicitudes.get(position).getClientAddress());
                startActivity(intent);
            }

            @Override
            public void onCancelClick(final int position) {
                removeItem(position);
            }
        });
    }

    private void putSolicitud() { // leer de la BD desde 0 y llenar el Recycler con una ArrayLsit
       // solicitudes = new ArrayList<>();
       // for () { // for each elemento del array de Solicitud en la base de datos, agregarlo
            //solicitudes.add(new Solicitud(elemento.getImagen,elemento.getNombre,elemento.getFecha,elemento.getTiempo,elemento.getDireccion));
            //solicitudes.add(new Solicitud("https://i.ytimg.com/vi/a_AqYohDuwM/maxresdefault.jpg","Juan Perez","12/12/12","30 mins","Periferico sur 456"));
       // }
    }

    public void insertItem(int position) { // debe actualizar la BD
        //solicitudes.add(position, new Solicitud(elemento.getImagen,elemento.getNombre,elemento.getFecha,elemento.getTiempo,elemento.getDireccion));
        mAdapter.notifyItemInserted(position);
    }

    public void removeItem(final int position) { // debe actualizar la BD

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Eliminar solicitud de paseo?")
                .setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        solicitudes.remove(position);
                        mAdapter.notifyItemRemoved(position);
                        buildRecyclerView();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getActivity(), "Borrado de solicitud cancelado", Toast.LENGTH_SHORT).show();
                    }
                });
        builder.create().show();

    }

    public void exampleList(){
        solicitudes = new ArrayList<>(); // aqui llenar
        solicitudes.add(new Solicitud("https://i.ytimg.com/vi/a_AqYohDuwM/maxresdefault.jpg","Juan Perez","12/12/12","30 mins","Periferico sur 456"));
        solicitudes.add(new Solicitud("http://4.bp.blogspot.com/_p8pkxcTk-Dk/Ro8cMn5QodI/AAAAAAAAAZU/wVDtGRqgB10/s400/kravitz-lenny-photo-xxl-lenny-kravitz-6223078.jpg","Jose Perez","13/13/13","40 mins","Periferico sur 557"));
        solicitudes.add(new Solicitud("http://www.mdig.com.br/imagens/famosos/negro_famoso_01.jpg","Julio Perez","14/14/14","50 mins","Periferico sur 658"));
        solicitudes.add(new Solicitud("http://www.indiehoy.com/wp-content/uploads/2018/01/Apu-Nahasapeemapetilon.jpg","Jacinto Perez","15/15/15","60 mins","Periferico sur 759"));
    }
}

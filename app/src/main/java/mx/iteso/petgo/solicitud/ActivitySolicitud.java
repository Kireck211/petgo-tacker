package mx.iteso.petgo.solicitud;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import mx.iteso.petgo.ActivityBase;
import mx.iteso.petgo.ActivityMain;
import mx.iteso.petgo.R;
import mx.iteso.petgo.clienteinfo.ActivityClienteInfo;

//Loader de lista
public class ActivitySolicitud extends ActivityBase {
    ListView listViewSolicitudes;
    SolicitudAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitud);
        listViewSolicitudes = findViewById(R.id.list);
        adapter = new SolicitudAdapter(this, GetListItems());
        listViewSolicitudes.setAdapter(adapter);
        listViewSolicitudes.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) { // abre el detalle del usuario
                Object o = listViewSolicitudes.getItemAtPosition(position);
                Intent intent = new Intent(view.getContext(), ActivityClienteInfo.class); // pendiente modificar para cada usuario de la BD
                startActivity(intent);
            }
        });

    }
    private ArrayList<SolicitudEntidad> GetListItems (){
        ArrayList<SolicitudEntidad> listItems = new ArrayList<>();
        listItems.add(new SolicitudEntidad(R.drawable.user,"Juan Perez","5/4/2018","18:00","Tlaquepaque Av. 123"));
        return listItems;
    }

}

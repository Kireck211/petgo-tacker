package mx.iteso.petgo.solicitud;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import mx.iteso.petgo.R;
import mx.iteso.petgo.clienteinfo.ActivityClienteInfo;

public class SolicitudAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<SolicitudEntidad> listItems;

    public SolicitudAdapter() {
    }

    public SolicitudAdapter(Context context, ArrayList<SolicitudEntidad> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int i) {
        return listItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        SolicitudEntidad item = (SolicitudEntidad) getItem(i);
        view = LayoutInflater.from(context).inflate(R.layout.solicitud_item, null);//
        ImageView imgPhoto = view.findViewById(R.id.solicitud_item_photo);
        TextView name = view.findViewById(R.id.solicitud_tv_nombre);
        TextView date = view.findViewById(R.id.solicitud_tv_fecha);
        TextView time = view.findViewById(R.id.solicitud_tv_hora);
        TextView address = view.findViewById(R.id.solicitud_tv_direccion);

        imgPhoto.setImageResource(item.getPhoto());
        name.setText(item.getNombre());
        date.setText(item.getFecha());
        time.setText(item.getHora());
        address.setText(item.getDireccion());

        return view;
    }
}

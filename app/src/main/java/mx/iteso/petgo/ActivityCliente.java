package mx.iteso.petgo;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

public class ActivityCliente extends AppCompatActivity {
    private String name;
    private String address;
    private String imageResourece;
    public SimpleDraweeView draweeView;

    TextView textViewNombre;
    TextView textViewPhone;
    ImageView location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);

        imageResourece = getIntent().getExtras().getString("PICTURE");
        name = getIntent().getExtras().getString("NAME");
        address = getIntent().getExtras().getString("ADDRESS");

        draweeView = findViewById(R.id.client_picture);
        textViewNombre = findViewById(R.id.client_name);
        textViewPhone = findViewById(R.id.client_phone);
        location = findViewById(R.id.client_location);
        textViewNombre.setText(name);
        // abrir  gmaps para trazar ruta
        //Uri location = Uri.parse("geo:0,0?q=1600 Av. Primavera, Parques del Bosque, 45609 San Pedro Tlaquepaque, Jal");;
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri location = Uri.parse("geo:"+address);
                Intent intent = new Intent(Intent.ACTION_VIEW,location);
                Intent chooser = Intent.createChooser(intent, "Launch Google Maps");
                startActivity(chooser);
            }
        });
        draweeView.setImageURI(Uri.parse(imageResourece));

    }
}

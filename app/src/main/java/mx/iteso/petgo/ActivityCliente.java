package mx.iteso.petgo;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import mx.iteso.petgo.beans.Solicitud;
import mx.iteso.petgo.beans.User;

import static mx.iteso.petgo.utils.Constants.PARCELABLE_USER;

public class ActivityCliente extends AppCompatActivity {
    private String name;
    private String address;
    private String imageResourece;
    private String petResourece;
    private String trip;
    public SimpleDraweeView draweeView;
    public SimpleDraweeView petView;
    public ImageView stopViaje;
    private FirebaseAuth mAuth;
    private User mUser;

    TextView textViewNombre;
    TextView textViewPhone;
    ImageView location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);
        mAuth = FirebaseAuth.getInstance();


        imageResourece = getIntent().getExtras().getString("PICTURE");
        name = getIntent().getExtras().getString("NAME");
        address = getIntent().getExtras().getString("ADDRESS");
        petResourece = getIntent().getExtras().getString("PET");
        mUser = getIntent().getParcelableExtra(PARCELABLE_USER);
        trip = getIntent().getExtras().getString("TRIP");

        draweeView = findViewById(R.id.client_picture);
        petView = findViewById(R.id.header_cover_image);
        textViewNombre = findViewById(R.id.client_name);
        textViewPhone = findViewById(R.id.client_phone);
        location = findViewById(R.id.client_location);
        stopViaje = findViewById(R.id.stopTrip);
        textViewNombre.setText(name);
        petView.setImageURI(Uri.parse(petResourece));
        draweeView.setImageURI(Uri.parse(imageResourece));

        Log.e("ADDRESS", "onCreate: "+address );
        // abrir  gmaps para trazar ruta
        //Uri location = Uri.parse("geo:0,0?q=1600 Av. Primavera, Parques del Bosque, 45609 San Pedro Tlaquepaque, Jal");;
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] latlon = address.split(",");
                Uri location = Uri.parse("http://maps.google.com/maps?q="+""+latlon[0]+","+""+latlon[1]+"(Client location)");
                Intent intent = new Intent(Intent.ACTION_VIEW,location);
                //Intent chooser = Intent.createChooser(intent, "Launch Google Maps");
                startActivity(intent);
            }
        });
        stopViaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityCliente.this);
                builder.setMessage("Terminar paseo?")
                        .setPositiveButton("Terminar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                                reference.child("users").child(mUser.getKeyDatabase()).child("trips").child(trip).child("status").setValue("finished");

                                Intent intent = new Intent(ActivityCliente.this,ActivityBottomMain.class);
                                intent.putExtra(PARCELABLE_USER, mUser);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //Toast.makeText(getActivity(), "Borrado de solicitud cancelado", Toast.LENGTH_SHORT).show();
                            }
                        });
                builder.create().show();

            }
        });
    }
}

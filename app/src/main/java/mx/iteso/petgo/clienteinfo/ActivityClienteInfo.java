package mx.iteso.petgo.clienteinfo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import mx.iteso.petgo.R;

public class ActivityClienteInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_info);
    }

    public void dialPhone(View view) {

    }

    public void searchLocation(View view) {

    }
    public void OnClick(View view){
        switch(view.getId()){
            case R.id.client_address:
                Uri location = Uri.parse("geo:0,0?q=1600 Av. Primavera, Parques del Bosque, 45609 San Pedro Tlaquepaque, Jal");
                Intent intent = new Intent(Intent.ACTION_VIEW,location);
                Intent chooser = Intent.createChooser(intent, "Launch Maps");
                startActivity(chooser);
                break;
            case R.id.client_phone:
                requestCallPermissions();
                break;

        }
    }
    public void requestCallPermissions(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.CALL_PHONE},1999);
        }else{
            //llamar
            call();
        }
    }
    public void call(){ // debe tener un try catch de security exception
        TextView phone = findViewById(R.id.client_phone);
        Intent call = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phone.getText().toString()));
        try{
            startActivity(call);
        }catch (SecurityException e){

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case 1999:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //llamar
                    call();
                }
                break;
        }
    }
}

package mx.iteso.petgo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ActivityCliente extends AppCompatActivity {
    private String name;
    private String address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);

        name = getIntent().getExtras().getString("NAME");
        address = getIntent().getExtras().getString("ADDRESS");

    }
}

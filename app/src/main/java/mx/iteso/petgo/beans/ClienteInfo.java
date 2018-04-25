package mx.iteso.petgo.beans;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ClienteInfo {
    private DatabaseReference mDatabase;
    private String nombre = "";
    private String domicilio = "";


    public ClienteInfo(String nombre, String domicilio) {
        this.nombre = nombre;
        this.domicilio = domicilio;
    }

    public void selectData() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").orderByChild("name").equalTo(nombre);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot nodo: dataSnapshot.child("users").getChildren()) {
                    User value = nodo.getValue(User.class);
                    Log.d("FIREBASE", "USER: "+value);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void insertData() {

    }
}

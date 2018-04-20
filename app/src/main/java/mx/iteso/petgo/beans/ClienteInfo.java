package mx.iteso.petgo.beans;

import com.google.firebase.database.DatabaseReference;

public class ClienteInfo {
    private DatabaseReference mDatabase;
    private String nombre = "";
    private String domicilio = "";

    public ClienteInfo(String nombre, String domicilio) {
        this.nombre = nombre;
        this.domicilio = domicilio;
    }

    public void selectData() {

    }

    public void insertData() {

    }
}

package mx.iteso.petgo.solicitud;

public class SolicitudEntidad {
    private int photo = 0;
    private String nombre = "";
    private String fecha = "";
    private String hora = "";
    private String direccion = "";

    public SolicitudEntidad() {
    }

    public SolicitudEntidad(int photo, String nombre, String fecha, String hora, String direccion) {
        this.photo = photo;
        this.nombre = nombre;
        this.fecha = fecha;
        this.hora = hora;
        this.direccion = direccion;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}

package academia.modelo;

public class EstudianteEnLinea extends Estudiante {

    private String plataforma;

    public EstudianteEnLinea(String nombre, int edad, String curso, String plataforma) {
        super(nombre, edad, curso, TipodCurso.EN_LINEA);
        this.plataforma = plataforma;
    }

    public String getPlataforma() { return plataforma; }

    @Override
    public String mostrarInformacion() {
        return "Tipo: Estudiante En Línea\n"
                + "Nombre: " + nombre + "\n"
                + "Edad: " + edad + "\n"
                + "Curso: " + curso + "\n"
                + "Plataforma: " + plataforma;
    }
}

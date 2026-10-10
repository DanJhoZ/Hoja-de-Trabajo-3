package academia.modelo;

public class EstudiantePresen extends Estudiante {

    private String aula;

    public EstudiantePresen(String nombre, int edad, String curso, String aula) {
        super(nombre, edad, curso, TipodCurso.PRESENCIAL);
        this.aula = aula;
    }

    public String getAula() { return aula; }

    @Override
    public String mostrarInformacion() {
        return "Tipo: Estudiante Presencial\n"
                + "Nombre: " + nombre + "\n"
                + "Edad: " + edad + "\n"
                + "Curso: " + curso + "\n"
                + "Aula asignada: " + aula;
    }
}

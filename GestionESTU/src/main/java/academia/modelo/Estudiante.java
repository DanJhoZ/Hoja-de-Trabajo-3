package academia.modelo;

public abstract class Estudiante {

    protected int id;
    protected String nombre;
    protected int edad;
    protected String curso;
    protected TipodCurso tipoCurso;

    public Estudiante(String nombre, int edad, String curso, TipodCurso tipoCurso) {
        this.nombre = nombre;
        this.edad = edad;
        this.curso = curso;
        this.tipoCurso = tipoCurso;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public int getEdad() { return edad; }
    public String getCurso() { return curso; }
    public TipodCurso getTipoCurso() { return tipoCurso; }

    public double calcularCostoTotal(double costoBase) {
        return costoBase;
    }

    public double calcularCostoTotal(double costoBase, double porcentajeBeca) {
        double descuento = costoBase * (porcentajeBeca / 100.0);
        return costoBase - descuento;
    }

    public abstract String mostrarInformacion();
}

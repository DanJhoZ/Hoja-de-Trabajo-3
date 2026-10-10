package academia.datos;

import academia.modelo.Estudiante;
import academia.modelo.EstudianteEnLinea;
import academia.modelo.EstudiantePresen;
import academia.modelo.TipodCurso;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class EstudianteDAO {

    public void insertar(Estudiante e, double costoBase, double costoTotal) throws SQLException {
        String sql = "INSERT INTO estudiantes "
                + "(nombre, edad, curso, tipo_curso, aula, plataforma, costo_base, costo_total) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexionABd.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, e.getNombre());
            ps.setInt(2, e.getEdad());
            ps.setString(3, e.getCurso());
            ps.setString(4, e.getTipoCurso().name());

            if (e instanceof EstudiantePresen) {
                EstudiantePresen ep = (EstudiantePresen) e;
                ps.setString(5, ep.getAula());
                ps.setNull(6, Types.VARCHAR);
            } else if (e instanceof EstudianteEnLinea) {
                EstudianteEnLinea el = (EstudianteEnLinea) e;
                ps.setNull(5, Types.VARCHAR);
                ps.setString(6, el.getPlataforma());
            }

            ps.setDouble(7, costoBase);
            ps.setDouble(8, costoTotal);

            ps.executeUpdate();
        }
    }

    public List<Estudiante> listarTodos() throws SQLException {
        List<Estudiante> lista = new ArrayList<>();
        String sql = "SELECT * FROM estudiantes ORDER BY id";

        try (Connection con = ConexionABd.getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(construirEstudiante(rs));
            }
        }
        return lista;
    }

    public List<Estudiante> buscarPorTipo(TipodCurso tipo) throws SQLException {
        List<Estudiante> lista = new ArrayList<>();
        String sql = "SELECT * FROM estudiantes WHERE tipo_curso = ? ORDER BY id";

        try (Connection con = ConexionABd.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, tipo.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(construirEstudiante(rs));
                }
            }
        }
        return lista;
    }

    private Estudiante construirEstudiante(ResultSet rs) throws SQLException {
        String nombre = rs.getString("nombre");
        int edad = rs.getInt("edad");
        String curso = rs.getString("curso");
        String tipo = rs.getString("tipo_curso");

        Estudiante e;
        if (tipo.equals(TipodCurso.PRESENCIAL.name())) {
            e = new EstudiantePresen(nombre, edad, curso, rs.getString("aula"));
        } else {
            e = new EstudianteEnLinea(nombre, edad, curso, rs.getString("plataforma"));
        }
        e.setId(rs.getInt("id"));
        return e;
    }
}

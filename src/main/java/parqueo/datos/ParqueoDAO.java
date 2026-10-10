package parqueo.datos;
 
import parqueo.modelo.Vehiculo;
import parqueo.modelo.auto;
import parqueo.modelo.moto;
import parqueo.modelo.TipodeVehiculo;
 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author zetd6
 */
public class ParqueoDAO {
 
    public void insertar(Vehiculo e, double costoBase, double costoTotal) throws SQLException {
        String sql = "INSERT INTO vehiculo (placa, hora, tipo_vehiculo, costo_base, costo_total) "
                + "VALUES (?, ?, ?, ?, ?)";
 
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
 
            ps.setString(1, e.getplaca());
            ps.setInt(2, e.gethora());
            ps.setString(3, e.getTipoVehiculo().name());
            ps.setDouble(4, costoBase);
            ps.setDouble(5, costoTotal);
 
            ps.executeUpdate();
        }
    }
 
    public List<Vehiculo> listarTodos() throws SQLException {
        List<Vehiculo> lista = new ArrayList<>();
        String sql = "SELECT * FROM vehiculo ORDER BY id";
 
        try (Connection con = Conexion.getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
 
            while (rs.next()) {
                lista.add(construirVehiculo(rs));
            }
        }
        return lista;
    }
 
    public List<Vehiculo> buscarPorTipo(TipodeVehiculo tipo) throws SQLException {
        List<Vehiculo> lista = new ArrayList<>();
        String sql = "SELECT * FROM vehiculo WHERE tipo_vehiculo = ? ORDER BY id";
 
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
 
            ps.setString(1, tipo.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(construirVehiculo(rs));
                }
            }
        }
        return lista;
    }
 
    private Vehiculo construirVehiculo(ResultSet rs) throws SQLException {
        String placa = rs.getString("placa");
        int hora = rs.getInt("hora");
        String tipo = rs.getString("tipo_vehiculo");
 
        Vehiculo e;
        if (tipo.equals(TipodeVehiculo.MOTO.name())) {
            e = new moto(placa, hora);
        } else {
            e = new auto(placa, hora);
        }
        e.setId(rs.getInt("id"));
        return e;
    }
}
 
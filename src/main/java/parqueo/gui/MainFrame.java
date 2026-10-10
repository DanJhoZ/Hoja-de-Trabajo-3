package parqueo.gui;

import parqueo.datos.ParqueoDAO;
import parqueo.modelo.Vehiculo;
import parqueo.modelo.auto;
import parqueo.modelo.moto;
import parqueo.modelo.TipodeVehiculo;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author zetd6
 */
public class MainFrame extends JFrame {

    private final ParqueoDAO dao = new ParqueoDAO();

    public MainFrame() {
        setTitle("Sistema de Parqueo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(430, 320);
        setLocationRelativeTo(null);
        setResizable(false);

        JButton btnRegistrar = new JButton("1. Registrar vehiculo");
        JButton btnListar = new JButton("2. Listar vehiculos en el estacionamiento");
        JButton btnBuscar = new JButton("3. Buscar por tipo de vehiculo");
        JButton btnSalir = new JButton("4. Salir");

        btnRegistrar.addActionListener(e -> registrarVehiculo());
        btnListar.addActionListener(e -> listarVehiculo());
        btnBuscar.addActionListener(e -> buscarPorTipo());
        btnSalir.addActionListener(e -> System.exit(0));

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));
        panel.add(btnRegistrar);
        panel.add(btnListar);
        panel.add(btnBuscar);
        panel.add(btnSalir);

        add(panel);
    }

    private void registrarVehiculo() {
        JTextField txtPlaca = new JTextField();
        JTextField txtHora = new JTextField();
        JComboBox<TipodeVehiculo> cbTipo = new JComboBox<>(TipodeVehiculo.values());
        JTextField txtCostoBase = new JTextField();
        JTextField txtBeca = new JTextField("0");

        JPanel panel = new JPanel(new GridLayout(0, 2, 8, 8));
        panel.add(new JLabel("Placa:"));
        panel.add(txtPlaca);
        panel.add(new JLabel("Horas:"));
        panel.add(txtHora);
        panel.add(new JLabel("Tipo de Vehiculo:"));
        panel.add(cbTipo);
        panel.add(new JLabel("Costo base del parqueo (Q):"));
        panel.add(txtCostoBase);
        panel.add(new JLabel("Porcentaje de descuento (0 si no aplica):"));
        panel.add(txtBeca);

        int resultado = JOptionPane.showConfirmDialog(this, panel, "Registrar Vehiculo",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (resultado != JOptionPane.OK_OPTION) return;

        try {
            String placa = txtPlaca.getText().trim();
            if (placa.isEmpty()) {
                throw new IllegalArgumentException("La placa no puede estar vacía.");
            }

            int hora;
            try {
                hora = Integer.parseInt(txtHora.getText().trim());
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("La hora debe ser un número entero (sin letras).");
            }
            if (hora <= 0 || hora > 24) {
                throw new IllegalArgumentException("Ingrese una hora válida (entre 1 y 24).");
            }

            double costoBase;
            try {
                costoBase = Double.parseDouble(txtCostoBase.getText().trim());
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("El costo base debe ser un número.");
            }
            if (costoBase < 0) {
                throw new IllegalArgumentException("El costo base no puede ser negativo.");
            }

            double beca;
            String textoBeca = txtBeca.getText().trim();
            try {
                beca = textoBeca.isEmpty() ? 0 : Double.parseDouble(textoBeca);
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("El porcentaje de descuento debe ser un número.");
            }
            if (beca < 0 || beca > 100) {
                throw new IllegalArgumentException("El porcentaje de descuento debe estar entre 0 y 100.");
            }

            TipodeVehiculo tipo = (TipodeVehiculo) cbTipo.getSelectedItem();
            Vehiculo vehiculo;
            if (tipo == TipodeVehiculo.MOTO) {
                vehiculo = new moto(placa, hora);
            } else {
                vehiculo = new auto(placa, hora);
            }

            double costoTotal = (beca > 0)
                    ? vehiculo.calcularCostoTotal(costoBase, beca)
                    : vehiculo.calcularCostoTotal(costoBase);

            dao.insertar(vehiculo, costoBase, costoTotal);

            JOptionPane.showMessageDialog(this,
                    vehiculo.mostrarInformacion()
                            + "\n\nCosto total a pagar: Q" + String.format("%.2f", costoTotal)
                            + "\n\n¡Guardado en la base de datos!",
                    "Vehiculo registrado", JOptionPane.INFORMATION_MESSAGE);

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Datos inválidos",
                    JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error de base de datos:\n" + ex.getMessage(),
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void listarVehiculo() {
        try {
            List<Vehiculo> lista = dao.listarTodos();
            mostrarLista(lista, "Listado de todos los vehiculos");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error de base de datos:\n" + ex.getMessage(),
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarPorTipo() {
        TipodeVehiculo tipo = (TipodeVehiculo) JOptionPane.showInputDialog(this,
                "Seleccione el tipo de vehiculo:", "Buscar por tipo de vehiculo",
                JOptionPane.QUESTION_MESSAGE, null, TipodeVehiculo.values(), TipodeVehiculo.MOTO);

        if (tipo == null) return;

        try {
            List<Vehiculo> lista = dao.buscarPorTipo(tipo);
            mostrarLista(lista, "Vehiculos de tipo: " + tipo);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error de base de datos:\n" + ex.getMessage(),
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarLista(List<Vehiculo> lista, String titulo) {
        if (lista.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay vehiculos registrados.", titulo,
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (Vehiculo v : lista) {
            sb.append(v.mostrarInformacion()).append("\n----------------------------\n");
        }

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setPreferredSize(new Dimension(450, 350));

        JOptionPane.showMessageDialog(this, scroll, titulo, JOptionPane.PLAIN_MESSAGE);
    }
}
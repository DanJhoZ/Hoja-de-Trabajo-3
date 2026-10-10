package academia.gui;

import academia.datos.EstudianteDAO;
import academia.modelo.Estudiante;
import academia.modelo.EstudianteEnLinea;
import academia.modelo.EstudiantePresen;
import academia.modelo.TipodCurso;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class MainFrame extends JFrame {

    private final EstudianteDAO dao = new EstudianteDAO();

    public MainFrame() {
        setTitle("Sistema de Registro de Estudiantes - Academia");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(430, 320);
        setLocationRelativeTo(null);
        setResizable(false);

        JButton btnRegistrar = new JButton("1. Registrar estudiante");
        JButton btnListar = new JButton("2. Listar todos los estudiantes");
        JButton btnBuscar = new JButton("3. Buscar por tipo de curso");
        JButton btnSalir = new JButton("4. Salir");

        btnRegistrar.addActionListener(e -> new Registro().setVisible(true));
        btnListar.addActionListener(e -> listarEstudiantes());
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

    private void registrarEstudiante() {
        JTextField txtNombre = new JTextField();
        JTextField txtEdad = new JTextField();
        JTextField txtCurso = new JTextField();
        JComboBox<TipodCurso> cbTipo = new JComboBox<>(TipodCurso.values());
        JTextField txtExtra = new JTextField();
        JTextField txtCostoBase = new JTextField();
        JTextField txtBeca = new JTextField("0");

        JLabel lblExtra = new JLabel("Aula:");
        cbTipo.addActionListener(e -> {
            if (cbTipo.getSelectedItem() == TipodCurso.PRESENCIAL) {
                lblExtra.setText("Aula:");
            } else {
                lblExtra.setText("Plataforma:");
            }
        });

        JPanel panel = new JPanel(new GridLayout(0, 2, 8, 8));
        panel.add(new JLabel("Nombre:")); panel.add(txtNombre);
        panel.add(new JLabel("Edad:")); panel.add(txtEdad);
        panel.add(new JLabel("Curso:")); panel.add(txtCurso);
        panel.add(new JLabel("Tipo de curso:")); panel.add(cbTipo);
        panel.add(lblExtra); panel.add(txtExtra);
        panel.add(new JLabel("Costo base del curso (Q):")); panel.add(txtCostoBase);
        panel.add(new JLabel("Porcentaje de beca (0 si no aplica):")); panel.add(txtBeca);

        int resultado = JOptionPane.showConfirmDialog(this, panel, "Registrar estudiante",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (resultado != JOptionPane.OK_OPTION) return;

        try {
            String nombre = txtNombre.getText().trim();
            if (nombre.isEmpty()) throw new IllegalArgumentException("El nombre no puede estar vacío.");

            int edad;
            try {
                edad = Integer.parseInt(txtEdad.getText().trim());
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("La edad debe ser un número entero (sin letras).");
            }
            if (edad <= 0 || edad > 120) throw new IllegalArgumentException("Ingrese una edad válida (entre 1 y 120).");

            String curso = txtCurso.getText().trim();
            if (curso.isEmpty()) throw new IllegalArgumentException("El curso no puede estar vacío.");

            String extra = txtExtra.getText().trim();
            if (extra.isEmpty()) throw new IllegalArgumentException("Debe completar el campo: " + lblExtra.getText());

            double costoBase;
            try {
                costoBase = Double.parseDouble(txtCostoBase.getText().trim());
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("El costo base debe ser un número.");
            }
            if (costoBase < 0) throw new IllegalArgumentException("El costo base no puede ser negativo.");

            double beca;
            String textoBeca = txtBeca.getText().trim();
            try {
                beca = textoBeca.isEmpty() ? 0 : Double.parseDouble(textoBeca);
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("El porcentaje de beca debe ser un número.");
            }
            if (beca < 0 || beca > 100) throw new IllegalArgumentException("El porcentaje de beca debe estar entre 0 y 100.");

            TipodCurso tipo = (TipodCurso) cbTipo.getSelectedItem();
            Estudiante estudiante;
            if (tipo == TipodCurso.PRESENCIAL) {
                estudiante = new EstudiantePresen(nombre, edad, curso, extra);
            } else {
                estudiante = new EstudianteEnLinea(nombre, edad, curso, extra);
            }

            double costoTotal = (beca > 0)
                    ? estudiante.calcularCostoTotal(costoBase, beca)
                    : estudiante.calcularCostoTotal(costoBase);

            dao.insertar(estudiante, costoBase, costoTotal);

            JOptionPane.showMessageDialog(this,
                    estudiante.mostrarInformacion()
                            + "\n\nCosto total a pagar: Q" + String.format("%.2f", costoTotal)
                            + "\n\n¡Guardado en la base de datos!",
                    "Estudiante registrado", JOptionPane.INFORMATION_MESSAGE);

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Datos inválidos", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error de base de datos:\n" + ex.getMessage(),
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void listarEstudiantes() {
        try {
            List<Estudiante> lista = dao.listarTodos();
            mostrarLista(lista, "Listado de todos los estudiantes");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error de base de datos:\n" + ex.getMessage(),
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarPorTipo() {
        TipodCurso tipo = (TipodCurso) JOptionPane.showInputDialog(this,
                "Seleccione el tipo de curso:", "Buscar por tipo de curso",
                JOptionPane.QUESTION_MESSAGE, null, TipodCurso.values(), TipodCurso.PRESENCIAL);

        if (tipo == null) return;

        try {
            List<Estudiante> lista = dao.buscarPorTipo(tipo);
            mostrarLista(lista, "Estudiantes de tipo: " + tipo);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error de base de datos:\n" + ex.getMessage(),
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarLista(List<Estudiante> lista, String titulo) {
        if (lista.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay estudiantes registrados.", titulo,
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (Estudiante e : lista) {
            sb.append(e.mostrarInformacion()).append("\n----------------------------\n");
        }

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setPreferredSize(new Dimension(450, 350));

        JOptionPane.showMessageDialog(this, scroll, titulo, JOptionPane.PLAIN_MESSAGE);
    }
}
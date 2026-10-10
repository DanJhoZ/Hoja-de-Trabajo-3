package com.mycompany.gestionestu;

import academia.gui.MainFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author zetd6
 */
public class GestionESTU {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
package com.mycompany.parcial2;

import parqueo.gui.MainFrame;
import javax.swing.SwingUtilities;
/**
 *
 * @author zetd6
 */
public class Parcial2 {

     public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}

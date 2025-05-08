package view;

import javax.swing.*;

public class Ventana extends JFrame {

    Controlador controlador;
    JButton EdificioA;
    JButton EdificioB;
    JButton EdificioC;
    JButton EdificioD;
    JButton EdificioE;

    public Ventana() {
        setTitle("UEMENU");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

    }
}

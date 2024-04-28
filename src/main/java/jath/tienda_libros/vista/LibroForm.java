package jath.tienda_libros.vista;

import jath.tienda_libros.servicio.LibroServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component
public class LibroForm extends JFrame {

    LibroServicio libroServicio;
    private JPanel panel;

    @Autowired
    public LibroForm(LibroServicio libroServicio){
        this.libroServicio = libroServicio;
        iniciarForma();
    }

    private void iniciarForma() {
        setContentPane(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setSize(900, 700);

        // centrar pantalla
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension tamPantalla = toolkit.getScreenSize();
        int x = (tamPantalla.width - getWidth() / 2);
        int y = (tamPantalla.height - getHeight() / 2);
        setLocation(x, y);
    }
}

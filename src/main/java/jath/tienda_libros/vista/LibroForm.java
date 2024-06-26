package jath.tienda_libros.vista;

import jath.tienda_libros.modelo.Libro;
import jath.tienda_libros.servicio.LibroServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Component
public class LibroForm extends JFrame {

    LibroServicio libroServicio;
    private JPanel panel;
    private JTable tablaLibros;
    private JTextField idTexto;
    private JTextField libroTexto;
    private JTextField autorTexto;
    private JTextField precioTexto;
    private JTextField existenciasTexto;
    private JButton agregarBoton;
    private JButton modificarButton;
    private JButton eliminarButton;
    private DefaultTableModel tablaModeloLibros;


    @Autowired
    public LibroForm(LibroServicio libroServicio){
        this.libroServicio = libroServicio;
        iniciarForma();
        agregarBoton.addActionListener(e -> agregarLibro());
        tablaLibros.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                agregarBoton.setVisible(false);
                cargarLibroSeleccionado();
            }
        });
        modificarButton.addActionListener(e -> modificarLibro());
        eliminarButton.addActionListener(e -> eliminarLibro());
    }

    private void eliminarLibro(){

        var fila = tablaLibros.getSelectedRow();
        if(fila != -1){
            String idLibro = tablaLibros.getModel().getValueAt(fila, 0).toString();
            String nombre = libroTexto.getText();

            var libro = new Libro();
            libro.setIdLibro(Integer.parseInt(idLibro));

            this.libroServicio.eliminarLibro(libro);
            mostrarMensaje("Se elimino el libro " + nombre + " exitosamente...");

            limpiarFormulario();
            listarLibros();
            agregarBoton.setVisible(true);
        } else {
            mostrarMensaje("Debes seleccionar algun libro...");
        }




    }

    private void modificarLibro() {
        if (this.idTexto.getText().isEmpty()){
            mostrarMensaje("Debe seleccionar un registro...");
        } else{

            //        Leer valores del form
            if(libroTexto.getText().isEmpty()){
                mostrarMensaje("Proporciona el nombre del Libro");
                libroTexto.requestFocusInWindow();
                return;
            }

            int idLibro = Integer.parseInt(idTexto.getText());
            var nombreLibro = libroTexto.getText();
            var autor = autorTexto.getText();
            var precio = Double.parseDouble(precioTexto.getText());
            var existencias = Integer.parseInt(existenciasTexto.getText());

            // Crear objeto Libro
            var libro = new Libro(idLibro, nombreLibro, autor, precio, existencias);

            this.libroServicio.guardarLibro(libro);
            mostrarMensaje("Se modifico el libro...");

            limpiarFormulario();
            listarLibros();
            agregarBoton.setVisible(true);
        }

    }

    private void cargarLibroSeleccionado(){
        // Indices de las columnas inician en 0
        var fila = tablaLibros.getSelectedRow();
        if (fila != -1) {
            String idLibro = tablaLibros.getModel().getValueAt(fila, 0).toString();
            idTexto.setText(idLibro);

            String nombreLibro = tablaLibros.getModel().getValueAt(fila, 1).toString();
            libroTexto.setText(nombreLibro);
            String autor = tablaLibros.getModel().getValueAt(fila, 2).toString();
            autorTexto.setText(autor);
            String precio = tablaLibros.getModel().getValueAt(fila, 3).toString();
            precioTexto.setText(precio);
            String existencias = tablaLibros.getModel().getValueAt(fila, 4).toString();
            existenciasTexto.setText(existencias);
        }
    }

    private void agregarLibro() {
//        Leer valores del form
        if(libroTexto.getText().isEmpty()){
            mostrarMensaje("Proporciona el nombre del Libro");
            libroTexto.requestFocusInWindow();
            return;
        }

        var nombreLibro = libroTexto.getText();
        var autor = autorTexto.getText();
        var precio = Double.parseDouble(precioTexto.getText());
        var existencias = Integer.parseInt(existenciasTexto.getText());

        // Crear objeto Libro
        var libro = new Libro();
        libro.setNombreLibro(nombreLibro);
        libro.setAutor(autor);
        libro.setPrecio(precio);
        libro.setExistencias(existencias);

        this.libroServicio.guardarLibro(libro);
        mostrarMensaje("Se agrego el libro...");

        limpiarFormulario();
        listarLibros();
    }

    private void limpiarFormulario() {
        libroTexto.setText("");
        autorTexto.setText("");
        precioTexto.setText("");
        existenciasTexto.setText("");
    }

    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
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

    private void createUIComponents() {
        // TODO: place custom component creation code here
        // Creamos el elemento idTexto oculto
        idTexto = new JTextField("");
        idTexto.setVisible(false);

        this.tablaModeloLibros = new DefaultTableModel(0, 5){
            // Evitar que se edite en la tabla(fila)
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        String[] cabeceros = {"Id", "Libro", "Autor", "Precio", "Existencias"};
        this.tablaModeloLibros.setColumnIdentifiers(cabeceros);

        // Instanciar objeto JTable
        this.tablaLibros = new JTable(tablaModeloLibros);
        // Evitar multiple seleccion
        tablaLibros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listarLibros();
    }

    private void listarLibros() {
        // Limpiar tabla
        tablaModeloLibros.setRowCount(0);
        // Obtener libros
        var libros = libroServicio.listarLibros();
        libros.forEach((libro)->{
            Object[] renglonLibro = {
                    libro.getIdLibro(),
                    libro.getNombreLibro(),
                    libro.getAutor(),
                    libro.getPrecio(),
                    libro.getExistencias()
            };
            this.tablaModeloLibros.addRow(renglonLibro);
        });
    }
}

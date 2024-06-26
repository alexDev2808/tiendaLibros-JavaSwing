package jath.tienda_libros.servicio;

import jath.tienda_libros.modelo.Libro;

import java.util.List;

public interface ILibroServicio {
    public List<Libro> listarLibros();
    public Libro buscarLibroPorID(Integer idLibro);
    public void guardarLibro(Libro libro);
    public void eliminarLibro(Libro libro);
}

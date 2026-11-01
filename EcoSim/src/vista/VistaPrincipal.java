package vista;

/**
 * Lo que el ControladorPrincipal necesita de la ventana: navegar entre las
 * tres pantallas y mostrar mensajes.
 */
public interface VistaPrincipal {
    void mostrarConfiguracion();
    void mostrarSimulacion();
    void mostrarReporte();

    /** Mensaje de error o aviso para la persona. */
    void mostrarMensaje(String mensaje);
}

// Importamos las clases necesarias
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// Definimos una interfaz para manejar excepciones
interface ManejadorExcepciones {
    void manejarExcepcion(Exception e);
}

// Implementamos la interfaz ManejadorExcepciones usando Log4j para registrar las excepciones
class ManejadorExcepcionesLog4j implements ManejadorExcepciones {
    private static final Logger logger = LogManager.getLogger(ManejadorExcepcionesLog4j.class);

    public void manejarExcepcion(Exception e) {
        // Registramos la excepción con Log4j
        logger.error("Se produjo una excepción", e);
    }
}

class Pedido {
    private CalculadorTotal calculador;
    private ManejadorExcepciones manejadorExcepciones;

    // Inyectamos las dependencias en el constructor
    public Pedido(CalculadorTotal calculador, ManejadorExcepciones manejadorExcepciones) {
        this.calculador = calculador;
        this.manejadorExcepciones = manejadorExcepciones;
    }

    public double calcularTotal(List<String> pedido, Inventario inventario) {
        try {
            // Intentamos calcular el total
            return calculador.calcularTotal(pedido, inventario);
        } catch (Exception e) {
            // Si se produce una excepción, la manejamos y devolvemos 0
            manejadorExcepciones.manejarExcepcion(e);
            return 0;
        }
    }
}

public class Main {
    public static void main(String[] args) {
        // Creamos un inventario y agregamos algunos productos
        Inventario inventario = new Inventario();
        inventario.agregarProducto(new Producto("Pan blanco", 2.5));
        inventario.agregarProducto(new Producto("Croissant", 1.8));
        inventario.agregarProducto(new Producto("Baguette", 3.0));

        // Creamos un calculador de total y un manejador de excepciones
        CalculadorTotal calculador = new CalculadorTotalPorNombre();
        ManejadorExcepciones manejadorExcepciones = new ManejadorExcepcionesLog4j();

        // Creamos un pedido e inyectamos las dependencias
        Pedido pedido = new Pedido(calculador, manejadorExcepciones);

        // Creamos una lista de productos para el pedido
        List<String> listaProductos = Arrays.asList("Pan blanco", "Croissant");

        // Calculamos el total del pedido y lo imprimimos
        double total = pedido.calcularTotal(listaProductos, inventario);

        System.out.println("Total del pedido: $" + total);
    }
}

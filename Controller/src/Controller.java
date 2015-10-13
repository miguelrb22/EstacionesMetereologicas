import java.io.File;
import java.net.*;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.text.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class Controller {

    /**
     * @param args
     */
    public static void main(String[] args) {
		/*
		* Descriptores de socket servidor y de socket con el cliente
		*/
        int puerto = 0;

        try {

            puerto = 10900;
            ServerSocket skServidor = new ServerSocket(puerto);
            //System.out.println("Controlador corriendo en el puerto " + puerto);


            for (; ; ) {
				/*
				* Se espera un cliente que quiera conectarse
				*/
                Socket skCliente = skServidor.accept(); // Crea objeto
                System.out.println("Iniciando controlador...");

                Thread t = new HiloController(skCliente);
                t.start();
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
    }
}

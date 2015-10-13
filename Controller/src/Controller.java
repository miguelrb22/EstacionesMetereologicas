import java.net.*;


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
            System.out.println("Controlador corriendo en el puerto " + puerto);


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

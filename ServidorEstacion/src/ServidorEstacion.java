import java.net.*;

/**
 * Created by Miguel on 05/10/2015.
 */
public class ServidorEstacion {


    public String serverip = ""; //IP DEL SERVIDOR

    public int serverport = 1800; // PUERTO DEL SERVIDOR

    public int conexiones = 0; //CONEXIONES ACTIVAS

    public int max = 10;  //NUMERO MAXIMO DE CONEXIONES SIMULTANEAS PERMITIDAS


    public int getServerport() {
        return serverport;
    }

    /**
     * Establece un  nuevo puerto para el servidor
     *
     * @param serverport
     */

    public void setServerport(int serverport) {
        this.serverport = serverport;
    }


    /**
     * Inicializa el servidor principal, y se mantiene a la espera de nuevas peticiones
     *
     * @param args
     */

    public static void main(String[] args) {

        ServidorEstacion server = new ServidorEstacion();
        int puerto = server.getServerport();

        try {

            ServerSocket skServidor = new ServerSocket(puerto);
            System.out.println("Servidor miniHTTP corriendo en el puerto ");


            for (; ; ) {
                /*
                 * Se espera un cliente que quiera conectarse
                 */
                Socket skCliente = skServidor.accept(); // Crea objeto

                if (server.conexiones < server.max) {

                    Thread t = new HiloServidor(skCliente, server);
                    t.start();
                    server.conexiones++;
                    System.out.println("Conexiones activas: " + server.conexiones);

                } else {

                    System.out.println("No se permiten mas conexiones");
                    skCliente.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
    }
}

import java.net.*;

/**
 * Created by Miguel on 05/10/2015.
 */
public class ServidorEstacion {

    /**
     * IP del servidor
     */
    public String serverip = "";

    /**
     * Puerto del servidor
     */
    public int serverport = 1800;

    /**
     * Devuelve la ip establecida para el servidor de sockets
     *
     * @return
     */
    public String getServerip() {
        return serverip;
    }

    /**
     * Establece una nueva ip para el servidor
     *
     * @param serverip
     */

    public void setServerip(String serverip) {
        this.serverip = serverip;
    }

    /**
     * Devuelve el puerto establecido para el servidor
     *
     * @return
     */

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
     * Establece la ip actual de la maquina, en el servidor de sockets
     */
    public void setIp() {
        try {
            this.serverip = (Inet4Address.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inicializa el servidor principal, y se mantiene a la espera de nuevas peticiones
     *
     * @param args
     */

    public static void main(String[] args) {

        ServidorEstacion server = new ServidorEstacion();
        server.setIp();

        int puerto = server.getServerport();
        try {

            ServerSocket skServidor = new ServerSocket(puerto);
            System.out.println("Servidor miniHTTP corriendo en el puerto " + puerto);

            /*
             * Mantenemos la comunicacion con el cliente
             */
            for (; ; ) {
                /*
                 * Se espera un cliente que quiera conectarse
                 */
                Socket skCliente = skServidor.accept(); // Crea objeto
                System.out.println("Sirviendo cliente...");


                Thread t = new HiloServidor(skCliente);
                t.start();
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }

    }


}

import java.net.*;

/**
 * Created by Miguel on 05/10/2015.
 */
public class ServidorEstacion {


    public int serverport = 1800; // PUERTO DEL SERVIDOR

    public String ipcontroller = ""; // ip del controlador

    public int puertocontroller = 1900; // puerto del controlador

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
     * @param args conexiones, puerto, ipcontrolador, puertocontrolador
     */



    public static void main(String[] args) {

        if (args.length == 4) {

            ServidorEstacion server = new ServidorEstacion();
            server.max = Integer.parseInt(args[0]); //estableciendo el numero maximo de conexiones
            server.serverport = Integer.parseInt(args[1]); // puerto de escucha del servidor
            server.ipcontroller = args[2]; //donde esta ubicado el controlador
            server.puertocontroller = Integer.parseInt(args[3]); // en que puerto escucha el controlador

            try {

                ServerSocket skServidor = new ServerSocket(server.getServerport());
                System.out.println("Servidor miniHTTP corriendo en el puerto " + server.serverport);


                for (; ; ) {
                /*
                 * Se espera un cliente que quiera conectarse
                 */
                    Socket skCliente = skServidor.accept(); // Crea objeto

                    if (server.conexiones < server.max) {

                        Thread t = new HiloServidor(skCliente, server, server.ipcontroller, server.puertocontroller);
                        t.start();
                        server.conexiones++;
                        System.out.println("Conexiones activas: " + server.conexiones);

                    } else {

                        System.out.println("No se permiten mas conexiones");
                        skCliente.close();
                    }
                }
            }

            catch (BindException e){

                System.out.println("El puerto indicado para el http ya esta ocupado por otro servicio");
            }
            catch (Exception e) {
                System.out.println("Error: " + e.toString());
            }
        }else {

            System.out.println("Se esperan 4 argumentos: numero de conexiones simultaneas, puerto de escucha del servidor, ip del controlador, puerto del controlador");
        }

    }
}

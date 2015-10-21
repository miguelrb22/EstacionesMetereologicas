import java.net.*;


public class Controller {


    public int puerto = 1900;

    public String iprmi = "";

    public int puertormi = 2000;
    /**
     * @param args
     */


    public static void main(String[] args) {
		/*
		* Descriptores de socket servidor y de socket con el cliente
		*/



        if (args.length == 3) {

            Controller c = new Controller();

            c.puerto = Integer.parseInt(args[0]);
            c.iprmi = args[1];
            c.puertormi = Integer.parseInt(args[2]);

            try {

                ServerSocket skServidor = new ServerSocket(c.puerto);
                System.out.println("Controlador corriendo en el puerto " + c.puerto);


                for (; ; ) {
				/*
				* Se espera un cliente que quiera conectarse
				*/
                    Socket skCliente = skServidor.accept(); // Crea objeto

                    Thread t = new HiloController(skCliente,c.iprmi,c.puertormi);
                    t.start();
                }
            }

            catch (BindException e){

                System.out.println("El puerto indicado para el http ya esta ocupado por otro servicio");
            }

            catch (Exception e) {
                System.out.println("Error: " + e.toString());
            }


        } else { System.out.println("Se esperan 3 argumentos: Puero de escucha, ip registro rmi, puerto registro rmi"); }
    }


}

import javax.annotation.processing.SupportedSourceVersion;
import java.io.*;
import java.lang.Exception;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;

public class HiloController extends Thread {

    private Socket skCliente;
    private String metodo = "";
    private String variables[];
    private String estacion[];
    private String msg[];

    private static final String inicioHTTP = "<html>" + "<head><title> Controlador Vending</title>";
    private static final String finHTTP = "</body>" + "</html>";
    private String cuerpo = new String();


    public HiloController(Socket p_cliente)
    {
        this.skCliente = p_cliente;
    }

    /*
    * Lee datos del socket. Supone que se le pasa un buffer con hueco
    *	suficiente para los datos. Devuelve el numero de bytes leidos o
    * 0 si se cierra fichero o -1 si hay error.
    */
    public String leeSocket (Socket p_sk, String p_Datos)
    {
        p_Datos = new String();
        try
        {
            BufferedReader in = new BufferedReader(new InputStreamReader(p_sk.getInputStream()));
            p_Datos += in.readLine();
        }
        catch (Exception e)
        {
            System.out.println("Error: " + e.toString());
        }
        return p_Datos;
    }

    /*
    * Escribe dato en el socket cliente. Devuelve numero de bytes escritos,
    * o -1 si hay error.
    */
    public void escribeSocket(Socket p_sk, String p_Datos) {

        try {


            PrintWriter out = new PrintWriter(p_sk.getOutputStream(),true);

            cuerpo+= inicioHTTP;
            cuerpo+="<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\"></head><body>";
            cuerpo+="<p><span style=\"color:blue\">" + p_Datos + "</span></p>";
            cuerpo+=finHTTP;
            out.print(cuerpo);
            out.flush();


        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }

        return;
    }



    public void run() {

        String Cadena = new String();

        try {



            Cadena = leeSocket(this.skCliente, Cadena);
            Cadena = Cadena.substring(15);


            String metodos_variables[] = Cadena.split("\\?",2);

            this.metodo = metodos_variables[0];
            this.variables = metodos_variables[1].split("&");
            this.estacion = variables[0].split("=");
            this.msg = variables[1].split("=");



            Metodos met = Metodos.valueOf(metodo.toUpperCase());

            escribeSocket(skCliente,"hola");

            switch (met){

                case TEMPERATURA: {



                    if(checkUrl(0)==true){

                        System.out.println("todo va bien");
                    }


                }
                break;
                default:{ System.out.println("Metodo no encontrado");}
            }
            this.skCliente.close();



        } catch (Exception e) {
            System.out.println("Error al iniciar: " + e.toString());
        }
    }

    public enum Metodos{

        TEMPERATURA,
        HUMEDAD
    }

    public boolean checkUrl(int metod){

        boolean accept = false;


        if(metod==0){

            if(estacion[0].equals("estacion") && isNumeric(estacion[1])){accept=true;}

        }

        return accept;
    }

    public static boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }
}
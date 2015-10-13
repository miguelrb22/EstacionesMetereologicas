import javax.annotation.processing.SupportedSourceVersion;
import java.io.*;
import java.lang.Exception;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class HiloController extends Thread {

    private Socket skCliente;
    private String metodo = "";
    private String Cadena = new String();
    private String metodos_variables[];
    private HashMap<String, String> parametros = new HashMap<String, String>();
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

        try {

            Cadena = leeSocket(this.skCliente, Cadena);

            Cadena = Cadena.substring(15);

            boolean checkmetod = extMetodo();
            boolean checkvariable = false;

            if(checkmetod == true) { checkvariable = extVariables(); }


            Metodos met = Metodos.valueOf(parametros.get("metodo").toUpperCase());

            escribeSocket(skCliente,"hola");

            switch (met){

                case TEMPERATURA: {

                    if(checkUrl(0)==true){ System.out.println("todo va bien");

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

    public boolean extMetodo(){

        try {

            if (Cadena.contains("?")) {

                metodos_variables = Cadena.split("\\?", 2);
                parametros.put("metodo",metodos_variables[0]);

            }else{

                return false;

            }

            return true;

        }catch (Exception e){

            return false;

        }
    }


    public boolean extVariables() {

        String variables[];
        String aux[];

        System.out.println(this.metodos_variables[1]);

        try {

            if (metodos_variables[1].contains("&")) {

                variables = metodos_variables[1].split("&");

                if (variables.length <= 2) {

                    for (int i = 0; i < variables.length; i++) {

                        if (variables[0].contains("=")) {
                            aux = variables[0].split("=");

                            parametros.put("parametro" + i, aux[1]);

                        } else {
                            return false;
                        }
                    }

                } else {
                    return false;
                }

            } else {

                if (metodos_variables[1].contains("=")) {
                    aux = metodos_variables[1].split("=");

                    parametros.put("parametro0", aux[1]);

                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }




    public boolean checkUrl(int metod){

        boolean accept = false;

        if(metod==0){

            //if(estacion[0].equals("estacion") && isNumeric(estacion[1])){accept=true;}

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
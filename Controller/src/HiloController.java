import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

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

            //leo la cadena
            Cadena = leeSocket(this.skCliente, Cadena);

            //le quito la cabecera de controladorSD
            Cadena = Cadena.substring(15);

            //Extraer el metodo de la url
            boolean checkmetod = extMetodo();

            //iniciacilzo a false el estado de las variables
            boolean checkvariable = false;

            //si hay un metodo, extraigo las variables
            if(checkmetod == true) { checkvariable = extVariables(); }

            //asigno el metodo de entre los enum
            Metodos met = setMetodo();


            //si hay metodo y variables, comienzo las pruebas...
            if(checkmetod && checkvariable) {

                switch (met) {

                    case TEMPERATURA: {


                            if(chUrlSensores()==2){ System.out.println("Demasiados argumentos"); }
                            else if  (chUrlSensores()==1){ System.out.println("Se espera una variable de tipo estacion"); }
                            else if  (chUrlSensores()==3){ System.out.println("La variable estacion debe ser un entero"); }
                            else{ System.out.println("obteniendo temperatura...");}

                    }
                    break;

                    case HUMEDAD: {

                            System.out.println("Obteniendo humedad...");

                    }
                    break;

                    case LUMINOSIDAD: {

                            System.out.println("Obteniendo luminosidad...");

                    }
                    break;
                    default: {
                        System.out.println("Metodo no encontrado");
                    }
                }

            }else{

                System.out.println("Url incorrecta");
                escribeSocket(skCliente, "Url incorrecta");

            }

            this.skCliente.close();


        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
    }

    /**
     * Los diferentes metodos que se pueden usar en el controlador
     */

    public enum Metodos{

        TEMPERATURA,
        HUMEDAD,
        LUMINOSIDAD,
        PANTALLA,
        NINGUNO
    }

    /**
     * Extrae el metodo de la url
     * @return
     */

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


    /**
     * Extrae las variables de la url
     * @return
     */

    public boolean extVariables() {

        String variables[];
        String aux[];

        try {

            if (metodos_variables[1].contains("&")) {

                variables = metodos_variables[1].split("&");

                if (variables.length <= 2) {

                    for (int i = 0; i < variables.length; i++) {

                        if (variables[0].contains("=")) {
                            aux = variables[0].split("=");

                            parametros.put("parametro" + i, aux[1]);
                            parametros.put("nombre" + i, aux[0]);

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
                    parametros.put("nombre0", aux[0]);

                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }



    /**
     * Comprueba si un string es numerico
     * @param str
     * @return
     */

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





    /**
     * Establece el metodo a tratar a partir de la url
     * @return
     */

    public Metodos setMetodo(){

        try{

            Metodos met = Metodos.valueOf(parametros.get("metodo").toUpperCase());
            return met;

        }catch (Exception e){

            return Metodos.NINGUNO;


        }

    }


    //0 correcto
    //1 error variable estacion
    //2 demasiados argumentos
    //3 variable estacion no es numerica

    public int chUrlSensores(){



        if(parametros.get("nombre0").equals("estacion")){

            if(parametros.get("parametro1")!=null){
                return 2;
            }else if(!isNumeric(parametros.get("parametro0"))){

                return 3;

            }else  return 0;

        }else{

            return 1;
        }
    }
}
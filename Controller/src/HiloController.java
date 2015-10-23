import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import javax.annotation.processing.SupportedSourceVersion;
import java.io.*;
import java.lang.Exception;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.rmi.*;


//export CLASSPATH=$CLASSPATH:/home/miguel/IdeaProjects/EstacionesMetereologicas/Estacion/cliente.jar

//java -Djava.security.policy=registrar.policy Controller 1900 192.168.190.129 1099
//java -Djava.security.policy=registrar.policy Controller 1900 192.168.190.129 1099


//Contenido necesario de "etc/hosts"
       // 127.0.0.1 localhost
       // 192.168.0.xxx mimaquina


public class HiloController extends Thread {

    private Socket skCliente;
    private String metodo = "";
    private String Cadena = new String();
    private String metodos_variables[];
    private HashMap<String, String> parametros = new HashMap<String, String>();
    private String cuerpo = new String();
    private String servidor_rmi = "";



    public HiloController(Socket p_cliente, String iprmi, int puertormi)
    {

        this.skCliente = p_cliente;
        servidor_rmi =  "rmi://"+iprmi+":"+puertormi+"/Estacion";
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

            out.print(p_Datos);
            out.flush();


        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }

        return;
    }


    public void run() {


        try {


            String[] namesR = Naming.list("//" + "192.168.190.129:1099" + "/");
            for (int i = 0; i < namesR.length; i++)  System.out.println(namesR[i]);



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

                System.setSecurityManager(new RMISecurityManager());
                InterfazRemoto objetoRemoto = null;


                switch (met) {


                    case TEMPERATURA: {

                            if(chUrlSensores()==2){ error400(1); }
                            else if  (chUrlSensores()==1){ error400(2); }
                            else if  (chUrlSensores()==3){ error400(3); }
                            else{


                                //ruta a la maquina requerida
                                String route = servidor_rmi + parametros.get("parametro0");
                                System.out.println(route);
                                //instanciando el objeto remoto
                                objetoRemoto = (InterfazRemoto) Naming.lookup(route);
                                int res = objetoRemoto.getTempertura();
                                escribeSocket(skCliente,makeHtml(1,res,""));


                            }

                        this.skCliente.close();

                    }
                    break;

                    case HUMEDAD: {

                        if(chUrlSensores()==2){ error400(1); }
                        else if  (chUrlSensores()==1){ error400(2); }
                        else if  (chUrlSensores()==3){ error400(3); }
                        else{

                            //ruta a la maquina requerida
                            String route = servidor_rmi + parametros.get("parametro0");
                            //instanciando el objeto remoto
                            objetoRemoto = (InterfazRemoto) Naming.lookup(route);
                            int res = objetoRemoto.getHumedad();
                            escribeSocket(skCliente,makeHtml(2,res,""));

                        }
                        this.skCliente.close();

                    }
                    break;

                    case LUMINOSIDAD: {

                        if(chUrlSensores()==2){ error400(1); }
                        else if  (chUrlSensores()==1){ error400(2); }
                        else if  (chUrlSensores()==3){ error400(3); }
                        else{
                            //System.out.println(parametros.get("parametro0"));

                            //ruta a la maquina requerida
                            String route = servidor_rmi + parametros.get("parametro0");
                            //instanciando el objeto remoto
                            objetoRemoto = (InterfazRemoto) Naming.lookup(route);
                            int res = objetoRemoto.getLuminosidad();
                            escribeSocket(skCliente,makeHtml(3,res,""));
                        }
                        this.skCliente.close();

                    }

                    break;

                    case PANTALLA: {

                        if(chUrlActuador()==2){ error400(1); }
                        else if  (chUrlActuador()==1){ error400(2); }
                        else if  (chUrlActuador()==3){ error400(3); }
                        else if  (chUrlActuador()==4){ error400(4); }
                        else{

                            //System.out.println(parametros.get("parametro0"));
                            //System.out.println(parametros.get("parametro1"));

                            //ruta a la maquina requerida
                            String route = servidor_rmi + parametros.get("parametro0");
                            //instanciando el objeto remoto
                            objetoRemoto = (InterfazRemoto) Naming.lookup(route);
                            objetoRemoto.setPantalla(parametros.get("parametro1"));
                            escribeSocket(skCliente,makeHtml(0,4,parametros.get("parametro1")));

                        }
                        this.skCliente.close();

                    }
                    break;

                    default: {

                        escribeSocket(skCliente, "Error: Metodo invalido");
                        this.skCliente.close();

                    }
                    break;
                }

            }else{


                error400(5);
                this.skCliente.close();


            }


        }

        catch (NotBoundException e){

            escribeSocket(skCliente,"NO existe esa estacion");
            try {
                skCliente.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        catch (Exception e) {
            escribeSocket(skCliente, "La máquina esta apagada, o no se te permite el acceso");
            try {
                skCliente.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

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

                        if (variables[i].contains("=")) {
                            aux = variables[i].split("=");

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
                    parametros.put("parametro1", "");
                    parametros.put("nombre0", aux[0]);
                    parametros.put("nombre1", "");

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


    public void error400(int error){


        if(error == 1)         escribeSocket(skCliente, "Error: Demasiados argumentos");
        else if(error == 2)    escribeSocket(skCliente, "Error: Se espera una variable de tipo estacion");
        else if(error == 3)    escribeSocket(skCliente, "Error: La variable estacion debe ser un entero");
        else if(error == 4)    escribeSocket(skCliente, "Error: Se espera una variable de tipo msg");
        else                   escribeSocket(skCliente, "Error: Estructura de la url incorrrecta");


    }


    //0 correcto
    //1 error variable estacion
    //2 demasiados argumentos
    //3 variable estacion no es numerica


    public int chUrlSensores(){


        if(parametros.get("nombre0").equals("estacion")){

            if(!parametros.get("parametro1").equals("")){
                return 2;
            }else if(!isNumeric(parametros.get("parametro0"))){

                return 3;

            }else  return 0;

        }else{

            return 1;
        }
    }

    //0 correcto
    //1 error variable estacion
    //2 demasiados argumentos
    //3 variable estacion no es numerica
    //se espera una variable msg

    public int chUrlActuador(){


        if(parametros.get("nombre0").equals("estacion")){

            if(isNumeric(parametros.get("parametro0"))){

                if(!parametros.get("nombre1").equals("msg")){

                    return 4;

                } else return 0;
            }else{
                return 3;
            }

        }else{

            return 1;
        }
    }


    public String makeHtml(int state, int dato,String msg){

        String html ="";
        html+="<!DOCTYPE html>";
        html+="<html lang=\"en\">";
        html+="<head>";
        html+="<title>Resultado</title>";
        html+="<style type=\"text/css\">";

        if(state==1)
        html+="body { background: url('http://www.itoa-ireland.com/wp-content/uploads/glossy-light.png') no-repeat fixed center center; background-size: cover;}";
        else if (state==2)
        html+="body { background: url('http://img11.deviantart.net/94dc/i/2013/076/4/d/_nube_png__by_lovetheawayyoulie-d5yen4h.png') no-repeat fixed center center; background-size: cover;}";
        else if(state==3)
            html+="body { background: url('http://www.danbrown.com/wp-content/themes/danbrown/images/db/top.light.png') no-repeat fixed center center; background-size: cover;}";

        else
            html+="body { background: url('http://img11.deviantart.net/94dc/i/2013/076/4/d/_nube_png__by_lovetheawayyoulie-d5yen4h.png') no-repeat fixed center center; background-size: cover;}";

        html+="</style>";
        html+="</head>";
        html+="<body>";
        if(state==1)
            html+="<h1 style='color: #FFCDD2; font-size:50px'>Temperatura: "+dato+"</h1>";
        else if (state==2)
            html+="<h1 style='color: #FFCDD2; font-size:50px'>Humedad: "+dato+"</h1>";
        else if(state==3)
            html+="<h1 style='color: #FFCDD2; font-size:50px'>Luminosidad: "+dato+"</h1>";
        else
            html+="<h1 style='color: #FFCDD2; font-size:50px'>Mensaje establecido: "+msg+"</h1>";

        html+="</body>";
        html+="</html>";

        return html;
    }
}
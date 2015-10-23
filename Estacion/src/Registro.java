import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.net.*;
import java.io.*;
import java.util.Arrays;

public class Registro {

    public static int number = 1;

    public static void main (String args[])
    {
        String URLRegistro;
        try
        {


            //En una terminal ejecuto el servidor de registro de nombres, en el directorio donde este el registrar.policy y el stub
            //rmiregistry 1099 –J-Djava.security.policy=registrar.policy


            //En otro registro lo objetos donde tengamos el objeto registro
            //out/production/Estacion/

            //export CLASSPATH=$CLASSPATH:/home/miguel/IdeaProjects/EstacionesMetereologicas/Estacion/cliente.jar
            //java -Djava.security.policy=registrar.policy Registro

            System.setSecurityManager(new RMISecurityManager());


            //BUscar un numero de estacion vacio y registrarlo
            String[] names = Naming.list("//" + "127.0.0.1:1099" + "/");
            int estacion = getEstacionLibre(names);
            URLRegistro = "/Estacion"+ estacion;
            ObjetoRemoto objetoRemoto = new ObjetoRemoto(estacion);

            System.out.println(URLRegistro);
            Naming.rebind (URLRegistro, objetoRemoto);
            System.out.println("Servidor de objeto preparado...");


            //Estaciones registradas
            System.out.println("Estaciones registradas:");
            String[] namesR = Naming.list("//" + "127.0.0.1:1099" + "/");
            for (int i = 0; i < namesR.length; i++)  System.out.println(namesR[i]);

        }
        catch (Exception ex)
        {

            System.out.println(ex);
        }
    }

    /**
     * @return
     */
    private static int getEstacionLibre(String[] names){

        boolean encontrado = false;
        int aux = 0;

        while (encontrado==false) {

            if(Arrays.asList(names).contains("//127.0.0.1:1099/Estacion"+aux)){

                aux++;
            }else{
                encontrado=true;
            }

        }
        return aux;
    }
}
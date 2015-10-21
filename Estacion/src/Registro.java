import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.net.*;
import java.io.*;

public class Registro {

    public static void main (String args[])
    {
        String URLRegistro;
        try
        {

            //java -Djava.security.policy=registrar.policy Registro

            System.setSecurityManager(new RMISecurityManager());
            ObjetoRemoto objetoRemoto = new ObjetoRemoto();
            int estacionN = objetoRemoto.estacionN;


            String[] names = Naming.list("//" + "localhost:1099" + "/");
            for (int i = 0; i < names.length; i++)  System.out.println(names[i]);

            URLRegistro = "/ObjetoRemoto"+ estacionN;
            System.out.println(URLRegistro);
            Naming.rebind (URLRegistro, objetoRemoto);
            System.out.println("Servidor de objeto preparado...");
        }
        catch (Exception ex)
        {

            System.out.println(ex);
        }
    }
}
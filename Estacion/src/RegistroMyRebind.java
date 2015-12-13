import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.util.Arrays;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.net.*;
import java.io.*;
import java.util.Arrays;

/**
 * Created by miguel on 13/12/15.
 */
public class RegistroMyRebind {




        public static void main (String args[])
        {
            String URLRegistro;
            try
            {


                //En una terminal ejecuto el servidor de registro de nombres, en el directorio donde este el registrar.policy y el stub
                //rmiregistry 1099 –J-Djava.security.policy=registrar.policy


                //En otro registro lo objetos donde tengamos el objeto registro
                //cd out/production/Estacion/

                //export CLASSPATH=$CLASSPATH:/home/miguel/IdeaProjects/EstacionesMetereologicas/Estacion/cliente.jar
                //java -Djava.security.policy=registrar.policy Registro

                System.setSecurityManager(new RMISecurityManager());

                URLRegistro = "/Registrador";
                MyRebind myrebind = new MyRebind();
                System.out.println(URLRegistro);
                Naming.rebind(URLRegistro, myrebind);

                System.out.println("Registrador registrado");


            }
            catch (Exception ex)
            {

                System.out.println(ex);
            }
        }


}

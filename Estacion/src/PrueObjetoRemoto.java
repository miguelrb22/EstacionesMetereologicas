import java.rmi.RemoteException;

/**
 * Created by miguel on 22/10/15.
 */
public class PrueObjetoRemoto {

    public static void main (String args[]) throws RemoteException {

        ObjetoRemoto obj = new ObjetoRemoto(1);
        System.out.println(obj.getTempertura());
        System.out.println(obj.getHumedad());
        System.out.println(obj.getLuminosidad());
        obj.setPantalla("hola que tal");

        System.out.println(obj.getTempertura());
        System.out.println(obj.getHumedad());
        System.out.println(obj.getLuminosidad());
        obj.setPantalla("hola que tal 2");


    }
}

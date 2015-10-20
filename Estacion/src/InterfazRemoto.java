import java.rmi.Remote;

/**
 * Created by Miguel on 20/10/2015.
 */
public interface InterfazRemoto extends Remote {

    public static int estacionN = 0;
    public int getTempertura (int maquina)throws java.rmi.RemoteException;
    public int getHumedad (int maquina)throws java.rmi.RemoteException;
    public int getLuminosidad (int maquina)throws java.rmi.RemoteException;
    public boolean setPantalla (int maquina, String msg)throws java.rmi.RemoteException;
}

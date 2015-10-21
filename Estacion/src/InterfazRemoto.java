import java.rmi.Remote;

/**
 * Created by Miguel on 20/10/2015.
 */
public interface InterfazRemoto extends Remote {

    public int getTempertura ()throws java.rmi.RemoteException;
    public int getHumedad ()throws java.rmi.RemoteException;
    public int getLuminosidad ()throws java.rmi.RemoteException;
    public boolean setPantalla (String msg)throws java.rmi.RemoteException;
}

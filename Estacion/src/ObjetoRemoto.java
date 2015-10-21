import java.io.Serializable;
import java.rmi.*;
import java.rmi.server.*;
public class ObjetoRemoto extends UnicastRemoteObject implements InterfazRemoto, Serializable
{


    protected ObjetoRemoto() throws RemoteException {}

    @Override
    public int getTempertura() throws RemoteException {
        return 5;
    }

    @Override
    public int getHumedad() throws RemoteException {
        return 6;
    }

    @Override
    public int getLuminosidad() throws RemoteException {
        return 7;
    }

    @Override
    public boolean setPantalla(String msg) throws RemoteException {
        return false;
    }
}
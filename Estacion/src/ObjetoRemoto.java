import java.io.Serializable;
import java.rmi.*;
import java.rmi.server.*;
public class ObjetoRemoto extends UnicastRemoteObject implements InterfazRemoto, Serializable
{

    public static int estacionN;

    protected ObjetoRemoto() throws RemoteException {

        estacionN++;
    }

    @Override
    public int getTempertura(int maquina) throws RemoteException {
        return 5;
    }

    @Override
    public int getHumedad(int maquina) throws RemoteException {
        return 6;
    }

    @Override
    public int getLuminosidad(int maquina) throws RemoteException {
        return 7;
    }

    @Override
    public boolean setPantalla(int maquina, String msg) throws RemoteException {
        return false;
    }
}
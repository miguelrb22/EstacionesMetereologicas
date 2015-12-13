import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by miguel on 13/12/15.
 */
public class MyRebind extends UnicastRemoteObject implements InterfazMyRebind, Serializable {


    protected MyRebind() throws RemoteException {
        super();
    }

    public void rebind(String URLRegistro, ObjetoRemoto objetoRemoto) throws MalformedURLException, RemoteException {

        Naming.rebind(URLRegistro, objetoRemoto);

    }
}

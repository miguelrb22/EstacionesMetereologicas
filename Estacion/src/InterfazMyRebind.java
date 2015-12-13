import java.net.MalformedURLException;
import java.rmi.Remote;


/**
 * Created by Miguel on 20/10/2015.
 */
public interface InterfazMyRebind extends Remote {

    public void rebind (String URLRegistro, ObjetoRemoto objetoRemoto)throws java.rmi.RemoteException, MalformedURLException;

}
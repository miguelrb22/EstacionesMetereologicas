import java.io.*;
import java.rmi.*;
import java.rmi.server.*;
public class ObjetoRemoto extends UnicastRemoteObject implements InterfazRemoto, Serializable
{


    private File archivo = null;
    private FileReader fr = null;
    private BufferedReader br = null;
    private FileWriter fichero = null;
    private PrintWriter pw = null;
    private String ruta ="";

    protected ObjetoRemoto() throws RemoteException {

        super();


    }

    protected ObjetoRemoto(int estacion) throws RemoteException {

        super();

        ruta = "/home/miguel/Estacion"+estacion+".txt";

        try
        {
            fichero = new FileWriter(ruta);
            pw = new PrintWriter(fichero);

                pw.println("Temperatura="+ (int) Math.floor(Math.random()*(50-(-30)+1)+(-30)));
                pw.println("Humedad=" + (int)Math.floor(Math.random()*(100-(0)+1)+(0)));
                pw.println("Luminosidad="  + (int) Math.floor(Math.random()*(800-(0)+1)+(0)));
                pw.println("Pantalla=Hola, esto es la practica de SD");
            //M MENOR


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Nuevamente aprovechamos el finally para
                // asegurarnos que se cierra el fichero.
                if (null != fichero)
                    fichero.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }


    }


    @Override
    public int getTempertura() throws RemoteException {

        int temperatura = 0;
        String[] data;
        try {
            // Apertura del fichero y creacion de BufferedReader para poder
            // hacer una lectura comoda (disponer del metodo readLine()).
            archivo = new File (ruta);
            fr = new FileReader (archivo);
            br = new BufferedReader(fr);

            // Lectura del fichero
            String linea;
            linea=br.readLine();
            data = linea.split("=");
            temperatura = Integer.parseInt(data[1]);

        }
        catch(Exception e){
            e.printStackTrace();
        }finally{
            // En el finally cerramos el fichero, para asegurarnos
            // que se cierra tanto si todo va bien como si salta
            // una excepcion.
            try{
                if( null != fr ){
                    fr.close();
                }
            }catch (Exception e2){
                e2.printStackTrace();
            }
        }

        return temperatura;
    }

    @Override
    public int getHumedad() throws RemoteException {

        int humedad = 0;
        String[] data;
        try {
            // Apertura del fichero y creacion de BufferedReader para poder
            // hacer una lectura comoda (disponer del metodo readLine()).
            archivo = new File (ruta);
            fr = new FileReader (archivo);
            br = new BufferedReader(fr);

            // Lectura del fichero
            String linea;
            linea=br.readLine();
            linea=br.readLine();
            data = linea.split("=");
            humedad = Integer.parseInt(data[1]);

        }
        catch(Exception e){
            e.printStackTrace();
        }finally{
            // En el finally cerramos el fichero, para asegurarnos
            // que se cierra tanto si todo va bien como si salta
            // una excepcion.
            try{
                if( null != fr ){
                    fr.close();
                }
            }catch (Exception e2){
                e2.printStackTrace();
            }
        }

        return humedad;
    }

    @Override
    public int getLuminosidad() throws RemoteException {

        int luminosidad = 0;
        String[] data;
        try {
            // Apertura del fichero y creacion de BufferedReader para poder
            // hacer una lectura comoda (disponer del metodo readLine()).
            archivo = new File (ruta);
            fr = new FileReader (archivo);
            br = new BufferedReader(fr);

            // Lectura del fichero
            String linea;
            linea=br.readLine();
            linea=br.readLine();
            linea=br.readLine();
            data = linea.split("=");
            luminosidad = Integer.parseInt(data[1]);

        }
        catch(Exception e){
            e.printStackTrace();
        }finally{
            // En el finally cerramos el fichero, para asegurarnos
            // que se cierra tanto si todo va bien como si salta
            // una excepcion.
            try{
                if( null != fr ){
                    fr.close();
                }
            }catch (Exception e2){
                e2.printStackTrace();
            }
        }

        return luminosidad;
    }

    @Override
    public boolean setPantalla(String msg) throws RemoteException {

        try
        {

            String lineas[] = auxPantalla();
            fichero = new FileWriter(ruta);
            pw = new PrintWriter(fichero);


            pw.println(lineas[0]);
            pw.println(lineas[1]);
            pw.println(lineas[2]);
            pw.println("Pantalla="+msg);

            return true;


        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                // Nuevamente aprovechamos el finally para
                // asegurarnos que se cierra el fichero.
                if (null != fichero)
                    fichero.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }




    public String[] auxPantalla(){

        String lineas[] = new String[3];

        try {
            // Apertura del fichero y creacion de BufferedReader para poder
            // hacer una lectura comoda (disponer del metodo readLine()).
            archivo = new File (ruta);
            fr = new FileReader (archivo);
            br = new BufferedReader(fr);

            // Lectura del fichero
            lineas[0]=br.readLine();
            lineas[1]=br.readLine();
            lineas[2]=br.readLine();

        }
        catch(Exception e){
            e.printStackTrace();
        }finally{
            // En el finally cerramos el fichero, para asegurarnos
            // que se cierra tanto si to-do va bien como si salta
            // una excepcion.
            try{
                if( null != fr ){
                    fr.close();
                }
            }catch (Exception e2){
                e2.printStackTrace();
            }
        }

        return lineas;

    }
}

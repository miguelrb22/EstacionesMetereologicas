import java.io.*;
import java.lang.Exception;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by EPS on 06/10/2015.
 */
public class HiloServidor extends Thread{

    private Socket skCliente;
    //private gui g = new gui();
    private String metodoHTTP;
    private String versionHTTP;
    private String urlHTTP;
    private static final String inicioHTTP = "<html>" + "<head><title>Servidor PRACTICA SD: Vending</title>";
    private static final String finHTTP = "</body>" + "</html>";
    private String cuerpo = new String();

    public HiloServidor(Socket p_cliente) {
        this.skCliente = p_cliente;
    }


    public String leeSocket(Socket p_sk, String p_Datos) {
        p_Datos = new String();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(p_sk.getInputStream()));
            p_Datos += in.readLine();
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
        return p_Datos;
    }

    public String procesarLineaHTTP(String p_Cadena) {

        StringTokenizer st = new StringTokenizer(p_Cadena);
        metodoHTTP = st.nextToken();
        urlHTTP = st.nextToken();
        versionHTTP = st.nextToken();
        String res = metodoHTTP + "\t" + urlHTTP + "\t" + versionHTTP;
        return res;
    }

    public void escribeSocket(Socket p_sk, String p_Datos) {
        try {
            PrintWriter out = new PrintWriter(p_sk.getOutputStream());
            out.flush();

            cuerpo+= inicioHTTP;
            cuerpo+="<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\"></head><body>";
            cuerpo+="<p><span style=\"color:blue\">" + p_Datos + "</span></p>";
            cuerpo+=finHTTP;

            out.println("HTTP/1.1 200 OK");
            out.println("Connection: close");
            out.println("Content-Lenght: "+ cuerpo.getBytes().length);
            out.println("Content-Type: text/html; charset=UTF-8");
            out.println("Server: informacion del servidor");
            out.println("\n");

            out.println(cuerpo);

            out.flush();
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
        return;
    }

    public void escribeSocketAcontrolar(Socket p_sk, String p_Datos) {
        try {
            PrintWriter out = new PrintWriter(p_sk.getOutputStream());

            out.println(p_Datos);
            out.flush();
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
        return;
    }

    public void run() {

        String Cadena = new String();
        String nt = "No te entiendo";

        try {
            //ServerSocket skServidor = new ServerSocket(Integer.parseInt(sr.puerto));

            System.out.println("Iniciado nuevo hilo...");

            Cadena = leeSocket(this.skCliente, Cadena);

            String aux = this.procesarLineaHTTP(Cadena);

            String orden = this.urlHTTP;


            if(this.metodoHTTP.equals("GET")){

                System.out.println("Peticion permitida");
                this.escribeSocket(this.skCliente,"Error");

            }else{

                System.out.println("Peticion no permitida");
            }


            /**
            if ((orden.equals("/controladorSD/") || orden.equals("/controladorSD")) && orden.length() <= 15) {
                escribeSocket(skCliente, "");
            } else if (orden.length() > 15 && (this.urlHTTP.substring(0, 15)).equals("/controladorSD/")) {

                //Canal de comunicacion con el controlador
                Socket canalControlador = new Socket("192.168.100.102", 10900);

                String acontrolar = this.urlHTTP.substring(15);
                System.out.println(acontrolar);
                this.escribeSocketAcontrolar(canalControlador, acontrolar);

                escribeSocket(skCliente, "Accediendo al controlador");

            } else {
                escribeSocket(skCliente, "ERROR 404 PAGINA NO ENCONTRADA");
            } */

            //else escribeSocket(skCliente, "FAIL");
            System.out.println("Hilo finalizado");
            this.skCliente.close();

        } catch (Exception e) {
            System.out.println("Error al iniciar: " + e.toString());
        }
    }

}

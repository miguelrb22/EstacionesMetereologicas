import java.io.*;
import java.lang.Exception;
import java.net.Socket;
import java.util.StringTokenizer;


public class HiloServidor extends Thread {

    private Socket skCliente; //cliente
    private String metodoHTTP; //metodo [GET,POST,HEAD,PUT,DELETE]
    private String versionHTTP; //version del http [HTTP 1.1]
    private String urlHTTP; //url peticion
    public ServidorEstacion servidor; // servidor principal


    /**
     * Construsctor del hilo
     * @param p_cliente
     * @param servidor
     */
    public HiloServidor(Socket p_cliente, ServidorEstacion servidor) {
        this.skCliente = p_cliente;
        this.servidor = servidor;
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

    public void escribeSocket(Socket p_sk, String p_Datos, boolean cabeceras) {

        try {

            String cuerpo = new String();

            PrintWriter out = new PrintWriter(p_sk.getOutputStream());
            out.flush();

            if (cabeceras == true) {

                cuerpo += "<html>" + "<head><title> Servidor </title>";
                cuerpo += "<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\"></head><body>";
                cuerpo += "<p><span style=\"color:blue\">" + p_Datos + "</span></p>";
                cuerpo += "</body>" + "</html>";

            } else {
                cuerpo = p_Datos;
            }

            out.println("HTTP/1.1 200 OK");
            out.println("Connection: close");
            out.println("Content-Lenght: " + cuerpo.getBytes().length);
            out.println("Content-Type: text/html; charset=UTF-8");
            out.println("Server: informacion del servidor");
            out.println("\n");
            out.println(cuerpo);
            out.flush();
            out.close();

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

        try {

            Cadena = leeSocket(this.skCliente, Cadena); //leemos la cadena recibida por el cliente
            procesarLineaHTTP(Cadena); // procesamos la cadena para separarla
            int ordensize = urlHTTP.length(); //longitud de la orden pedida


            if (this.metodoHTTP.equals("GET")) {


                if (ordensize >= 14) {
                    if (urlHTTP.substring(0, 14).equals("/controladorSD")) { toController();  } // al controlador
                    else { getFile(2);}
                }

                else if (ordensize >= 15) {
                    if (urlHTTP.substring(0, 15).equals("/controladorSD/")) { toController(); } // al controlador
                    else { getFile(2);}
                }

                else {

                    if (urlHTTP.equals("/")) { getFile(1); } //index
                    else { getFile(2); }
                }

            } else {

                this.escribeSocket(this.skCliente, "Error", false);
            }

            this.skCliente.close();

        } catch (FileNotFoundException e) {


        } catch (Exception e) {

            System.out.println("Error al iniciar: " + e.toString());
        }

        servidor.conexiones--; // Hilo terminado
    }


    public void getFile(int action) {

        String result = new String();
        String cadena = new String();
        FileReader fr;

        try {

            if (action == 1) fr = new FileReader("C:/html/index.html.txt");
            else if (action == 2) fr = new FileReader("C:/html/" + this.urlHTTP + ".txt");
            else fr = new FileReader("C:/html/404.html.txt");


            BufferedReader br = new BufferedReader(fr);
            while ((cadena = br.readLine()) != null) {
                result += cadena;
            }
            escribeSocket(skCliente, result, false);
            skCliente.close();

        } catch (FileNotFoundException e) {
            getFile(3);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void toController() {


        String result = new String();
        try {

            Socket canalControlador = new Socket("192.168.1.104", 10900); //inicio comunicacion con el controlador

            escribeSocketAcontrolar(canalControlador, this.urlHTTP);// le escribo la peticion

            result = leeSocket(canalControlador, result); //leo la respuesta

            escribeSocket(skCliente, result, true); // la escribo en el cliente

            canalControlador.close(); // cierro comunicacion con el controlador

        } catch (IOException e) {

        }
    }
}

import java.io.*;
import java.lang.Exception;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;


public class HiloServidor extends Thread {

    private Socket skCliente; //cliente

    private String metodoHTTP; //metodo [GET,POST,HEAD,PUT,DELETE]

    private String versionHTTP; //version del http [HTTP 1.1]

    private String urlHTTP; //url peticion

    public ServidorEstacion servidor; // servidor principal

    public String ipcontrolador = "";

    public int puertocontrolador = 1900;


    /**
     * Construsctor del hilo
     * @param p_cliente
     * @param servidor
     */
    public HiloServidor(Socket p_cliente, ServidorEstacion servidor, String ipcontroller, int puertocontroller) {
        this.skCliente = p_cliente;
        this.servidor = servidor;
        this.ipcontrolador = ipcontroller;
        this.puertocontrolador = puertocontroller;
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

    public void escribeSocket(Socket p_sk, String p_Datos, boolean cabeceras, int state) {

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


            //state: 1  = 200 ok
            // 2 = 404
            //3 405 method not allowed
            //4 400 Bas request

            if(p_Datos.substring(0,5).equals("Error") && state != 3){ state =4; }

            if(state == 1) {

                out.println("HTTP/1.1 200 OK");
            }else if(state == 2){

                out.println("HTTP/1.1 404 Not Found");

            }else if (state ==3){

                out.println("HTTP/1.1 405 Method Not Allowed");
            }

            else if (state ==4){
                out.println("HTTP/1.1 400 Bad Request");
            }

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


            if (this.metodoHTTP.equals("GET") && !urlHTTP.equals("/favicon.ico")) {


                if (ordensize >= 14) {
                    if (urlHTTP.substring(0, 14).equals("/controladorSD")) { toController();  } // al controlador
                    else { getFile(2,1);}
                }

                else if (ordensize >= 15) {
                    if (urlHTTP.substring(0, 15).equals("/controladorSD/")) { toController(); } // al controlador
                    else { getFile(2,1);}
                }

                else {

                    if (urlHTTP.equals("/")) { getFile(1,1); } //index
                    else { getFile(2,1); }
                }

            } else {

                this.escribeSocket(this.skCliente, "Error: 405 Method Not Allowed", false,3);
            }

            this.skCliente.close();

        }catch (NoSuchElementException e){ }
         catch (Exception e) { e.printStackTrace(); }

        servidor.conexiones--; // Hilo terminado
    }


    public void getFile(int action, int state) {

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
            escribeSocket(skCliente, result, false,state);
            skCliente.close();

        } catch (FileNotFoundException e) {
            getFile(3,2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void toController() {


        String result = new String();

        System.out.println("Accediendo al controlador...");

        try {

            Socket canalControlador = new Socket(this.ipcontrolador, this.puertocontrolador); //inicio comunicacion con el controlador

            escribeSocketAcontrolar(canalControlador, this.urlHTTP);// le escribo la peticion

            result = leeSocket(canalControlador, result); //leo la respuesta

            escribeSocket(skCliente, result, true,1); // la escribo en el cliente

            canalControlador.close(); // cierro comunicacion con el controlador

        } catch (IOException e) {

            System.out.println("No se ha podido conectar con el controlador");

        }
    }
}

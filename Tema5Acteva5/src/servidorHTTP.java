import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import com.sun.net.httpserver.HttpServer;

// La clase servidorHTTP lo que hara sera, que a traves de una direccionIP, un puerto y una ruta, gestionara gracias a los multihilos,
// las peticiones Get y Post, desde la clase gestorHTTP.
public class servidorHTTP 
{
	public static void main(String[] args) throws Exception 
	{
		// TODO Auto-generated method stub
		
		String host = "localhost"; // IP: 127.0.0.1
		
		int puerto = 7777;
		
		InetSocketAddress direccionTCPIP = new InetSocketAddress(host,puerto);
		
		int backlog = 0;
		
		HttpServer servidor = HttpServer.create(direccionTCPIP,backlog);
		
		gestorHTTP gestorHTTP = new gestorHTTP(); // Esta es la clase, que gestionara la peticiones Get y Post. 
				
		String ruta = "/estufa"; // Ruta de la Url, desde la cual el servidor dara respuesta, a las peticiones.
		
		servidor.createContext(ruta,gestorHTTP);
		
		// Esto lo que hara sera a traves de los multihilos, gestionar las peticiones de la clase gestorHTTP, en este caso 10 veces.
		ThreadPoolExecutor tpe = (ThreadPoolExecutor)Executors.newFixedThreadPool(10);
		servidor.setExecutor(tpe);
		servidor.start();
		
		System.out.println("El servidor http, se esta ejecutando desde el puerto: " + puerto);
	}
}
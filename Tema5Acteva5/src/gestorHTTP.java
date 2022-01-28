import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

// La clase gestorHTTP lo que hara sera, que a traves de varios metodos, recogera, leera y mostrara,
// toda la infromacion que reciba por parte del cliente.
public class gestorHTTP implements HttpHandler
{
	//Temperarturas (Variables) que inicialmente estaran a 15 grados.
	private static int temperaturaActual = 15;
	private static int temperaturaTermostato = 15;
	
	@Override
	public void handle(HttpExchange httpExchange) throws IOException 
	{
		// TODO Auto-generated method stub
		
		String parametros = null;
	
		if("GET".equals(httpExchange.getRequestMethod()))
		{
			parametros = handleGetRequest(httpExchange);
			
			try 
			{
				handleGetResponse(httpExchange,parametros);
			} catch (Exception e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if("POST".equals(httpExchange.getRequestMethod()))
		{
			parametros = handlePostRequest(httpExchange);
			
			try 
			{
				handlePostResponse(httpExchange,parametros);
			} catch (Exception e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	// Este metodo cogera la informacion de tipo Get de la URI, que hemos recibido por parte del cliente.
	public String handleGetRequest(HttpExchange httpExchange) 
	{
		System.out.println("Esta el Uri de tipo Get que hemos recibido: " + httpExchange.getRequestURI().toString());
		
		return httpExchange.getRequestURI().toString().split("\\?")[1];
	}
	
	// Leemos linea a linea, la informacion de la peticion Post que hay en la URI, que hemos recibido por parte del cliente.
	public String handlePostRequest(HttpExchange httpExchange) 
	{
		System.out.println("Esta es la Uri de tipo Post que hemos recibido: " + httpExchange.getRequestBody().toString());
		
		InputStream is = httpExchange.getRequestBody();
		
		InputStreamReader isr = new InputStreamReader(is);
		
		BufferedReader br = new BufferedReader(isr);
		
		StringBuilder sb = new StringBuilder();
		
		String linea;
		
		try 
		{
			while((linea = br.readLine()) != null) 
			{
				sb.append(linea);
			}
			br.close();
		}
		catch(IOException e) 
		{
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	// Mostramos a traves de una pagina html, la informacion del Get de la URI, que hemos recibido por parte del cliente.
	public void handleGetResponse(HttpExchange httpExchange, String parametros) throws Exception 
	{
		OutputStream os = httpExchange.getResponseBody();
		
		if(parametros.equals("temperaturaActual"))
		{
			String htmlrespuesta = "<html><body><h1>Temperatura Actual " + temperaturaActual + "<h1><h2>Temperatura Termostato " +
			temperaturaTermostato + "<h2></body></html>";
			
			regularTemperatura(parametros);
										
			httpExchange.sendResponseHeaders(200, htmlrespuesta.length());
										
			os.write(htmlrespuesta.getBytes());
										
			System.out.println("Respuesta del html: " + htmlrespuesta);
		}
		else 
		{
			System.out.println("Error con la Uri introducida.");
		}
		
		os.flush();
		
		os.close();
	}
	
	// Mostranos a traves de consola, el parametro que hemos cogido a la derecha de la URI, que hemos recibido por parte del cliente.
	public void handlePostResponse(HttpExchange httpExchange, String parametros) throws Exception
	{
		OutputStream os = httpExchange.getResponseBody();
		
		String htmlrespuesta = "Parametros Post: " + parametros + " -> que se procesara por parte del servidor.";
		
		String temp = parametros.split("=")[1];
		
		temperaturaTermostato = Integer.parseInt(temp);
		
		httpExchange.sendResponseHeaders(200,htmlrespuesta.length());
		
		os.write(htmlrespuesta.getBytes());
		
		os.flush();
		
		os.close();
		
		System.out.println("Respuesta del html: " + htmlrespuesta);
	}
	
	// Este metodo lo que hara sera si la temperaturaActual que nosotros tenemos inicializada, es menor a la que cogemos del metodo
	// handlePostResponse, la ira sumando de uno en uno, hasta que sean iguales y si son iguales, la temperaturaActual,
	// ira disminuyendo de uno a uno.
		// Reseña: Todo esto se hara cada vez que recargemos la pagina, con una pausa de 5 segundos entre recarga y recarga de la pagina.
	private static void regularTemperatura(String parametros)
	{
		try 
		{
			if(temperaturaActual < temperaturaTermostato)
			{	
				temperaturaActual++;
				Thread.sleep(5000);
			}
			else 
			{
				temperaturaActual--;
				Thread.sleep(5000);
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}
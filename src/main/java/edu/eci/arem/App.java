package edu.eci.arem;

import static spark.Spark.*;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;

import spark.Request;
import spark.Response;
import com.mongodb.BasicDBObject;

import edu.eci.arem.persistence.MongoConnection;


/**
 * @author Fernando Barrera Barrera
 *
 */

public class App 
{
	private static MongoConnection mongo;
	/**
     * Este metodo main inicia el logservice y donde se definene una conexion a la base de datos mongoDB Y dos servicios
     *  rest por medio de funciones lambda
     */
    public static void main( String[] args ) throws UnknownHostException
    {
    	port(getPort());
    	mongo = new MongoConnection();
    	get("/consultlogs", (req, res) ->  consult(req, res));
    	get("/savelogs", (req, res) ->  save(req, res));
    	
    	   	
    }
    /**
     *Este metodo es el metodo get encargado de recibir un nuevo log por medio de la requests y se encarga de registar en
     *la base de datos el nuevo mensage log junto con la fecha en la que inserta este nuevo log y llama el consult para que   
     *entregar un vista actualizad del consult con el nuevo log insertado 
     *
     * @param req Tiene la informacion de la petición que llega al servidor.
     * @param res Tiene la información con la respuesta del servidor.
     * @return String con la informacion html actualizada del consult.
     */
    private static String  save(Request req, Response res){
    	mongo.add(req.queryParams("message"), new Date());
    	return  consult(req,res);
    	
    }
    /**
     *Este metodo se encarga de recibir retornar un doumento json con los 10 ultimos  logs insertados en la base de ddatos  
     *
     * @param req Tiene la informacion de la petición que llega al servidor.
     * @param res Tiene la información con la respuesta del servidor.
     * @return String con la informacion html de la vista consult .
     */
    private static String  consult(Request req, Response res){
    	res.type("application/json");
    	String datos[]=null;
		ArrayList<BasicDBObject> list= mongo.consult();
		int cont = 1;
		if(list.size()<=10) {
			datos =  new String[list.size()];
			for(BasicDBObject d:list) {
				 
				   datos[cont-1]=String.valueOf(cont)+"-"+d.get("message").toString()+"-"+d.get("date").toString();
				   cont++;
			}
		}
		else {
			datos =  new String[10];
			for(int i=list.size()-10;i<list.size();i++) {
				 datos[cont-1]=String.valueOf(cont)+"-"+(list.get(i)).get("message").toString()+"-"+(list.get(i)).get("date").toString();
				 cont++;
			}
		}
    	return String.join(",", datos);
    	
    }
    /**
     *Este metodo se encarga de retonar el puerto por defecto que esta definido en una variable de entorno 
     *para correr el servidor web sobre ese puerto.
     */
    static int getPort() {
	   	 if (System.getenv("PORT") != null) {
	   		 return Integer.parseInt(System.getenv("PORT"));
	   	 }
	   	 return 5000; //returns default port if heroku-port isn't set
   }
}

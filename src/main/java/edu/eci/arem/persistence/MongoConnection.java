package edu.eci.arem.persistence;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

/**
 * @author Fernando Barrera Barrera
 *
 */

public class MongoConnection {
	private DBCollection coleccion;
	private DB db; 
	/**
	 * Este mertodo constructor de la conexion a la base de datos usando un mongoclient
	 * @throws UnknownHostException en caso de no poder conectarse a la base de datos.
	 */
	public MongoConnection() throws UnknownHostException {
		MongoClient mongoClient = new MongoClient("ec2-18-207-203-91.compute-1.amazonaws.com" , 27017);
		db= mongoClient.getDB("arem");
        coleccion= db.getCollection("logs");
        
	}
	/**
	 * Este metodo recibe un mensaje del log y la fecha de insercion del log y lo inserta 
	 * en la coleccion denominada logs de la base de datos arem
	 * 
	 * @param message mensaje del log.
	 * @param date fecha de insercion del log.
	 */
	public void add(String message,Date date) {
		BasicDBObject objeto= new BasicDBObject();
        objeto.put("message",message);
        objeto.put("date",date);
        coleccion.insert(objeto);
		
	}
	/**
	 * Este metodo  se encarga de retorna un arraylist con todos los elementos dentro de la coleccion logs
	 * de la base de datos arem
	 * @return contenido de la coleccion logs de la base de datos.
	 */
	public ArrayList<BasicDBObject> consult() {
		ArrayList<BasicDBObject> registros = new ArrayList<BasicDBObject>();
		DBCursor mensajes = coleccion.find();
		while (mensajes.hasNext()){
			  
			  registros.add((BasicDBObject) mensajes.next());
		}
		return registros;
	}

}
import java.io.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.rmi.server.UnicastRemoteObject;

/**
 * Beschreiben Sie hier die Klasse Server.
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 */
public class ObjectServer<T extends Remote>
{
    private T serverObj;
    private String objName;

    public ObjectServer(T objectInstance) throws RemoteException
    {
        serverObj = objectInstance;
        objName= serverObj.getClass().getName();
        T stub = (T) UnicastRemoteObject.exportObject( serverObj, 0 );
        Registry registry = LocateRegistry.getRegistry();
        registry.rebind( objName, stub );
        System.out.println( objName+" registered" );
    }
}
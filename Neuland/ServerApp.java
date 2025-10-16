import java.io.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.rmi.server.UnicastRemoteObject;

/**
 * Beschreiben Sie hier die Klasse ServerApp.
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 */
public class ServerApp<T extends Remote>
{
    public static void startRegistry() throws RemoteException
    {
        LocateRegistry.createRegistry( Registry.REGISTRY_PORT);
        RemoteServer.setLog( System.err);
    }

    public static void main(String [] arg)
    {
        try{
            new ObjectServer<ISpiel>(new Spiel());
        }
        catch(RemoteException re){
            re.printStackTrace();
        }
    }
}
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
        try {
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            System.out.println("RMI-Registry gestartet auf Port " + Registry.REGISTRY_PORT + ".");
        } catch (RemoteException ex) {
            // Prüfen, ob bereits eine Registry aktiv ist.
            LocateRegistry.getRegistry().list();
            System.out.println("Existierende RMI-Registry wird verwendet.");
        }
        RemoteServer.setLog(System.err);
    }

    public static void main(String [] arg)
    {
        try{
            startRegistry();
            new ObjectServer<ISpiel>(new Spiel());
            System.out.println("Spielserver bereit. Logischer Name: '" + Spiel.class.getName() + "'.");
            // Hauptthread blockieren, damit die Anwendung aktiv bleibt.
            synchronized (ServerApp.class) {
                ServerApp.class.wait();
            }
        }
        catch(RemoteException re){
            re.printStackTrace();
        }
        catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }
}
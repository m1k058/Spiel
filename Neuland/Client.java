import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

/**
 * Beschreiben Sie hier die Klasse Client.
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 */
public class Client<T>
{
    private T remoteObj;
    private String ip;

    public Client(String regEntry)
    {        
        this( "127.0.0.1", regEntry); // lokale IP Adresse
    }
    
    public Client(String ip, String regEntry)
    {

        try
        {
            Registry registry = LocateRegistry.getRegistry(ip);
            remoteObj = (T) registry.lookup(regEntry);
        }
        catch (RemoteException re)
        { // Fehler bei Aufrufvermittlung behandeln 
            System.err.println(re);
        }
        catch (NotBoundException nbe)
        { // kein Server mit logischem Namen regEntry registriert
            System.err.println(nbe);
        }
    }

    public T getObjRef()
    {
        return remoteObj;
    }
}
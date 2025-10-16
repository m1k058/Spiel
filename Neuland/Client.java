import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.util.Scanner;

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

    public static void main(String[] args)
    {
        String host = args.length > 0 ? args[0] : "127.0.0.1";
        String regEntry = args.length > 1 ? args[1] : Spiel.class.getName();

        Client<ISpiel> client = new Client<>(host, regEntry);
        ISpiel spiel = client.getObjRef();

        if (spiel == null) {
            System.err.println("Spiel konnte nicht gefunden werden. Läuft der Server?");
            return;
        }

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print(spiel.start());
            while (!spiel.istBeendet()) {
                System.out.print("> ");
                if (!scanner.hasNextLine()) {
                    break;
                }
                String kommando = scanner.nextLine();
                String antwort = spiel.verarbeiteBefehl(kommando);
                System.out.print(antwort);
            }
        } catch (RemoteException e) {
            System.err.println("Kommunikationsfehler: " + e.getMessage());
        }
    }
}

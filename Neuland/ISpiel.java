import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Tragen Sie hier eine Beschreibung des Interface ISpiel ein.
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 */

public interface ISpiel extends Remote
{
    void spielen() throws RemoteException;
}
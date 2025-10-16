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
    /**
     * Initialisiert das Spiel und liefert alle anfallenden Ausgaben als Text.
     *
     * @return gesammelte Konsolenausgaben der Initialisierung.
     * @throws RemoteException falls ein Verbindungsfehler auftritt.
     */
    String start() throws RemoteException;

    /**
     * Verarbeitet einen einzelnen Spielbefehl.
     *
     * @param eingabe Der komplette Befehl als Textzeile.
     * @return die bei der Verarbeitung angefallenen Ausgaben.
     * @throws RemoteException falls ein Verbindungsfehler auftritt.
     */
    String verarbeiteBefehl(String eingabe) throws RemoteException;

    /**
     * @return {@code true}, wenn das Spiel beendet ist.
     * @throws RemoteException falls ein Verbindungsfehler auftritt.
     */
    boolean istBeendet() throws RemoteException;
}

import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * Dieser Parser liest Benutzereingaben und wandelt sie in
 * Befehle für das Adventure-Game um. Bei jedem Aufruf
 * liest er eine Zeile von der Konsole und versucht, diese als
 * einen Befehl aus bis zu zwei Wörtern zu interpretieren. Er
 * liefert den Befehl als ein Objekt der Klasse Befehl zurück.
 * 
 * Der Parser verfügt über einen Satz an bekannten Befehlen. Er
 * vergleicht die Eingabe mit diesen Befehlen. Wenn die Eingabe
 * keinen bekannten Befehl enthält, dann liefert der Parser ein als 
 * unbekannter Befehl gekennzeichnetes Objekt zurück.
 * 
 * @author  Michael Kölling und David J. Barnes
 * @version 31.07.2011
 */
class Parser 
{
    private Befehlswoerter befehle;  // hält die gültigen Befehlswörter
    private Scanner leser;         // Lieferant für eingegebene Befehle

    /**
     * Erzeuge einen Parser, der Befehle von der Konsole einliest.
     */
    public Parser()
    {
        befehle = new Befehlswoerter();
        leser = new Scanner(System.in);
    }

    /**
     * @return Den nächsten Befehl des Benutzers.
     */
    public Befehl liefereBefehl()
    {
        if (!leser.hasNextLine()) {
            return new LogBefehl(Befehlswort.UNKNOWN, null);
        }
        return liefereBefehl(leser.nextLine());
    }

    /**
     * Parst eine übergebene Eingabezeile zu einem {@link Befehl}.
     *
     * @param eingabezeile Vollständige Eingabezeile.
     * @return Der entsprechende {@link Befehl}.
     */
    public Befehl liefereBefehl(String eingabezeile)
    {
        String wort1 = null;
        String wort2 = null;

        if (eingabezeile == null) {
            return new LogBefehl(Befehlswort.UNKNOWN, null);
        }

        // Finde bis zu zwei Wörter in der Zeile
        Scanner zerleger = new Scanner(eingabezeile);
        if(zerleger.hasNext()) {
            wort1 = zerleger.next();     // erstes Wort lesen
            if (zerleger.hasNext()) {
                wort2 = zerleger.next();    // zweites Wort lesen
                // Hinweis: Wir ignorieren den Rest der Eingabezeile.
            }
        }
        zerleger.close();

        return new LogBefehl(befehle.gibBefehlswort(wort1), wort2);
    }

    /**
     * Gib eine Liste der bekannten Befehlswörter aus.
     */
    public void zeigeBefehle()
    {
        befehle.alleAusgeben();
    }
}

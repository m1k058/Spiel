import java.util.HashMap;
import java.util.Set;
import java.util.HashMap;

/**
 * Diese Klasse modelliert R�ume im Neualand.
 * 
 * Ein "Raum" repr�sentiert einen Ort in der virtuellen Landschaft des
 * Spiels. Ein Raum ist mit anderen R�umen �ber Ausg�nge verbunden.
 * F�r jeden existierenden Ausgang h�lt ein Raum eine Referenz auf 
 * den benachbarten Raum. Jeder Raum ist einer bestimmten Kategorie und
 * hat ensprechend bestimmte nutzbare Methoden
 * 
 * @author  Michal Kos und Cedric Wilke
 * @version 04.06.2025
 */

class Raum 
{
    private Raumkategorie kategorie;
    private HashMap<String, Raum> ausgaenge;        // die Ausg�nge dieses Raums
    private Inventar raumInventar;                  // Inventar f�r den Raum

    /**
     * Erzeuge einen Raum  einer Kategorie. Ein Raum
     * hat anfangs keine Ausg�nge und nichts im inventar.
     * @param kategorie des Raumes.
     */
    public Raum(Raumkategorie kategorie) 
    {
        this.kategorie = kategorie;
        ausgaenge = new HashMap<String, Raum>();
        raumInventar = new Inventar();
    }

    /**
     * Definiere einen Ausgang f�r diesen Raum.
     * @param richtung die Richtung, in der der Ausgang liegen soll
     * @param nachbar der Raum, der �ber diesen Ausgang erreicht wird
     */
    public void setzeAusgangRaum(String richtung, Raum nachbar) 
    {
        ausgaenge.put(richtung, nachbar);
    }

    /**
     * Liefere eine Zeichenkette, die die Ausg�nge dieses Raumes
     * beschreibt, beispielsweise
     * "Ausg�nge: north west".
     * @return eine Beschreibung der Ausg�nge dieses Raumes.
     */
    private String gibRaumAusgaengeAlsString()
    {
        String ergebnis = "Ausgaenge: ";
        Set<String> keys = ausgaenge.keySet();
        boolean ersterAusgang = true;
        for(String ausgang : keys){            
            if (ersterAusgang) {
            ergebnis += " " + ausgang;
            ersterAusgang = false;
            } else {
            ergebnis += ", " + ausgang;
            }
        }
        return ergebnis;
    }
    
    /**
     * Gibt zus�tzlich zu den Raumausg�ngen auch die Items im Raum aus
     * @return Eine Beschreibung der Ausg�nge und Items des Raumes.
     */
    public String gibRaumInfoString() {
        String info = gibRaumAusgaengeAlsString();
        info += "\n";
        if (raumInventar != null && !raumInventar.istLeer()) {         
            String inventarInfo = raumInventar.gibInventarAlsString();
            
            if (!raumInventar.istLeer()) {
                info += "Hier siehst du: \n" + inventarInfo;
            }
        }
        return info;
    }
    
    /**
     * Liefere den Raum, der in die angegebene Richtung liegt.
     * Liefere null, wenn in dieser Richtung kein Ausgang ist.
     * @param richtung Die Richtung des Ausgangs.
     * @return Den Raum in der angegebenen Richtung oder null.
     */
    public Raum gibAusgangRaum(String richtung) {
        return ausgaenge.get(richtung);
    }

    /**
     * Liefere eine Zeichenkette mit der Beschreibung
     * f�r disesn Raum
     *
     * @return    eine Bschreibung des Raumes
     */
    public String gibBeschreibung()
    {
        return kategorie.gibBeschreibung();
    }

    /**
     * Gibt die kategorie des Raums zur�ck
     *
     * @return    Raumkategorie kategorie
     */
    public Raumkategorie gibKategorie()
    {
        return kategorie;
    }

    /**
     * Gibt das Inventar dieses Raumes zur�ck.
     *
     * @return Das Inventar des Raumes.
     */
    public Inventar gibInventar() // Getter f�r das Inventar
    {
        return raumInventar;
    }

}

import java.util.HashMap;
import java.util.Set;


/**
 * Das ist eine Region. Sie besteht aus mehreren Raeumen. Es gibt immer einen Raum in der Region von
 * dem man in eine andere Region reisen kann.
 *
 * @author  Michal Kos und Cedric Wilke
 * @version 04.06.2025
 */
public class Region
{
    private String beschreibung;
    private HashMap<String, Region> ausgaenge;        // die Ausgaenge dieser Region
    private HashMap<String, Raum> raeume; 

    /**
     * Erzeuge eine Region mit einer Beschreibung. Eine Region
     * hat anfangs keine Ausgaenge und Raeume.
     *  
     * @param beschreibung enthaelt eine Beschreibung in der Form
     *        "in Hamburg" oder "an der Küste".
     */
    public Region(String beschreibung, HashMap<String, Raum> raeume) 
    {
        this.beschreibung = beschreibung;        
        this.raeume = raeume;
        ausgaenge = new HashMap<String, Region>();
    }

    /**
     * Gibt eine Referenz auf den Raum mit dem ensprechenden Namen
     * aus.
     *
     * @param  Name des Raumes
     * @return    Referenz auf den Raum
     */
    public Raum gibRaum(String raumName) {
        return raeume.get(raumName);
    }

    /**
     * Definiere einen Ausgang für diese Region.
     * @param richtung die Richtung, in der der Ausgang liegen soll
     * @param nachbar der Region, der über diesen Ausgang erreicht wird
     */
    public void setzeAusgangRegion(String richtung, Region nachbar)
    {
        ausgaenge.put(richtung, nachbar);
    }

    /**
     * Liefere eine Zeichenkette, die die Ausgänge dieser Region
     * die mit dem Zug errecihbar sind
     * @return eine Beschreibung der Ausgänge dieses Raumes.
     */
    public String gibRegionAusgaengeZugAlsString()
    {
        String ergebnis = "";
        Set<String> keys = ausgaenge.keySet();
        boolean ersterAusgang = true;
        for(String ausgang : keys){ 
            if(istZugAusgang(ausgang)){
                if (ersterAusgang) {
                    ergebnis += " " + ausgang;
                    ersterAusgang = false;
                } 
                else {
                    ergebnis += ", " + ausgang;
                }
            }
        }
        return ergebnis;
    }
    
    /**
     * Liefere eine Zeichenkette, die die Ausgänge dieser Region
     * die mit dem Auto errecihbar sind
     * @return eine Beschreibung der Ausgänge dieses Raumes.
     */
    public String gibRegionAusgaengeAutoAlsString()
    {
        String ergebnis = "";
        Set<String> keys = ausgaenge.keySet();
        boolean ersterAusgang = true;
        for(String ausgang : keys){ 
            if(istAutoAusgang(ausgang)){
                if (ersterAusgang) {
                    ergebnis += " " + ausgang;
                    ersterAusgang = false;
                } 
                else {
                    ergebnis += ", " + ausgang;
                }
            }
        }
        return ergebnis;
    }

    /**
     * Prüft, ob die angegebene Zielregion
     * über einen Bahnhof verfügt und somit per Zug erreichbar ist.
     *
     * @param zielRegionName Der Name des Ausgangs zur Zielregion.
     * @return true, wenn die Zielregion über diesen Ausgang per Zug erreichbar ist.
     */
    public boolean istZugAusgang(String zielRegionName) 
    {
        Region zielRegion = ausgaenge.get(zielRegionName); // verbundene Region
        if (zielRegion != null) {
            Raum ankunftsBahnhof = zielRegion.gibRaum("Bahnhof"); // Sucht Raum "Bahnhof" in der Region
            return ankunftsBahnhof != null && ankunftsBahnhof.gibKategorie() == Raumkategorie.BAHNHOF;
        }
        return false;
    }
    
    /**
     * Prüft, ob die angegebene Zielregion
     * über eine Autobahn verfügt und somit per Auto erreichbar ist.
     *
     * @param zielRegionName Der Name des Ausgangs zur Zielregion.
     * @return true, wenn die Zielregion über diesen Ausgang per Auto erreichbar ist.
     */
    public boolean istAutoAusgang(String zielRegionName) {
        Region zielRegion = ausgaenge.get(zielRegionName); // verbundene Region
        if (zielRegion != null) {
            Raum ankunftsAutobahn = zielRegion.gibRaum("Autobahn"); // Sucht Raum "Autobahn" in der Region
            return ankunftsAutobahn != null && ankunftsAutobahn.gibKategorie() == Raumkategorie.AUTOBAHN;
        }
        return false;
    }
    
     /**
     * Liefere die Region, die wir erreichen, wenn wir aus dieser Region
     * in die angegebene Richtung gehen. Liefere 'null', wenn in
     * dieser Richtung kein Ausgang ist.
     * @param richtung die Richtung, in die gegangen werden soll.
     * @return die Region in der angegebenen Richtung oder null.
     */
    public Region gibAusgangRegion(String richtung) {
        return ausgaenge.get(richtung);
    }

    /**
     * Gibt die HashMap aller Räume in dieser Region zurück
     * @return Eine HashMap, die Raum-Objekte enthält
     */
    public HashMap<String, Raum> gibRaeumeMap() {
        return this.raeume;
    }
    
    /**
     * @return die kurze Beschreibung dieser Region (die dem Konstruktor
     * uebergeben wurde).
     */
    public String gibBeschreibung()
    {
        return beschreibung;
    }
}

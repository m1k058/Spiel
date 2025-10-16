/**
 * Repraesentationen fuer alle Items im Inventar,
 * zusammen mit einer Zeichenkette f�r den Namen und eine Beschreibung.
 * 
 * @author  Michal Kos
 * @version 04.06.2025
 */

public enum ItemTyp {
    BAUGENEHMIGUNG("Baugenehmigung", "Dieses St�ck Papier darf nur per Fax versendet werden"),
    MUENZE("M�nze", "Ein Pl�tchen aus einer Nickel-Messing-Legierung.");

    private final String anzeigename;
    private final String beschreibung;
    
    /**
     * Initialisieren mit der entsprechenden  Namen und 
     * einer Beschreibung.
     * @param name des Items als Zeichenkette und 
     * Beschreibung als Zeichenkette.
     */
    ItemTyp(String anzeigename, String beschreibung) {
        this.anzeigename = anzeigename;
        this.beschreibung = beschreibung;
    }

    /**
     * Gibt den Namen des Items aus    
     * @return    Name des Items
     */
    public String getAnzeigename() {
        return anzeigename;
    }

    /**
     * Gibt die Beschreibung des Items aus
     * @return    Beschreibung des Items
     */
    public String getBeschreibung() {
        return beschreibung;
    }
}
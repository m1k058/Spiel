import java.util.HashMap;
import java.util.Map;

/**
 * Verwaltet eine Sammlung von Gegenständen (Items) für Entitäten im Spiel,
 * wie Spieler oder Räume. Dieses Inventar zählt die Vorkommen jedes ItemTyps.
 * Es ermöglicht das Hinzufügen, Entfernen und Abfragen von Items und deren Anzahl,
 * sowie das Verschieben von Items zwischen Inventaren.
 *
 * @author  Michal Kos
 * @version 04.06.2025
 */

public class Inventar {
    
    private Map<ItemTyp, Integer> itemAnzahl;

    /**
     * Erzeugt ein neues, leeres Inventar.
     */
    public Inventar() {
        this.itemAnzahl = new HashMap<>();
    }

    /**
     * Fügt einen Gegenstand des angegebenen Typs zum Inventar hinzu.
     * Wenn der ItemTyp bereits im Inventar vorhanden ist, wird dessen Anzahl um 1 erhöht.
     * Andernfalls wird der ItemTyp mit der Anzahl 1 neu hinzugefügt.
     *
     * @param typName Der Name des hinzuzufügenden ItemTyps (z.B. "Münze").
     * Die Methode vernachlässigt Groß-/Kleinschreibung.
     */
    public void addItem(String typName) {
        ItemTyp item = findeItemTyp(typName);
        if (item == null) {
            return;  // Typ nicht gefunden oder typName war null, keine Aktion
        }
        // Erhöhe die Anzahl des Items oder füge es neu hinzu.
        if (this.itemAnzahl.containsKey(item)) {
            int aktuelleAnzahl = this.itemAnzahl.get(item);
            this.itemAnzahl.put(item, aktuelleAnzahl + 1);
        } else {
            this.itemAnzahl.put(item, 1);
        }
    }
    
    /**
     * Fügt eine bestimmte Anzahl von Gegenständen eines ItemTyps zum Inventar hinzu.
     * Wenn der ItemTyp bereits im Inventar vorhanden ist, wird dessen Anzahl um die
     * angegebene Menge erhöht. Andernfalls wird der ItemTyp mit der angegebenen Anzahl neu hinzugefügt.
     *
     * @param typName Der Name des hinzuzufügenden ItemTyps.
     * @param anzahl Die Anzahl der Items, die hinzugefügt werden sollen. Muss positiv sein
     */
    public void addItemAnzahl(String typName, int anzahl) {
        ItemTyp item = findeItemTyp(typName);
        if (item == null || anzahl <= 0) {
            return; // Typ nicht gefunden oder typName war null, keine Aktion.
        }
        // Erhöhe die Anzahl des Items um 'anzahl' oder füge es neu hinzu.
        if (this.itemAnzahl.containsKey(item)) {
            int aktuelleAnzahl = this.itemAnzahl.get(item);
            this.itemAnzahl.put(item, aktuelleAnzahl + anzahl);
        } else {
            this.itemAnzahl.put(item, anzahl);
        }
    }

    /**
     * Entfernt ein Exemplar eines ItemTyps aus dem Inventar.
     * Wenn der angegebene ItemTyp mehrfach vorhanden war, wird seine Anzahl dekrementiert.
     * Ist es das letzte Exemplar dieses Typs, wird der ItemTyp komplett aus dem Inventar entfernt.
     *
     * @param typName Der Name des zu entfernenden ItemTyps.
     * Wenn der `typName` null ist oder keinem bekannten ItemTyp entspricht,
     * oder das Item nicht im Inventar ist, wird `false` zurückgegeben.
     * @return `true`, wenn ein Exemplar des Typs erfolgreich entfernt wurde;
     */
    public boolean removeItem(String typName) {
        ItemTyp item = findeItemTyp(typName);
        if (item == null || !this.itemAnzahl.containsKey(item)) {
            return false; // Item nicht vorhanden oder Typ ungültig
        }

        int aktuelleAnzahl = this.itemAnzahl.get(item);
        if (aktuelleAnzahl > 1) {
            this.itemAnzahl.put(item, aktuelleAnzahl - 1);
        } else {
            this.itemAnzahl.remove(item); // Letztes Exemplar, entferne den Eintrag komplett
        }
        return true;
    }
    
    /**
     * Entfernt eine bestimmte Anzahl von Exemplaren eines ItemTyps aus dem Inventar.
     * Wenn nach dem Entfernen die Anzahl des ItemTyps auf 0 oder weniger fallen würde,
     * wird der ItemTyp komplett aus dem Inventar entfernt.
     *
     * @param typName Der Name des ItemTyps, von dem Exemplare entfernt werden sollen.
     * @param anzahl  Die Anzahl der zu entfernenden Exemplare.
     * Wenn das Item nicht in ausreichender
     * Menge vorhanden ist, wird `false` zurückgegeben und das Inventar
     * nicht verändert.
     * @return `true`, wenn die angegebene Anzahl von Exemplaren erfolgreich entfernt wurde,
     * `false` sonst.
     */
    public boolean removeItemAnzahl(String typName, int anzahl) {
        ItemTyp item = findeItemTyp(typName);
        if (item == null || !this.itemAnzahl.containsKey(item)) {
            return false;
        }

        int aktuelleAnzahl = this.itemAnzahl.get(item);
        if (aktuelleAnzahl > anzahl) {
            this.itemAnzahl.put(item, aktuelleAnzahl - anzahl);
        } else {
            this.itemAnzahl.remove(item);
        }
        return true;
    }

    /**
     * Gibt die Anzahl der Items eines bestimmten ItemTyps im Inventar zurück.
     *
     * @param typName Der Name des ItemTyps, dessen Anzahl ermittelt werden soll.
     * @return Die Anzahl der Items dieses Typs im Inventar. Gibt 0 zurück,
     * wenn der ItemTyp nicht im Inventar vorhanden ist oder `typName` ungültig ist.
     */
     public int gibAnzahlItems(String typName) {
         ItemTyp item = findeItemTyp(typName);
         if (item == null) {
             return 0;
         }

         if (this.itemAnzahl.containsKey(item)) {
             return this.itemAnzahl.get(item);
         } else {
              return 0;
         }
    }
    
    /**
     * Prüft, ob das Inventar leer ist.
     * @return `true`, wenn das Inventar leer ist, sonst `false`.
     */
    public boolean istLeer(){
        return itemAnzahl.isEmpty();
    }

    /**
     * Erzeugt eine Zeichenkette, die den Inhalt des Inventars darstellt.
     * Jeder ItemTyp wird mit seiner Anzahl und Beschreibung aufgelistet.
     * Beispiel: "- Baugenehmigung (2)  [Dieses Stück Papier darf nur per Fax versendet werden]"     *
     * @return Eine Zeichenkette, die den Inhalt des Inventars beschreibt.
     * Gibt `null` zurück, wenn das Inventar leer ist.
     */
    public String gibInventarAlsString() {
        if (itemAnzahl.isEmpty()) {
            return null;
        }

        String inventar = "";
        for (Map.Entry<ItemTyp, Integer> entry : itemAnzahl.entrySet()) {
            ItemTyp typ = entry.getKey();
            int anzahl = entry.getValue();

            inventar += "- " + typ.getAnzeigename();
            if (anzahl > 1) {
                inventar += " (" + anzahl + ")";
            }
            inventar += " \t[" + typ.getBeschreibung() + "]\n";
        }
        return inventar;
    }

    /**
     * Verschiebt ein Item eines bestimmten ItemTyps von einem Quellinventar
     * in ein Zielinventar.
     * @param typName     Der Name des ItemTyps des zu verschiebenden Items.
     * @param vonInventar Das Inventar, aus dem das Item entfernt werden soll.
     * @param zuInventar  Das Inventar, zu dem das Item hinzugefügt werden soll.
     * @return `true`, wenn das Item erfolgreich verschoben wurde, sonst `false`.
     */
    public static boolean bewegeItem(String typName, Inventar vonInventar, Inventar zuInventar) {
         ItemTyp item = findeItemTyp(typName);
        if (item == null || vonInventar == null || zuInventar == null) {
            return false;
        }
        // Versuche, das Item aus dem Quellinventar zu entfernen.
        if (vonInventar.removeItem(typName)) {
            // Wenn erfolgreich, füge es dem Zielinventar hinzu.
            zuInventar.addItem(typName);
            return true;
        }
        // Item war nicht im Quellinventar oder konnte nicht entfernt werden.
        return false;
    }

    /**
     * Findet einen {@link ItemTyp} basierend auf dem übergebenen Anzeigenamen.
     * Die Suche ignoriert Groß- und Kleinschreibung.
     *
     * @param name Der Anzeigename des gesuchten ItemTyps (z.B. "Münze").
     * @return Der gefundene ItemTyp oder `null`, wenn kein passender Typ existiert
     * oder der `name` null ist.
     */
    public static ItemTyp findeItemTyp(String name) {
        if (name == null) return null;
        for (ItemTyp typ : ItemTyp.values()) {
            if (typ.getAnzeigename().equalsIgnoreCase(name)) {
                return typ;
            }
        }
        return null;
    }
}
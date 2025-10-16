import java.util.HashMap;
import java.util.Map;

/**
 * Verwaltet eine Sammlung von Gegenst�nden (Items) f�r Entit�ten im Spiel,
 * wie Spieler oder R�ume. Dieses Inventar z�hlt die Vorkommen jedes ItemTyps.
 * Es erm�glicht das Hinzuf�gen, Entfernen und Abfragen von Items und deren Anzahl,
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
     * F�gt einen Gegenstand des angegebenen Typs zum Inventar hinzu.
     * Wenn der ItemTyp bereits im Inventar vorhanden ist, wird dessen Anzahl um 1 erh�ht.
     * Andernfalls wird der ItemTyp mit der Anzahl 1 neu hinzugef�gt.
     *
     * @param typName Der Name des hinzuzuf�genden ItemTyps (z.B. "M�nze").
     * Die Methode vernachl�ssigt Gro�-/Kleinschreibung.
     */
    public void addItem(String typName) {
        ItemTyp item = findeItemTyp(typName);
        if (item == null) {
            return;  // Typ nicht gefunden oder typName war null, keine Aktion
        }
        // Erh�he die Anzahl des Items oder f�ge es neu hinzu.
        if (this.itemAnzahl.containsKey(item)) {
            int aktuelleAnzahl = this.itemAnzahl.get(item);
            this.itemAnzahl.put(item, aktuelleAnzahl + 1);
        } else {
            this.itemAnzahl.put(item, 1);
        }
    }
    
    /**
     * F�gt eine bestimmte Anzahl von Gegenst�nden eines ItemTyps zum Inventar hinzu.
     * Wenn der ItemTyp bereits im Inventar vorhanden ist, wird dessen Anzahl um die
     * angegebene Menge erh�ht. Andernfalls wird der ItemTyp mit der angegebenen Anzahl neu hinzugef�gt.
     *
     * @param typName Der Name des hinzuzuf�genden ItemTyps.
     * @param anzahl Die Anzahl der Items, die hinzugef�gt werden sollen. Muss positiv sein
     */
    public void addItemAnzahl(String typName, int anzahl) {
        ItemTyp item = findeItemTyp(typName);
        if (item == null || anzahl <= 0) {
            return; // Typ nicht gefunden oder typName war null, keine Aktion.
        }
        // Erh�he die Anzahl des Items um 'anzahl' oder f�ge es neu hinzu.
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
     * oder das Item nicht im Inventar ist, wird `false` zur�ckgegeben.
     * @return `true`, wenn ein Exemplar des Typs erfolgreich entfernt wurde;
     */
    public boolean removeItem(String typName) {
        ItemTyp item = findeItemTyp(typName);
        if (item == null || !this.itemAnzahl.containsKey(item)) {
            return false; // Item nicht vorhanden oder Typ ung�ltig
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
     * Wenn nach dem Entfernen die Anzahl des ItemTyps auf 0 oder weniger fallen w�rde,
     * wird der ItemTyp komplett aus dem Inventar entfernt.
     *
     * @param typName Der Name des ItemTyps, von dem Exemplare entfernt werden sollen.
     * @param anzahl  Die Anzahl der zu entfernenden Exemplare.
     * Wenn das Item nicht in ausreichender
     * Menge vorhanden ist, wird `false` zur�ckgegeben und das Inventar
     * nicht ver�ndert.
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
     * Gibt die Anzahl der Items eines bestimmten ItemTyps im Inventar zur�ck.
     *
     * @param typName Der Name des ItemTyps, dessen Anzahl ermittelt werden soll.
     * @return Die Anzahl der Items dieses Typs im Inventar. Gibt 0 zur�ck,
     * wenn der ItemTyp nicht im Inventar vorhanden ist oder `typName` ung�ltig ist.
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
     * Pr�ft, ob das Inventar leer ist.
     * @return `true`, wenn das Inventar leer ist, sonst `false`.
     */
    public boolean istLeer(){
        return itemAnzahl.isEmpty();
    }

    /**
     * Erzeugt eine Zeichenkette, die den Inhalt des Inventars darstellt.
     * Jeder ItemTyp wird mit seiner Anzahl und Beschreibung aufgelistet.
     * Beispiel: "- Baugenehmigung (2)  [Dieses St�ck Papier darf nur per Fax versendet werden]"     *
     * @return Eine Zeichenkette, die den Inhalt des Inventars beschreibt.
     * Gibt `null` zur�ck, wenn das Inventar leer ist.
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
     * @param zuInventar  Das Inventar, zu dem das Item hinzugef�gt werden soll.
     * @return `true`, wenn das Item erfolgreich verschoben wurde, sonst `false`.
     */
    public static boolean bewegeItem(String typName, Inventar vonInventar, Inventar zuInventar) {
         ItemTyp item = findeItemTyp(typName);
        if (item == null || vonInventar == null || zuInventar == null) {
            return false;
        }
        // Versuche, das Item aus dem Quellinventar zu entfernen.
        if (vonInventar.removeItem(typName)) {
            // Wenn erfolgreich, f�ge es dem Zielinventar hinzu.
            zuInventar.addItem(typName);
            return true;
        }
        // Item war nicht im Quellinventar oder konnte nicht entfernt werden.
        return false;
    }

    /**
     * Findet einen {@link ItemTyp} basierend auf dem �bergebenen Anzeigenamen.
     * Die Suche ignoriert Gro�- und Kleinschreibung.
     *
     * @param name Der Anzeigename des gesuchten ItemTyps (z.B. "M�nze").
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
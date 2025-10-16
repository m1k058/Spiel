
/**
 * Repräsentiert einen Spieler im Spiel "Rette das Neuland".
 * Ein Spieler verfügt über ein Inventar, in dem Gegenstände und auch
 * die aktuelle Geldmenge des Spielers verwaltet werden.
 * @author Cedric Wilke, Michal Kos 
 * @version 04.06.2025
 */
public class Spieler
{
    private Inventar spielerInventar;

    /**
     * Erzeugt einen neuen Spieler und initialisiert sein Inventar.
     * Der Spieler startet mit einem festgelegten Geldbetrag.
     * * @param startGeld Der Geldbetrag, mit dem der Spieler das Spiel beginnt.
     * Dieser Betrag wird als "Münze"-Items im Inventar gespeichert.
     */
    public Spieler(int startGeld)
    {
        spielerInventar = new Inventar();
        spielerInventar.addItemAnzahl("Münze", startGeld);
    }

    /**
     * Gibt die aktuelle Geldmenge des Spielers zurück.
     * * @return Die Anzahl der "Münze"-Items im Inventar des Spielers,
     * was dem Geldbetrag entspricht.
     */
    public int gibGeld()
    {
        return spielerInventar.gibAnzahlItems("Münze");
    }
    
    /**
     * Gibt das Inventar des Spielers zurück.
     * Das Inventar enthält alle Gegenstände und die Münzen des Spielers.
     *
     * @return Das Inventar-Objekt des Spielers.
     */
    public Inventar gibSpielerInventar() { 
        return spielerInventar;
    }
    
    /**
     * Ändert die Geldmenge des Spielers um den angegebenen Betrag.
     * Eine positive Änderung erhöht das Geld, eine negative verringert es.
     * * @param aenderung Der Betrag, um den das Geld geändert werden soll.
     * Kann positiv (Geld erhalten) oder negativ (Geld ausgeben) sein.
     */
    public void aendereGeld(int aenderung)
    {
        if(aenderung>0){
            spielerInventar.addItemAnzahl("Münze", aenderung);
        }
        else if (aenderung<0){
            spielerInventar.removeItemAnzahl("Münze", -aenderung);
        }
    }
}

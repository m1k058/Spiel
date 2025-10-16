
/**
 * Repr�sentiert einen Spieler im Spiel "Rette das Neuland".
 * Ein Spieler verf�gt �ber ein Inventar, in dem Gegenst�nde und auch
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
     * Dieser Betrag wird als "M�nze"-Items im Inventar gespeichert.
     */
    public Spieler(int startGeld)
    {
        spielerInventar = new Inventar();
        spielerInventar.addItemAnzahl("M�nze", startGeld);
    }

    /**
     * Gibt die aktuelle Geldmenge des Spielers zur�ck.
     * * @return Die Anzahl der "M�nze"-Items im Inventar des Spielers,
     * was dem Geldbetrag entspricht.
     */
    public int gibGeld()
    {
        return spielerInventar.gibAnzahlItems("M�nze");
    }
    
    /**
     * Gibt das Inventar des Spielers zur�ck.
     * Das Inventar enth�lt alle Gegenst�nde und die M�nzen des Spielers.
     *
     * @return Das Inventar-Objekt des Spielers.
     */
    public Inventar gibSpielerInventar() { 
        return spielerInventar;
    }
    
    /**
     * �ndert die Geldmenge des Spielers um den angegebenen Betrag.
     * Eine positive �nderung erh�ht das Geld, eine negative verringert es.
     * * @param aenderung Der Betrag, um den das Geld ge�ndert werden soll.
     * Kann positiv (Geld erhalten) oder negativ (Geld ausgeben) sein.
     */
    public void aendereGeld(int aenderung)
    {
        if(aenderung>0){
            spielerInventar.addItemAnzahl("M�nze", aenderung);
        }
        else if (aenderung<0){
            spielerInventar.removeItemAnzahl("M�nze", -aenderung);
        }
    }
}

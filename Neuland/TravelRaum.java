
/**
 * Die Klasse TravelRaum erweitert die Klasse Raum um
 * die möglichkeit aus der Region sich rauszubewegen. 
 * Man kann 
 *
 * @author Michal Kos
 * @version 23.05.2025
 */
public class TravelRaum extends Raum
{
    private boolean zug;
    private boolean auto;
    /**
     * Constructor for objects of class TravelRaum
     */
    public TravelRaum(Raumkategorie kategorie)
    {
        super(kategorie);
        auto = true;
        zug = true;
    }

    /**
     * Gibt aus ob der Raum erlaubt Zug zu fahren
     *
     * @return    true oder false 
     */
    public boolean zugErlaubt()
    {
        return zug;
    }

    /**
     * Gibt aus ob der Raum erlaubt Auto zu fahren
     *
     * @return    true oder false 
     */
    public boolean autoErlaubt()
    {
        return auto;
    }

    /**
     * ändere die Erlaubniss Zug zu fahren
     *
     * @param  true oder false
     */
    public void erlaubnissZug(boolean erlaubniss)
    {
        zug = erlaubniss;
    }
    
    /**
     * ändere die Erlaubniss Auto zu fahren
     *
     * @param  true oder false
     */
    public void erlaubnissAuto(boolean erlaubniss)
    {
        auto = erlaubniss;
    }
}

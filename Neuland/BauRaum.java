
/**
 * Die Klasse BauRaum erweitert die Klasse Raum um
 * die möglichkeit hier Wind- und Solaranlagen zu bauen.
 * Die Maximale bauzahl ist im enum SRamkategorie festgelegt. 
 *
 * @Michal Kos
 * @ 23.05.2025
 */
public class BauRaum extends Raum
{
    private int solar;      // Anzahl der gebauten Solaranlagen
    private int wind;       // Anzahl der gebauten Windanlagen

    /**
     * Konstruktor für Objekte der Klasse BauRaum
     */
    public BauRaum(Raumkategorie kategorie)
    {
        super(kategorie);
        solar = 0;
        wind = 0;
    }

    /**
     * Versucht eine Solaranlage zu bauen, falls es erfolgreich
     * war wird true zurückgegeben und falls nicht false
     *
     * @return    true wenn erfolgreich und false wenn nicht 
     */
    public boolean bauSolar()
    {
        if(wind <= 0 && solar < gibKategorie().maxSolar()){
            solar +=1;
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Versucht eine Windanlage zu bauen, falls es erfolgreich
     * war wird true zurückgegeben und falls nicht false
     *
     * @return    true wenn erfolgreich und false wenn nicht 
     */
    public boolean bauWind()
    {
        if(solar <= 0 && wind < gibKategorie().maxWind()){
            wind +=1;
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Gibt die Anzahl der gebauten Wind oder Solaranlagen
     * als String aus.
     *
     * @return   string z.B. "Windanlagen: 2   Freie Plätze: 0"
     */
    public String gibAnlagenString()
    {
        String rueckgabe = "Gebaute Anlagen: ";
        if(wind > 0){
            rueckgabe += "Windanlage(n): " + wind + "\tFreie Plätze: " + (gibKategorie().maxWind() - wind);
        }
        else if(solar > 0){
            rueckgabe += "Solaranlage(n): " + solar + "\tFreie Plätze: " + (gibKategorie().maxSolar() - solar);
        }
        else{
            rueckgabe += "noch keine\tFreie Plätze: " + gibKategorie().maxSolar() + " Solaranlage(n) oder " + gibKategorie().maxWind() + " Windanlage(n)";
        }
        return rueckgabe;
    }
    
    /**
     * Gibt die Anzahl der gebauten Windanlagen als int
     *
     * @return   int 2
     */
    public int gibAnlagenWind()
    {
        return wind;
    }
    
    /**
     * Gibt die Anzahl der gebauten Solaranlagen als int
     *
     * @return   int 2
     */
    public int gibAnlagenSolar()
    {
        return solar;
    }
}

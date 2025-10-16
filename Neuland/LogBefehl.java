import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Write a description of class LogBefehl here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class LogBefehl extends Befehl
{
    private final String LogDatei = "befehle.log";
    private String befehlswort;
    private String zweitesWort;
    private static boolean add = false;
    
    /**
     * Constructor for objects of class LogBefehl
     */
    public LogBefehl(Befehlswort befehlswort, String zweitesWort)
    {
        super(befehlswort, zweitesWort);
        this.befehlswort = befehlswort.toString();
        this.zweitesWort = zweitesWort;
        inDateiSpeichern();
    }

    private void inDateiSpeichern()
    {
        if(zweitesWort == null)
        {
            zweitesWort = "";
        }
        String inhalt = befehlswort + " " + zweitesWort;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LogDatei, add))) {
            writer.write(inhalt);
            writer.newLine();
            add = true;
        } catch (IOException e) {
            e.printStackTrace();
        
        }
    }
}

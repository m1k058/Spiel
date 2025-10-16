import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.util.HashMap;

/**
 * Dies ist die Hauptklasse der Anwendung "Rette das Neuland".
 * "Rette das Neuland" ist ein sehr einfaches, textbasiertes
 * Strategie-Game. Ein Spieler kann sich in einer Umgebung bewegen,
 * Gegenstände aufheben und Anlagen bauen, um das fiktive Neuland zu retten.
 * * Zum Spielen muss eine Instanz dieser Klasse erzeugt werden und
 * an ihr die Methode "spielen" aufgerufen werden.
 * * Diese Instanz dieser Klasse erzeugt und initialisiert alle
 * anderen Objekte der Anwendung: Sie legt alle Räume und Regionen
 * über die Spielumgebung-Klasse an, erzeugt einen Parser
 * sowie einen Spieler und startet das Spiel. Sie wertet auch die Befehle
 * aus, die der Parser liefert, und sorgt für ihre Ausführung.
 * 
 * @author  Michael Kölling, David J. Barnes, Michal Kos und Cedric Wilke
 * @version 05.06.2025
 */

class Spiel implements ISpiel
{
    private Parser parser;  // Verantwortlich für das Einlesen und Interpretieren von Benutzereingaben.
    private Spieler spieler;    // Das Spieler-Objekt, das das Inventar und Geld des Spielers verwaltet.

    private Spielumgebung spielumgebung;    // Hält die gesamte Spielwelt, geladen aus einer JSON-Datei.
    private Region aktuelleRegion;  // Die Region, in der sich der Spieler aktuell befindet.
    private Raum aktuellerRaum; // Der genaue Raum, in dem sich der Spieler aktuell befindet.

    private int gesamtSolaranlagen; // Gesamtzahl der gebauten Solaranlagen im Spiel.
    private int gesamtWindanlagen;  // Gesamtzahl der gebauten Windanlagen im Spiel.
    private int einkommenMultiplikator; // Faktor, der das Einkommen pro Reise bestimmt. Erhöht sich durch den Bau von Anlagen.
    private int punkte; // Aktuelle Punktzahl des Spielers.
    private int zugfahrtenCount;    // Zählt die Anzahl der Reisen, um eine Spielzeitbegrenzung zu implementieren.
    
    // GUI-Referenz, um Bild nach Raumwechsel zu aktualisieren
    private SpielGUI gui;

    private boolean gestartet;
    private boolean beendet;

     /**
     * Erzeugt ein neues Spiel und initialisiert die interne Raumkarte sowie den Spieler.
     * Die Spielwelt wird aus der Datei 'map_v2.json' geladen.
     */
    public Spiel() 
    {
        
        spieler = new Spieler(5);
        parser = new Parser();
        try {
            spielumgebung = new Spielumgebung("map_v2.json");
        } catch (Exception e) {
            System.err.println("Kritischer Fehler beim Laden der Spielumgebung: " + e.getMessage());
            System.err.println("Das Spiel kann nicht gestartet werden. Bitte überprüfe die Datei 'map_v2.json'.");
        }
        // Setzt die Startposition des Spielers.
        aktuelleRegion = spielumgebung.gibRegion("Westseekueste");
        aktuellerRaum = aktuelleRegion.gibRaum("Offshore1");
        
        // Initialisiert die Spielstandvariablen.
        einkommenMultiplikator = 1;
        gesamtSolaranlagen = 0;
        gesamtWindanlagen = 0;
        punkte = 0;
        zugfahrtenCount = 0;

        gestartet = false;
        beendet = false;
    }

    public Region getAktuelleRegion() {
        return aktuelleRegion;
    }
    
    private void setGui(SpielGUI gui) {
        this.gui = gui;
        if (this.gui != null && aktuellerRaum != null) {
            this.gui.aktualisiereBild(aktuellerRaum);
        }
    }

    /**
     * Die Hauptmethode des Spiels. Führt eine Endlosschleife aus,
     * die auf Benutzereingaben wartet, diese verarbeitet und den Spielzustand
     * aktualisiert, bis eine Endbedingung erreicht ist.
     */
    public void spielen()
    {
        // Initialisierung der GUI
        SpielGUI gui = new SpielGUI();
        setGui(gui);

        try {
            System.out.print(start());
            while (!istBeendet()) {
                Befehl befehl = parser.liefereBefehl();
                String antwort = verarbeiteBefehlIntern(befehl);
                System.out.print(antwort);
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized String start() throws RemoteException
    {
        return sammleAusgabe(() -> {
            if (beendet) {
                System.out.println("Das Spiel ist bereits beendet. Bitte starten Sie den Server neu, um erneut zu spielen.");
                return;
            }

            if (!gestartet) {
                gestartet = true;
                willkommenstextAusgeben();
            } else {
                raumInfoAusgeben();
            }
        });
    }

    @Override
    public synchronized String verarbeiteBefehl(String eingabe) throws RemoteException
    {
        Befehl befehl = parser.liefereBefehl(eingabe);
        return verarbeiteBefehlIntern(befehl);
    }

    private synchronized String verarbeiteBefehlIntern(Befehl befehl)
    {
        return sammleAusgabe(() -> {
            if (!gestartet) {
                System.out.println("Spiel wurde noch nicht gestartet. Bitte zuerst start() aufrufen.");
                return;
            }

            if (beendet) {
                System.out.println("Das Spiel ist bereits beendet.");
                return;
            }

            boolean moechteBeenden = verarbeiteBefehl(befehl);
            if(!moechteBeenden) {
                moechteBeenden = testGewinn();
            }
            if(moechteBeenden) {
                beendet = true;
                System.out.println("Danke fuer dieses Spiel. Auf Wiedersehen.");
            }
        });
    }

    @Override
    public synchronized boolean istBeendet() throws RemoteException
    {
        return beendet;
    }

    private String sammleAusgabe(Runnable aktion)
    {
        StringBuilder ausgabe = new StringBuilder();
        PrintStream originalOut = System.out;
        PrintStream originalErr = System.err;
        PrintStream sammler = new PrintStream(new StringBuilderOutputStream(ausgabe), true);
        try {
            System.setOut(sammler);
            System.setErr(sammler);
            aktion.run();
        } finally {
            sammler.flush();
            System.setOut(originalOut);
            System.setErr(originalErr);
            sammler.close();
        }
        return ausgabe.toString();
    }

    private static class StringBuilderOutputStream extends OutputStream
    {
        private final StringBuilder builder;

        private StringBuilderOutputStream(StringBuilder builder)
        {
            this.builder = builder;
        }

        @Override
        public void write(int b)
        {
            builder.append((char) b);
        }

        @Override
        public void write(byte[] b, int off, int len)
        {
            builder.append(new String(b, off, len, StandardCharsets.UTF_8));
        }
    }

    /**
     * Prüft, ob die Gewinn- oder Verlustbedingungen des Spiels erfüllt sind.
     * Gewinn: 100 oder mehr Punkte.
     * Verlust: 50 oder mehr Reisen unternommen.
     *
     * @return `true`, wenn das Spiel beendet werden soll, andernfalls `false`.
     */
    private boolean testGewinn()
    {
        if(punkte>=100){
            System.out.println("\n\nSIE HABEN " + punkte + " PUNKTE ERREICHT!!!\nSie haben Neuland gerettet und das Spiel gewonnen.");
            return true;
        }
        else if (zugfahrtenCount >= 50){
            System.out.println("\n\nSie haben leider zu lange gebraucht. Die Zuege fahren nicht mehr. Sie verlieren.");
            return true;
        }
        else {
            return false;
        }
    }

    
    /**
     * Gibt eine Willkommensnachricht und die anfänglichen Spielinformationen aus.
     */
    private void willkommenstextAusgeben()
    {
        System.out.println();
        System.out.println("Willkommen im Neuland Alpha!");
        System.out.println("Das Neuland ist ein weit unterentwickeltes Land und benoetigt dringend deine Hilfe!");
        System.out.println("Nur du kannst das Land noch retten!!!");
        System.out.println("Tippen sie '" + Befehlswort.HELP + "', wenn Sie Hilfe brauchen.");
        System.out.println();
        raumInfoAusgeben();
    }

     /**
     * Gibt alle relevanten Informationen über den aktuellen Standort des Spielers aus.
     * Dazu gehören der aktuelle Spielstand (Geld, Punkte), das Inventar, die Beschreibung
     * des Raumes und der Region sowie mögliche Ausgänge.
     */
    private void raumInfoAusgeben() {
        System.out.println();
        System.out.println("------------------------------------------------------------------------------------------");
        // Zeigt Spielstand-Informationen an.
        System.out.print("\t\t\tGeld/Fahrt: " + einkommenMultiplikator + "  |  ");
        System.out.println("Punkte: " + punkte);
        // Zeigt das Inventar des Spielers an.
        System.out.println("Inventar:");
        System.out.println(spieler.gibSpielerInventar().gibInventarAlsString());
        // Gibt die Beschreibung des aktuellen Ortes aus.
        System.out.println("Du befindest dich " + aktuellerRaum.gibBeschreibung() + 
            " in der Region '" + aktuelleRegion.gibBeschreibung() + "'.");

        // Zeige Regionsausgänge nur, wenn der Raum ein TravelRaum ist
        if (aktuellerRaum instanceof TravelRaum) {
            if (aktuellerRaum.gibKategorie() == Raumkategorie.BAHNHOF) { //
                System.out.println("\nVon dieser Region '" + aktuelleRegion.gibBeschreibung() +
                    "' kannst du reisen nach:" + aktuelleRegion.gibRegionAusgaengeZugAlsString());
            }
            else{
                System.out.println("\nVon dieser Region '" + aktuelleRegion.gibBeschreibung() +
                    "' kannst du reisen nach:" + aktuelleRegion.gibRegionAusgaengeAutoAlsString());
            }
        }
        // Wenn aktuellerRaum ein BauRaum ist, zeige gebaute Anlagen
        if (aktuellerRaum instanceof BauRaum) {
            BauRaum bauRaum = (BauRaum) aktuellerRaum;
            System.out.println(bauRaum.gibAnlagenString());
        } 
        // Zeigt die Ausgänge zu benachbarten Räumen innerhalb der aktuellen Region.
        System.out.print(aktuellerRaum.gibRaumInfoString());               
        System.out.println("------------------------------------------------------------------------------------------");
    }

    /**
     * Verarbeitet einen vom Spieler eingegebenen Befehl.
     * @param befehl Der zu verarbeitende Befehl.
     * @return `true`, wenn der Befehl das Spiel beendet, ansonsten `false`.
     */
    public boolean verarbeiteBefehl(Befehl befehl)
    {
        boolean moechteBeenden = false;

        Befehlswort befehlswort = befehl.gibBefehlswort();

        switch(befehlswort) { 
            case UNKNOWN:
                System.out.println("Ich weiss nicht, was Sie meinen...");
                break;

            case HELP:
                hilfstextAusgeben();
                break;

            case GO:
                wechsleRaumInnerhalbRegion(befehl);
                break;

            case DRIVE:
                wechsleInAndereRegion(befehl);
                break;

            case QUIT:
                moechteBeenden = beenden(befehl);
                break;

            case BUILD:
                baueAnlage(befehl);
                break;

            case PICKUP:
                itemAufheben(befehl);
                break;
        }
        // ansonsten: Befehl nicht erkannt.
        return moechteBeenden;
    }

    /**
     * Gibt eine einfache Hilfestellung und eine Liste der verfügbaren Befehlswörter aus.
     */
    private void hilfstextAusgeben() 
    {
        System.out.println("Sie sind die letzte Hoffnung fuer das Land!!!");        
        System.out.println();
        System.out.println("Ihnen stehen folgende Befehle zur Verfuegung:");
        parser.zeigeBefehle();
    }

    /**
     * Versucht, den Spieler in einen benachbarten Raum innerhalb der aktuellen Region zu bewegen.
     * @param befehl Der Befehl, der die Richtungsinformation enthält (z.B. "gehe norden").
     */
    private void wechsleRaumInnerhalbRegion(Befehl befehl) {
        if (!befehl.hatZweitesWort()) {
            System.out.println("Wohin gehen?");
            return;
        }
        String richtung = befehl.gibZweitesWort();
        // Versuche, den Raum in der angegebenen Richtung zu verlassen.
        Raum naechsterRaum = aktuellerRaum.gibAusgangRaum(richtung);

        if (naechsterRaum == null) {
            System.out.println("Dort geht es nicht lang!");
        } else {
            aktuellerRaum = naechsterRaum;
            raumInfoAusgeben();
            if (gui != null) gui.aktualisiereBild(aktuellerRaum);
        }
    }

    /**
     * Versucht, den Spieler in eine andere Region zu bewegen (Reisen).
     * Dies ist nur von bestimmten Räumen aus möglich (Bahnhof, Autobahn).
     * @param befehl Der Befehl, der die Zielregion enthält (z.B. "fahre Voltavia").
     */
    private void wechsleInAndereRegion(Befehl befehl) {
        // Prüft, ob der aktuelle Raum das Reisen überhaupt erlaubt.
        if (!(aktuellerRaum instanceof TravelRaum)) {
            System.out.println("Von hier ("+ aktuellerRaum.gibBeschreibung() +") aus kannst du nicht reisen.");
            return;
        }
        Raum alterRaum =  aktuellerRaum;

        // Gibt Hilfe, wenn keine Zielregion angegeben wurde.
        if (!befehl.hatZweitesWort()) { //
            System.out.println("Wohin möchtest du fahren?");
            // Prüft, ob der Ankunftsort in der Zielregion existiert und zum Transportmittel passt.
            if(alterRaum.gibKategorie() == Raumkategorie.BAHNHOF){
                System.out.println("Mögliche Regionen:" + aktuelleRegion.gibRegionAusgaengeZugAlsString());
            }
            else{
                System.out.println("Mögliche Regionen:" + aktuelleRegion.gibRegionAusgaengeAutoAlsString());
            }
        }
        Region naechsteRegion = spielumgebung.gibRegion(befehl.gibZweitesWort());

        if(naechsteRegion != null){
            Raum ankunftsRaum = null;

            // Prüfen ob die Zielregion ein Bahnhof/eine Autobahn hat
            if (alterRaum.gibKategorie() == Raumkategorie.BAHNHOF && aktuelleRegion.istZugAusgang(naechsteRegion.gibBeschreibung())) {
                ankunftsRaum = naechsteRegion.gibRaum("Bahnhof");
            } 
            else if (alterRaum.gibKategorie() == Raumkategorie.AUTOBAHN && aktuelleRegion.istAutoAusgang(naechsteRegion.gibBeschreibung())) {
                ankunftsRaum = naechsteRegion.gibRaum("Autobahn");
            }

            Region alteRegion = aktuelleRegion;
            aktuelleRegion = naechsteRegion; // Wechsel zur neuen Region

            if (ankunftsRaum != null) {
                aktuellerRaum = ankunftsRaum; // Wechsel zum neuen Raum
                System.out.println("Du reist von " + alteRegion.gibBeschreibung() + " nach " + aktuelleRegion.gibBeschreibung() + ".");
                // Belohne den Spieler für die Reise und erhöhe den Reisezähler.
                spieler.aendereGeld(einkommenMultiplikator);
                zugfahrtenCount +=1;
                raumInfoAusgeben();
                if (gui != null) gui.aktualisiereBild(aktuellerRaum);
            }
            else {
                System.out.println("Reise fehlgeschlagen. Du bleibst in " + alteRegion.gibBeschreibung() + ".");
                aktuelleRegion = alteRegion; // Zurück zur alten Region, da Ankunftsort ungültig
            }
        }
        else{
            System.out.println("Diese Region ist ungueltig!");
        }

    }
    
    /**
     * Versucht, eine Energieanlage (Solar oder Wind) im aktuellen Raum zu bauen.
     * Dies ist nur in Räumen vom Typ BauRaum möglich und erfordert
     * Geld und eine Baugenehmigung.
     * @param befehl Der Befehl, der den Anlagentyp enthält (z.B. "baue solar").
     */
    private void baueAnlage(Befehl befehl) { 
        // Prüft, ob im aktuellen Raum gebaut werden kann.
        if (!(aktuellerRaum instanceof BauRaum)) {
            System.out.println("Hier (" + aktuellerRaum.gibBeschreibung() + ") können keine Anlagen gebaut werden.");
            return;
        }
        BauRaum bauRaum = (BauRaum) aktuellerRaum;

        if (!befehl.hatZweitesWort()) { 
            System.out.println("Welche Art von Anlage soll gebaut werden? Verfügbar: 'solar' oder 'wind'."); 
            System.out.println(bauRaum.gibAnlagenString()); // Zeige aktuelle Belegung
            return;
        }
        String anlagenArt = befehl.gibZweitesWort(); 

        int kosten = 0;
        int einkommenPlus = 0;
        boolean erfolgreich = false;
        
        // Prüft, ob der Spieler eine Baugenehmigung hat.
        if(spieler.gibSpielerInventar().gibAnzahlItems("Baugenehmigung")>=1){
            // Logik für den Bau einer Solaranlage.
            if (anlagenArt.equals("solar")) 
            {
                kosten = 10;
                einkommenPlus = 6;
                if (spieler.gibGeld() >= kosten) {
                    if (bauRaum.bauSolar()) {
                        System.out.println("Eine Solaranlage wurde " + bauRaum.gibBeschreibung() + " gebaut.");
                        erfolgreich = true;
                    } else {
                        System.out.println("Solaranlage konnte nicht gebaut werden. " + bauRaum.gibAnlagenString());
                    }
                } else {
                    System.out.println("Nicht genug Geld für eine Solaranlage. Benötigt: " + kosten + " Münzen.");
                }
            } 
            // Logik für den Bau einer Windanlage.
            else if (anlagenArt.equals("wind")) 
            {
                kosten = 2;
                einkommenPlus = 1;
                if (spieler.gibGeld() >= kosten) {
                    if (bauRaum.bauWind()) {
                        System.out.println("Eine Windkraftanlage wurde " + bauRaum.gibBeschreibung() + " gebaut.");
                        erfolgreich = true;
                    } else {
                        System.out.println("Windkraftanlage konnte nicht gebaut werden. " + bauRaum.gibAnlagenString());
                    }
                } else {
                    System.out.println("Nicht genug Geld für eine Windkraftanlage. Benötigt: " + kosten + " Münzen.");
                }
            } else {
                System.out.println("Es können nur 'solar'- oder 'wind'-Anlagen gebaut werden.");
            }
        }
        else
        {
            System.out.print("Keine Baugenemigungen im Inventar! Besorge dir erstmal eine Baugenehmigung.");
        }
        
        // Wenn der Bau erfolgreich war, aktualisiere den Spielzustand.
        if(erfolgreich) {
            spieler.aendereGeld(-kosten);
            spieler.gibSpielerInventar().removeItem("Baugenehmigung");
            einkommenMultiplikator += einkommenPlus;
            System.out.println("Du erhältst nun " + einkommenPlus + " zusätzliche Münze(n) pro Bahnfahrt.");
            raumInfoAusgeben();
        }
        aktualisierePunkte();
    }

    /**
     * Zählt alle gebauten Anlagen in der gesamten Spielwelt und berechnet
     * daraus die aktuelle Gesamtpunktzahl des Spielers.
     */
    private void aktualisierePunkte()
    {
        gesamtSolaranlagen = 0;
        gesamtWindanlagen = 0;
        HashMap<String, Region> regionenMap = spielumgebung.gibRegionenMap();
        // Iteriere über alle Region-Objekte in der Map
        for (Region region : regionenMap.values()) {
            HashMap<String, Raum> raeumeMap = region.gibRaeumeMap();
            // Iteriere über alle Raum-Objekte in der Map
            for (Raum raum : raeumeMap.values()) {
                // Prüfe, ob der Raum ein BauRaum ist
                if (raum instanceof BauRaum) {
                    BauRaum bauRaum = (BauRaum) raum;
                    gesamtSolaranlagen += bauRaum.gibAnlagenSolar();
                    gesamtWindanlagen += bauRaum.gibAnlagenWind();
                }
            }
        }
        // Berechnet die Punkte basierend auf der Anzahl der Anlagen.
        punkte = (gesamtSolaranlagen * 10) + (gesamtWindanlagen * 5);
    }

    /**
     * Versucht, einen Gegenstand vom Boden des aktuellen Raumes aufzuheben
     * und dem Spielerinventar hinzuzufügen.
     * @param befehl Der Befehl, der den Namen des aufzuhebenden Gegenstands enthält.
     */
    private void itemAufheben(Befehl befehl)
    {
        if (!befehl.hatZweitesWort()) {
            System.out.println("Was möchtest du aufheben?");
            return;
        }
        String itemName = befehl.gibZweitesWort();

        // Prüft, ob im Raum überhaupt etwas zum Aufheben liegt.
        if (aktuellerRaum.gibInventar() == null || aktuellerRaum.gibInventar().istLeer()) {
            System.out.println("Hier gibt es nichts zum Aufheben.");
            return;
        }

        Inventar raumInventar = aktuellerRaum.gibInventar();
        Inventar spielerInventar = spieler.gibSpielerInventar();
        
        // Verschiebt das Item vom Raum zum Spieler.
        if (Inventar.bewegeItem(itemName, raumInventar, spielerInventar)) { //
            System.out.println(itemName + " wurde zum Inventar hinzugefügt.");
            raumInfoAusgeben();
        } else {
            System.out.println("Konnte " + itemName + " nicht aufheben. Ist es hier vorhanden?");
        }
    }

    /**
     * Implementiert den "verlassen"-Befehl.
     * @param befehl Der zu verarbeitende Befehl.
     * @return `true`, wenn das Spiel beendet werden soll, `false` wenn der Befehl
     * ein zweites Wort hatte und das Spiel weitergehen soll.
     */
    private boolean beenden(Befehl befehl) 
    {
        if(befehl.hatZweitesWort()) {
            System.out.println("Was soll beendet werden?");
            return false;
        }
        else {
            return true;  // Das Spiel soll beendet werden.
        }
    }
}

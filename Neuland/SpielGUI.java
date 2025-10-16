import javax.swing.*;
import java.awt.*;

/**
 * Minimalistische Swing-GUI für das Neualand-Spiel.
 * Zeigt ausschließlich das Bild des aktuellen Spielraums an.
 */
public class SpielGUI extends JFrame {
    private JLabel imageLabel;

    public SpielGUI() {
        super("Neualand Adventure");
        // Fenster-Basics
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Label für Raum-Bild
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        add(imageLabel, BorderLayout.CENTER);

        // Standardgröße
        setSize(1024, 1024);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Aktualisiere das gezeigte Bild basierend auf dem aktuellen Raum.
     * @param raum Die aktuelle Raum-Instanz (Bildklassen erben von Raum).
     */
    public void aktualisiereBild(Raum raum) {
        ImageIcon icon = null;
        if (raum instanceof BildRaum) {
            icon = ((BildRaum) raum).getImage();
        } else if (raum instanceof BildTravelRaum) {
            icon = ((BildTravelRaum) raum).getImage();
        } else if (raum instanceof BildBauRaum) {
            icon = ((BildBauRaum) raum).getImage();
        }
        imageLabel.setIcon(icon);
        repaint();
    }

    
}

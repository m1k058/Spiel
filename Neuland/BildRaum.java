import javax.swing.ImageIcon;

public class BildRaum extends Raum {
    private final ImageIcon image;

    public BildRaum(Raumkategorie kategorie) {
        super(kategorie);
        this.image = new ImageIcon(kategorie.getImageFile());
    }

    public ImageIcon getImage() {
        return image;
    }
}

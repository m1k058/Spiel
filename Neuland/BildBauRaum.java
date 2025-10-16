import javax.swing.ImageIcon;

public class BildBauRaum extends BauRaum {
    private final ImageIcon image;

    public BildBauRaum(Raumkategorie kategorie) {
        super(kategorie);
        this.image = new ImageIcon(kategorie.getImageFile());
    }

    public ImageIcon getImage() {
        return image;
    }
}

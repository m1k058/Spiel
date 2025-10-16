import javax.swing.ImageIcon;

public class BildTravelRaum extends TravelRaum {
    private final ImageIcon image;

    public BildTravelRaum(Raumkategorie kategorie) {
        super(kategorie);
        this.image = new ImageIcon(kategorie.getImageFile());
    }

    public ImageIcon getImage() {
        return image;
    }
}

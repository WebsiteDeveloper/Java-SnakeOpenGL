package snakeopengl;

/**
 *
 * Beschreibung
 *
 * @version 1.0 vom 20.09.2012
 * @author Bernhard Sirlinger
 */
public class Schlange {

    // Anfang Attribute
    private Knoten anfang;
    public int length = 0;
    // Ende Attribute

    public Schlange() {
        this.anfang = null;
    }

    public Knoten getAnfang() {
        return this.anfang;
    }

    public void amEndeEinfuegen(Knoten knoten) {
        if (this.anfang == null) {
            this.anfang = knoten;
            this.length++;
        } else {
            this.anfang.hintenAnfügen(knoten);
            this.length++;
        }
    }
    
    public void draw() {
        this.anfang.draw();
    }
}

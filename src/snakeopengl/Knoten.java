package snakeopengl;

import java.awt.Color;
import org.lwjgl.opengl.GL11;

/**
 *
 * Beschreibung
 *
 * @version 1.0 vom 20.09.2012
 * @author Bernhard Sirlinger
 */
public class Knoten {

		// Anfang Attribute
		private Knoten nachfolger;
		private int felderbreite;
		private int felderhoehe;
		private String nachfolger_pos;
		private int[] coords;
		// Ende Attribute

		public Knoten(int x, int y, int felderbreite, int felderhoehe) {
				this(null, x, y, felderbreite, felderhoehe);
		}

		public Knoten(int[] coords, int felderbreite, int felderhoehe) {
				this(null, coords, felderbreite, felderhoehe);
		}

		public Knoten(Knoten nachfolger, int[] coords, int felderbreite, int felderhoehe) {
				this.nachfolger = nachfolger;
				this.nachfolger_pos = this.getRelativePos(nachfolger);
				this.felderbreite = felderbreite;
				this.felderhoehe = felderhoehe;
				this.coords = coords;
		}

		public Knoten(Knoten nachfolger, int x, int y, int felderbreite, int felderhoehe) {
				this.nachfolger = nachfolger;
				this.nachfolger_pos = this.getRelativePos(nachfolger);
				this.felderbreite = felderbreite;
				this.felderhoehe = felderhoehe;
				this.coords = new int[2];
				this.coords[0] = x;
				this.coords[1] = y;
		}

		/**
		 * Diese Methode gibt den Nachfolger dieses Knotens zurück
		 *
		 * @author Bernhard Sirlinger
		 * @since 1.0 20.10.2012
		 * @return Nachfolger
		 */
		public Knoten getNachfolger() {
				return nachfolger;
		}

		/**
		 * Diese Methode setzt den Nachfolger diese Knotens
		 *
		 * @author Bernhard Sirlinger
		 * @since 1.0 20.10.2012
		 * @param nachfolger Knoten der als nachfolger eingefügt werden soll
		 */
		public void setNachfolger(Knoten nachfolger) {
				this.nachfolger = nachfolger;
		}

		/**
		 * Diese Methode gibt die Koordinaten des Knotens zurück
		 *
		 * @author Bernhard Sirlinger
		 * @since 1.0 20.10.2012
		 * @return Die Koordinaten des Knotens
		 */
		public int[] getCoords() {
				return this.coords;
		}

		public void hintenAnfügen(Knoten knoten) {
				if (this.nachfolger == null) {
						this.nachfolger = knoten;
						this.setNachfolgerPos(this.getRelativePos(knoten));
				} else {
						this.nachfolger.hintenAnfügen(knoten);
				}
		}

		/**
		 *
		 * Diese Methode setzt die Nachfolger Position anahand dem übergebenen
		 * String
		 *
		 * @author Bernhard Sirlinger
		 * @since 1.0 20.10.2012
		 * @param relative_pos Erlaubt sind {"links","rechts","oben","unten"}
		 */
		public void setNachfolgerPos(String relative_pos) {
				this.nachfolger_pos = relative_pos;
		}

		/**
		 *
		 * Diese Methode gibt die relative Position des Nachfolger zu diesem Knoten
		 * zurück
		 *
		 * @author Bernhard Sirlinger
		 * @since 1.0 20.10.2012
		 * @return Die relative Position des Nachfolger zu diesem Knoten
		 */
		public String getNachfolgerPos() {
				return this.nachfolger_pos;
		}

		public boolean isColliding(int x, int y) {
				if (x == this.coords[0] && y == this.coords[1]) {
						return true;
				}
				if (this.nachfolger != null) {
						return this.nachfolger.isColliding(x, y);
				}
				return false;
		}

		/**
		 * Diese Methode bewegt den Knoten um die Felderbreite nach Links
		 *
		 * @author Bernhard Sirlinger
		 * @since 1.0 20.10.2012
		 */
		public void nachLinks() {
				if (this.nachfolger_pos != "links") {
						if (this.coords[0] == 0) {
								//this.coords[0] = Display.getWidth();
								this.coords[0] = Field.getFieldWidth();
						}
						this.coords[0] -= this.felderbreite;
						//System.out.println("Knoten Schrittlaenge = " + this.felderbreite);
						this.callMethodToNachfolgerPos();
						this.nachfolger_pos = "rechts";
				}
		}

		/**
		 * Diese Methode bewegt den Knoten um die Felderbreite nach Rechts
		 *
		 * @author Bernhard Sirlinger
		 * @since 1.0 20.10.2012
		 */
		public void nachRechts() {
				if (this.nachfolger_pos != "rechts") {
						if (this.coords[0] == Field.getFieldWidth() - this.felderbreite) {
								this.coords[0] = -this.felderbreite;
						}
						this.coords[0] += this.felderbreite;
						//System.out.println("Knoten " + this.toString() + " Schrittlaenge = " + this.felderbreite);
						this.callMethodToNachfolgerPos();
						this.nachfolger_pos = "links";
				}
		}

		/**
		 * Diese Methode bewegt den Knoten um die Felderhöhe nach Unten
		 *
		 * @author Bernhard Sirlinger
		 * @since 1.0 20.10.2012
		 */
		public void nachUnten() {
				if (this.nachfolger_pos != "unten") {
						if (this.coords[1] == Field.getFieldHeight() - this.felderhoehe) {
								this.coords[1] = -this.felderhoehe;
						}
						this.coords[1] += this.felderhoehe;
						//System.out.println("Knoten Schrittlaenge = " + this.felderhoehe);
						this.callMethodToNachfolgerPos();
						this.nachfolger_pos = "oben";
				}
		}

		/**
		 * Diese Methode bewegt den Knoten um die Felderhöhe nach Oben
		 *
		 * @author Bernhard Sirlinger
		 * @since 1.0 20.10.2012
		 */
		public void nachOben() {
				if (this.nachfolger_pos != "oben") {
						if (this.coords[1] == 0) {
								this.coords[1] = Field.getFieldHeight();
						}
						this.coords[1] -= this.felderhoehe;
						//System.out.println("Knoten Schrittlaenge = " + this.felderhoehe);
						this.callMethodToNachfolgerPos();
						this.nachfolger_pos = "unten";
				}
		}

		/**
		 * Diese Methode gibt die relative Position des übergebenen Knotens zu
		 * diesem Knoten zurück
		 *
		 * @author Bernhard Sirlinger
		 * @since 1.0 20.10.2012
		 * @param knoten Der Knoten für den die relative Position zu diesem Knoten
		 * bestimmt werden soll
		 * @return Die relative Position des übergebenen Knotens zu diesem Knoten
		 */
		public final String getRelativePos(Knoten knoten) {
				if (knoten == null) {
						return "null";
				}
				int[] endcoords = this.coords;
				int[] newcoords = knoten.getCoords();
				if (endcoords[0] > newcoords[0]) {
						return "links";
				} else if (endcoords[0] < newcoords[0]) {
						return "rechts";
				} else if (endcoords[0] == newcoords[0]) {
						if (endcoords[1] > newcoords[1]) {
								return "oben";
						} else {
								return "unten";
						}
				}
				return "null";

		}

		/**
		 * Diese Methode ruft die zur Nachfolgerposition passende Bewegungsmethode
		 * des Nachfolgers auf
		 *
		 * @author Bernhard Sirlinger
		 * @since 1.0 20.10.2012
		 */
		private void callMethodToNachfolgerPos() {
				if (this.nachfolger != null) {

						switch (this.nachfolger_pos) {
								case "links":
										this.nachfolger.nachRechts();
										break;

								case "rechts":
										this.nachfolger.nachLinks();
										break;

								case "oben":
										this.nachfolger.nachUnten();
										break;

								case "unten":
										this.nachfolger.nachOben();
										break;

								default:
										break;
						}
				}
		}

		/**
		 * Diese Methode gibt den vorletzten Knoten der Schlange zurück
		 *
		 * @author Bernhard Sirlinger
		 * @since 1.0 20.10.2012
		 * @return Gibt den vorletzten Knoten zurück
		 */
		public Knoten getSecondToLast() {
				if (this.nachfolger.getNachfolger() == null) {
						return this;
				} else {
						return this.nachfolger.getSecondToLast();
				}
		}

		/**
		 * Diese Methode fügt am Ende der Schlange einen Knoten ein
		 *
		 * @author Bernhard Sirlinger
		 * @since 1.0 20.10.2012
		 * @param relative_pos Die relative Position des einzufügenden Knotens im
		 * bezug auf das letzte Schlangenelement
		 */
		public void hintenAnfügen(String relative_pos) {
				Knoten knoten;

				switch (relative_pos) {
						case "links":
								knoten = new Knoten(this.coords[0] - this.felderbreite, this.coords[1], this.felderbreite, this.felderhoehe);
								break;

						case "rechts":
								knoten = new Knoten(this.coords[0] + this.felderbreite, this.coords[1], this.felderbreite, this.felderhoehe);
								break;

						case "oben":
								knoten = new Knoten(this.coords[0], this.coords[1] - this.felderhoehe, this.felderbreite, this.felderhoehe);
								break;

						case "unten":
								knoten = new Knoten(this.coords[0], this.coords[1] + this.felderhoehe, this.felderbreite, this.felderhoehe);
								break;

						default:
								knoten = null;
								break;
				}

				if (this.nachfolger == null && knoten != null) {
						this.nachfolger = knoten;
						this.setNachfolgerPos(relative_pos);
				} else if (knoten != null) {
						this.nachfolger.hintenAnfügen(knoten);
				}

				//System.out.println(knoten.toString());
		}

		/**
		 * Diese Methode zeichnet den Knoten
		 *
		 * @author Bernhard Sirlinger
		 * @since 1.0 20.10.2012
		 */
		public void draw() {
				int add_x = 0, add_y = 0, add_width = -Field.border_width, add_height = -Field.border_width;
				/*
				 System.out.println("Drawing Knoten: " + this.toString());
				 System.out.println("X-Coord: " + this.coords[0]);
				 System.out.println("Y-Coord: " + this.coords[1]);
				 System.out.println("Nachfolger pos: "+ this.nachfolger_pos);
				 * */
				if (this.nachfolger != null) {
						switch (this.nachfolger_pos) {
								case "links":
										add_x = -Field.border_width;
										add_y = 0;
										add_width = -Field.border_width;
										add_height = -Field.border_width;
										break;

								case "rechts":
										add_x = 0;
										add_y = 0;
										add_width = 0;
										add_height = -Field.border_width;
										break;

								case "oben":
										add_x = 0;
										add_y = -Field.border_width;
										add_width = -Field.border_width;
										add_height = -Field.border_width;
										break;

								case "unten":
										add_x = 0;
										add_y = 0;
										add_width = -Field.border_width;
										add_height = 0;
										break;

								default:
										System.out.println("Error: no nachfolger pos at Coords: (" + this.coords[0] + " | " + this.coords[1] + ") ");
										break;
						}
				}

				if (this.coords[0] == 0) {
						add_x += Field.border_width;
				} else if (this.coords[1] == 0) {
						add_y += Field.border_width;
				}

				GL11.glColor3d(Color.RED.getRed() / 255.0, Color.RED.getGreen() / 255.0, Color.RED.getBlue() / 255.0);
				GL11.glRecti(this.coords[0] + add_x, this.coords[1] + add_y, this.coords[0] + this.felderbreite + add_width, this.coords[1] + this.felderhoehe + add_height);

				if (this.nachfolger != null) {
						this.nachfolger.draw();
				}
		}
		// Ende Methoden
} // end of Part

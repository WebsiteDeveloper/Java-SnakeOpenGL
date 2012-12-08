package snakeopengl;

import java.awt.Font;
import java.util.Random;
import javax.swing.JOptionPane;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

/**
 *
 * @author Bernhard Sirlinger
 */
public class Game {

		private UnicodeFont font;
		private UnicodeFont smallfont;
		private Field spielfeld;
		private Futter food;
		private Schlange snake;
		private String snake_direction = "down";
		private String snake_last_movement_direction;
		private long time;
		private short difficulty;
		private int points = 0;

		public static void main(String[] args) {
				Game game = new Game();
				game.start();
		}

		public void start() {

				while (true) {
						try {
								String input = JOptionPane.showInputDialog(null, "Bitte geben sie den gewünschten Schwierigkeitgrad an [1-10]", "Schwierigkeitsgrad?", JOptionPane.QUESTION_MESSAGE);
								if (input == null) {
										System.exit(0);
								}
								this.difficulty = Short.parseShort(input);
						} catch (NumberFormatException e) {
						}

						if (this.difficulty >= 1 && this.difficulty <= 10) {
								break;
						}
				}

				int value = JOptionPane.showConfirmDialog(null, "Wollen sie Snake im Vollbildmodus spielen?", "Vollbild? ", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				boolean want_fullscreen = (value == 0) ? true : false;
				
				try {

						DisplayMode mode = new DisplayMode(600, 600);
						if (want_fullscreen) {
								DisplayMode[] modes = null;
								try {
										modes = Display.getAvailableDisplayModes();
								} catch (LWJGLException ex) {
								}

								for (int i = 0; i < modes.length; i++) {
										if (modes[i].getWidth() >= mode.getWidth() && modes[i].getHeight() >= mode.getHeight() && modes[i].isFullscreenCapable()) {
												mode = modes[i];
												break;
										}
								}
						}

						Display.setDisplayMode(mode);
						Display.setTitle("Snake");
						Display.setFullscreen(want_fullscreen);
						Display.create();
				} catch (Exception e) {
						System.out.println(e.getMessage());
				}

				this.play();

		}

		private void play() {
				/* Initialisierung der OpenGL-Umgebung Start */
				GL11.glMatrixMode(GL11.GL_PROJECTION);

				GL11.glLoadIdentity();

				GL11.glOrtho(0, Display.getWidth(), Display.getHeight(), 0, 1, -1);
				GL11.glMatrixMode(GL11.GL_MODELVIEW);

				GL11.glEnable(GL11.GL_TEXTURE_2D);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				this.setUpFonts();
				/* Initialisierung der OpenGL-Umgebung Ende */

				//TODO: Optimize Field
				//int felderanzahl_vertikal = 20;
				//this.spielfeld = new Field(felderanzahl_vertikal, (Display.getHeight()/(int) Math.floor(Display.getWidth()/felderanzahl_vertikal)), 0, 0, Display.getWidth(), Display.getHeight());

				//Initialisierung der Spielfeldes
				this.spielfeld = new Field(30, 30, 0, 0, Display.getWidth(), Display.getHeight());

				//Initialisierung der Schlnage
				this.snake = new Schlange();

				/* Block zum erzeugen der Anfangsschlange mit Länge 3 Start */
				int[] coords = new int[2];
				coords[0] = this.spielfeld.getFelderbreite();
				coords[1] = this.spielfeld.getFelderhoehe() * 2;
				this.snake.amEndeEinfuegen(new Knoten(coords, this.spielfeld.getFelderbreite(), this.spielfeld.getFelderhoehe()));

				int[] coords2 = new int[2];
				coords2[0] = this.spielfeld.getFelderbreite();
				coords2[1] = this.spielfeld.getFelderhoehe();

				this.snake.amEndeEinfuegen(new Knoten(coords2, this.spielfeld.getFelderbreite(), this.spielfeld.getFelderhoehe()));

				int[] coords3 = new int[2];
				coords3[0] = this.spielfeld.getFelderbreite();
				coords3[1] = 0;
				this.snake.amEndeEinfuegen(new Knoten(coords3, this.spielfeld.getFelderbreite(), this.spielfeld.getFelderhoehe()));
				/* Block zum erzeugen der Anfangsschlange mit Länge 3 Start */

				/* Futter Initialisierung Start */
				int radix = (this.spielfeld.getFelderbreite() < this.spielfeld.getFelderhoehe()) ? Math.round(this.spielfeld.getFelderbreite() / 2) : Math.round(this.spielfeld.getFelderhoehe() / 2);
				int[] futtercoords = this.generateRandCoords(this.spielfeld.getBreiteInFeldern(), this.spielfeld.getHoeheInFeldern(), this.spielfeld.getFelderbreite(), this.spielfeld.getFelderhoehe());
				this.food = new Futter(futtercoords[0] + (this.spielfeld.getFelderbreite() / 2), futtercoords[1] + (this.spielfeld.getFelderhoehe() / 2), futtercoords, radix - 2);
				/* Futter Initialisierung Ende */

				//Initialisierung der Zeit
				this.time = this.getTime();

				
				//Render Loop
				while (!Display.isCloseRequested()) {
						GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
						this.spielfeld.draw();
						this.executeKeyCommands();

						if (this.snake.getAnfang().isColliding(this.food.getXforCollision(), this.food.getYforCollision())) {
								this.generateNewFood();
								Knoten knoten = this.snake.getAnfang().getSecondToLast();
								knoten.getNachfolger().hintenAnfügen(knoten.getNachfolgerPos());
								//System.out.println("Collision");
								this.points += this.difficulty;
						}

						long delta = this.getDelta();
						if (delta >= (700 / this.difficulty)) {
								//System.out.println(delta);
								if (this.collisionTest()) {
										this.showGameOverScreen();
								}
								//System.out.println("Update Snake");
								this.updateSnake();
								this.time = this.getTime();
						}

						this.food.draw();
						this.snake.draw();
						Display.update();
						Display.sync(60);
				}

				Display.destroy();
		}
		
		@SuppressWarnings("unchecked")
		private void setUpFonts() {
				Font awtfont = new Font("Times New Roman", Font.BOLD, 20);
				Font smallfont = new Font("Arial", Font.BOLD, 12);
				this.font = new UnicodeFont(awtfont);
				this.smallfont = new UnicodeFont(smallfont);
				this.font.getEffects().add(new ColorEffect(java.awt.Color.GREEN)); //Unchecked Warning
				this.smallfont.getEffects().add(new ColorEffect(java.awt.Color.WHITE)); //Unchecked Warning
				
				this.font.addAsciiGlyphs();
				this.smallfont.addAsciiGlyphs();
				try {
						this.font.loadGlyphs();
						this.smallfont.loadGlyphs();
				} catch (SlickException ex) {
						System.out.println(ex.getMessage());
				}
		}

		private void generateNewFood() {
				boolean is_colliding = true;

				//solange das Futter mit einem Element der Schlange kollidiert
				while (is_colliding) {
						int[] coords = this.generateRandCoords(this.spielfeld.getBreiteInFeldern(), this.spielfeld.getHoeheInFeldern(), this.spielfeld.getFelderbreite(), this.spielfeld.getFelderhoehe());
						if (!this.snake.getAnfang().isColliding(coords[0], coords[1])) {
								is_colliding = false;
								this.food.setFieldX(coords[0]);
								this.food.setFieldY(coords[1]);
								this.food.setX(coords[0] + (this.spielfeld.getFelderbreite() / 2));
								this.food.setY(coords[1] + (this.spielfeld.getFelderhoehe() / 2));
						}
				}
		}

		private boolean collisionTest() {
				int[] coords = this.snake.getAnfang().getCoords();

				switch (this.snake_direction) {
						case "left":
								if (this.snake.getAnfang().getNachfolger().isColliding(coords[0] - spielfeld.getFelderbreite(), coords[1])) {
										return true;
								}
								break;

						case "right":
								if (this.snake.getAnfang().getNachfolger().isColliding(coords[0] + spielfeld.getFelderbreite(), coords[1])) {
										return true;
								}
								break;

						case "up":
								if (this.snake.getAnfang().getNachfolger().isColliding(coords[0], coords[1] - spielfeld.getFelderhoehe())) {
										return true;
								}
								break;

						case "down":
								if (this.snake.getAnfang().getNachfolger().isColliding(coords[0], coords[1] + spielfeld.getFelderhoehe())) {
										return true;
								}
								break;
				}
				return false;
		}

		private void showGameOverScreen() {
				while (Display.isCreated() && !Display.isCloseRequested()) {
						GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
						this.drawGameOverScreen();
						if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
								Display.destroy();
								System.exit(0);
						} else if (Keyboard.isKeyDown(Keyboard.KEY_F1)) {
								this.snake_direction = "down";
								this.snake_last_movement_direction = null;
								this.points = 0;
								this.play();
						}
						//System.out.println("Game Over");
						if (Display.isCreated()) {
								Display.update();
								Display.sync(60);
						}
				}
				Display.destroy();
				System.exit(0);
		}

		private void drawGameOverScreen() {
				String gameovertext = "Game Over";

				int[] coords = new int[2];
				coords[0] = (Display.getWidth() / 2) - (this.font.getWidth(gameovertext) / 2);
				coords[1] = (Display.getHeight() / 2) - (this.font.getHeight(gameovertext) / 2);
				this.font.drawString( coords[0], coords[1], gameovertext);
				this.font.drawString( coords[0], coords[1]+40+this.font.getHeight(this.points+" Punkte"), this.points+" Punkte");
				this.font.drawString(5, 5, "Press F1 to start a new game");
				this.font.drawString(5, 25, "Press ESC to exit the game");
				this.smallfont.drawString(0,Display.getHeight()-15,"© 2012  Bernhard Sirlinger");
		}

		private int[] generateRandCoords(int breite_in_feldern, int hoehe_in_feldern, int felderbreite, int felderhoehe) {
				// Erzeugung eine Random Objektes mit der Zeit in Nanosekunden als Seed
				Random rand = new Random(System.nanoTime());
				// Generierung der Feldnummer
				int fieldnr = rand.nextInt(breite_in_feldern * hoehe_in_feldern);
				// Deklaration des Koordinaten Arrays
				int[] coords = new int[2];

				/* Block zum Feststellen der Koordinaten   Start */
				if (fieldnr % breite_in_feldern == 0) {
						int multiplikator = fieldnr / breite_in_feldern;
						coords[0] = 0;
						coords[1] = multiplikator * felderhoehe;
				} else {
						int multiplikator_x = (fieldnr % breite_in_feldern);
						int multiplikator_y = (fieldnr - (fieldnr % breite_in_feldern)) / breite_in_feldern;

						coords[0] = multiplikator_x * felderbreite;
						coords[1] = multiplikator_y * felderhoehe;
				}
				/* Block zum Feststellen der Koordinaten   Ende */

				return coords;
		}

		private void updateSnake() {
				switch (this.snake_direction) {
						case "left":
								this.snake.getAnfang().nachLinks();
								this.snake_last_movement_direction = "left";
								break;
						case "right":
								this.snake.getAnfang().nachRechts();
								this.snake_last_movement_direction = "right";
								break;
						case "up":
								this.snake.getAnfang().nachOben();
								this.snake_last_movement_direction = "up";
								break;
						case "down":
								this.snake.getAnfang().nachUnten();
								this.snake_last_movement_direction = "down";
								break;
				}
		}

		private void executeKeyCommands() {
				/* Solange KeyEvents vorhanden sind */
				while (Keyboard.next()) {
						switch (Keyboard.getEventKey()) {
								case Keyboard.KEY_ESCAPE:
										Display.destroy();
										System.exit(0);
										break;
								case Keyboard.KEY_LEFT:
										if (this.snake_last_movement_direction != "right") {
												this.snake_direction = "left";
										}
										break;
								case Keyboard.KEY_RIGHT:
										if (this.snake_last_movement_direction != "left") {
												this.snake_direction = "right";
										}
										break;
								case Keyboard.KEY_UP:
										if (this.snake_last_movement_direction != "down") {
												this.snake_direction = "up";
										}
										break;
								case Keyboard.KEY_DOWN:
										if (this.snake_last_movement_direction != "up") {
												this.snake_direction = "down";
										}
										break;
						}
				}
		}

		private void pause() {
				while (true) {
						GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
						this.spielfeld.draw();
						this.food.draw();
						this.snake.draw();
						Display.update();
						Display.sync(60);
						if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) || Display.isCloseRequested()) {
								Display.destroy();
								System.exit(0);
						}
						if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
								break;
						}
				}
		}

		private long getTime() {
				return (Sys.getTime() * 1000 / Sys.getTimerResolution());
		}

		private long getDelta() {
				long current_time = this.getTime();
				int delta = (int) (current_time - this.time);
				//this.elapsed_time = this.getTime();

				return delta;
		}
}
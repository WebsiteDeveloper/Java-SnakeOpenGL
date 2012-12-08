/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package snakeopengl;

import java.awt.Color;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author Bernhard Sirlinger
 */
public class Field {

		private final int fensterhoehe, fensterbreite;
		private static int breite_in_feldern;
		private static int hoehe_in_feldern;
		private int felderhoehe;
		private int felderbreite;
		private int[] startcoords = {0, 0};
		public final static int border_width = 1;
		private static int field_height = 0, field_width = 0;

		public Field(int breite_in_feldern, int hoehe_in_feldern, int[] startcoords, int fensterbreite, int fensterhoehe) {
				this.fensterhoehe = fensterhoehe;
				this.fensterbreite = fensterbreite;
				Field.breite_in_feldern = breite_in_feldern;
				Field.hoehe_in_feldern = hoehe_in_feldern;
				this.felderhoehe = ((int) Math.floor(this.fensterhoehe / hoehe_in_feldern) <= (int) Math.floor(this.fensterbreite / breite_in_feldern)) ? (int) Math.floor(this.fensterhoehe / hoehe_in_feldern) : (int) Math.floor(this.fensterbreite / breite_in_feldern) ;
				this.felderbreite = this.felderhoehe;
				this.startcoords = startcoords;
		}

		public Field(int breite_in_feldern, int hoehe_in_feldern, int x_startcoord, int y_startcoord, int fensterbreite, int fensterhoehe) {
				this.fensterhoehe = fensterhoehe;
				this.fensterbreite = fensterbreite;
				Field.breite_in_feldern = breite_in_feldern;
				Field.hoehe_in_feldern = hoehe_in_feldern;
				this.felderhoehe = ((int) Math.floor(this.fensterhoehe / hoehe_in_feldern) <= (int) Math.floor(this.fensterbreite / breite_in_feldern)) ? (int) Math.floor(this.fensterhoehe / hoehe_in_feldern) : (int) Math.floor(this.fensterbreite / breite_in_feldern) ;
				this.felderbreite = this.felderhoehe;
				this.startcoords[0] = x_startcoord;
				this.startcoords[1] = y_startcoord;
		}

		public int getFelderhoehe() {
				return this.felderhoehe;
		}

		public int getFelderbreite() {
				return this.felderbreite;
		}

		public int getBreiteInFeldern() {
				return Field.breite_in_feldern;
		}

		public int getHoeheInFeldern() {
				return Field.hoehe_in_feldern;
		}

		public static int getBorderWidth() {
				return border_width;
		}
		
		public static int getFieldHeight() {
				return field_height;
		}
		
		public static int getFieldWidth() {
				return field_width;
		}

		public void draw() {
				this.drawRectangles();
		}

		private void drawRectangles() {
				int max_x_coord = this.startcoords[0] ,max_y_coord = this.startcoords[1];
				
				for (int x_coord = this.startcoords[0]; x_coord + this.felderbreite <= (this.startcoords[0] + this.fensterbreite); x_coord += this.felderbreite) {
						
						for (int y_coord = this.startcoords[1]; y_coord + this.felderhoehe <= (this.startcoords[1] + this.fensterhoehe); y_coord += this.felderhoehe) {
								max_y_coord = y_coord;
								this.drawFieldRectangle(x_coord, y_coord, this.felderhoehe, this.felderbreite);
						}
						max_x_coord = x_coord;
				}
				if(Field.field_height == 0 && Field.field_width == 0) {
						Field.field_height = max_y_coord+this.felderhoehe;
						Field.field_width = max_x_coord+this.felderbreite;
				}
		}

		private void drawFieldRectangle(int x, int y, int height, int width) {
				float[] cell_border_rgbcolor = Color.WHITE.getRGBColorComponents(null);
				float[] cell_inner_rgbcolor = Color.BLACK.getRGBColorComponents(null);

				if (x == this.startcoords[0] && y == this.startcoords[1]) {
						GL11.glColor3f(cell_border_rgbcolor[0], cell_border_rgbcolor[1], cell_border_rgbcolor[2]);
						GL11.glRecti(x, y, x + width, y + height);
						GL11.glColor3f(cell_inner_rgbcolor[0], cell_inner_rgbcolor[1], cell_inner_rgbcolor[2]);
						GL11.glRecti(x + Field.border_width, y + Field.border_width, x + width - Field.border_width, y + height - Field.border_width);
				} else if (x == this.startcoords[0]) {
						GL11.glColor3f(cell_border_rgbcolor[0], cell_border_rgbcolor[1], cell_border_rgbcolor[2]);
						GL11.glRecti(x, y, x + width, y + height);
						GL11.glColor3f(cell_inner_rgbcolor[0], cell_inner_rgbcolor[1], cell_inner_rgbcolor[2]);
						GL11.glRecti(x + Field.border_width, y, x + width - Field.border_width, y + height - Field.border_width);
				} else if (y == this.startcoords[1]) {
						GL11.glColor3f(cell_border_rgbcolor[0], cell_border_rgbcolor[1], cell_border_rgbcolor[2]);
						GL11.glRecti(x, y, x + width, y + height);
						GL11.glColor3f(cell_inner_rgbcolor[0], cell_inner_rgbcolor[1], cell_inner_rgbcolor[2]);
						GL11.glRecti(x, y + Field.border_width, x + width - Field.border_width, y + height - Field.border_width);
				} else {
						GL11.glColor3f(cell_border_rgbcolor[0], cell_border_rgbcolor[1], cell_border_rgbcolor[2]);
						GL11.glRecti(x, y, x + width, y + height);
						GL11.glColor3f(cell_inner_rgbcolor[0], cell_inner_rgbcolor[1], cell_inner_rgbcolor[2]);
						GL11.glRecti(x, y, x + width - Field.border_width, y + height - Field.border_width);
				}
		}
}

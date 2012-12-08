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
public class Futter {

		private static int[] coords = {0, 0};
		private static int[] fieldcoords;
		private static int radix;

		public Futter(int[] coords, int[] fieldcoords, int radix) {
				Futter.coords = coords;
				Futter.fieldcoords = fieldcoords;
				Futter.radix = radix;
		}

		public Futter(int x_coord, int y_coord, int[] fieldcoords, int radix) {
				Futter.coords[0] = x_coord;
				Futter.coords[1] = y_coord;
				Futter.fieldcoords = fieldcoords;
				Futter.radix = radix;
		}
		
		public void setX(int x) {
				Futter.coords[0] = x;
		}
		
		public void setY(int y) {
				Futter.coords[1] = y;
		}
		
		public void setFieldX(int x) {
				Futter.fieldcoords[0] = x;
		}
		
		public void setFieldY(int y) {
				Futter.fieldcoords[1] = y;
		}

		public int getXforCollision() {
				return Futter.fieldcoords[0];
		}

		public int getYforCollision() {
				return Futter.fieldcoords[1];
		}

		public void draw() {
				float[] yellow = Color.YELLOW.getRGBColorComponents(null);
				float[] green = Color.GREEN.getRGBColorComponents(null);
				
				GL11.glBegin(GL11.GL_POLYGON);
				GL11.glColor3f(yellow[0], yellow[1], yellow[2]);
				GL11.glVertex2d(Futter.coords[0], Futter.coords[1]);
				double increment = 0.0;
				if(Futter.radix > 100) {
						increment = 10.0/100;
				}
				else {
						increment = 10.0/Futter.radix;
				}
				for (double angle = 0; angle <= 360; angle += increment) {
						if(angle >= 180){
								GL11.glColor3f(green[0], green[1], green[2]);
						}
						else {
								GL11.glColor3f(yellow[0], yellow[1], yellow[2]);
						}
						GL11.glVertex2d(Futter.coords[0] + Math.sin(angle) * Futter.radix, Futter.coords[1] + Math.cos(angle) * Futter.radix);
				}
				GL11.glEnd();
		}
}

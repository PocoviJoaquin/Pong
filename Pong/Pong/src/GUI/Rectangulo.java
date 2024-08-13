// Interfaz simple de cómo se van a ver en la pantalla las barras y la pelota
package GUI;

import java.awt.Graphics2D;
import java.awt.Color;

public class Rectangulo {
	int x;
	int y;
	int altura;
	int anchura;
	private Color color;
	
	public Rectangulo(int x, int y, int altura, int anchura, Color color) {
		this.x = x;
		this.y = y;
		this.altura = altura;
		this.anchura = anchura;
		this.color = color;
	}
	
	public void dibujar(Graphics2D g2) {
		g2.setColor(color);
		g2.fillRect(x, y, anchura, altura);
	}
}

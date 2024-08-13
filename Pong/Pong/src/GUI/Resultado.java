// Texto para los resultados (también es usado para todos los casos de texto pero me di cuenta tarde y ahora no puedo cambiar el nombre)
package GUI;

import java.awt.*;

public class Resultado {
	public String texto;
	public Font fuente;
	public double x, y;
	public double anchura, altura;
	public Color color = Color.WHITE;
	
	public Resultado(String texto, Font fuente, double x, double y, Color color) {
		this.fuente = fuente;
		this.texto = texto;
		this.x = x;
		this.y = y;
		this.anchura = fuente.getSize() * texto.length();
		this.altura = fuente.getSize();
	}
	
	public Resultado(int texto, Font fuente, double x, double y) {
		this.fuente = fuente;
		this.texto = "" + texto;
		this.x = x;
		this.y = y;
	}
	
	public void dibujar(Graphics2D g2) {
		g2.setColor(color);
		g2.setFont(fuente);
		g2.drawString(texto, (float)x, (float)y);
	}
}

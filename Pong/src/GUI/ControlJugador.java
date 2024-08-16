// Cómo se mueve el jugador
package GUI;

import java.awt.Color;
import java.awt.event.KeyEvent;
import logica.KL;
import ventana.Main;
import ventana.Menu;

public class ControlJugador {
    public Rectangulo rectangulo, rectangulo2;
    public KL keyListener;
    private boolean controlesIntercambiados = false; // Variable para rastrear el estado de los controles intercambiados

    public ControlJugador(Rectangulo rectangulo, Rectangulo rectangulo2, KL keyListener) {
        this.rectangulo = rectangulo;
        if(rectangulo2 != null) {
            this.rectangulo2 = rectangulo2;        	
        }
        
        else {
        	this.rectangulo2 = new Rectangulo(0, 0, 0, 0, Color.BLACK);
        }
        this.keyListener = keyListener;
    }

    public ControlJugador(Rectangulo rectangulo) {
        this.rectangulo = rectangulo;
        this.rectangulo2 = null;
        this.keyListener = null;
    }

    public void actualizar(double deltaTime) {
        if (keyListener != null) {
            // Verifica el estado de los controles
            if (controlesIntercambiados) {
                // Controles intercambiados
                if (keyListener.isKeyPressed(KeyEvent.VK_UP)) {
                    moverArriba(deltaTime, rectangulo); // Controla la raqueta del segundo jugador
                }
                if (keyListener.isKeyPressed(KeyEvent.VK_DOWN)) {
                    moverAbajo(deltaTime, rectangulo); // Controla la raqueta del segundo jugador
                }
                // Controles para el primer jugador (W y S) en la segunda raqueta
                if (Menu.getEstadoJugador() == 1) {
                    if (keyListener.isKeyPressed(KeyEvent.VK_W)) {
                        moverArriba(deltaTime, rectangulo2); // Controla la raqueta del primer jugador
                    }
                    if (keyListener.isKeyPressed(KeyEvent.VK_S)) {
                        moverAbajo(deltaTime, rectangulo2); // Controla la raqueta del primer jugador
                    }
                }
            } else {
                // Controles no intercambiados
                if (keyListener.isKeyPressed(KeyEvent.VK_ESCAPE)) {
                    Main.cambiarEstado(0);
                }
                if (keyListener.isKeyPressed(KeyEvent.VK_W)) {
                    moverArriba(deltaTime, rectangulo); // Controla la raqueta del primer jugador
                }
                if (keyListener.isKeyPressed(KeyEvent.VK_S)) {
                    moverAbajo(deltaTime, rectangulo); // Controla la raqueta del primer jugador
                }
                // Controles para el segundo jugador (flechas arriba y abajo)
                if (Menu.getEstadoJugador() == 1) {
                    if (keyListener.isKeyPressed(KeyEvent.VK_UP)) {
                        moverArriba(deltaTime, rectangulo2); // Controla la raqueta del segundo jugador
                    }
                    if (keyListener.isKeyPressed(KeyEvent.VK_DOWN)) {
                        moverAbajo(deltaTime, rectangulo2); // Controla la raqueta del segundo jugador
                    }
                }
            }
        }
    }
    public void moverArriba(double deltaTime, Rectangulo rect) {
        if (rect.y - 100 * deltaTime > 40) {
            rect.y -= (int) (400 * deltaTime);
        }
    }

    public void moverAbajo(double deltaTime, Rectangulo rect) {
        if ((rect.y + 100 * deltaTime) + rect.altura < 690) {
            rect.y += (int) (400 * deltaTime);
        }
    }

    public void moverArribaCpu(double deltaTime) {
        moverArriba(deltaTime, rectangulo);
    }

    public void moverAbajoCpu(double deltaTime) {
        moverAbajo(deltaTime, rectangulo);
    }
    
    public void intercambiarControles() {
        controlesIntercambiados = !controlesIntercambiados; // Cambia el estado de los controles
    }
}

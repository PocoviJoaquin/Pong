// Cómo se mueve el jugador
package GUI;

import java.awt.Color;
import java.awt.event.KeyEvent;
import logica.KL;
import ventana.Menu;

public class ControlJugador {
    public Rectangulo rectangulo, rectangulo2;
    public KL keyListener;

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
            // Controles para el primer jugador (W y S)
            if (keyListener.isKeyPressed(KeyEvent.VK_W)) {
                moverArriba(deltaTime, rectangulo);
            }
            if (keyListener.isKeyPressed(KeyEvent.VK_S)) {
                moverAbajo(deltaTime, rectangulo);
            }

            // Controles para el segundo jugador (flechas arriba y abajo)
            if (Menu.getEstadoJugador() == 1) {
                if (keyListener.isKeyPressed(KeyEvent.VK_UP)) {
                    moverArriba(deltaTime, rectangulo2);
                }
                if (keyListener.isKeyPressed(KeyEvent.VK_DOWN)) {
                    moverAbajo(deltaTime, rectangulo2);
                }
            }
        }
    }

    public void moverArriba(double deltaTime, Rectangulo rect) {
        if (rect.y - 100 * deltaTime > 30) {
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
}

// Se encarga de manejar que se lean las teclas.

package logica;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KL implements KeyListener {
	
	private boolean keyPressed[] = new boolean[128];
	
	KeyEvent keyEvent;

	@Override
	public void keyTyped(KeyEvent e) {
		
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
	    keyPressed[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
	    keyPressed[e.getKeyCode()] = false;
	}	

	public boolean isKeyPressed(int keyCode) {
		return keyPressed[keyCode];
	}

}

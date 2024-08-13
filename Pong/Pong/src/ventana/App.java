// El programa del juego
package ventana;

import java.awt.*;
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JPanel;

import GUI.ControlJugador;
import GUI.CpuControl;
import GUI.Pelota;
import GUI.Rectangulo;
import GUI.Resultado;
import logica.Constantes;
import logica.KL;

import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

public class App extends JPanel implements Runnable {
	
    private JFrame frame;
    private Canvas canvas;
	private static final long serialVersionUID = 1L;
	
	public KL keyListener = new KL();
	public Rectangulo jugadorUno, computadora, pelota;
	public ControlJugador controlJugador;
	public CpuControl cpuControl;
	public Pelota pelotaLogica;
	public Resultado resultadoIzquierdaTexto, resultadoDerechaTexto, tiempo;
	public boolean isRunning = true;
    private long tiempoRestante; // Tiempo restante en segundos
	
	public App() {
        frame = new JFrame("PONG");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 700);
        frame.setResizable(false);

        canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(700, 700));
        canvas.setMaximumSize(new Dimension(700, 700));
        canvas.setMinimumSize(new Dimension(700, 700));
        canvas.setFocusable(false);

        this.add(canvas);
        frame.add(this);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.addKeyListener(keyListener);
        frame.setVisible(true);
		
	    resultadoIzquierdaTexto = new Resultado(0, new Font("Times New Roman", Font.PLAIN, 40), 20, 40);
	    resultadoDerechaTexto = new Resultado(0, new Font("Times New Roman", Font.PLAIN, 40), 670, 40);
	    tiempo = new Resultado("", new Font("Times New Roman", Font.PLAIN, 40), 340, 40, Color.WHITE);
	    jugadorUno = new Rectangulo(20, 100, 80, 15, Color.WHITE);
	    computadora = new Rectangulo(665, 100, 80, 15, Color.WHITE);
        tiempoRestante = OpcionesModoVictoria.getTemporizador() * 60; // Convertir minutos a segundos
	    if(Menu.getEstadoJugador() == 0) {
		    controlJugador = new ControlJugador(jugadorUno, null, keyListener);
	    }
	    
	    else if(Menu.getEstadoJugador() == 1) {
	    	controlJugador = new ControlJugador(jugadorUno, computadora, keyListener);
	    }
	    
	    pelota = new Rectangulo(340, 340, 10, 10, Color.WHITE);
	    pelotaLogica = new Pelota(pelota, jugadorUno, computadora, resultadoIzquierdaTexto, resultadoDerechaTexto);
	    cpuControl = new CpuControl(new ControlJugador(computadora), pelota);
	}
	
    private int tickCount = 0; // Contador de ticks
	
	public void actualizar(double deltaTime) {
		controlJugador.actualizar(deltaTime);
		pelotaLogica.actualizar(deltaTime);
		if(Menu.getEstadoJugador() == 0) {
			cpuControl.actualizar(deltaTime);			
		}
		
	    if (OpcionesPartida.getTipoPartido() == 1) {
	    	tickCount++;
	    	if(tickCount % 60 == 0) {
		        tiempoRestante -= 1;
		        int minutos = (int) (tiempoRestante / 60);
		        int segundos = (int) (tiempoRestante % 60);
		        tiempo.texto = String.format("%02d:%02d", minutos, segundos);	
		        tickCount = 0;
	    	}
	    }
		
        verificarCondicionesDeVictoria();
		
	}
	
	private void verificarCondicionesDeVictoria() {
	    int diferencia = Math.abs(Integer.parseInt(resultadoIzquierdaTexto.texto) - Integer.parseInt(resultadoDerechaTexto.texto));

	    if (OpcionesPartida.getTipoPartido() == 0) {
	        // Modo de puntaje
	        if ((Integer.parseInt(resultadoIzquierdaTexto.texto) >= OpcionesModoVictoria.getPuntaje() || Integer.parseInt(resultadoDerechaTexto.texto) >= OpcionesModoVictoria.getPuntaje()) 
	            && diferencia >= OpcionesModoVictoria.getDiferencia()) {
	            detenerJuego();
	        }
	    } else if (OpcionesPartida.getTipoPartido() == 1) {
	        // Modo de temporizador
	        if (tiempoRestante <= 0) {
	            detenerJuego();
	        }
	    }
	}

    private void detenerJuego() {
        isRunning = false;
        JOptionPane.showMessageDialog(frame, "El juego ha terminado");
        frame.dispose();
    }

	@Override
	protected void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    Graphics2D g2 = (Graphics2D) g;
	    g2.setColor(Color.BLACK);
	    g2.fillRect(0, 0, getWidth(), getHeight());
	    
	    resultadoIzquierdaTexto.dibujar(g2);
	    resultadoDerechaTexto.dibujar(g2);
	    if(OpcionesPartida.getTipoPartido() == 1) {
	    	tiempo.dibujar(g2);
	    }
	    jugadorUno.dibujar(g2);
	    computadora.dibujar(g2);
	    pelota.dibujar(g2);
	}
	
	public void stop() {
		isRunning = false;
	}
	
	public void run() {
	    long lastTime = System.nanoTime();
	    double amountOfTicks = 60.0;
	    double ns = 1000000000 / amountOfTicks;
	    double delta = 0;
	    
	    canvas.createBufferStrategy(2);
	    BufferStrategy bs = canvas.getBufferStrategy();
	    
	    while(isRunning) {
	        long now = System.nanoTime();
	        delta += (now - lastTime) / ns;
	        lastTime = now;
	        
	        if(delta >= 1) {
	            actualizar(1.0 / amountOfTicks);
	            
	            do {
	                do {
	                    Graphics g = bs.getDrawGraphics();
	                    g.setColor(Color.BLACK);
	                    g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
	                    
	                    resultadoIzquierdaTexto.dibujar((Graphics2D) g);
	                    resultadoDerechaTexto.dibujar((Graphics2D) g);
	            	    if(OpcionesPartida.getTipoPartido() == 1) {
	            	    	tiempo.dibujar((Graphics2D) g);
	            	    }
	                    jugadorUno.dibujar((Graphics2D) g);
	                    computadora.dibujar((Graphics2D) g);
	                    pelota.dibujar((Graphics2D) g);
	                    
	                    g.dispose();
	                } while(bs.contentsRestored());
	                bs.show();
	            } while(bs.contentsLost());
	            
	            delta--;
	        }
	        
	        try {
	            Thread.sleep(1);
	        } catch(Exception e) {
	            e.printStackTrace();
	        }
	    }
	    
	    frame.dispose();
	    return;
	}
}

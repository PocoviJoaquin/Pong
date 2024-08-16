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
import javax.swing.Timer;
public class App extends JPanel implements Runnable {
	
    private JFrame frame;
    private Canvas canvas;
	private static final long serialVersionUID = 1L;
	
	public KL keyListener = new KL();
	public Rectangulo jugadorUno, computadora, pelota;
	public ControlJugador controlJugador;
	public CpuControl cpuControl;
	public Pelota pelotaLogica;
	public Resultado resultadoIzquierdaTexto, resultadoDerechaTexto, tiempo, textoFinalPartido, nombreIzquierda, nombreDerecha;
	public boolean isRunning = true;
    private long tiempoRestante; // Tiempo restante en segundos
    public static boolean controlesIntercambiados = false;
    private boolean juegoPausado = false;
	
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
		
	    resultadoIzquierdaTexto = new Resultado(0, new Font("Times New Roman", Font.PLAIN, 40), 250, 40);
	    nombreIzquierda = new Resultado(0, new Font("Times New Roman", Font.PLAIN, 30), 20, 40);
	    resultadoDerechaTexto = new Resultado(0, new Font("Times New Roman", Font.PLAIN, 40), 440, 40);
	    nombreDerecha = new Resultado(0, new Font("Times New Roman", Font.PLAIN, 30), 480, 40);
	    tiempo = new Resultado("", new Font("Times New Roman", Font.PLAIN, 40), 310, 40, Color.WHITE);
	    textoFinalPartido = new Resultado("", new Font("Times New Roman", Font.PLAIN, 30), 60, 670, Color.WHITE);
	    jugadorUno = new Rectangulo(20, 100, 80, 15, Color.WHITE);
	    computadora = new Rectangulo(665, 100, 80, 15, Color.WHITE);
        tiempoRestante = OpcionesModoVictoria.getTemporizador() * 60; // Convertir minutos a segundos
        nombreIzquierda.texto = OpcionNombreJugador.getNombre1();
        
	    if(Menu.getEstadoJugador() == 0) {
		    controlJugador = new ControlJugador(jugadorUno, null, keyListener);
		    nombreDerecha.texto = "CPU";
	    }
	    
	    else if(Menu.getEstadoJugador() == 1) {
	    	controlJugador = new ControlJugador(jugadorUno, computadora, keyListener);
	    	nombreDerecha.texto = OpcionNombreJugador.getNombre2();
	    }
	    
	    pelota = new Rectangulo(340, 340, 10, 10, Color.WHITE);
	    pelotaLogica = new Pelota(pelota, jugadorUno, computadora, resultadoIzquierdaTexto, resultadoDerechaTexto);
	    cpuControl = new CpuControl(new ControlJugador(computadora), pelota);
	}
	
    private int tickCount = 0; // Contador de ticks
    
    public static boolean getControlesIntercambiados() {
    	return controlesIntercambiados;
    }
	
	public void actualizar(double deltaTime) {
	    if (juegoPausado) {
	        return;
	    }
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
	    	
            if (!controlesIntercambiados && tiempoRestante <= OpcionesModoVictoria.getTemporizador() * 30) {
                // Solo intercambiar controles si se juega de a dos
                if (Menu.getEstadoJugador() == 1) {
                    intercambiarControles();
                }
                controlesIntercambiados = true;
            }
        }

		
        verificarCondicionesDeVictoria();
		
	}

    private void intercambiarControles() {
        // Intercambiar los rectángulos controlados por cada jugador
        Rectangulo temp = controlJugador.rectangulo;
        controlJugador.rectangulo = controlJugador.rectangulo2;
        controlJugador.rectangulo2 = temp;

        // Intercambiar los resultados
        String tempResultado = resultadoIzquierdaTexto.texto;
        resultadoIzquierdaTexto.texto = resultadoDerechaTexto.texto;
        resultadoDerechaTexto.texto = tempResultado;
        
        String tempNombre = nombreIzquierda.texto;
        nombreIzquierda.texto = nombreDerecha.texto;
        nombreDerecha.texto = tempNombre;

        // Llamar al método para intercambiar los controles
        controlJugador.intercambiarControles();



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
	
	private boolean juegoTerminado = false; // Nueva variable para controlar si el juego ha terminado

	private void detenerJuego() {
	    if (Integer.parseInt(resultadoIzquierdaTexto.texto) > Integer.parseInt(resultadoDerechaTexto.texto)) {
	    	if(Menu.getEstadoJugador() == 0) {
		        textoFinalPartido.texto = "CPU GANA";
	    	}
	    	else if(Menu.getEstadoJugador() == 1) {
		        textoFinalPartido.texto = OpcionNombreJugador.getNombre2() + " GANA";
	    	}
	    } else if (Integer.parseInt(resultadoIzquierdaTexto.texto) < Integer.parseInt(resultadoDerechaTexto.texto)) {
		        textoFinalPartido.texto = OpcionNombreJugador.getNombre1() + " GANA";
		        textoFinalPartido.x = 370;	    		
	    } else {
	        textoFinalPartido.texto = "EMPATE";
	        textoFinalPartido.x = 280;
	    }
	    
	    // Detiene las actualizaciones del juego
	    juegoTerminado = true;

	    // Crea un temporizador que espere 2 segundos antes de cerrar la ventana
	    Timer timer = new Timer(2000, e -> {
	        isRunning = false;
	        frame.dispose();
	    });

	    timer.setRepeats(false);
	    timer.start();
	}

	@Override
	protected void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    Graphics2D g2 = (Graphics2D) g;
	    g2.setColor(Color.BLACK);
	    g2.fillRect(0, 0, getWidth(), getHeight());

	    // Dibuja las rayas verticales en el centro de la cancha
	    dibujarRayasCentro(g2);

	    resultadoIzquierdaTexto.dibujar(g2);
	    resultadoDerechaTexto.dibujar(g2);
	    if(OpcionesPartida.getTipoPartido() == 1) {
	        tiempo.dibujar(g2);
	    }
	    textoFinalPartido.dibujar(g2);
	    jugadorUno.dibujar(g2);
	    computadora.dibujar(g2);
	    pelotaLogica.dibujar(g2);
	    nombreIzquierda.dibujar(g2);
	    nombreDerecha.dibujar(g2);
	}

    private void dibujarRayasCentro(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        int y = 40;
        int lineHeight = 10;
        int lineSpacing = 10;
        int centerX = getWidth() / 2 - 1; // Centrar las rayas en la mitad de la cancha
        while (y < getHeight()) {
            g2.fillRect(centerX, y, 2, lineHeight); // Dibuja una raya de 2 píxeles de ancho
            y += lineHeight + lineSpacing; // Espacio entre las rayas
        }
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

	    while (isRunning) {
	        long now = System.nanoTime();
	        delta += (now - lastTime) / ns;
	        lastTime = now;

	        if (delta >= 1) {
	            if (!juegoTerminado) {
	                actualizar(1.0 / amountOfTicks);
	            }

	            do {
	                do {
	                    Graphics g = bs.getDrawGraphics();
	                    g.setColor(Color.BLACK);
	                    g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

	                    Graphics2D g2 = (Graphics2D) g;

	                    // Dibuja las rayas verticales en el centro de la cancha
	                    dibujarRayasCentro(g2);

	                    resultadoIzquierdaTexto.dibujar(g2);
	                    resultadoDerechaTexto.dibujar(g2);
	                    if (OpcionesPartida.getTipoPartido() == 1) {
	                        tiempo.dibujar(g2);
	                    }
	                    textoFinalPartido.dibujar(g2);
	                    jugadorUno.dibujar(g2);
	                    computadora.dibujar(g2);
	                    pelotaLogica.dibujar(g2);
	                    nombreIzquierda.dibujar(g2);
	                    nombreDerecha.dibujar(g2);

	                    g.dispose();
	                } while (bs.contentsRestored());
	                bs.show();
	            } while (bs.contentsLost());

	            delta--;
	        }

	        try {
	            Thread.sleep(1);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    frame.dispose();
	    return;
	}
}
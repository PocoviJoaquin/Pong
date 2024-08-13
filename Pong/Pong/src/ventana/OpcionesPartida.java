package ventana;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import GUI.Pelota;
import GUI.Resultado;
import logica.KL;
import logica.ML;

import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.io.File;

public class OpcionesPartida extends JPanel implements Runnable {
    private JFrame frame;
    private Canvas canvas;
	
	private static final long serialVersionUID = 1L;
	
	public KL keyListener = new KL();
	public Resultado atras, opcionPuntaje, opcionTemporizador, textoElegir;
	public ML mouseListener = new ML();
	public boolean isRunning = true;
	private int opcionSeleccionada = 0;
	public static int estadoTipoPartido = 0;
	public int puntajeGanador = 0;
	public int temporizador = 0;
    private int ultimaOpcionHover = -1;
    
    private void reproducirSonido(String nombreArchivo) {
        try {
            // Obtener la ruta del directorio donde está la clase Pelota
            String ruta = Pelota.class.getResource("").getPath();
            File archivo = new File(ruta + nombreArchivo);
            if (!archivo.exists()) {
                System.err.println("No se pudo encontrar el archivo de sonido: " + archivo.getAbsolutePath());
                return;
            }
            
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(archivo);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	public OpcionesPartida() {
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
        canvas.addMouseListener(mouseListener);
        canvas.addMouseMotionListener(mouseListener);
        frame.setVisible(true);
		this.opcionPuntaje = new Resultado("POR PUNTAJE", new Font("Times New Roman", Font.PLAIN, 40), 150, 350, Color.WHITE);
		this.opcionTemporizador = new Resultado("POR TIEMPO", new Font("Times New Roman", Font.PLAIN, 40), 150, 450, Color.WHITE);
		this.textoElegir = new Resultado("ELEGIR TIPO DE PARTIDO", new Font("Times New Roman", Font.PLAIN, 40), 170, 250, Color.WHITE);
		this.atras = new Resultado("ATRAS", new Font("Times New Roman", Font.PLAIN, 40), 250, 650, Color.WHITE);
		
	    Graphics2D g2 = (Graphics2D)getGraphics();
	}
	
	public void setTipoPartido(int numero) {
		estadoTipoPartido = numero;
	}
	
	public static int getTipoPartido() {
		return estadoTipoPartido;
	}
	
	public void actualizar(double deltaTime) {
	    // Actualizar colores basados en la selección
	    double mouseX = mouseListener.getMouseX();
	    double mouseY = mouseListener.getMouseY();
        int opcionHoverActual = -1;
	    if (mouseX > opcionPuntaje.x
	            && mouseX < opcionPuntaje.x + opcionPuntaje.anchura
	            && mouseY > opcionPuntaje.y - opcionPuntaje.altura / 2
	            && mouseY < opcionPuntaje.y + opcionPuntaje.altura / 2) {
	    	opcionPuntaje.color = new Color(158, 158, 158);
            opcionHoverActual = 0;
	        
	        if (mouseListener.isMousePressed()) {
	            Main.cambiarEstado(5);
	            setTipoPartido(0);
	            }
		}
		
		else {
			opcionPuntaje.color = Color.white;
		}
	    
	    if (mouseX > opcionTemporizador.x
	            && mouseX < opcionTemporizador.x + opcionTemporizador.anchura
	            && mouseY > opcionTemporizador.y - opcionTemporizador.altura / 2
	            && mouseY < opcionTemporizador.y + opcionTemporizador.altura / 2) {
	    	opcionTemporizador.color = new Color(158, 158, 158);
            opcionHoverActual = 1;
	        
	        if (mouseListener.isMousePressed()) {
	            Main.cambiarEstado(5);
	            setTipoPartido(1);
	            }
		}
		
		else {
			opcionTemporizador.color = Color.white;
		}
		
		if (mouseX > atras.x
	            && mouseX < atras.x + atras.anchura
	            && mouseY > atras.y - atras.altura / 2
	            && mouseY < atras.y + atras.altura / 2) {
			atras.color = new Color(158, 158, 158);
            opcionHoverActual = 2;
	        
	        if (mouseListener.isMousePressed()) {
	            Main.cambiarEstado(3);
	            }
		}
		
		else {
			atras.color = Color.white;
		}
		
        if (opcionHoverActual != ultimaOpcionHover) {
            if (opcionHoverActual != -1) {
                reproducirSonido("snd_select.wav");
            }
            ultimaOpcionHover = opcionHoverActual;
        }
    }


	@Override
	public void paint(Graphics g) {
	    super.paint(g);
	    Graphics2D g2 = (Graphics2D) g;
	    g2.setColor(Color.BLACK);
	    g2.fillRect(0, 0, getWidth(), getHeight());
	    
	    textoElegir.dibujar(g2);
	    opcionPuntaje.dibujar(g2);
	    opcionTemporizador.dibujar(g2);
	    atras.dibujar(g2);
	}
	
	public void stop() {
		isRunning = false;
	}
	
	public void run() {
	    long lastTime = System.nanoTime();
	    double amountOfTicks = 30.0; // Aumenta a 60 FPS para una animación más suave
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
	                    
	                    atras.dibujar((Graphics2D) g);
	                    opcionTemporizador.dibujar((Graphics2D) g);
	                    opcionPuntaje.dibujar((Graphics2D) g);
	                    textoElegir.dibujar((Graphics2D) g);
	                    
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
	}
}

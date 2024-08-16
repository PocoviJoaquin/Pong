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

public class ElegirDificultadCpu extends JPanel implements Runnable {
    private JFrame frame;
    private Canvas canvas;
	
	private static final long serialVersionUID = 1L;
	
	public KL keyListener = new KL();
	public Resultado atras, continuar, textoDificultadCpu, textoElegirCpu, descripcionDificultad;
	public ML mouseListener = new ML();
	public boolean isRunning = true;
	public static int dificultadCpu = 1;
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
            if(OpcionesMenu.getFlagSonido() == 1) {
                clip.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	public ElegirDificultadCpu() {
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
		this.textoElegirCpu = new Resultado("ELEGIR DIFICULTAD DE CPU", new Font("Times New Roman", Font.PLAIN, 40), 90, 350, Color.WHITE);
        this.continuar = new Resultado("CONTINUAR", new Font("Times New Roman", Font.PLAIN, 40), 220, 250, Color.WHITE);
		this.textoDificultadCpu = new Resultado("< FACIL >", new Font("Times New Roman", Font.PLAIN, 40), 250, 450, Color.WHITE);
		this.atras = new Resultado("ATRAS", new Font("Times New Roman", Font.PLAIN, 40), 280, 650, Color.WHITE);
		this.descripcionDificultad = new Resultado("PARA UNA EXPERIENCA CASUAL", new Font("Times New Roman", Font.PLAIN, 20), 185, 500, Color.WHITE);			
		
	    Graphics2D g2 = (Graphics2D)getGraphics();
	}
	
	public static int getDificultadCpu() {
		return dificultadCpu;
	}
	
	
	public void actualizar(double deltaTime) {
	    // Actualizar colores basados en la selección
	    double mouseX = mouseListener.getMouseX();
	    double mouseY = mouseListener.getMouseY();
        int opcionHoverActual = -1;
	    if (mouseX > continuar.x
	            && mouseX < continuar.x + continuar.anchura
	            && mouseY > continuar.y - continuar.altura / 2
	            && mouseY < continuar.y + continuar.altura / 2) {
	    	continuar.color = new Color(158, 158, 158);
            opcionHoverActual = 0;
	        
	        if (mouseListener.isMousePressed()) {
	            Main.cambiarEstado(8);
	            }
		}
		
		else {
			continuar.color = Color.white;
		}
	    		
		if (mouseX > atras.x
	            && mouseX < atras.x + atras.anchura
	            && mouseY > atras.y - atras.altura / 2
	            && mouseY < atras.y + atras.altura / 2) {
			atras.color = new Color(158, 158, 158);
            opcionHoverActual = 1;
	        
	        if (mouseListener.isMousePressed()) {
	            Main.cambiarEstado(9);
	            }
		}
		
		else {
			atras.color = Color.white;
		}
		
		if (keyListener.isKeyPressed(KeyEvent.VK_LEFT)) {
			if (dificultadCpu > 1) {
				dificultadCpu--;
				if(dificultadCpu == 1) {
					textoDificultadCpu.texto = "< FACIL >";
					descripcionDificultad.texto = "PARA UNA EXPERIENCIA CASUAL";
					descripcionDificultad.x = 185.0;
				}
				
				else if(dificultadCpu == 2) {
					textoDificultadCpu.texto = "< MEDIA >";
					descripcionDificultad.texto = "PARA DESAFIARSE";
					descripcionDificultad.x = 255.0;
					
				}
                reproducirSonido("snd_select.wav");
		        try { Thread.sleep(200); } catch (InterruptedException e) { } // Pequeña pausa para evitar cambios rápidos
			}
		}
		
		if (keyListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
			if (dificultadCpu < 3) {
				dificultadCpu++;
				if(dificultadCpu == 2) {
					textoDificultadCpu.texto = "< MEDIA >";	
					descripcionDificultad.texto = "PARA DESAFIARSE";
					descripcionDificultad.x = 255.0;
				}
				
				else if(dificultadCpu == 3) {
					textoDificultadCpu.texto = "< DIFICIL >";
					descripcionDificultad.texto = "PARA UN VERDADERO RETO";
					descripcionDificultad.x = 215.0;
				}
                reproducirSonido("snd_select.wav");
		        try { Thread.sleep(200); } catch (InterruptedException e) { } // Pequeña pausa para evitar cambios rápidos
			}
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
	    
	    textoElegirCpu.dibujar(g2);
	    continuar.dibujar(g2);
	    textoDificultadCpu.dibujar(g2);
	    descripcionDificultad.dibujar(g2);
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
	                    textoDificultadCpu.dibujar((Graphics2D) g);
	                    continuar.dibujar((Graphics2D) g);
	                    descripcionDificultad.dibujar((Graphics2D) g);
	                    textoElegirCpu.dibujar((Graphics2D) g);
	                    
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
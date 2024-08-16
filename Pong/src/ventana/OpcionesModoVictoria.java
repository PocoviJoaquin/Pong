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

public class OpcionesModoVictoria extends JPanel implements Runnable {
    private JFrame frame;
    private Canvas canvas;
	
	private static final long serialVersionUID = 1L;
	
	public KL keyListener = new KL();
	public Resultado atras, elegirPuntaje, elegirTemporizador, textoElegirPuntaje, textoElegirTemporizador, elegirDiferencia, textoElegirDiferencia, jugar;
	public ML mouseListener = new ML();
	public boolean isRunning = true;
	private int opcionSeleccionada = 0;
	public static int estadoTipoPartido = 0;
	public static int puntajeGanador = 1;
	public static int diferenciaGol = 1;
	public static int temporizador = 1;
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
	
	public OpcionesModoVictoria() {
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
		this.elegirPuntaje = new Resultado("ELEGIR PUNTAJE", new Font("Times New Roman", Font.PLAIN, 40), 190, 350, Color.WHITE);
		this.textoElegirPuntaje = new Resultado("< " + puntajeGanador + " >", new Font("Times New Roman", Font.PLAIN, 40), 300, 400, Color.WHITE);
		this.elegirDiferencia = new Resultado("ELEGIR DIFERENCIA DE GOL", new Font("Times New Roman", Font.PLAIN, 40), 60, 500, Color.WHITE);
		this.textoElegirDiferencia = new Resultado("< " + diferenciaGol + " >", new Font("Times New Roman", Font.PLAIN, 40), 300, 550, Color.WHITE);
		this.elegirTemporizador = new Resultado("ELEGIR CANTIDAD DE MINUTOS", new Font("Times New Roman", Font.PLAIN, 40), 50, 350, Color.WHITE);
		this.textoElegirTemporizador = new Resultado("< " + temporizador + " >", new Font("Times New Roman", Font.PLAIN, 40), 300, 400, Color.WHITE);
		this.jugar = new Resultado("ELEGIR NOMBRE", new Font("Times New Roman", Font.PLAIN, 40), 190, 200, Color.WHITE);
		this.atras = new Resultado("ATRAS", new Font("Times New Roman", Font.PLAIN, 40), 280, 650, Color.WHITE);
		
	    Graphics2D g2 = (Graphics2D)getGraphics();
	}
	
	public static int getTipoPartido() {
		return estadoTipoPartido;
	}
	
	public static int getTemporizador() {
		return temporizador;
	}
	
	public static int getPuntaje() {
		return puntajeGanador;
	}
	
	public static int getDiferencia() {
		return diferenciaGol;
	}
	
	public void actualizar(double deltaTime) {
	    // Actualizar colores basados en la selección
	    double mouseX = mouseListener.getMouseX();
	    double mouseY = mouseListener.getMouseY();
        int opcionHoverActual = -1;
		// Controlar el color del botón "Atras"
		if (mouseX > jugar.x
	            && mouseX < jugar.x + jugar.anchura
	            && mouseY > jugar.y - jugar.altura / 2
	            && mouseY < jugar.y + jugar.altura / 2) {
			jugar.color = new Color(158, 158, 158);
            opcionHoverActual = 0;
	        
	        if (mouseListener.isMousePressed()) {
	            Main.cambiarEstado(7);
	        }
		} else {
			atras.color = Color.white;
		}
        
        if (mouseX > atras.x
	            && mouseX < atras.x + atras.anchura
	            && mouseY > atras.y - atras.altura / 2
	            && mouseY < atras.y + atras.altura / 2) {
			atras.color = new Color(158, 158, 158);
            opcionHoverActual = 1;
	        
	        if (mouseListener.isMousePressed()) {
	            Main.cambiarEstado(6);
	        }
		} else {
			atras.color = Color.white;
		}
		
		// Manejar teclas de flechas
		if (keyListener.isKeyPressed(KeyEvent.VK_LEFT)) {
			if (OpcionesPartida.getTipoPartido() == 0 && puntajeGanador > 1) {
				puntajeGanador--;
				textoElegirPuntaje.texto = "< " + puntajeGanador + " >";
				if(diferenciaGol > puntajeGanador) {
					diferenciaGol--;
					textoElegirDiferencia.texto = "< " + diferenciaGol + " >";					
				}
                reproducirSonido("snd_select.wav");
		        try { Thread.sleep(200); } catch (InterruptedException e) { } // Pequeña pausa para evitar cambios rápidos
			} else if (OpcionesPartida.getTipoPartido() == 1 && temporizador > 1) {
				temporizador--;
				textoElegirTemporizador.texto = "< " + temporizador + " >";
                reproducirSonido("snd_select.wav");
		        try { Thread.sleep(200); } catch (InterruptedException e) { } // Pequeña pausa para evitar cambios rápidos
			}
		}
		
		if (keyListener.isKeyPressed(KeyEvent.VK_A)) {
			if (OpcionesPartida.getTipoPartido() == 0 && diferenciaGol > 1) {
				diferenciaGol--;
				textoElegirDiferencia.texto = "< " + diferenciaGol + " >";
                reproducirSonido("snd_select.wav");
		        try { Thread.sleep(200); } catch (InterruptedException e) { } // Pequeña pausa para evitar cambios rápidos
			}
		}
		
		if (keyListener.isKeyPressed(KeyEvent.VK_D)) {
			if (OpcionesPartida.getTipoPartido() == 0) {
				diferenciaGol++;
				textoElegirDiferencia.texto = "< " + diferenciaGol + " >";
				if(diferenciaGol > puntajeGanador) {
					puntajeGanador++;
					textoElegirPuntaje.texto = "< " + puntajeGanador + " >";					
				}
                reproducirSonido("snd_select.wav");
		        try { Thread.sleep(200); } catch (InterruptedException e) { } // Pequeña pausa para evitar cambios rápidos
			}
		}
		
		
		
		if (keyListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
			if (OpcionesPartida.getTipoPartido() == 0) {
				puntajeGanador++;
				textoElegirPuntaje.texto = "< " + puntajeGanador + " >";
				reproducirSonido("snd_select.wav");
		        try { Thread.sleep(200); } catch (InterruptedException e) { } // Pequeña pausa para evitar cambios rápidos
			} else if (OpcionesPartida.getTipoPartido() == 1) {
				temporizador++;
				textoElegirTemporizador.texto = "< " + temporizador + " >";
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
	    
        if(OpcionesPartida.getTipoPartido() == 0) {
            elegirPuntaje.dibujar(g2);
            textoElegirPuntaje.dibujar(g2);
            elegirDiferencia.dibujar(g2);
            textoElegirDiferencia.dibujar(g2);
            jugar.dibujar(g2);	                    
            }
        
        else if(OpcionesPartida.getTipoPartido() == 1) {
            elegirTemporizador.dibujar(g2);
            textoElegirTemporizador.dibujar(g2);
            jugar.dibujar(g2);
        }

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
	                    if(OpcionesPartida.getTipoPartido() == 0) {
		                    elegirPuntaje.dibujar((Graphics2D) g);
		                    textoElegirPuntaje.dibujar((Graphics2D) g);
		                    elegirDiferencia.dibujar((Graphics2D) g);
		                    textoElegirDiferencia.dibujar((Graphics2D) g);
		                    jugar.dibujar((Graphics2D) g);	                    
		                    }
	                    
	                    else if(OpcionesPartida.getTipoPartido() == 1) {
		                    elegirTemporizador.dibujar((Graphics2D) g);
		                    textoElegirTemporizador.dibujar((Graphics2D) g);
		                    jugar.dibujar((Graphics2D) g);
	                    }
	                    
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

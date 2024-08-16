package ventana;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;
import javax.swing.JPanel;

import GUI.Pelota;
import GUI.Resultado;
import logica.KL;
import logica.ML;

public class OpcionNombreJugador extends JPanel implements Runnable {
    private JFrame frame;
    private Canvas canvas;
    private static final long serialVersionUID = 1L;

    public KL keyListener = new KL();
    public Resultado atras, elegirNombre, textoNombre, jugar;
    public ML mouseListener = new ML();
    public boolean isRunning = true;
    public static int estadoTipoPartido = 0;
    private int ultimaOpcionHover = -1;
    private int turno = 0;
    private static char[] nombre1 = new char[8];
    private static char[] nombre2 = new char[8];
    private int index = 0;

    private void reproducirSonido(String nombreArchivo) {
        try {
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

    public OpcionNombreJugador() {
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
        
        for(int i = 0;i<8;i++) {
        	nombre1[i] = '\0';
        	nombre2[i] = '\0';
        }

        this.elegirNombre = new Resultado("ELEGIR NOMBRE DEL J1", new Font("Times New Roman", Font.PLAIN, 40), 120, 350, Color.WHITE);
        this.textoNombre = new Resultado("_ _ _ _ _ _ _ _", new Font("Times New Roman", Font.PLAIN, 40), 220, 400, Color.WHITE);
        if(Menu.getEstadoJugador() == 0) {
            this.jugar = new Resultado("JUGAR", new Font("Times New Roman", Font.PLAIN, 40), 280, 200, Color.WHITE);            
        } else if(Menu.getEstadoJugador() == 1) {
            this.jugar = new Resultado("CONTINUAR", new Font("Times New Roman", Font.PLAIN, 40), 240, 200, Color.WHITE);            
        }
        this.atras = new Resultado("ATRAS", new Font("Times New Roman", Font.PLAIN, 40), 280, 650, Color.WHITE);
    }

    public void actualizar(double deltaTime) {
        double mouseX = mouseListener.getMouseX();
        double mouseY = mouseListener.getMouseY();
        int opcionHoverActual = -1;

        // Controlar el color del botón "Jugar"
        if (mouseX > jugar.x && mouseX < jugar.x + jugar.anchura && mouseY > jugar.y - jugar.altura / 2 && mouseY < jugar.y + jugar.altura / 2) {
            jugar.color = new Color(158, 158, 158);
            opcionHoverActual = 0;

            if (mouseListener.isMousePressed() && Menu.getEstadoJugador() == 1 && turno == 0 && nombre1[0] != '\0') {
                index = 0;
                turno = 1;
                this.elegirNombre.texto = "ELEGIR NOMBRE DEL J2";
                this.textoNombre.texto = "_ _ _ _ _ _ _ _";
		        try { Thread.sleep(300); } catch (InterruptedException e) { }
            }
            else if(mouseListener.isMousePressed() && Menu.getEstadoJugador() == 0) {
            	Main.cambiarEstado(14);
            }
            else if(mouseListener.isMousePressed() && turno == 1 && nombre1[0] != '\0' && nombre2[0] != '\0') {
                Main.cambiarEstado(14);            	
            }
        } else {
            jugar.color = Color.white;
        }

        // Controlar el color del botón "Atras"
        if (mouseX > atras.x && mouseX < atras.x + atras.anchura && mouseY > atras.y - atras.altura / 2 && mouseY < atras.y + atras.altura / 2) {
            atras.color = new Color(158, 158, 158);
            opcionHoverActual = 1;

            if (mouseListener.isMousePressed()) {
                Main.cambiarEstado(15);
            }
        } else {
            atras.color = Color.white;
        }

        // Reproducir sonido al cambiar entre opciones
        if (opcionHoverActual != ultimaOpcionHover) {
            if (opcionHoverActual != -1) {
                reproducirSonido("snd_select.wav");
            }
            ultimaOpcionHover = opcionHoverActual;
        }

        // Manejar la entrada del teclado para el nombre
        if (keyListener.isKeyPressed(KeyEvent.VK_BACK_SPACE) && index > 0) {
            index--;
            if (turno == 0) {
                nombre1[index] = '\0';
		        try { Thread.sleep(100); } catch (InterruptedException e) { } // Pequeña pausa para evitar cambios rápidos
            } else {
                nombre2[index] = '\0';
		        try { Thread.sleep(100); } catch (InterruptedException e) { } // Pequeña pausa para evitar cambios rápidos
            }
            actualizarTextoNombre();
        } else if (index < 8) {
            for (char c = 'A'; c <= 'Z'; c++) {
                if (keyListener.isKeyPressed(c)) {
                    if (turno == 0) {
                        nombre1[index] = c;
        		        try { Thread.sleep(200); } catch (InterruptedException e) { } // Pequeña pausa para evitar cambios rápidos
                    } else {
                        nombre2[index] = c;
        		        try { Thread.sleep(200); } catch (InterruptedException e) { } // Pequeña pausa para evitar cambios rápidos
        		        }
                    index++;
                    actualizarTextoNombre();
                    break;
                }
            }
        }
    }

    private void actualizarTextoNombre() {
        StringBuilder sb = new StringBuilder();
        char[] nombreActual = turno == 0 ? nombre1 : nombre2;
        for (char c : nombreActual) {
            sb.append(c).append(' ');
        }
        textoNombre.texto = sb.toString().trim();
    }
    
    public static String getNombre1() {
        return new String(nombre1).trim();
    }

    public static String getNombre2() {
        return new String(nombre2).trim();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, getWidth(), getHeight());

        jugar.dibujar(g2);
        elegirNombre.dibujar(g2);
        textoNombre.dibujar(g2);
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
                        jugar.dibujar((Graphics2D) g);
                        elegirNombre.dibujar((Graphics2D) g);
                        textoNombre.dibujar((Graphics2D) g);

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

// Donde se elije si salir o entrar al juego
package ventana;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JPanel;

import GUI.Pelota;
import GUI.Resultado;
import logica.KL;
import logica.ML;

public class Menu extends JPanel implements Runnable {
    private JFrame frame;
    private Canvas canvas;

    private static final long serialVersionUID = 1L;

    public KL keyListener = new KL();
    public Resultado unJugador, dosJugadores, terminarJuego, pong, opciones;
    public ML mouseListener = new ML();
    public boolean isRunning = true;
    private int opcionSeleccionada = 0;
    private static int estadoJugador = 1; // Mantener static
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


    public Menu() {
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
        this.pong = new Resultado("PONG", new Font("Arial", Font.PLAIN, 120), 170, 200, Color.WHITE);
        this.unJugador = new Resultado("UN JUGADOR", new Font("Times New Roman", Font.PLAIN, 40), 220, 350, Color.WHITE);
        this.dosJugadores = new Resultado("DOS JUGADORES", new Font("Times New Roman", Font.PLAIN, 40), 180, 450, Color.WHITE);
        this.terminarJuego = new Resultado("SALIR", new Font("Times New Roman", Font.PLAIN, 40), 280, 650, Color.WHITE);
        this.opciones = new Resultado("OPCIONES", new Font("Times New Roman", Font.PLAIN, 40), 240, 550, Color.WHITE);
    }

    public void setEstadoJugador(int numero) {
        estadoJugador = numero; // Cambiar valor del estadoJugador
    }

    public static int getEstadoJugador() {
        return estadoJugador; // Obtener estadoJugador
    }

    private void seleccionarOpcion() {
        switch (opcionSeleccionada) {
            case 0: // Empezar Juego
                Main.cambiarEstado(4);
                setEstadoJugador(0);
                break;
            case 1: // Dos Jugadores
                Main.cambiarEstado(4);
                setEstadoJugador(1);
                break;
            case 2: // Salir
                Main.cambiarEstado(2);
                break;
            case 3: // Opciones
                System.out.println("Opciones seleccionadas");
                break;
        }
    }

    public void actualizar(double deltaTime) {
        if (keyListener.isKeyPressed(KeyEvent.VK_UP)) {
            opcionSeleccionada = (opcionSeleccionada - 1 + 4) % 4;
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
            } // Pequeña pausa para evitar cambios rápidos
        } else if (keyListener.isKeyPressed(KeyEvent.VK_DOWN)) {
            opcionSeleccionada = (opcionSeleccionada + 1) % 4;
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
            } // Pequeña pausa para evitar cambios rápidos
        }

        // Selección con Enter
        if (keyListener.isKeyPressed(KeyEvent.VK_ENTER)) {
            seleccionarOpcion();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
            } // Pequeña pausa para evitar múltiples selecciones
        }

        // Actualizar colores basados en la selección
        unJugador.color = (opcionSeleccionada == 0) ? new Color(158, 158, 158) : Color.WHITE;
        dosJugadores.color = (opcionSeleccionada == 1) ? new Color(158, 158, 158) : Color.WHITE;
        terminarJuego.color = (opcionSeleccionada == 1) ? new Color(158, 158, 158) : Color.WHITE;
        opciones.color = (opcionSeleccionada == 2) ? new Color(158, 158, 158) : Color.WHITE;
        
        // Actualizar con interacción del mouse
        double mouseX = mouseListener.getMouseX();
        double mouseY = mouseListener.getMouseY();
        int opcionHoverActual = -1;
        if (mouseX > unJugador.x
                && mouseX < unJugador.x + unJugador.anchura
                && mouseY > unJugador.y - unJugador.altura / 2
                && mouseY < unJugador.y + unJugador.altura / 2) {
            unJugador.color = new Color(158, 158, 158);
            opcionHoverActual = 0;

            if (mouseListener.isMousePressed()) {
                Main.cambiarEstado(10);
                setEstadoJugador(0);
            }
        } else {
            unJugador.color = Color.white;
        }

        if (mouseX > dosJugadores.x
                && mouseX < dosJugadores.x + dosJugadores.anchura
                && mouseY > dosJugadores.y - dosJugadores.altura / 2
                && mouseY < dosJugadores.y + dosJugadores.altura / 2) {
            dosJugadores.color = new Color(158, 158, 158);
            opcionHoverActual = 1;
            
            if (mouseListener.isMousePressed()) {
                Main.cambiarEstado(4);
                setEstadoJugador(1);
            }
        } else {
            dosJugadores.color = Color.white;
        }

        if (mouseX > terminarJuego.x
                && mouseX < terminarJuego.x + terminarJuego.anchura
                && mouseY > terminarJuego.y - terminarJuego.altura / 2
                && mouseY < terminarJuego.y + terminarJuego.altura / 2) {
            terminarJuego.color = new Color(158, 158, 158);
            opcionHoverActual = 2;

            if (mouseListener.isMousePressed()) {
                Main.cambiarEstado(2);
            }
        } else {
            terminarJuego.color = Color.white;
        }

        if (mouseX > opciones.x
                && mouseX < opciones.x + opciones.anchura
                && mouseY > opciones.y - opciones.altura / 2
                && mouseY < opciones.y + opciones.altura / 2) {
            opciones.color = new Color(158, 158, 158);
            opcionHoverActual = 3;
            if (mouseListener.isMousePressed()) {
                Main.cambiarEstado(12);
            }
        } else {
            opciones.color = Color.white;
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

        pong.dibujar(g2);
        unJugador.dibujar(g2);
        dosJugadores.dibujar(g2);
        terminarJuego.dibujar(g2);
        opciones.dibujar(g2);
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

        while (isRunning) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            if (delta >= 1) {
                actualizar(1.0 / amountOfTicks);

                do {
                    do {
                        Graphics g = bs.getDrawGraphics();
                        g.setColor(Color.BLACK);
                        g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

                        pong.dibujar((Graphics2D) g);
                        unJugador.dibujar((Graphics2D) g);
                        dosJugadores.dibujar((Graphics2D) g);
                        terminarJuego.dibujar((Graphics2D) g);
                        opciones.dibujar((Graphics2D) g);

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
    }
}

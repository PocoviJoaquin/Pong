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
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import GUI.Pelota;
import GUI.Resultado;
import logica.KL;
import logica.ML;

public class OpcionesCanchas extends JPanel implements Runnable {
    private JFrame frame;
    private Canvas canvas;
    private static final long serialVersionUID = 1L;

    public KL keyListener = new KL();
    public ML mouseListener = new ML();
    public boolean isRunning = true;

    private Resultado titulo;
    private Resultado[] opciones;
    private Resultado volver;
    private int opcionSeleccionada = 0;
    private int ultimaOpcionHover = -1;

    private String[] tiposCanchas = {"DESIERTO", "PLAYA", "RETRO", "ESTANDAR"};

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

    public OpcionesCanchas() {
        frame = new JFrame("Selección de Cancha");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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

        this.titulo = new Resultado("ELEGIR CANCHA", new Font("Arial", Font.PLAIN, 60), 140, 100, Color.WHITE);
        this.opciones = new Resultado[tiposCanchas.length];
        for (int i = 0; i < tiposCanchas.length; i++) {
            this.opciones[i] = new Resultado(tiposCanchas[i], new Font("Times New Roman", Font.PLAIN, 40), 250, 250 + i * 100, Color.WHITE);
        }
        this.volver = new Resultado("VOLVER", new Font("Times New Roman", Font.PLAIN, 40), 280, 650, Color.WHITE);

        new Thread(this).start();
    }

    public void actualizar(double deltaTime) {
        if (keyListener.isKeyPressed(KeyEvent.VK_UP)) {
            opcionSeleccionada = (opcionSeleccionada - 1 + opciones.length) % opciones.length;
            reproducirSonido("snd_select.wav");
            try { Thread.sleep(200); } catch (InterruptedException e) { }
        } else if (keyListener.isKeyPressed(KeyEvent.VK_DOWN)) {
            opcionSeleccionada = (opcionSeleccionada + 1) % opciones.length;
            reproducirSonido("snd_select.wav");
            try { Thread.sleep(200); } catch (InterruptedException e) { }
        }

        if (keyListener.isKeyPressed(KeyEvent.VK_ENTER)) {
            seleccionarOpcion();
            try { Thread.sleep(200); } catch (InterruptedException e) { }
        }

        for (int i = 0; i < opciones.length; i++) {
            opciones[i].color = (opcionSeleccionada == i) ? new Color(158, 158, 158) : Color.WHITE;
        }

        double mouseX = mouseListener.getMouseX();
        double mouseY = mouseListener.getMouseY();
        int opcionHoverActual = -1;

        for (int i = 0; i < opciones.length; i++) {
            if (mouseX > opciones[i].x
                    && mouseX < opciones[i].x + opciones[i].anchura
                    && mouseY > opciones[i].y - opciones[i].altura / 2
                    && mouseY < opciones[i].y + opciones[i].altura / 2) {
                opciones[i].color = new Color(158, 158, 158);
                opcionHoverActual = i;

                if (mouseListener.isMousePressed()) {
                    seleccionarOpcion();
                }
            } else if (opcionSeleccionada != i) {
                opciones[i].color = Color.white;
            }
        }

        if (mouseX > volver.x
                && mouseX < volver.x + volver.anchura
                && mouseY > volver.y - volver.altura / 2
                && mouseY < volver.y + volver.altura / 2) {
            volver.color = new Color(158, 158, 158);
            opcionHoverActual = opciones.length;

            if (mouseListener.isMousePressed()) {
                volverAlMenu();
            }
        } else {
            volver.color = Color.white;
        }

        if (opcionHoverActual != ultimaOpcionHover) {
            if (opcionHoverActual != -1) {
                reproducirSonido("snd_select.wav");
            }
            ultimaOpcionHover = opcionHoverActual;
        }
    }

    private void seleccionarOpcion() {
        // Aquí puedes agregar la lógica para cambiar la cancha en el juego
        System.out.println("Cancha seleccionada: " + tiposCanchas[opcionSeleccionada]);
        volverAlMenu();
    }

    private void volverAlMenu() {
        stop();
        frame.dispose();
        SwingUtilities.invokeLater(() -> {
            OpcionesMenu opcionesMenu = new OpcionesMenu();
            new Thread(opcionesMenu).start();
        });
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, getWidth(), getHeight());

        titulo.dibujar(g2);
        for (Resultado opcion : opciones) {
            opcion.dibujar(g2);
        }
        volver.dibujar(g2);
    }

    public void stop() {
        isRunning = false;
    }

    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 30.0;
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

                        titulo.dibujar((Graphics2D) g);
                        for (Resultado opcion : opciones) {
                            opcion.dibujar((Graphics2D) g);
                        }
                        volver.dibujar((Graphics2D) g);

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
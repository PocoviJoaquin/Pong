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
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.io.File;

public class OpcionesMenu extends JPanel implements Runnable {
    private JFrame frame;
    private Canvas canvas;
    
    private static final long serialVersionUID = 1L;
    
    public KL keyListener = new KL();
    public Resultado atras, textoElegirSonido, textoFlagSonido, elegirCancha, textoElegirOnFire, textoFlagOnFire;
    public ML mouseListener = new ML();
    public boolean isRunning = true;
    public static int flagSonido = 1;
    public static int flagOnFire = 1;
    private int ultimaOpcionHover = -1;
    
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
            if(getFlagSonido() == 1) {
                clip.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public OpcionesMenu() {
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
        this.textoElegirSonido = new Resultado("HABILITAR SONIDO?", new Font("Times New Roman", Font.PLAIN, 40), 160, 150, Color.WHITE);
        if(getFlagSonido() == 1) {
            this.textoFlagSonido = new Resultado("< SI >", new Font("Times New Roman", Font.PLAIN, 40), 290, 200, Color.WHITE);            
        }
        else if(getFlagSonido() == 0) {
            this.textoFlagSonido = new Resultado("< NO >", new Font("Times New Roman", Font.PLAIN, 40), 290, 200, Color.WHITE);
        }
        this.textoElegirOnFire = new Resultado("HABILITAR MODO 'ON FIRE'?", new Font("Times New Roman", Font.PLAIN, 40), 80, 350, Color.WHITE);
        if(getFlagOnFire() == 1) {
            this.textoFlagOnFire = new Resultado("< SI >", new Font("Times New Roman", Font.PLAIN, 40), 290, 400, Color.WHITE);            
        }
        else if(getFlagOnFire() == 0) {
            this.textoFlagOnFire = new Resultado("< NO >", new Font("Times New Roman", Font.PLAIN, 40), 290, 400, Color.WHITE);
        }
        
        this.atras = new Resultado("ATRAS", new Font("Times New Roman", Font.PLAIN, 40), 280, 650, Color.WHITE);
        this.elegirCancha = new Resultado("ELEGIR CANCHA", new Font("Times New Roman", Font.PLAIN, 40), 200, 500, Color.WHITE);
    }
    
    public static int getFlagSonido() {
        return flagSonido;
    }
    
    public static int getFlagOnFire() {
    	return flagOnFire;
    }
    
    public void actualizar(double deltaTime) {
        double mouseX = mouseListener.getMouseX();
        double mouseY = mouseListener.getMouseY();
        int opcionHoverActual = -1;
                
        if (mouseX > atras.x
                && mouseX < atras.x + atras.anchura
                && mouseY > atras.y - atras.altura / 2
                && mouseY < atras.y + atras.altura / 2) {
            atras.color = new Color(158, 158, 158);
            opcionHoverActual = 1;
            
            if (mouseListener.isMousePressed()) {
                Main.cambiarEstado(11);
            }
        } else {
            atras.color = Color.white;
        }
        
        if (mouseX > elegirCancha.x
                && mouseX < elegirCancha.x + elegirCancha.anchura
                && mouseY > elegirCancha.y - elegirCancha.altura / 2
                && mouseY < elegirCancha.y + elegirCancha.altura / 2) {
            elegirCancha.color = new Color(158, 158, 158);
            opcionHoverActual = 2;
            
            if (mouseListener.isMousePressed()) {
                SwingUtilities.invokeLater(() -> new OpcionesCanchas());
                stop();
            }
        } else {
            elegirCancha.color = Color.white;
        }
        
        if (keyListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            if (flagSonido > 0) {
                flagSonido--;
                textoFlagSonido.texto = "< NO >";
                reproducirSonido("snd_select.wav");
                try { Thread.sleep(200); } catch (InterruptedException e) { }
            }
        }
        
        if (keyListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            if (flagSonido < 1) {
                flagSonido++;
                textoFlagSonido.texto = "< SI >";
                reproducirSonido("snd_select.wav");
                try { Thread.sleep(200); } catch (InterruptedException e) { }
            }
        }
        
        if (keyListener.isKeyPressed(KeyEvent.VK_A)) {
            if (flagOnFire > 0) {
            	flagOnFire--;
                textoFlagOnFire.texto = "< NO >";
                reproducirSonido("snd_select.wav");
                try { Thread.sleep(200); } catch (InterruptedException e) { }
            }
        }
        
        if (keyListener.isKeyPressed(KeyEvent.VK_D)) {
            if (flagOnFire < 1) {
            	flagOnFire++;
                textoFlagOnFire.texto = "< SI >";
                reproducirSonido("snd_select.wav");
                try { Thread.sleep(200); } catch (InterruptedException e) { }
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
        
        textoElegirSonido.dibujar(g2);
        textoFlagSonido.dibujar(g2);
        atras.dibujar(g2);
        textoElegirOnFire.dibujar(g2);
        textoFlagOnFire.dibujar(g2);
        //elegirCancha.dibujar(g2);
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
                        textoElegirSonido.dibujar((Graphics2D) g);
                        textoFlagSonido.dibujar((Graphics2D) g);
                        textoElegirOnFire.dibujar((Graphics2D) g);
                        textoFlagOnFire.dibujar((Graphics2D) g);
                        //elegirCancha.dibujar((Graphics2D) g);
                        
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
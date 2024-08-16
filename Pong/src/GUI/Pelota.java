// Tiene la lógica y la interfaz de la pelota
package GUI;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.LinkedList;

import ventana.App;
import ventana.Menu;
import ventana.OpcionesMenu;

import java.io.File;

public class Pelota {
    public Rectangulo rectangulo;
    public Rectangulo barraIzquierda, barraDerecha;
    public Resultado resultadoIzquierdaTexto, resultadoDerechaTexto;
    
    private double direcciony;
    private double direccionx;
    private double y;
    private double x;
    
    // Variables para el reinicio de la posición
    private final double initialX;
    private final double initialY;
    
    // Variables para la velocidad
    private final double velocidadInicialX = 200.0;
    private final double velocidadInicialY = 10.0;
    private double velocidadY = 150;
    private double velocidadX = 150;
    private double velocidadIncremento = 10.0;
    public static boolean controlesIntercambiados;
    
    private LinkedList<int[]> posicionesAnteriores;
    private final int maxEstela = 10; // Tamaño máximo de la estela
    
    public Pelota(Rectangulo rectangulo, Rectangulo barraIzquierda, Rectangulo barraDerecha, Resultado resultadoIzquierdaTexto, Resultado resultadoDerechaTexto) {
        this.rectangulo = rectangulo;
        this.barraIzquierda = barraIzquierda;
        this.barraDerecha = barraDerecha;
        this.x = rectangulo.x;
        this.y = rectangulo.y;
        this.resultadoIzquierdaTexto = resultadoIzquierdaTexto;
        this.resultadoDerechaTexto = resultadoDerechaTexto;
        
        // Guardar la posición inicial de la pelota
        this.initialX = rectangulo.x;
        this.initialY = rectangulo.y;

        // Establecer la velocidad inicial
        this.direccionx = -velocidadInicialX;
        this.direcciony = velocidadInicialY;
        
        posicionesAnteriores = new LinkedList<>();
    }
    
    public double calcularVelocidadAngulo(Rectangulo barra) {
        double interseccionRelativaY = (barra.y + (barra.altura / 2.0)) - (this.rectangulo.y + (this.rectangulo.altura / 2.0));
        double interseccionNormalY = interseccionRelativaY / (barra.altura / 2.0);
        double theta = interseccionNormalY * 75;
        
        return Math.toRadians(theta);
    }
    
    private void parpadearMarcador(final Resultado marcador) {
        final Color originalColor = marcador.getColor();
        final Timer timer = new Timer(200, null);
        final int[] count = {0};

        timer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (count[0] < 6) {
                    marcador.setColor((count[0] % 2 == 0) ? Color.RED : originalColor);
                    count[0]++;
                } else {
                    marcador.setColor(originalColor);
                    timer.stop();
                }
            }
        });

        timer.start();
    }
    
    private Color calcularColorEstela() {
        // Calcula la magnitud de la velocidad de la pelota
        float velocidad = (float) Math.sqrt(direccionx * direccionx + direcciony * direcciony);

        if (velocidad < 500.0f) {
            return new Color(0.0f, 0.0f, 0.0f, 0.0f); // Totalmente transparente
        }

        float intensidad = Math.min(1.0f, velocidad / 700.0f);
        return new Color(1.0f, intensidad, 0.0f, intensidad); // Rojo a Naranja con transparencia
    }


    // Método para dibujar la estela
    public void dibujarEstela(Graphics2D g2) {
        Color colorEstela = calcularColorEstela();
        g2.setColor(colorEstela);
        for (int[] pos : posicionesAnteriores) {
            g2.fillRect(pos[0], pos[1], rectangulo.anchura, rectangulo.altura);
        }
    }

    public void actualizar(double deltaTime) {
    	
        posicionesAnteriores.addFirst(new int[] {(int) x, (int) y});
        if (posicionesAnteriores.size() > maxEstela) {
            posicionesAnteriores.removeLast();
        }
    	
    	int numeroTicks = 0;
    	numeroTicks++;
    	
    	if(numeroTicks % 60 == 0) {
    		velocidadY += 30;
    		velocidadX += 30;
    	}
    	
        if(direcciony >= 0.0) {
            if(this.rectangulo.y + this.rectangulo.altura > 690) {
                direcciony *= -1;
                reproducirSonido("snd_impact.wav");
            }
        }
        else if(direcciony < 0.0) {
            if(this.rectangulo.y < 40) {
                direcciony *= -1;
                reproducirSonido("snd_impact.wav");
            }
        }

        if(direccionx < 0.0) {
            if(this.rectangulo.x + (direccionx * deltaTime) < barraIzquierda.x + barraIzquierda.anchura) {
                if(this.rectangulo.y + (direcciony * deltaTime) > barraIzquierda.y &&
                        this.rectangulo.y + (direcciony * deltaTime) + this.rectangulo.altura < barraIzquierda.y + barraIzquierda.altura) {
                    double theta = calcularVelocidadAngulo(barraIzquierda);
                    double nuevoVx = Math.abs(Math.cos(theta)) * (Math.abs(direccionx) + velocidadX);
                    double nuevoVy = (-Math.sin(theta)) * (Math.abs(direcciony) + velocidadY);
                    double signoViejo = Math.signum(direccionx);
                    this.direccionx = nuevoVx * (-1.0 * signoViejo);
                    this.direcciony = nuevoVy;
                    reproducirSonido("snd_impact.wav");
                }
            }
        }
        else if(direccionx >= 0.0) {
            if(this.rectangulo.x + (direccionx * deltaTime) + rectangulo.anchura > barraDerecha.x) {
                if(this.rectangulo.y + (direcciony * deltaTime) > barraDerecha.y &&
                        this.rectangulo.y + (direcciony * deltaTime) + this.rectangulo.altura < barraDerecha.y + barraDerecha.altura) {
                    double theta = calcularVelocidadAngulo(barraDerecha);
                    double nuevoVx = Math.abs(Math.cos(theta)) * (Math.abs(direccionx) + velocidadX);
                    double nuevoVy = (-Math.sin(theta)) * (Math.abs(direcciony) + velocidadY);
                    double signoViejo = Math.signum(direccionx);
                    this.direccionx = nuevoVx * (-1.0 * signoViejo);
                    this.direcciony = nuevoVy;
                    reproducirSonido("snd_impact.wav");
                }
            }
        }

        this.y += direcciony * deltaTime;
        this.x += direccionx * deltaTime;

        this.rectangulo.y = (int) y;
        this.rectangulo.x = (int) x;

        // Caso donde se da punto para el segundo jugador o computadora
        if (this.rectangulo.x < barraIzquierda.x) {
            int resultadoDerecha = Integer.parseInt(resultadoDerechaTexto.texto);
            resultadoDerecha++;
            resultadoDerechaTexto.texto = "" + resultadoDerecha;
            if(Menu.getEstadoJugador() == 0) {
                reproducirSonido("snd_wrongvictory.wav");          	
            }
            else if(Menu.getEstadoJugador() == 1) {
                reproducirSonido("snd_dumbvictory.wav");           	
            }
            parpadearMarcador(resultadoDerechaTexto); // Parpadear marcador derecha
            reiniciarPosicion(true);
        }
        // Caso donde se da punto para el primer jugador
        else if (this.rectangulo.x > barraDerecha.x + barraDerecha.anchura) {
            int resultadoIzquierda = Integer.parseInt(resultadoIzquierdaTexto.texto);
            resultadoIzquierda++;
            resultadoIzquierdaTexto.texto = "" + resultadoIzquierda;
            reproducirSonido("snd_dumbvictory.wav");           	
            parpadearMarcador(resultadoIzquierdaTexto); // Parpadear marcador izquierda
            reiniciarPosicion(false);
        }
    }

    // Método para reiniciar la posición y dirección de la pelota
    private void reiniciarPosicion(boolean direccionDerecha) {
        this.rectangulo.x = (int) initialX;
        this.rectangulo.y = (int) initialY;
        this.x = initialX;
        this.y = initialY;
        
        // Reiniciar la velocidad a su valor inicial
        if (direccionDerecha) {
            this.direccionx = -velocidadInicialX;
            this.direcciony = velocidadInicialY;
        } else {
            this.direccionx = velocidadInicialX;
            this.direcciony = -velocidadInicialY;
        }
    }
    
    public void dibujar(Graphics2D g2) {
    	if(OpcionesMenu.getFlagOnFire() == 1) {
            dibujarEstela(g2);    		
    	}
        rectangulo.dibujar(g2);
    }
    
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


/*		if(vx < 0) {
			if(this.rectangulo.x <= this.barraIzquierda.x + this.barraIzquierda.anchura
				&& this.rectangulo.x + this.rectangulo.anchura >= this.barraIzquierda.x
				&& this.rectangulo.y >= this.barraIzquierda.y
				&& this.rectangulo.y <= this.barraIzquierda.y + this.barraIzquierda.altura) {
					this.vx *= -1.01;
					this.vy *= -1.01;
			}
		}
		
		else if(this.rectangulo.x + this.rectangulo.anchura < this.barraIzquierda.x) {
			// Punto para la computadora o segundo jugador
		}

		
		else if(vx > 0) {
			if(this.rectangulo.x + this.rectangulo.anchura >= this.barraDerecha.x
				&& this.rectangulo.x <= this.barraDerecha.x + this.barraDerecha.anchura
				&& this.rectangulo.y >= this.barraDerecha.y
				&& this.rectangulo.y <= this.barraDerecha.y + this.barraDerecha.altura) {
					this.vx *= -1.01;
					this.vy *= -1.01;
			}
			
		}
		
		else if(this.rectangulo.x + this.rectangulo.anchura > this.barraDerecha.x + this.barraDerecha.anchura) {
			// Punto para el jugador
		}
		
		if(vy > 0) {
			if(this.rectangulo.y + this.rectangulo.altura > 670) {
				this.vy *= -1.01;
			}
		}
		
		else if(vy < 0) {
			if(this.rectangulo.y < 30) {
				this.vy *= -1.01;
			}
		}
		
		this.rectangulo.x += vx * deltaTime;
		this.rectangulo.y += vy * deltaTime;
		*/
	}

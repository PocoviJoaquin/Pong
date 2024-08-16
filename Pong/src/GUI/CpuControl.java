// Cómo se mueve la computadora
package GUI;

import java.util.Random;

import ventana.ElegirDificultadCpu;

public class CpuControl {
    public ControlJugador controlJugador;
    public Rectangulo pelota;
    private double tiempoReaccion;
    private double errorMargen;
    private Random random;
    private double velocidadExtra;  // Nueva variable para incrementar la velocidad

    public CpuControl(ControlJugador controlJugador, Rectangulo pelota) {
        this.controlJugador = controlJugador;
        this.pelota = pelota;
        if(ElegirDificultadCpu.getDificultadCpu() == 1) {
        	this.tiempoReaccion = 0.06;
        }
        
        else if(ElegirDificultadCpu.getDificultadCpu() == 2) {
        	this.tiempoReaccion = 0.04;
        }
        
        else if(ElegirDificultadCpu.getDificultadCpu() == 3) {
        	this.tiempoReaccion = 0;
        }
        this.tiempoReaccion = 0.07; // Reducir el tiempo de reacción de la CPU
        this.errorMargen = 3; // Reducir el margen de error para mayor precisión
        this.velocidadExtra = 1.5; // Incrementar la velocidad para hacerla más rápida
        this.random = new Random();
    }

    public void actualizar(double deltaTime) {
        controlJugador.actualizar(deltaTime);

        // Retardar la reacción de la CPU
        tiempoReaccion -= deltaTime;
        if (tiempoReaccion > 0) {
            return;
        }

        // Resetear el tiempo de reacción para el próximo movimiento
        if(ElegirDificultadCpu.getDificultadCpu() == 1) {
        	this.tiempoReaccion = 0.08;
        }
        
        else if(ElegirDificultadCpu.getDificultadCpu() == 2) {
        	this.tiempoReaccion = 0.06;
        }
        
        else if(ElegirDificultadCpu.getDificultadCpu() == 3) {
        	this.tiempoReaccion = 0.04;
        }

        // Introducir un error aleatorio en la precisión de la CPU
        int error = random.nextInt((int) errorMargen * 2) - (int) errorMargen;

        // Movimiento de la CPU con mayor velocidad y con posibilidad de error
        if (pelota.y < controlJugador.rectangulo.y + 10 + error) {
            controlJugador.moverArribaCpu(deltaTime * velocidadExtra);
        } else if (pelota.y + pelota.altura > controlJugador.rectangulo.y - 10 + error) {
            controlJugador.moverAbajoCpu(deltaTime * velocidadExtra);
        }
    }
}

package ventana;

import javax.swing.SwingUtilities;

public class Main {
    public static int estado = 0;
    public static Thread threadPrincipal;
    public static Menu menu;
    public static App app;
    public static OpcionesPartida opcionesPartida;
    public static OpcionesModoVictoria opcionesModoVictoria;
    public static ElegirDificultadCpu elegirDificultadCpu;
    public static OpcionesMenu opcionesMenu;
    public static OpcionNombreJugador opcionNombreJugador;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            menu = new Menu();
            threadPrincipal = new Thread(menu);
            threadPrincipal.start();
        });
    }

    public static void cambiarEstado(int nuevoEstado) {
        if (nuevoEstado == estado) {
            return; // Si el estado no cambia, no hacer nada
        }
        
        // Manejar transición del menú al juego
        if (nuevoEstado == 1 && estado == 0) {
            if (menu != null) {
                menu.stop();
            }
            app = new App();
            threadPrincipal = new Thread(app);
            threadPrincipal.start();
        }
        // Manejar transición del juego al menú
        else if (nuevoEstado == 0 && estado == 7) {
            if (app != null) {
                app.stop();
            }
            menu = new Menu(); // Solo crea una nueva instancia si es null
            threadPrincipal = new Thread(menu);
            threadPrincipal.start();
        }
        
        else if(nuevoEstado == 2) {
        	menu.stop();
        }
        // Manejar opciones de partida
        else if (nuevoEstado == 4) {
            if (menu != null) {
                menu.stop();
            }
            if (app != null) {
                app.stop();
            }
            opcionesPartida = new OpcionesPartida();
            threadPrincipal = new Thread(opcionesPartida);
            threadPrincipal.start();
        }
        // Manejar opciones de modo de victoria
        else if (nuevoEstado == 5) {
            if (menu != null) {
                menu.stop();
            }
            if (opcionesPartida != null) {
                opcionesPartida.stop();
            }
            opcionesModoVictoria = new OpcionesModoVictoria();
            threadPrincipal = new Thread(opcionesModoVictoria);
            threadPrincipal.start();
        }
        
        else if (nuevoEstado == 6) {
            if (opcionesModoVictoria != null) {
                opcionesModoVictoria.stop();
            }
            opcionesPartida = new OpcionesPartida();
            threadPrincipal = new Thread(opcionesPartida);
            threadPrincipal.start();
        }
        // Regresar al menú desde las opciones
        else if (nuevoEstado == 3) {
        	if(opcionesPartida != null) {
        		opcionesPartida.stop();
        	}
            elegirDificultadCpu = new ElegirDificultadCpu();
            threadPrincipal = new Thread(elegirDificultadCpu);
            threadPrincipal.start();
        }
        
        else if (nuevoEstado == 7) {
            if (opcionesModoVictoria != null) {
                opcionesModoVictoria.stop();
            }
            opcionNombreJugador = new OpcionNombreJugador();
            threadPrincipal = new Thread(opcionNombreJugador);
            threadPrincipal.start();
        }
        
        else if (nuevoEstado == 8) {
            if (elegirDificultadCpu != null) {
            	elegirDificultadCpu.stop();
            }
            opcionesPartida = new OpcionesPartida();
            threadPrincipal = new Thread(opcionesPartida);
            threadPrincipal.start();
        }
        
        else if (nuevoEstado == 9) {
            if (elegirDificultadCpu != null) {
            	elegirDificultadCpu.stop();
            }
            menu = new Menu();
            threadPrincipal = new Thread(menu);
            threadPrincipal.start();
        }
        
        else if (nuevoEstado == 10) {
            if (menu != null) {
            	menu.stop();
            }
            elegirDificultadCpu = new ElegirDificultadCpu();
            threadPrincipal = new Thread(elegirDificultadCpu);
            threadPrincipal.start();
        }
        
        else if (nuevoEstado == 11) {
            if (opcionesMenu != null) {
            	opcionesMenu.stop();
            }
            menu = new Menu();
            threadPrincipal = new Thread(menu);
            threadPrincipal.start();
        }
        
        else if (nuevoEstado == 12) {
            if (menu != null) {
            	menu.stop();
            }
            opcionesMenu = new OpcionesMenu();
            threadPrincipal = new Thread(opcionesMenu);
            threadPrincipal.start();
        }
        
        else if (nuevoEstado == 13) {
        	if(opcionesPartida != null) {
        		opcionesPartida.stop();
        	}
            menu = new Menu();
            threadPrincipal = new Thread(menu);
            threadPrincipal.start();
        }
        
        else if (nuevoEstado == 14) {
        	if(opcionNombreJugador != null) {
        		opcionNombreJugador.stop();
        	}
            app = new App();
            threadPrincipal = new Thread(app);
            threadPrincipal.start();
        }
        
        else if (nuevoEstado == 15) {
        	if(opcionNombreJugador != null) {
        		opcionNombreJugador.stop();
        	}
            opcionesModoVictoria = new OpcionesModoVictoria();
            threadPrincipal = new Thread(opcionesModoVictoria);
            threadPrincipal.start();
        }
        
        estado = nuevoEstado;
    }
}

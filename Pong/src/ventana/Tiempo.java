// Define el tiempo

package ventana;

public class Tiempo {
	public static double tiempoEmpezado = System.nanoTime();
	
	public static double getTiempo() {
		return (System.nanoTime() - tiempoEmpezado) * 1E-9;
	}
}

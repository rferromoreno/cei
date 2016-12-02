package modulos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import excepciones.lexicas.ExcepcionLexica;
import excepciones.semanticas.ExcepcionSemantica;
import excepciones.sintacticas.ExcepcionSintactica;

public class Principal {

	public static void main(String[] args) throws Exception{
		try {				
			if (args.length == 0 || args.length > 2) {
				System.out.println("[Error] Cantidad invalida de argumentos.");
				System.out.println("Modo de uso: java modulos.Principal <Entrada> [<Salida>]");
				System.out.println("En caso de no especificarse archivo de salida, se le otorgara el mismo nombre que el de entrada con extension .ceiasm");
			} else {
				File archEntrada = new File(args[0]);
				if (!archEntrada.exists()) {
					System.out.println("[Error] No existe el archivo de entrada especificado.");
				} else {
					// Si no ingresaron archivo de salida, preparo el nombre de uno con extension ceiasm
					String salida = null;
					if (args.length == 1) {
						salida = args[0] + ".ceiasm";
						System.out.println("[Advertencia] No se especifico archivo de salida."); 
						System.out.println("[Advertencia] Se utilizara la siguiente salida: \""+salida+"\".");
					} else {
						salida = args[1];
					}
					
					// De ser posible, creo el nuevo archivo
					File archSalida = new File(salida);
					try {
						if (!archSalida.exists()) {
							archSalida.createNewFile();
						}
					} catch (Exception e) {
						System.out.println("[Error] Fallo al intentar crear el archivo de salida");
					}
	
					if (archSalida.exists()) {
						BufferedWriter bw = new BufferedWriter(new FileWriter(archSalida));
						GenCod.setBuffer(bw);
						BufferedReader br = new BufferedReader(new FileReader(archEntrada));
						AnalizadorSintactico asintactico = new AnalizadorSintactico(br);
						asintactico.analizar();
					}
				}
			}
		} catch (IOException e1) {
			System.out.println("Error de archivos. Revisar que el archivo de entrada sea correcto."); 
		} catch (ExcepcionLexica e2) {
			System.out.println("No se pudo completar el analisis lexico.");
		} catch (ExcepcionSintactica e3) {
			System.out.println("No se pudo completar el analisis sintactico.");
		} catch (ExcepcionSemantica e4) {
			System.out.println("No se pudo completar el analisis semantico.");
		} catch (Exception e5) {
			System.out.println("Se produjo un error.");
		}
	}

}

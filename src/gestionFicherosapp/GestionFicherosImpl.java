package gestionFicherosapp;

import java.io.File;
import java.io.IOException;

import gestionficheros.FormatoVistas;
import gestionficheros.GestionFicheros;
import gestionficheros.GestionFicherosException;
import gestionficheros.TipoOrden;

public class GestionFicherosImpl implements GestionFicheros {
	private File carpetaDeTrabajo = null;
	private Object[][] contenido;
	private int filas = 0;
	private int columnas = 3;
	private FormatoVistas formatoVistas = FormatoVistas.NOMBRES;
	private TipoOrden ordenado = TipoOrden.DESORDENADO;

	public GestionFicherosImpl() {
		carpetaDeTrabajo = File.listRoots()[0];
		actualiza();
	}

	private void actualiza() {

		String[] ficheros = carpetaDeTrabajo.list(); // obtener los nombres
		// calcular el número de filas necesario
		filas = ficheros.length / columnas;
		if (filas * columnas < ficheros.length) {
			filas++; // si hay resto necesitamos una fila más
		}

		// dimensionar la matriz contenido según los resultados

		contenido = new String[filas][columnas];
		// Rellenar contenido con los nombres obtenidos
		for (int i = 0; i < columnas; i++) {
			for (int j = 0; j < filas; j++) {
				int ind = j * columnas + i;
				if (ind < ficheros.length) {
					contenido[j][i] = ficheros[ind];
				} else {
					contenido[j][i] = "";
				}
			}
		}
	}

	@Override
	public void arriba() {

		System.out.println("holaaa");
		if (carpetaDeTrabajo.getParentFile() != null) {
			carpetaDeTrabajo = carpetaDeTrabajo.getParentFile();
			actualiza();
		}

	}

	@Override
	public void creaCarpeta(String arg0) throws GestionFicherosException {
		File file = new File(carpetaDeTrabajo,arg0);
		boolean temp=true;
		if(file.exists()) {
			//que no exista -> lanzará una excepción
			temp=false;
			throw new GestionFicherosException("Esta carpeta ya existe ");
		}
		if(!carpetaDeTrabajo.canWrite()) {
			//que se pueda escribir -> lanzará una excepción
			temp=false;
			throw new GestionFicherosException("No tienes permiso de escritura en "+file.getParent());
		}
		if(temp) {
			file.mkdirs();
		}


		//crear la carpeta -> lanzará una excepción
		actualiza();
	}

	@Override
	public void creaFichero(String arg0) throws GestionFicherosException {
		File file = new File(carpetaDeTrabajo,arg0);
		boolean temp=true;
		if(file.exists()) {
			//que no exista -> lanzará una excepción
			temp=false;
			throw new GestionFicherosException("Esta carpeta ya existe ");
		}
		if(carpetaDeTrabajo.canWrite()) {
			//que se pueda escribir -> lanzará una excepción
			temp=false;
			throw new GestionFicherosException("No tienes permiso de escritura en "+file.getParent());
		}
		if(temp) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				throw new GestionFicherosException("Este fichero ya existe");
			}
		}
		actualiza();

	}

	@Override
	public void elimina(String arg0) throws GestionFicherosException {
		File file = new File(carpetaDeTrabajo,arg0);
		boolean temp=true;
		if(!file.exists()) {
			//que no exista -> lanzará una excepción
			temp=false;
			throw new GestionFicherosException("Este fichero no existe ");
		}
		if(!carpetaDeTrabajo.canWrite()) {
			//que se pueda escribir -> lanzará una excepción
			temp=false;
			throw new GestionFicherosException("No tienes permiso de escritura en "+file.getParent());
		}
		if(temp) {
			file.delete();
		}


		//crear la carpeta -> lanzará una excepción
		actualiza();
	}

	@Override
	public void entraA(String arg0) throws GestionFicherosException {
		File file = new File(carpetaDeTrabajo, arg0);//IMPORTANTE
		// se controla que el nombre corresponda a una carpeta existente
		if (!file.isDirectory()) {
			throw new GestionFicherosException("Error. Se ha encontrado "
					+ file.getAbsolutePath()
					+ " pero se esperaba un directorio");
		}
		// se controla que se tengan permisos para leer carpeta
		if (!file.canRead()) {
			throw new GestionFicherosException("Alerta. No se puede acceder a "
					+ file.getAbsolutePath() + ". No hay permiso");
		}
		// nueva asignación de la carpeta de trabajo
		carpetaDeTrabajo = file;
		// se requiere actualizar contenido
		actualiza();

	}

	@Override
	public int getColumnas() {
		return columnas;
	}

	@Override
	public Object[][] getContenido() {
		return contenido;
	}

	@Override
	public String getDireccionCarpeta() {
		return carpetaDeTrabajo.getAbsolutePath();
	}

	@Override
	public String getEspacioDisponibleCarpetaTrabajo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getEspacioTotalCarpetaTrabajo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getFilas() {
		return filas;
	}

	@Override
	public FormatoVistas getFormatoContenido() {
		return formatoVistas;
	}

	@Override
	public String getInformacion(String arg0) throws GestionFicherosException {

		StringBuilder strBuilder=new StringBuilder();
		File file = new File(carpetaDeTrabajo,arg0);
		boolean temp;

		if (file.exists() && file.canRead()) {
			//Título
			strBuilder.append("INFORMACIÓN DEL SISTEMA");
			strBuilder.append("\n\n");
			//Nombre
			strBuilder.append("Nombre: ");
			strBuilder.append(arg0);
			strBuilder.append("\n");
			//Tipo: fichero o directorio
			if (file.isDirectory()) {
				temp = true;
				strBuilder.append("Directorio");
				strBuilder.append("\n");
			} else {
				temp = false;
				strBuilder.append("Fichero");
				strBuilder.append("\n");
			}
			//Ubicación
			strBuilder.append("Ruta: ");
			strBuilder.append(file.getAbsolutePath());
			strBuilder.append("\n");
			//Fecha de última modificación
			strBuilder.append("Ultima modificación: ");
			strBuilder.append(file.lastModified());
			strBuilder.append("\n");

			if (temp) {
				//Fichero que contiene
				strBuilder.append("Ficheros que contiene: ");
				String[] x = file.list();
				strBuilder.append(x.length);
				strBuilder.append("\n");
				//Espacio libre
				strBuilder.append("Espacio libre: ");
				strBuilder.append(file.getFreeSpace());
				strBuilder.append("\n");
				//Espacio  disponible
				strBuilder.append("Espacio disponible: ");
				strBuilder.append(file.getUsableSpace());
				strBuilder.append("\n");
				//Espacio total
				strBuilder.append("Espacio total: ");
				strBuilder.append(file.getTotalSpace());
				strBuilder.append("\n");
			} else {
				if (file.isHidden()) {
					//Si es un fichero oculto o no
					strBuilder.append("Este fichero esta oculto");
					strBuilder.append("\n");
				}
				//Tamaño total
				strBuilder.append("Tamaño total: ");
				strBuilder.append(file.getTotalSpace());
				strBuilder.append("\n");
			} 
		}else {
			//Controlar que existe. Si no, se lanzará una excepción
			//Controlar que haya permisos de lectura. Si no, se lanzará una excepción
			throw new GestionFicherosException("Fichero en ruta: "+file.getAbsolutePath()+" no existe o no se tiene acceso");

		}


		//Si es un fichero: Tamaño en bytes

		//Si es directorio: Número de elementos que contiene, 
		//Si es directorio: Espacio libre, espacio disponible, espacio total (bytes)

		return strBuilder.toString();
	}

	@Override
	public boolean getMostrarOcultos() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getNombreCarpeta() {
		return carpetaDeTrabajo.getName();
	}

	@Override
	public TipoOrden getOrdenado() {
		return ordenado;
	}

	@Override
	public String[] getTituloColumnas() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getUltimaModificacion(String arg0)
			throws GestionFicherosException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String nomRaiz(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int numRaices() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void renombra(String arg0, String arg1)
			throws GestionFicherosException {
		File file1 = new File(carpetaDeTrabajo,arg0);
		File file2 = new File(carpetaDeTrabajo,arg1);
		boolean temp=true;
		if(!file1.exists()) {
			temp=false;
			throw new GestionFicherosException("No existe el archivo a renombrar");
		}
		if(file2.exists()) {
			temp=false;
			throw new GestionFicherosException("El nombre ya existe");
		}
		if(!carpetaDeTrabajo.canWrite()) {
			//que se pueda escribir -> lanzará una excepción
			temp=false;
			throw new GestionFicherosException("No tienes permiso de escritura en "+file1.getParent());
		}
		if(temp) {
			boolean correcto = file1.renameTo(file2);
			if(!correcto) {
				throw new GestionFicherosException("El renombrado no se ha podido realizar");
			}
		}


		//crear la carpeta -> lanzará una excepción
		actualiza();

	}

	@Override
	public boolean sePuedeEjecutar(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sePuedeEscribir(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sePuedeLeer(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setColumnas(int arg0) {
		columnas = arg0;

	}

	@Override
	public void setDirCarpeta(String arg0) throws GestionFicherosException {
		File file = new File(arg0);

		// se controla que la dirección exista y sea directorio
		if (!file.isDirectory()) {
			throw new GestionFicherosException("Error. Se esperaba "
					+ "un directorio, pero " + file.getAbsolutePath()
					+ " no es un directorio.");
		}

		// se controla que haya permisos para leer carpeta
		if (!file.canRead()) {
			throw new GestionFicherosException(
					"Alerta. No se puede acceder a  " + file.getAbsolutePath()
					+ ". No hay permisos");
		}

		// actualizar la carpeta de trabajo
		carpetaDeTrabajo = file;

		// actualizar el contenido
		actualiza();

	}

	@Override
	public void setFormatoContenido(FormatoVistas arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMostrarOcultos(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setOrdenado(TipoOrden arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSePuedeEjecutar(String arg0, boolean arg1)
			throws GestionFicherosException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSePuedeEscribir(String arg0, boolean arg1)
			throws GestionFicherosException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSePuedeLeer(String arg0, boolean arg1)
			throws GestionFicherosException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setUltimaModificacion(String arg0, long arg1)
			throws GestionFicherosException {
		// TODO Auto-generated method stub

	}

}

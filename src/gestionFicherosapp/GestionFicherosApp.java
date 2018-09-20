package gestionFicherosapp;

import gestionficheros.MainGUI;

public class GestionFicherosApp {

	public static void main(String[] args) {
		GestionFicherosImpl getFicherosImpl = new GestionFicherosImpl();
		new MainGUI(getFicherosImpl).setVisible(true);

	}

}

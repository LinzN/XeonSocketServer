package de.nlinz.javaSocket.server;

public class SocketDebug {
	private static String prefix = "[SocketServer]";

	public static void sendStartSuccess(String adress) {
		System.out.println(prefix + "Server " + adress + " succesful started!");
	}

	public static void sendStartError(int port) {
		System.out.println(prefix + "Fehler beim Starten des HostServers auf Port " + port);
	}

	public static void sendShutdownSuccess() {
		System.out.println(prefix + "Server successful closed!");
	}

	public static void sendShutdownError(int port) {
		System.out.println(prefix + "Fehler beim Herunterfahren des HostServers mit Port " + port);
	}

	public static void sendNewClient(String client) {
		System.out.println(prefix + "Registering new client: " + client + "!");
	}

	public static void sendDeleteClient(String client) {
		System.out.println(prefix + "Client " + client + " lost connection!");
	}
}

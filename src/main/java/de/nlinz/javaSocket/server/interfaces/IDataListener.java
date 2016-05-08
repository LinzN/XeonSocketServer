package de.nlinz.javaSocket.server.interfaces;

import de.nlinz.javaSocket.server.events.SocketDataEvent;

public interface IDataListener {

	public String getChannel();

	public void onDataRecieve(SocketDataEvent event);
}
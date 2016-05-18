package de.nlinz.javaSocket.server.interfaces;

import de.nlinz.javaSocket.server.events.SocketTypeEvent;

/* Interface for the TypeEvent*/
public interface ITypeListener {

	public SocketServerEventType getType();

	public void onTypeEvent(SocketTypeEvent event);
}
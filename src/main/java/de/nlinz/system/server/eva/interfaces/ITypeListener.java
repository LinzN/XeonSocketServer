package de.nlinz.system.server.eva.interfaces;

import de.nlinz.system.server.eva.server.SocketTypeEvent;

public interface ITypeListener {

	public EvaEventType getType();

	public void onTypeEvent(SocketTypeEvent event);
}
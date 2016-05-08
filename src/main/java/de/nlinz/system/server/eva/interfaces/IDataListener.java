package de.nlinz.system.server.eva.interfaces;

import de.nlinz.system.server.eva.server.SocketDataEvent;

public interface IDataListener {

	public String getChannel();

	public void onDataRecieve(SocketDataEvent event);
}
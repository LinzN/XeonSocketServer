package de.nlinz.javaSocket.server.interfaces;

public interface IServerMask {
	void serverSchedulerAsync(final Runnable runnable);

	void serverSchedulerSync(final Runnable runnable);

}
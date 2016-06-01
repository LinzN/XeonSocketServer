package de.nlinz.cookieSocketBungee.mask;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;

import de.nlinz.javaSocket.server.JavaSocketServer;
import de.nlinz.javaSocket.server.interfaces.IServerMask;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class CookieSocketBungeeMask extends Plugin implements IServerMask {
	private static CookieSocketBungeeMask inst;

	private String socketHost;
	private int socketPort;
	private Configuration config;

	private JavaSocketServer socketServer;

	public static CookieSocketBungeeMask inst() {
		return inst;
	}

	@Override
	public void onDisable() {
		this.socketServer.stop();
	}

	@Override
	public void onEnable() {
		inst = this;

		this.config = initConfig("config.yml");
		this.socketHost = this.config.getString("connect.host");
		this.socketPort = this.config.getInt("connect.port");

		this.socketServer = new JavaSocketServer(this, socketHost, socketPort);
		this.socketServer.start();

	}

	public JavaSocketServer getSocketServer() {
		return this.socketServer;
	}

	public Configuration initConfig(final String name) {
		if (!this.getDataFolder().exists()) {
			this.getDataFolder().mkdir();
		}
		final File file = new File(this.getDataFolder(), name);
		if (!file.exists()) {
			try {
				Files.copy(this.getResourceAsStream(name), file.toPath(), new CopyOption[0]);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			return ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void serverScheduler(Runnable runnable) {
		// TODO Auto-generated method stub
		ProxyServer.getInstance().getScheduler().runAsync(this, runnable);

	}
}

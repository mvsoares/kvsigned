package com.microsoft.kv;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigVO {
	String clientId;
	String vaultUrl;
	String clientKey;

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getVaultUrl() {
		return vaultUrl;
	}

	public void setVaultUrl(String vaultUrl) {
		this.vaultUrl = vaultUrl;
	}

	public String getClientKey() {
		return clientKey;
	}

	public void setClientKey(String clientKey) {
		this.clientKey = clientKey;
	}

	@Override
	public String toString() {
		return "clientId=" + clientId + " vaultUrl=" + vaultUrl + " clientKey=" + clientKey;
	}

	public static ConfigVO createFromProperties() {
		ConfigVO result = new ConfigVO();

		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		Properties properties = new Properties();
		try (InputStream resourceStream = loader.getResourceAsStream("application.properties")) {
			properties.load(resourceStream);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (properties.getProperty("clientId") != null)
			result.setClientId(properties.getProperty("clientId"));

		if (properties.getProperty("vaultUrl") != null)
			result.setVaultUrl(properties.getProperty("vaultUrl"));

		if (properties.getProperty("clientKey") != null)
			result.setClientKey(properties.getProperty("clientKey"));

		return result;

	}
}

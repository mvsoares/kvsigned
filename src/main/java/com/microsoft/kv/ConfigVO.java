package com.microsoft.kv;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigVO {
	String clientId;
	String vaultURL;
	String keyName;
	String clientKey;

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getVaultURL() {
		return vaultURL;
	}

	public void setVaultURL(String vaultURL) {
		this.vaultURL = vaultURL;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public String getClientKey() {
		return clientKey;
	}

	public void setClientKey(String clientKey) {
		this.clientKey = clientKey;
	}

	@Override
	public String toString() {
		return "clientId=" + clientId + " clientId=" + vaultURL + " keyName=" + keyName + " clientKey=" + clientKey;
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
		if (properties.getProperty("clientId") != null)
			result.setVaultURL(properties.getProperty("vaultURL"));
		if (properties.getProperty("keyName") != null)
			result.setKeyName(properties.getProperty("keyName"));
		if (properties.getProperty("clientKey") != null)
			result.setClientKey(properties.getProperty("clientKey"));

		return result;

	}
}

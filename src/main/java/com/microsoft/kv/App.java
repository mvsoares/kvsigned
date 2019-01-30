package com.microsoft.kv;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.microsoft.azure.keyvault.KeyVaultClient;
import com.microsoft.azure.keyvault.models.SecretBundle;

/**
 * Hello world!
 *
 */
public class App {

	private static final Logger LOGGER = LogManager.getLogger(App.class.getName());

	public static void main(String[] args) {
		ConfigVO vo = ConfigVO.createFromProperties();
		LOGGER.info(vo.toString());

		LOGGER.info("Hello World!");

		KeyVaultClient client = new KeyVaultClient(
				new ClientSecretKeyVaultCredential(vo.getClientId(), vo.getClientKey()));

		SecretBundle secret = client.getSecret(vo.getVaultUrl(), "testSecret");
		LOGGER.info(secret.value());

	}
}

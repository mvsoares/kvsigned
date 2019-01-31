package com.microsoft.kv.impl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.microsoft.azure.keyvault.KeyVaultClient;
import com.microsoft.azure.keyvault.models.KeyOperationResult;
import com.microsoft.azure.keyvault.models.SecretBundle;
import com.microsoft.azure.keyvault.webkey.JsonWebKeySignatureAlgorithm;
import com.microsoft.kv.ConfigVO;

public class KvDocumentUtil {
	private static final Logger LOG = LogManager.getLogger(KvDocumentUtil.class.getName());

	KeyVaultClient client;
	ConfigVO vo;

	public KvDocumentUtil() {
		vo = ConfigVO.createFromProperties();
		if (vo == null) {
			LOG.fatal("Erro ao ler application.properties");
			return;
		} else {
			LOG.debug(vo.toString());
		}

		LOG.info("Iniciando conexão com o kv");
		client = new KeyVaultClient(new ClientSecretKeyVaultCredential(vo.getClientId(), vo.getClientKey()));
		if (client == null) {
			LOG.fatal("Erro na conexão com o kv");
		}
	}

	public String getSecret(String secretName) {
		LOG.debug("Obtendo secret " + secretName);
		SecretBundle secret = client.getSecret(vo.getVaultUrl(), secretName);
		if (secret != null) {
			LOG.debug("Secret OK -" + secret.toString());
			return secret.value();
		} else {
			LOG.error("Secret error - " + secretName);
			return null;
		}
	}

	public void signDocument(String content, String keyName)
			throws InterruptedException, ExecutionException, NoSuchAlgorithmException, NoSuchProviderException {
		LOG.debug("Assinando documento " + keyName);

		String shaType = "SHA-256";
		MessageDigest hash = MessageDigest.getInstance(shaType, BouncyCastleProvider.PROVIDER_NAME);
		hash.update(content.getBytes());
		byte[] digest = hash.digest();

		KeyOperationResult signResult = client.signAsync(keyName, JsonWebKeySignatureAlgorithm.RS256, digest, null)
				.get();

//				client.signAsync(vo.getVaultUrl(), keyName, keyVersion, JsonWebKeySignatureAlgorithm.RS256, digest,
//						null).get();

		LOG.info(signResult.toString());
//		
//		  var sresult = await keyClient.SignAsync(keyvaultUri, keyIdentifier, keyVersion,
//	                Microsoft.Azure.KeyVault.WebKey.JsonWebKeySignatureAlgorithm.RS256, digest);
//	return sresult.Result; 
//		nQBfmCaEYQ85kDje_jELyszUa54s3ILkfvC3-rimzTU60yFwwgujbIGNQx9wIL6dfKvBDqOHlgTZ5Y9IGBQ9BCGs6T4i5qH_bpaI0z9wtcdGq3WrHkieYJH9XUNYjLBYhRfVQfRXtJZ79GRWRewMJg149SuJAt9evg-DuXsnHoDCg5cP6T483KsgkA-_o0TCHJ2eBjo1ct5gLMX0KsMhKLrTE649bmhVxVi5ECJZYiPUaEUUPXTIh8ZQcy3fPY2FZf2wMCVixUFK_IEhP2xFD4TL_SzcGZkORvBHrX9XF1R59MVPoQXN2Uz9BNVKYafHYnUf6wyNUz8Q2sYCqAC_pg
	}

}

package com.microsoft.kv.impl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.concurrent.ExecutionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.microsoft.azure.keyvault.KeyVaultClient;
import com.microsoft.azure.keyvault.models.KeyOperationResult;
import com.microsoft.azure.keyvault.models.KeyVerifyResult;
import com.microsoft.azure.keyvault.models.SecretBundle;
import com.microsoft.azure.keyvault.webkey.JsonWebKeySignatureAlgorithm;
import com.microsoft.kv.ConfigVO;
import com.microsoft.kv.DigestSignResult;
import com.microsoft.rest.ServiceFuture;

public class KvDocumentUtil {
	private static final String SHA_TYPE = "SHA-256";
	private static final Logger LOG = LogManager.getLogger(KvDocumentUtil.class.getName());
	private static final JsonWebKeySignatureAlgorithm ALGORITHM = JsonWebKeySignatureAlgorithm.RS256;

	KeyVaultClient kvClient;
	ConfigVO vo;

	/**
	 * Initilize KeyVault client
	 */
	public KvDocumentUtil() {
		vo = ConfigVO.createFromProperties();
		if (vo == null) {
			LOG.fatal("Error reading application.properties");
			return;
		} else {
			LOG.debug(vo.toString());
		}

		LOG.info("Trying to connect to key vault");
		kvClient = new KeyVaultClient(new ClientSecretKeyVaultCredential(vo.getClientId(), vo.getClientKey()));
		if (kvClient == null) {
			LOG.fatal("Error connecting to kault");
		} else {
			LOG.info("Connected to key vault");
		}
	}

	/**
	 * Obtain a secret from vault
	 * 
	 * @param secretName name of secret on vault
	 * @return secret's value
	 */
	public String getSecret(String secretName) {
		LOG.debug("Getting secret " + secretName);
		SecretBundle secret = kvClient.getSecret(vo.getVaultUrl(), secretName);
		if (secret != null) {
			LOG.debug("Secret OK - " + secret.toString());
			return secret.value();
		} else {
			LOG.error("Secret error - " + secretName);
			return null;
		}
	}

	/**
	 * Sign a String with a given vault-certificate
	 * 
	 * @param keyName Certificate full path on vault.
	 *                https://{VAULT_NAME}.vault.azure.net/keys/{CERT_NAME}/{XXXX}
	 * @param content Content to be signed (as string). Internally converted to
	 *                byte[]
	 * @return DigestSignResult with result
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 */
	public DigestSignResult signDocument(String keyIdentifier, String content)
			throws InterruptedException, ExecutionException, NoSuchAlgorithmException, NoSuchProviderException {
		LOG.debug("Signing document" + keyIdentifier);

		byte[] digest = generateHashFromString(content);

		LOG.debug("Calling kv.signAsync. Digest=" + digest.toString());
		ServiceFuture<KeyOperationResult> signResult = kvClient.signAsync(keyIdentifier, ALGORITHM, digest, null);
		return new DigestSignResult(digest, signResult);
	}

	
	/**
	 * Generate a hash for a given String
	 * @param content String to get Hash
	 * @return byte[] with generated hash
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 */
	public static byte[] generateHashFromString(String content) throws NoSuchAlgorithmException, NoSuchProviderException {
		MessageDigest hash = MessageDigest.getInstance(SHA_TYPE, BouncyCastleProvider.PROVIDER_NAME);
		hash.update(content.getBytes());
		byte[] digest = hash.digest();
		return digest;
	}

	/**
	 * 
	 * @param keyIdentifier Certificate full path on vault.
	 *                      https://{VAULT_NAME}.vault.azure.net/keys/{CERT_NAME}/{XXXX}
	 * @param signedResult  previously signedResult
	 * @param digestInfo    digestInfo for you date
	 * @return true in case of sucess
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public boolean verifyDocument(String keyIdentifier, byte[] signedResult, byte[] digestInfo)
			throws InterruptedException, ExecutionException {
		LOG.debug("Calling kv.verifyAsync.");
		ServiceFuture<KeyVerifyResult> b = kvClient.verifyAsync(keyIdentifier, ALGORITHM, digestInfo, signedResult,
				null);
		return b.get().value();
	}
}

package com.microsoft.kv;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.microsoft.kv.impl.KvDocumentUtil;

/**
 * Hello world!
 *
 */
public class KVClient {

	private static final Logger LOG = LogManager.getLogger(KVClient.class.getName());

	static {
		Security.addProvider(new BouncyCastleProvider());
	}

	public static void main(String[] args)
			throws NoSuchAlgorithmException, NoSuchProviderException, InterruptedException, ExecutionException {

//		String shaType = "SHA-256";
//		MessageDigest hash = MessageDigest.getInstance(shaType, BouncyCastleProvider.PROVIDER_NAME);
//		System.out.println(hash);
//
//		byte[] plainText = new byte[100];
//		new Random(0x1234567L).nextBytes(plainText);
//		hash.update(plainText);
//		byte[] digest = hash.digest();

//		KeyOperationResult signResult = keyVaultClient
//				.signAsync(keyBundle.key().kid(), JsonWebKeySignatureAlgorithm.RS256, digest, null).get();
//		Assert.assertNotNull(signResult);
//
//		KeyVerifyResult verifypResult = keyVaultClient.verifyAsync(keyBundle.key().kid(),
//				JsonWebKeySignatureAlgorithm.RS256, digest, signResult.result(), null).get();
//Assert.assertTrue(verifypResult.value());

		LOG.info("Iniciando KV tests");
		KvDocumentUtil kv = new KvDocumentUtil();

		String xml = "<note><to>Tove</to><from>Jani</from><heading>Reminder</heading><body>Don't forget me this weekend!</body></note>";
		kv.signDocument(xml, "https://kvshimoo.vault.azure.net/keys/cert01/cc68db3ef12d424385220fb3895077b5");

		String value = kv.getSecret("testSecret");
		LOG.info("testSecret=" + value);

		value = kv.getSecret("testSecret2");
		LOG.info("testSecret=" + value);

		LOG.info("Fim dos testes ");
	}
}

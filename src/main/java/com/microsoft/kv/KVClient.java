package com.microsoft.kv;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
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

		String keyIdentifier = "https://kvshimoo.vault.azure.net/keys/cert01/cc68db3ef12d424385220fb3895077b5";

		LOG.info("Iniciando KV tests");
		KvDocumentUtil kv = new KvDocumentUtil();

		String xml = "<note><to>Someone</to><from>Myself</from><heading>Reminder</heading><body>Don't forget me this weekend!</body></note>";
		DigestSignResult signResult = kv.signDocument(keyIdentifier, xml);
		LOG.info(signResult.toString());

		boolean check = kv.verifyDocument(keyIdentifier, signResult.getResultSign().get().result(),
				signResult.digestInfo);
		LOG.info("verifyDocument=" + check);

		String value = kv.getSecret("testSecret");
		LOG.info("testSecret=" + value);

		value = kv.getSecret("testSecret2");
		LOG.info("testSecret=" + value);

		LOG.info("Fim dos testes ");
	}
}

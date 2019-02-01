package com.microsoft.kv;

import com.microsoft.azure.keyvault.models.KeyOperationResult;
import com.microsoft.rest.ServiceFuture;

public class DigestSignResult {
	byte[] digestInfo;
	ServiceFuture<KeyOperationResult> resultSign;

	public DigestSignResult(byte[] digestInfo, ServiceFuture<KeyOperationResult> resultSign) {
		this.digestInfo = digestInfo;
		this.resultSign = resultSign;
	}

	public byte[] getDigestInfo() {
		return digestInfo;
	}

	public void setDigestInfo(byte[] digestInfo) {
		this.digestInfo = digestInfo;
	}

	public ServiceFuture<KeyOperationResult> getResultSign() {
		return resultSign;
	}

	public void setResultSign(ServiceFuture<KeyOperationResult> resultSign) {
		this.resultSign = resultSign;
	}

	@Override
	public String toString() {
		return "digestInfo={" + digestInfo + "} resultSign={" + resultSign + "}";
	}
}

package com.depromeet.auth.dto;

import java.util.List;
import java.util.Optional;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApplePublicKeyResponse {
	private List<ApplePublicKey> keys;

	@Setter
	@Getter
	public static class ApplePublicKey {
		private String kty;
		private String kid;
		private String use;
		private String alg;
		private String n;
		private String e;
	}

	public Optional<ApplePublicKey> getMatchedKeyBy(String kid, String alg) {
		return this.keys.stream()
			.filter(key -> key.getKid().equals(kid) && key.getAlg().equals(alg))
			.findFirst();
	}

}

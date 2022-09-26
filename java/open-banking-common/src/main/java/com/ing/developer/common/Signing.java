package com.ing.developer.common;

import org.tomitribe.auth.signatures.Signature;


import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Locale;
import java.util.Map;

public class Signing {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);

    public static String getDate() {
        return ZonedDateTime.now(ZoneId.of("GMT")).format(FORMATTER);
    }

    public static String digest(String s) {
        final String algorithm = "SHA-256";
        try {
            final byte[] digest = MessageDigest.getInstance(algorithm).digest(s.getBytes());
            final String encodedString = java.util.Base64.getEncoder().encodeToString(digest);
            return algorithm + "=" + encodedString;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String base64encode(Certificate certificate) {
        try {
            return new String(Base64.getEncoder().encode(certificate.getEncoded()));
        } catch (CertificateEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static OBSigner getNewSigner(String configuredClientId, PrivateKey signatureKey) {
        return new OBSigner(signatureKey, getSignature(configuredClientId));
    }

    public static Signature sign(OBSigner signer, String method, String uri, Map<String, String> headers) {
        try {
            return signer.sign(method, uri, headers);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Signature getSignature(String keyId) {
        return new Signature(keyId, "rsa-sha256", null, "(request-target)", "date", "digest");
    }

}

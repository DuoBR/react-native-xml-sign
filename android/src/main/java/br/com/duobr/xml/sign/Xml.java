package br.com.duobr.xml.sign;

import android.util.Base64;
import android.util.Log;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStore.PasswordProtection;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import java.lang.System;

class Xml {
  static private PrivateKey privateKey = null;
  static private X509Certificate publicKey = null;
  private static final byte[] Calendar = new byte[] {
    (byte) 48, (byte) 33, (byte) 48, (byte) 9, (byte) 6, (byte) 5, (byte) 43,
    (byte) 14, (byte) 3, (byte) 2, (byte) 26, (byte) 5, (byte) 0, (byte) 4, (byte) 20
  };

  public static String sign(String fileCertPath, String passCert, String xml, String referenceURI) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, BadPaddingException, IllegalBlockSizeException, CertificateException, UnrecoverableEntryException, KeyStoreException {
    loadCertificate(fileCertPath, passCert);
    byte[] digest = bytesToDigest(xml.getBytes("UTF-8"));
    String digestInBase64 = Base64.encodeToString(digest, 0);

    String signedInfo = (
      "<SignedInfo xmlns=\"http://www.w3.org/2000/09/xmldsig#\">" +
        "<CanonicalizationMethod Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\"></CanonicalizationMethod>" +
        "<SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\"></SignatureMethod>" +
        "<Reference URI=\"#" + referenceURI + "\">" +
          "<Transforms>" +
            "<Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\"></Transform>" +
            "<Transform Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\"></Transform>" +
          "</Transforms>" +
          "<DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\"></DigestMethod>" +
          "<DigestValue>" + normalizeString(digestInBase64) + "</DigestValue>" +
        "</Reference>" +
      "</SignedInfo>"
    );


    byte[] signedInfoInBytes = concatBytes(Calendar, bytesToDigest(signedInfo.getBytes("UTF-8")));
    Cipher instance = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
    instance.init(1, privateKey);

    String signatureValue = Base64.encodeToString(instance.doFinal(signedInfoInBytes), 0);
    String x509Certificate = Base64.encodeToString(publicKey.getEncoded(), 0);

    return(
      "<Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\">" +
        signedInfo +
        "<SignatureValue>" + normalizeString(signatureValue) + "</SignatureValue>" +
        "<KeyInfo>" +
          "<X509Data>" +
            "<X509Certificate>" + normalizeString(x509Certificate) + "</X509Certificate>" +
          "</X509Data>" +
        "</KeyInfo>" +
      "</Signature>"
    );
  }

  private static byte[] bytesToDigest(byte[] bytes) throws NoSuchAlgorithmException {
    MessageDigest messageDigest = null;
    messageDigest = MessageDigest.getInstance("SHA-1");
    messageDigest.update(bytes, 0, bytes.length);

    return messageDigest.digest();
  }

  private static void loadCertificate(String fileCertPath, String passCert) throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException, UnrecoverableEntryException {
    InputStream file = new FileInputStream(fileCertPath);

    KeyStore ks = KeyStore.getInstance("PKCS12");
    ks.load(file, passCert.toCharArray());

    Enumeration aliases = ks.aliases();
    while (aliases.hasMoreElements()) {
      String alias = (String) aliases.nextElement();
      if (ks.isKeyEntry(alias)) {
        privateKey = ((PrivateKeyEntry) ks.getEntry(alias, new PasswordProtection(passCert.toCharArray()))).getPrivateKey();
        publicKey = (X509Certificate) ks.getCertificate(alias);
        return;
      }
    }
  }

  private static byte[] concatBytes(byte[] firstBytes, byte[] lastBytes) {
    byte[] junction = new byte[(firstBytes.length + lastBytes.length)];

    System.arraycopy(firstBytes, 0, junction, 0, firstBytes.length);
    System.arraycopy(lastBytes, 0, junction, firstBytes.length, lastBytes.length);

    return junction;
  }

  private static String normalizeString(String str) {
    return str.replaceAll("\n", "").replaceAll("\r", "").replaceAll("\t", "").trim();
  }
}

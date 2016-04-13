package cn.anline.www.ftp.server.primftpd.util;

import com.sshtools.publickey.SshPublicKeyFile;
import com.sshtools.publickey.SshPublicKeyFileFactory;
import com.sshtools.ssh.components.SshPublicKey;
import com.sshtools.ssh.components.jce.Ssh2DsaPublicKey;
import com.sshtools.ssh.components.jce.Ssh2RsaPublicKey;

import org.apache.ftpserver.util.IoUtils;
import cn.anline.www.ftp.server.slf4j.Logger;
import cn.anline.www.ftp.server.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.DSAPublicKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Locale;

public class KeyInfoProvider
{
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	public String fingerprint(byte[] pubKeyEnc, String hashAlgo) {
		try {
			MessageDigest md = MessageDigest.getInstance(hashAlgo);
			md.update(pubKeyEnc);
			byte[] fingerPrintBytes = md.digest();
			return beautify(fingerPrintBytes);
		} catch (Exception e) {
			logger.error("could not read key: " + e.getMessage(), e);
		}
		return null;
	}

	private static final int BUFFER_SIZE = 4096;

	public PublicKey readPublicKey(FileInputStream fis)
		throws NoSuchAlgorithmException, InvalidKeySpecException,
		IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		IoUtils.copy(fis, baos, BUFFER_SIZE);
		byte[] pubKeyBytes = baos.toByteArray();
		X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(pubKeyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KeyGenerator.KEY_ALGO);
		PublicKey publicKey = keyFactory.generatePublic(pubKeySpec);
		return publicKey;
	}

	public PrivateKey readPrivatekey(FileInputStream fis)
		throws NoSuchAlgorithmException, InvalidKeySpecException,
		IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		IoUtils.copy(fis, baos, BUFFER_SIZE);
		byte[] privKeyBytes = baos.toByteArray();
		PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privKeyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KeyGenerator.KEY_ALGO);
		PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
		return privateKey;
	}

	protected String beautify(byte[] fingerPrintBytes)
	{
		StringBuilder fingerPrint = new StringBuilder();
		for (int i=0; i<fingerPrintBytes.length; i++) {
			byte b = fingerPrintBytes[i];
			String hexString = Integer.toHexString(b);
			if (hexString.length() > 2) {
				hexString = hexString.substring(
				hexString.length() - 2,
				hexString.length());
			} else if (hexString.length() < 2) {
				hexString = "0" + hexString;
			}
			fingerPrint.append(hexString.toUpperCase(Locale.ENGLISH));
			if (i != fingerPrintBytes.length -1) {
				fingerPrint.append(":");

				if ((i + 1) % 10 == 0) {
					// force line breaks in UI
					fingerPrint.append("\n");
				}
			}
		}
		return fingerPrint.toString();
	}

	public byte[] encodeAsSsh(RSAPublicKey pubKey)
		throws IOException
	{
		ByteArrayOutputStream buf = new ByteArrayOutputStream();

		byte[] name = "ssh-rsa".getBytes("US-ASCII");
		writeKeyPart(name, buf);

		writeKeyPart(pubKey.getPublicExponent().toByteArray(), buf);
		writeKeyPart(pubKey.getModulus().toByteArray(), buf);

		return buf.toByteArray();
	}

	private void writeKeyPart(byte[] bytes, OutputStream os)
		throws IOException
	{
		for (int shift = 24; shift >= 0; shift -= 8) {
			os.write((bytes.length >>> shift) & 0xFF);
		}
		os.write(bytes);
	}

	public PublicKey readKeyAuthKey(String path)
	{
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(path);
			SshPublicKeyFile sshPubKeyFile = SshPublicKeyFileFactory.parse(fis);
			SshPublicKey sshPubKey = sshPubKeyFile.toPublicKey();
			// ssh2 rsa only
			if (sshPubKey instanceof Ssh2RsaPublicKey) {
				Ssh2RsaPublicKey rsaPubKey = (Ssh2RsaPublicKey) sshPubKey;
				BigInteger modulus = rsaPubKey.getModulus();
				BigInteger exponent = rsaPubKey.getPublicExponent();
				RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(modulus, exponent);
				KeyFactory keyFactory = KeyFactory.getInstance("RSA");
				return keyFactory.generatePublic(pubKeySpec);
			} else if (sshPubKey instanceof Ssh2DsaPublicKey) {
				Ssh2DsaPublicKey dsaPubKey = (Ssh2DsaPublicKey) sshPubKey;
				DSAPublicKeySpec pubKeySpec = new DSAPublicKeySpec(
					dsaPubKey.getY(),
					dsaPubKey.getP(),
					dsaPubKey.getQ(),
					dsaPubKey.getG()
				);
				KeyFactory keyFactory = KeyFactory.getInstance("DSA");
				return keyFactory.generatePublic(pubKeySpec);
			} else {
				logger.error("Could not read public key! Expected: '{}' or '{}', but got: '{}'",
					new Object[]{Ssh2RsaPublicKey.class.getName(),
						Ssh2DsaPublicKey.class.getName(),
						sshPubKey.getClass().getName()});
			}
		} catch (Exception e) {
			logger.error("could not read key auth key", e);
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
			}
		}
		return null;
	}
}

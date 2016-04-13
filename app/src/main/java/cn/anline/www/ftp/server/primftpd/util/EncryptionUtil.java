package cn.anline.www.ftp.server.primftpd.util;

import org.apache.mina.util.Base64;
import cn.anline.www.ftp.server.slf4j.Logger;
import cn.anline.www.ftp.server.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptionUtil
{
	protected static final Logger logger = LoggerFactory.getLogger(EncryptionUtil.class);

	static private final String SALT = "H§R&q}9";

	public static String encrypt(String str)
	{
		try {
			MessageDigest cipher = MessageDigest.getInstance("SHA-512");
			byte[] encrypted = cipher.digest((str + SALT).getBytes("UTF-8"));
			byte[] base64 = Base64.encodeBase64(encrypted);
			return new String(base64, "UTF-8");

		} catch (NoSuchAlgorithmException e) {
			logger.error("could not encrypt", e);
		} catch (UnsupportedEncodingException e) {
			logger.error("could not encrypt", e);
		}
		return null;
	}
}

package cn.anline.www.ftp.server.primftpd.util;

import android.os.Environment;

import java.io.File;

public final class Defaults {
	private Defaults(){}

	public static final File HOME_DIR = Environment.getExternalStorageDirectory();
	public static final String PUB_KEY_AUTH_KEY_PATH =
		HOME_DIR.getAbsolutePath() + "/.ssh/authorized_key.pub";
}

package cn.anline.www.ftp.server.primftpd.filesystem;

import org.apache.ftpserver.ftplet.FileSystemView;
import org.apache.ftpserver.ftplet.User;

import java.io.File;

public class FtpFileSystemView
	extends AndroidFileSystemView<FtpFile, org.apache.ftpserver.ftplet.FtpFile>
	implements FileSystemView
{
	private final User user;

	public FtpFileSystemView(File homeDir, User user) {
		super(homeDir);
		this.user = user;
	}

	@Override
	protected FtpFile createFile(File file)
	{
		return new FtpFile(file, user);
	}
}

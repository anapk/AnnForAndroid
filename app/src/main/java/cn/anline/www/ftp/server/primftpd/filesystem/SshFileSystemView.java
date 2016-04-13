package cn.anline.www.ftp.server.primftpd.filesystem;

import org.apache.sshd.common.Session;
import org.apache.sshd.common.file.FileSystemView;

import java.io.File;

public class SshFileSystemView
	extends AndroidFileSystemView<SshFile, org.apache.sshd.common.file.SshFile>
	implements FileSystemView
{
	private final Session session;

	public SshFileSystemView(File homeDir, Session session) {
		super(homeDir);
		this.session = session;
	}

	@Override
	protected SshFile createFile(File file)
	{
		return new SshFile(file, session);
	}

	@Override
	public org.apache.sshd.common.file.SshFile getFile(
			org.apache.sshd.common.file.SshFile arg0,
			String arg1)
	{
		return getFile(arg0.getAbsolutePath());
	}

	@Override
	public FileSystemView getNormalizedView()
	{
		return this;
	}
}

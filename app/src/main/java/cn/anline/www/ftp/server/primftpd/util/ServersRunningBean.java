package cn.anline.www.ftp.server.primftpd.util;

public class ServersRunningBean {
    public boolean ftp = false;
    public boolean ssh = false;

    public boolean atLeastOneRunning() {
        return ftp || ssh;
    }
}

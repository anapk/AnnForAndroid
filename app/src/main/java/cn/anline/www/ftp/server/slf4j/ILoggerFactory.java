package cn.anline.www.ftp.server.slf4j;

/**
 * Created by Administrator on 2016/4/14.
 */
public abstract interface ILoggerFactory
{
    public abstract Logger getLogger(String paramString);
}

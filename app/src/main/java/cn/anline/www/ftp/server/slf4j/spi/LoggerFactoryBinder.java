package cn.anline.www.ftp.server.slf4j.spi;

/**
 * Created by Administrator on 2016/4/14.
 */
import cn.anline.www.ftp.server.slf4j.ILoggerFactory;

public abstract interface LoggerFactoryBinder
{
    public abstract ILoggerFactory getLoggerFactory();

    public abstract String getLoggerFactoryClassStr();
}
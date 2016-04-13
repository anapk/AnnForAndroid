package cn.anline.www.ftp.server.slf4j.helpers;

/**
 * Created by Administrator on 2016/4/14.
 */
import cn.anline.www.ftp.server.slf4j.ILoggerFactory;
import cn.anline.www.ftp.server.slf4j.Logger;

public class NOPLoggerFactory
        implements ILoggerFactory
{
    public Logger getLogger(String name)
    {
        return NOPLogger.NOP_LOGGER;
    }
}

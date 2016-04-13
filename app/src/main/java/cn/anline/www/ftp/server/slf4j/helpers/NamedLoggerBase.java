package cn.anline.www.ftp.server.slf4j.helpers;

/**
 * Created by Administrator on 2016/4/14.
 */
import java.io.ObjectStreamException;
import java.io.Serializable;
import cn.anline.www.ftp.server.slf4j.Logger;
import cn.anline.www.ftp.server.slf4j.LoggerFactory;

abstract class NamedLoggerBase
        implements Logger, Serializable
{
    private static final long serialVersionUID = 7535258609338176893L;
    protected String name;

    public String getName()
    {
        return this.name;
    }

    protected Object readResolve()
            throws ObjectStreamException
    {
        return LoggerFactory.getLogger(getName());
    }
}

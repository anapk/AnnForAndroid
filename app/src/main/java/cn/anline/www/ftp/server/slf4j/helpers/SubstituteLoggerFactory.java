package cn.anline.www.ftp.server.slf4j.helpers;

/**
 * Created by Administrator on 2016/4/14.
 */
import java.util.ArrayList;
import java.util.List;
import cn.anline.www.ftp.server.slf4j.ILoggerFactory;
import cn.anline.www.ftp.server.slf4j.Logger;

public class SubstituteLoggerFactory
        implements ILoggerFactory
{
    final List loggerNameList;

    public SubstituteLoggerFactory()
    {
        this.loggerNameList = new ArrayList(); }

    public Logger getLogger(String name) {
        synchronized (this.loggerNameList) {
            this.loggerNameList.add(name);
        }
        return NOPLogger.NOP_LOGGER;
    }

    public List getLoggerNameList() {
        List copy = new ArrayList();
        synchronized (this.loggerNameList) {
            copy.addAll(this.loggerNameList);
        }
        return copy;
    }
}

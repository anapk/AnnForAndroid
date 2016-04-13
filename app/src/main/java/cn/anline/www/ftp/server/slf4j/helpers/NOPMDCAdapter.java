package cn.anline.www.ftp.server.slf4j.helpers;

/**
 * Created by Administrator on 2016/4/14.
 */
import java.util.Map;
import cn.anline.www.ftp.server.slf4j.spi.MDCAdapter;

public class NOPMDCAdapter
        implements MDCAdapter
{
    public void clear()
    {
    }

    public String get(String key)
    {
        return null;
    }

    public void put(String key, String val) {
    }

    public void remove(String key) {
    }

    public Map getCopyOfContextMap() {
        return null;
    }

    public void setContextMap(Map contextMap)
    {
    }
}

package cn.anline.www.ftp.server.slf4j.spi;

/**
 * Created by Administrator on 2016/4/14.
 */
import java.util.Map;

public abstract interface MDCAdapter
{
    public abstract void put(String paramString1, String paramString2);

    public abstract String get(String paramString);

    public abstract void remove(String paramString);

    public abstract void clear();

    public abstract Map getCopyOfContextMap();

    public abstract void setContextMap(Map paramMap);
}

package cn.anline.www.ftp.server.slf4j.spi;

/**
 * Created by Administrator on 2016/4/14.
 */
import cn.anline.www.ftp.server.slf4j.IMarkerFactory;

public abstract interface MarkerFactoryBinder
{
    public abstract IMarkerFactory getMarkerFactory();

    public abstract String getMarkerFactoryClassStr();
}

package cn.anline.www.ftp.server.slf4j.impl;

/**
 * Created by Administrator on 2016/4/14.
 */
import cn.anline.www.ftp.server.slf4j.IMarkerFactory;
import cn.anline.www.ftp.server.slf4j.helpers.BasicMarkerFactory;
import cn.anline.www.ftp.server.slf4j.spi.MarkerFactoryBinder;

public class StaticMarkerBinder
        implements MarkerFactoryBinder
{
    public static final StaticMarkerBinder SINGLETON = new StaticMarkerBinder();

    private final IMarkerFactory markerFactory = new BasicMarkerFactory();

    public IMarkerFactory getMarkerFactory()
    {
        return this.markerFactory;
    }

    public String getMarkerFactoryClassStr()
    {
        return BasicMarkerFactory.class.getName();
    }
}
package cn.anline.www.ftp.server.slf4j;

/**
 * Created by Administrator on 2016/4/14.
 */
import cn.anline.www.ftp.server.slf4j.helpers.BasicMarkerFactory;
import cn.anline.www.ftp.server.slf4j.helpers.Util;
import cn.anline.www.ftp.server.slf4j.impl.StaticMarkerBinder;

public class MarkerFactory
{
    static IMarkerFactory markerFactory;

    public static Marker getMarker(String name)
    {
        return markerFactory.getMarker(name);
    }

    public static Marker getDetachedMarker(String name)
    {
        return markerFactory.getDetachedMarker(name);
    }

    public static IMarkerFactory getIMarkerFactory()
    {
        return markerFactory;
    }

    static
    {
        try
        {
            markerFactory = StaticMarkerBinder.SINGLETON.getMarkerFactory();
        } catch (NoClassDefFoundError e) {
            markerFactory = new BasicMarkerFactory();
        }
        catch (Exception e)
        {
            Util.report("Unexpected failure while binding MarkerFactory", e);
        }
    }
}

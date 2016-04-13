package cn.anline.www.ftp.server.slf4j;

/**
 * Created by Administrator on 2016/4/14.
 */
public abstract interface IMarkerFactory
{
    public abstract Marker getMarker(String paramString);

    public abstract boolean exists(String paramString);

    public abstract boolean detachMarker(String paramString);

    public abstract Marker getDetachedMarker(String paramString);
}
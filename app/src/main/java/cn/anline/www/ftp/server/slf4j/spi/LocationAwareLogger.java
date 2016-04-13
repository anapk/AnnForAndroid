package cn.anline.www.ftp.server.slf4j.spi;

/**
 * Created by Administrator on 2016/4/14.
 */
import cn.anline.www.ftp.server.slf4j.Logger;
import cn.anline.www.ftp.server.slf4j.Marker;

public abstract interface LocationAwareLogger extends Logger
{
    public static final int TRACE_INT = 0;
    public static final int DEBUG_INT = 10;
    public static final int INFO_INT = 20;
    public static final int WARN_INT = 30;
    public static final int ERROR_INT = 40;

    public abstract void log(Marker paramMarker, String paramString1, int paramInt, String paramString2, Object[] paramArrayOfObject, Throwable paramThrowable);
}

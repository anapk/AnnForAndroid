package cn.anline.www.ftp.server.slf4j;

/**
 * Created by Administrator on 2016/4/14.
 */
import java.util.Map;
import cn.anline.www.ftp.server.slf4j.helpers.NOPMDCAdapter;
import cn.anline.www.ftp.server.slf4j.helpers.Util;
import cn.anline.www.ftp.server.slf4j.impl.StaticMDCBinder;
import cn.anline.www.ftp.server.slf4j.spi.MDCAdapter;
public class MDC
{
    static final String NULL_MDCA_URL = "http://www.slf4j.org/codes.html#null_MDCA";
    static final String NO_STATIC_MDC_BINDER_URL = "http://www.slf4j.org/codes.html#no_static_mdc_binder";
    static MDCAdapter mdcAdapter;

    public static void put(String key, String val)
            throws IllegalArgumentException
    {
        if (key == null) {
            throw new IllegalArgumentException("key parameter cannot be null");
        }
        if (mdcAdapter == null) {
            throw new IllegalStateException("MDCAdapter cannot be null. See also http://www.slf4j.org/codes.html#null_MDCA");
        }

        mdcAdapter.put(key, val);
    }

    public static String get(String key)
            throws IllegalArgumentException
    {
        if (key == null) {
            throw new IllegalArgumentException("key parameter cannot be null");
        }

        if (mdcAdapter == null) {
            throw new IllegalStateException("MDCAdapter cannot be null. See also http://www.slf4j.org/codes.html#null_MDCA");
        }

        return mdcAdapter.get(key);
    }

    public static void remove(String key)
            throws IllegalArgumentException
    {
        if (key == null) {
            throw new IllegalArgumentException("key parameter cannot be null");
        }

        if (mdcAdapter == null) {
            throw new IllegalStateException("MDCAdapter cannot be null. See also http://www.slf4j.org/codes.html#null_MDCA");
        }

        mdcAdapter.remove(key);
    }

    public static void clear()
    {
        if (mdcAdapter == null) {
            throw new IllegalStateException("MDCAdapter cannot be null. See also http://www.slf4j.org/codes.html#null_MDCA");
        }

        mdcAdapter.clear();
    }

    public static Map getCopyOfContextMap()
    {
        if (mdcAdapter == null) {
            throw new IllegalStateException("MDCAdapter cannot be null. See also http://www.slf4j.org/codes.html#null_MDCA");
        }

        return mdcAdapter.getCopyOfContextMap();
    }

    public static void setContextMap(Map contextMap)
    {
        if (mdcAdapter == null) {
            throw new IllegalStateException("MDCAdapter cannot be null. See also http://www.slf4j.org/codes.html#null_MDCA");
        }

        mdcAdapter.setContextMap(contextMap);
    }

    public static MDCAdapter getMDCAdapter()
    {
        return mdcAdapter;
    }

    static
    {
        try
        {
            mdcAdapter = StaticMDCBinder.SINGLETON.getMDCA();
        } catch (NoClassDefFoundError ncde) {
            mdcAdapter = new NOPMDCAdapter();
            String msg = ncde.getMessage();
            if ((msg != null) && (msg.indexOf("org/slf4j/impl/StaticMDCBinder") != -1)) {
                Util.report("Failed to load class \"org.slf4j.impl.StaticMDCBinder\".");
                Util.report("Defaulting to no-operation MDCAdapter implementation.");
                Util.report("See http://www.slf4j.org/codes.html#no_static_mdc_binder for further details.");
            }
            else {
                throw ncde;
            }
        }
        catch (Exception e) {
            Util.report("MDC binding unsuccessful.", e);
        }
    }
}

package cn.anline.www.ftp.server.slf4j.impl;

/**
 * Created by Administrator on 2016/4/14.
 */
import cn.anline.www.ftp.server.slf4j.helpers.NOPMDCAdapter;
import cn.anline.www.ftp.server.slf4j.spi.MDCAdapter;

public class StaticMDCBinder
{
    public static final StaticMDCBinder SINGLETON = new StaticMDCBinder();

    public MDCAdapter getMDCA()
    {
        return new NOPMDCAdapter();
    }

    public String getMDCAdapterClassStr()
    {
        return NOPMDCAdapter.class.getName();
    }
}

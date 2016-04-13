package cn.anline.www.ftp.server.primftpd.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import cn.anline.www.ftp.server.primftpd.PrefsBean;
import cn.anline.www.ftp.server.primftpd.prefs.LoadPrefsUtil;
import cn.anline.www.ftp.server.primftpd.util.ServersRunningBean;
import cn.anline.www.ftp.server.primftpd.util.ServicesStartStopUtil;
import cn.anline.www.ftp.server.slf4j.Logger;
import cn.anline.www.ftp.server.slf4j.LoggerFactory;

/**
 * Wrapper class which can be called by an @{link Intent} and which handles starting and stopping of services.
 */
public class ServicesStartingService extends Service {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Context context = getBaseContext();
        ServersRunningBean serversRunningBean = ServicesStartStopUtil.checkServicesRunning(context);
        if (!serversRunningBean.atLeastOneRunning()) {
            SharedPreferences prefs = LoadPrefsUtil.getPrefs(context);
            PrefsBean prefsBean = LoadPrefsUtil.loadPrefs(logger, prefs);
            ServicesStartStopUtil.startServers(context, prefsBean, null);
        } else {
            ServicesStartStopUtil.stopServers(context);
        }

        // this service is not intended to run in background
        // it is just used as relay for widget pendingIntent
        stopSelf();

        return 0;
    }

    @Override
    public void onDestroy() {
    }
}

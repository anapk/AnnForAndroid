package cn.anline.www.ftp.server.primftpd.services;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import cn.anline.www.R;
import cn.anline.www.ftp.server.primftpd.PrefsBean;
import cn.anline.www.ftp.server.primftpd.ServerStateChangedEvent;
import cn.anline.www.ftp.server.primftpd.util.ServicesStartStopUtil;
import cn.anline.www.ftp.server.slf4j.Logger;
import cn.anline.www.ftp.server.slf4j.LoggerFactory;

/**
 * Abstract base class for {@link Service}s wrapping servers.
 * <div>
 * Implements:
 * <ul>
 * 		<li>android lifecycle</li>
 * 		<li>statusbar notifications</li>
 * 		<li>bonjour/zeroconf announcements</li>
 * </ul>
 * </div>
 *
 */
public abstract class AbstractServerService
	extends Service
{
	protected static final int MSG_START = 1;
	protected static final int MSG_STOP = 2;

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private Looper serviceLooper;
	private ServerServiceHandler serviceHandler;
	PrefsBean prefsBean;
	private NsdManager.RegistrationListener nsdRegistrationListener;

	protected abstract ServerServiceHandler createServiceHandler(
		Looper serviceLooper,
		AbstractServerService service);

	protected abstract Object getServer();
	protected abstract boolean launchServer();
	protected abstract void stopServer();
	protected abstract int getPort();
	protected abstract String getServiceName();

	protected void handleServerStartError(Exception e)
	{
		logger.error("could not start server", e);

		String msg = getText(R.string.serverCouldNotBeStarted).toString();
		msg += e.getLocalizedMessage();
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		HandlerThread thread = new HandlerThread(
			"ServiceStartArguments",
			Process.THREAD_PRIORITY_BACKGROUND);
		thread.start();

		serviceLooper = thread.getLooper();
		serviceHandler = createServiceHandler(serviceLooper, this);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		logger.debug("onStartCommand()");

		if (intent == null) {
			logger.warn("intent is null in onStartCommand()");

			return START_REDELIVER_INTENT;
		}

		// get parameters
		Bundle extras = intent.getExtras();
		prefsBean = (PrefsBean)extras.get(ServicesStartStopUtil.EXTRA_PREFS_BEAN);

		// send start message (to handler)
		Message msg = serviceHandler.obtainMessage();
		msg.arg1 = MSG_START;
		serviceHandler.sendMessage(msg);

		// post event
		EventBus.getDefault().post(new ServerStateChangedEvent());

		// we don't want the system to kill the ftp server
		//return START_NOT_STICKY;
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		logger.debug("onDestroy()");

		// send stop message (to handler)
		Message msg = serviceHandler.obtainMessage();
		msg.arg1 = MSG_STOP;
		serviceHandler.sendMessage(msg);

		// post event
		EventBus.getDefault().post(new ServerStateChangedEvent());
	}

	/**
	 * Register a DNS-SD service (to be discoverable through Bonjour/Avahi).
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	protected void announceService () {
		nsdRegistrationListener = new NsdManager.RegistrationListener() {
			@Override
			public void onServiceRegistered(NsdServiceInfo serviceInfo) {
				logger.debug("onServiceRegistered()");
			}
			@Override
			public void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
				logger.debug("onRegistrationFailed()");
			}
			@Override
			public void onServiceUnregistered(NsdServiceInfo serviceInfo) {
				logger.debug("onServiceUnregistered()");
			}
			@Override
			public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
				logger.debug("onUnregistrationFailed()");
			}
		};

		NsdServiceInfo serviceInfo  = new NsdServiceInfo();
		serviceInfo.setServiceName("primitive ftpd");
		serviceInfo.setServiceType("_" + getServiceName() + "._tcp.");
		serviceInfo.setPort(getPort());

		NsdManager nsdManager = (NsdManager) getSystemService(Context.NSD_SERVICE);

		nsdManager.registerService(
			serviceInfo,
			NsdManager.PROTOCOL_DNS_SD,
			nsdRegistrationListener);
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	protected void unannounceService () {
		NsdManager nsdManager = (NsdManager) getSystemService(Context.NSD_SERVICE);
		nsdManager.unregisterService(nsdRegistrationListener);
	}
}

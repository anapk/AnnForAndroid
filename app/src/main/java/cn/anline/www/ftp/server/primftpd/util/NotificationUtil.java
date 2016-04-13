package cn.anline.www.ftp.server.primftpd.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;

public class NotificationUtil
{
	protected static final int NOTIFICATION_ID = 1;

	public static void createStatusbarNotification(
			Context androidObject,
			Notification notification)
	{
		NotificationManager notiMgr = (NotificationManager) androidObject.getSystemService(
			Context.NOTIFICATION_SERVICE);
		notiMgr.notify(NOTIFICATION_ID, notification);
	}

	public static void removeStatusbarNotification(Context androidObject) {
		NotificationManager notiMgr = (NotificationManager) androidObject.getSystemService(
			Context.NOTIFICATION_SERVICE);
		notiMgr.cancel(NOTIFICATION_ID);
	}
}

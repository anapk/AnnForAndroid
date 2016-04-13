package cn.anline.www.ftp.server.primftpd.prefs;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import cn.anline.www.R;
import cn.anline.www.ftp.server.slf4j.Logger;
import cn.anline.www.ftp.server.slf4j.LoggerFactory;

public class AboutActivity extends Activity
{
	public static final String URL_APL =
		"https://www.apache.org/licenses/LICENSE-2.0";
	public static final String URL_GITHUB =
		"https://github.com/wolpi/prim-ftpd";
	public static final String URL_FDROID =
		"https://f-droid.org/repository/browse/?fdid=org.primftpd";
	public static final String URL_GOOGLE_PLAY =
		"https://play.google.com/store/apps/details?id=org.primftpd";
    public static final String URL_AMAZON =
		"http://www.amazon.com/wolpi-primitive-FTPd/dp/B00KERCPNY/ref=sr_1_1";

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		// set theme
        SharedPreferences prefs = LoadPrefsUtil.getPrefs(getBaseContext());
        Theme theme = LoadPrefsUtil.theme(prefs);
        setTheme(theme.resourceId());

		// set layout
		setContentView(R.layout.about);

		// show action bar to allow user to navigate back
		// -> the same as for PreferencesActivity
        getActionBar().setDisplayHomeAsUpEnabled(true);

		// show version num
        TextView versionLabel = (TextView)findViewById(R.id.versionLabel);
        versionLabel.setText("Version");

        TextView versionView = (TextView)findViewById(R.id.versionTextView);
        String pkgName = getPackageName();
        PackageManager pkgMgr = getPackageManager();
        PackageInfo packageInfo = null;
		try {
			packageInfo = pkgMgr.getPackageInfo(
				pkgName,
				0);
		} catch (NameNotFoundException e) {
			logger.error("could not get version", e);
		}
        String version = packageInfo != null
        	? packageInfo.versionName
        	: "unknown";
        if (packageInfo != null) {
        	version += " (code: " + packageInfo.versionCode + ")";
        }

        logger.debug("pkgName: '{}'", pkgName);
        logger.debug("versionName: '{}'", version);

        versionView.setText(version);

        // show licence
        TextView lisenseView = ((TextView)findViewById(R.id.licenceTextView));
        lisenseView.setText("APL \n"+URL_APL);

        // show other links
        ((TextView)findViewById(R.id.githubLabel)).setText("GitHub");
        ((TextView)findViewById(R.id.githubTextView)).setText(URL_GITHUB);

        ((TextView)findViewById(R.id.fdroidLabel)).setText("F-Droid");
        ((TextView)findViewById(R.id.fdroidTextView)).setText(URL_FDROID);

        ((TextView)findViewById(R.id.googlePlayLabel)).setText("Google Play");
        ((TextView)findViewById(R.id.googlePlayTextView)).setText(URL_GOOGLE_PLAY);

        ((TextView)findViewById(R.id.amazonLabel)).setText("Amazon");
        ((TextView)findViewById(R.id.amazonTextView)).setText(URL_AMAZON);
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        // navigate back -> the same as for PreferencesActivity
        switch (item.getItemId()) {
            case android.R.id.home:
            	finish();
                break;
        }
        return true;
    }
}

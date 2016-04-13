package cn.anline.www.ftp.server.primftpd.prefs;

import android.content.Context;
import android.preference.EditTextPreference;
import android.util.AttributeSet;

import cn.anline.www.ftp.server.primftpd.util.EncryptionUtil;
import cn.anline.www.ftp.server.primftpd.util.StringUtils;
import cn.anline.www.ftp.server.slf4j.Logger;
import cn.anline.www.ftp.server.slf4j.LoggerFactory;

public class EncryptingEditTextPreference extends EditTextPreference
{

	public EncryptingEditTextPreference(Context context)
	{
		super(context);
	}

	public EncryptingEditTextPreference(
			Context context,
			AttributeSet attrs,
			int defStyle)
	{
		super(context, attrs, defStyle);
	}

	public EncryptingEditTextPreference(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}


	protected static final Logger logger = LoggerFactory.getLogger(EncryptingEditTextPreference.class);

	@Override
	public String getText()
	{
		logger.debug("getText()");

		return "";
	}

	@Override
	public void setText(String text)
	{
		logger.debug("setText()");

		if (StringUtils.isBlank(text)) {
			logger.debug("is blank");

			super.setText(null);
			return;
		}
		super.setText(EncryptionUtil.encrypt(text));
	}

	@Override
	protected void onSetInitialValue(boolean restoreValue, Object defaultValue)
	{
		super.setText(restoreValue
				? getPersistedString(null)
				: (String) defaultValue);
	}
}

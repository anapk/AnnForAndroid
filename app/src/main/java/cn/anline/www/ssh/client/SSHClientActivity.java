package cn.anline.www.ssh.client;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

import cn.anline.www.R;
import cn.anline.www.ssh.client.dialogs.SshConnectFragmentDialog;
import cn.anline.www.ssh.client.sshutils.ConnectionStatusListener;
import cn.anline.www.ssh.client.sshutils.ExecTaskCallbackHandler;
import cn.anline.www.ssh.client.sshutils.SessionController;

public class SSHClientActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private TextView mConnectStatus;

    private SshEditText mCommandEdit;
    private Button mButton, mEndSessionBtn, mSftpButton;

    private Handler mHandler;
    private Handler mTvHandler;
    private String mLastLine;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        // Set no title
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_ssh_client);
        mButton = (Button) findViewById(R.id.enterbutton);
        mEndSessionBtn = (Button) findViewById(R.id.endsessionbutton);
        mSftpButton = (Button) findViewById(R.id.sftpbutton);
        mCommandEdit = (SshEditText) findViewById(R.id.command);
        mConnectStatus = (TextView) findViewById(R.id.connectstatus);
        // set onclicklistener
        mButton.setOnClickListener(this);
        mEndSessionBtn.setOnClickListener(this);
        mSftpButton.setOnClickListener(this);

        mConnectStatus.setText("连接状态：未连接");
        //handlers
        mHandler = new Handler();
        mTvHandler = new Handler();

        //text change listener, for getting the current input changes.
        mCommandEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String[] sr = editable.toString().split("\r\n");
                String s = sr[sr.length - 1];
                mLastLine = s;

            }
        });


        mCommandEdit.setOnEditorActionListener(
                new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        //Log.d(TAG, "editor action " + event);
                        if (isEditTextEmpty(mCommandEdit)) {
                            return false;
                        }

                        // run command
                        else {
                            if (event == null || event.getAction() != KeyEvent.ACTION_DOWN) {
                                return false;
                            }
                            // get the last line of terminal
                            String command = getLastLine();
                            ExecTaskCallbackHandler t = new ExecTaskCallbackHandler() {
                                @Override
                                public void onFail() {
                                    makeToast(R.string.taskfail);
                                }

                                @Override
                                public void onComplete(String completeString) {
                                }
                            };
                            mCommandEdit.AddLastInput(command);
                            SessionController.getSessionController().executeCommand(mHandler, mCommandEdit, t, command);
                            return false;
                        }
                    }
                }
        );
    }


    /**
     * Displays toast to user.
     *
     * @param text
     */

    private void makeToast(int text) {
        Toast.makeText(this, getResources().getString(text), Toast.LENGTH_SHORT).show();
    }

    /**
     * Start activity to do SFTP transfer. User will choose from list of files
     * to transfer.
     */
    private void startSftpActivity() {
        Intent intent = new Intent(this, SSHFileListActivity.class);
        String[] info = {
                SessionController.getSessionController().getSessionUserInfo().getUser(),
                SessionController.getSessionController().getSessionUserInfo().getHost(),
                SessionController.getSessionController().getSessionUserInfo().getPassword()
        };

        intent.putExtra("UserInfo", info);

        startActivity(intent);
    }

    /**
     * @return
     */
    private String getLastLine() {
        int index = mCommandEdit.getText().toString().lastIndexOf("\n");
        if (index == -1) {
            return mCommandEdit.getText().toString().trim();
        }
        if(mLastLine == null){
            Toast.makeText(this, "no text to process", Toast.LENGTH_LONG);
            return "";
        }
        String[] lines = mLastLine.split(Pattern.quote(mCommandEdit.getPrompt()));
        String lastLine = mLastLine.replace(mCommandEdit.getPrompt().trim(), "");
        Log.d(TAG, "command is " + lastLine + ", prompt is  " + mCommandEdit.getPrompt());
        return lastLine.trim();
    }

    private String getSecondLastLine() {

        String[] lines = mCommandEdit.getText().toString().split("\n");
        if (lines == null || lines.length < 2) return mCommandEdit.getText().toString().trim();

        else {
            int len = lines.length;
            String ln = lines[len - 2];
            return ln.trim();
        }
    }

    /**
     * Checks if the EditText is empty.
     *
     * @param editText
     * @return true if empty
     */
    private boolean isEditTextEmpty(EditText editText) {
        if (editText.getText() == null || editText.getText().toString().equalsIgnoreCase("")) {
            return true;
        }
        return false;
    }

    public void onClick(View v) {
        if (v == mButton) {
            showDialog();

        } else if (v == mSftpButton) {
            if (SessionController.isConnected()) {

                startSftpActivity();

            }
        } else if (v == this.mEndSessionBtn) {
            try {
                if (SessionController.isConnected()) {
                    SessionController.getSessionController().disconnect();
                }
            } catch (Throwable t) { //catch everything!
                Log.e(TAG, "Disconnect exception " + t.getMessage());
            }

        }

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    void showDialog() {

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");

        ft.addToBackStack(null);

        // Create and show the dialog.
        SshConnectFragmentDialog newFragment = SshConnectFragmentDialog.newInstance();
        newFragment.setListener(new ConnectionStatusListener() {
            @Override
            public void onDisconnected() {

                mTvHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mConnectStatus.setText("连接状态：未连接");
                    }
                });
            }

            @Override
            public void onConnected() {

                mTvHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mConnectStatus.setText("连接状态：已连接");
                    }
                });
            }
        });

        newFragment.show(ft, "dialog");
    }
}


package cn.anline.www.ssh.client.sshutils;


public interface ExecTaskCallbackHandler {

    void onFail();

    void onComplete(String completeString);
}

package com.wyliodrin.mobileapp.api;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smack.util.TLSUtils;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Andreea Stoican on 08.06.2015.
 */
public class ServerConnection {
    private static ServerConnection instance = null;

    private AbstractXMPPConnection connection;

    public enum LoginResult {
        Success,
        Failed
        }

    private ServerConnection() {}

    public static ServerConnection getInstance() {
        if (instance == null) {
            instance = new ServerConnection();
        }

        return instance;
    }

    public LoginResult connect(String username, String password) {
        XMPPTCPConnectionConfiguration.Builder configBuilder = XMPPTCPConnectionConfiguration.builder()
                .setUsernameAndPassword(username, password)
                .setServiceName("wyliodrin.org")
                .setHost("wyliodrin.org")
                .setPort(5222);

        try {
            TLSUtils.acceptAllCertificates(configBuilder);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        TLSUtils.disableHostnameVerificationForTlsCertificicates(configBuilder);

        XMPPTCPConnectionConfiguration config = configBuilder.build();

        connection = new XMPPTCPConnection(config);
        try {
            connection.connect();
        } catch (Exception e) {
            e.printStackTrace();
            return LoginResult.Failed;
        }

        return LoginResult.Success;
    }

}

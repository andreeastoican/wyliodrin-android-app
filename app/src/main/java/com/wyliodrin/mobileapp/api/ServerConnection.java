package com.wyliodrin.mobileapp.api;

import android.util.Log;

import com.google.zxing.common.StringUtils;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smack.util.TLSUtils;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

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
            connection.login();
        } catch (Exception e) {
            e.printStackTrace();
            return LoginResult.Failed;
        }

        connection.addAsyncPacketListener(new PacketListener() {
            @Override
            public void processPacket(Stanza packet) throws SmackException.NotConnectedException {

            }
        }, new PacketFilter() {
            @Override
            public boolean accept(Stanza packet) {
                Log.d("serverPacket", packet.toString());
                return false;
            }
        });

        Presence presence = new Presence(Presence.Type.available, "waiting messages", 50, Presence.Mode.available);
        try {
            connection.sendPacket(presence);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }

        sendMessage("test", "asd");
        return LoginResult.Success;
    }

    public List<RosterEntry> getBoardsList() {
        LinkedList<RosterEntry> boards = new LinkedList<RosterEntry>();

        Roster roster = Roster.getInstanceFor(connection);

        if (!roster.isLoaded())
            try {
                roster.reloadAndWait();
            } catch (SmackException.NotLoggedInException e) {
                e.printStackTrace();
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
            }

        Collection<RosterEntry> entries = roster.getEntries();
        for (RosterEntry entry : entries) {
            Log.d("rosterEntry", "name" + entry.getUser());

            boards.add(entry);
        }

        return boards;
    }

    public void sendMessage(final String label, String message) {

        String to = getBoardsList().get(0).getUser();
        Stanza m = new Stanza() {
            @Override
            public CharSequence toXML() {
                long time = System.currentTimeMillis() / 1000;
                return "<signal xmlns='wyliodrin' signal='test' value='1' time=\"" + time + "\" id=''></signal>";
            }
        };
        //m.setBody(body);

        Log.d("test", m.toString());
        try {
            connection.sendPacket(m);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

}

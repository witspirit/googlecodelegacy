package growl.delegate.growltalk;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.LinkedHashSet;
import java.util.Set;

public class GrowlTalkDelegate {
    public static final String LOCALHOST = "127.0.0.1";
    public static final int DEFAULT_GROWLTALK_PORT = 9887;
    private static final int MIN_PORT = 0;
    private static final int MAX_PORT = 0xFFFF;

    private InetAddress destinationHost;
    private int destinationPort = DEFAULT_GROWLTALK_PORT;
    private final String applicationName;
    private final GrowlAuthentication auth;
    private final Set<NotificationType> supportedNotificationTypes = new LinkedHashSet<NotificationType>();

    public GrowlTalkDelegate(String applicationName) {
        this(applicationName, null, LOCALHOST, DEFAULT_GROWLTALK_PORT);
    }
    
    public GrowlTalkDelegate(String applicationName, String password) {
        this(applicationName, password, LOCALHOST, DEFAULT_GROWLTALK_PORT);
    }
    
    public GrowlTalkDelegate(String applicationName, String password, String destinationHost, int destinationPort) {
        this.applicationName = applicationName;
        if (password == null) {
            this.auth = new NoGrowlAuthentication();
        } else {
            this.auth = new MD5GrowlAuthentication(password);
        }
        setDestinationHost(destinationHost);
        setDestinationPort(destinationPort);
    }

    public InetAddress getDestinationHost() {
        return destinationHost;
    }

    public void setDestinationHost(InetAddress destinationHost) {
        if (destinationHost == null) {
            throw new NullPointerException("Destination cannot be null");
        }
        this.destinationHost = destinationHost;
    }

    public void setDestinationHost(String hostname) {
        try {
            this.destinationHost = InetAddress.getByName(hostname);
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException("Unknown Host: " + e.getMessage());
        }
    }

    public int getDestinationPort() {
        return destinationPort;
    }

    public void setDestinationPort(int port) {
        if (port < MIN_PORT || port > MAX_PORT) {
            throw new IllegalArgumentException("Port number has to be between " + MIN_PORT + " and " + MAX_PORT);
        }
        this.destinationPort = port;
    }

    public Notification addNotificationType(String name, boolean enabledByDefault) {
        NotificationType type = new SimpleNotificationType(name, enabledByDefault);
        return addNotificationType(type);
    }

    public Notification addNotificationType(NotificationType type) {
        supportedNotificationTypes.add(type);
        return new NotificationImpl(type);
    }
    

    public void register() {
        RegistrationPacket registrationPacket = new RegistrationPacket(auth, applicationName);
        for (NotificationType notificationType : supportedNotificationTypes) {
            registrationPacket.addNotificationType(notificationType.getName(), notificationType.isEnabledByDefault());
        }
        deliver(registrationPacket);
    }

    public void notify(NotificationType notificationType, String title, String description) {
        notify(notificationType, title, description, GrowlTalkPriority.NORMAL, false);
    }

    public void notify(NotificationType notificationType, String title, String description, GrowlTalkPriority priority,
            boolean sticky) {
        if (!supportedNotificationTypes.contains(notificationType)) {
            throw new RuntimeException("Unsupported NotificationType " + notificationType + ". Only "
                    + supportedNotificationTypes + " are supported");
        }
        NotificationPacket notificationPacket = new NotificationPacket(auth, applicationName, priority, sticky, notificationType.getName(),
                title, description);
        deliver(notificationPacket);
    }

    private void deliver(GrowlPacket growlPacket) {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();
            byte[] buf = growlPacket.asMessageBytes();
            DatagramPacket packet = new DatagramPacket(buf, 0, buf.length, destinationHost, destinationPort);
            socket.send(packet);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
    }

    private class SimpleNotificationType implements NotificationType {
        private String name;
        private boolean enabledByDefault;

        public SimpleNotificationType(String name, boolean enabledByDefault) {
            this.name = name;
            this.enabledByDefault = enabledByDefault;
        }

        public String getName() {
            return name;
        }

        public boolean isEnabledByDefault() {
            return enabledByDefault;
        }
    }

    private class NotificationImpl implements Notification {
        private NotificationType type;

        public NotificationImpl(NotificationType type) {
            this.type = type;
        }

        public NotificationType getType() {
            return type;
        }

        public void send(String title, String description) {
            GrowlTalkDelegate.this.notify(type, title, description);
        }

        public void send(String title, String description, GrowlTalkPriority priority, boolean sticky) {
            GrowlTalkDelegate.this.notify(type, title, description, priority, sticky);
        }

    }
}

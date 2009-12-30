package growl.delegate.growltalk;

public class NoGrowlAuthentication implements GrowlAuthentication {

    @Override
    public GrowlTalkVersion getProtocolVersion() {
        return GrowlTalkVersion.PLAIN;
    }
    
    @Override
    public GrowlTalkPacketType getRegistrationType() {
        return GrowlTalkPacketType.REGISTRATION_NOAUTH;
    }
    
    @Override
    public GrowlTalkPacketType getNotificationType() {
        return GrowlTalkPacketType.NOTIFICATION_NOAUTH;
    }

    @Override
    public byte[] generateChecksum(byte[] message) {
        return new byte[0];
    }
}
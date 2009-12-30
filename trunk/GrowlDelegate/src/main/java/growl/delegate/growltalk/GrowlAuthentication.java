package growl.delegate.growltalk;

public interface GrowlAuthentication {
    GrowlTalkVersion getProtocolVersion();
    
    GrowlTalkPacketType getRegistrationType();
    GrowlTalkPacketType getNotificationType();
    
    byte[] generateChecksum(byte[] message);
}

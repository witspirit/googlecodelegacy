package growl.delegate.growltalk;

import java.io.IOException;

public interface GrowlPacket {
	byte[] asMessageBytes() throws IOException;
}

package growl.delegate.growltalk;

import java.io.UnsupportedEncodingException;

public class Util {
    private static final String UTF8 = "UTF-8";
    
    public static byte[] toUTF8(String text) {
        try {
            return text.getBytes(UTF8);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}

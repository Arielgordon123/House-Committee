package House_Committee;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class Encoder {
    private static String bytesToHex(byte[] hash) {
        String hex;
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1)
                hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
    public static String strEncoder(String toEncode, String algo) throws NoSuchAlgorithmException {

        MessageDigest digest = MessageDigest.getInstance(algo);
        byte[] hash = digest.digest(toEncode.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hash);


    }
}

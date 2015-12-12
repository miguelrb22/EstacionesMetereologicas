import com.sun.crypto.provider.SunJCE;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.spec.KeySpec;

public class Cypher {
    public static void main(String[] args) throws Exception {
        String data = "1";

        String encrypt = Encrypt(data);
        System.out.println(encrypt);

        String decrypt = Decrypt(encrypt);
        System.out.println(decrypt);
    }

    private static String Encrypt(String raw) throws Exception {
        Cipher c = getCipher(Cipher.ENCRYPT_MODE);

        byte[] encryptedVal = c.doFinal(raw.getBytes("UTF-8"));
        return new BASE64Encoder().encode(encryptedVal);
    }

    private static Cipher getCipher(int mode) throws Exception {
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding", new SunJCE());

        //a random Init. Vector. just for testing
        byte[] iv = "e675f725e675f725".getBytes("UTF-8");

        c.init(mode, generateKey(), new IvParameterSpec(iv));
        return c;
    }

    private static String Decrypt(String encrypted) throws Exception {

        byte[] decodedValue = new BASE64Decoder().decodeBuffer(encrypted);

        Cipher c = getCipher(Cipher.DECRYPT_MODE);
        byte[] decValue = c.doFinal(decodedValue);

        return new String(decValue);
    }

    private static Key generateKey() throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        char[] password = "Pass@word1".toCharArray();
        byte[] salt = "S@1tS@1t".getBytes("UTF-8");

        KeySpec spec = new PBEKeySpec(password, salt, 65536, 128);
        SecretKey tmp = factory.generateSecret(spec);
        byte[] encoded = tmp.getEncoded();
        return new SecretKeySpec(encoded, "AES");

    }
}

package nightcanteen.ashish.com.nightcanteen;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by ashish on 21/4/16.
 */
public class EncryptPassword {

    String convert(String str){

        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        messageDigest.update(str.getBytes());
        String encryptedString = new String(messageDigest.digest());
        return encryptedString;
    }
}

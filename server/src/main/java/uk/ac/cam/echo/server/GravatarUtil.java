package uk.ac.cam.echo.server;

import uk.ac.cam.echo.data.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class GravatarUtil {
    public static String hash(String input) {
        try {
            byte[] hash = MessageDigest.getInstance("MD5").digest(input.getBytes());

            StringBuffer sb = new StringBuffer();
            for(byte b : hash) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String getUrl(String hash) {
        return "http://www.gravatar.com/avatar/" + hash + "?d=identicon";
    }

    public static String getIdenticon() {
        return null;
    }

    public static String getUrl(User user) {
        String hash;
        if (user.getEmail() == null)
            hash = hash(user.getId() + user.getUsername());
        else
            hash = hash(user.getEmail());
        return getUrl(hash);
    }
}

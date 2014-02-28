package uk.ac.cam.echo;

import android.graphics.Bitmap;


public class UserCache {
    Bitmap avatar;
    String user;
    String jobAndCompany;
    String interests;
    String phone;
    String email;
    String lastActive;
    int colour;
    boolean hasAttributes;

    public UserCache(String s, int col) {
        lastActive = s;
        colour = col;
    }

    public UserCache setAttributes(Bitmap avatar, String user, String jobAndCompany,
                                   String interests, String phone, String email) {
        hasAttributes = true;

        this.avatar = avatar;
        this.user = user;
        this.jobAndCompany = jobAndCompany;
        this.interests = interests;
        this.phone = phone;
        this.email = email;

        return this;
    }

    public boolean hasAttributes() {
        return hasAttributes;
    }
}
package uk.ac.cam.echo;


import java.util.Collection;

import uk.ac.cam.echo.data.Interest;
import uk.ac.cam.echo.data.Tag;
import uk.ac.cam.echo.data.User;

public class ConversationStringUtil {

    public static String getUserText(Collection<User> users) {

        if(users.size() > 0) {
            StringBuffer userString = new StringBuffer();
            for(User u : users)
                userString.append(u.getUsername() +" ");
            return userString.toString();
        } else {
            return "No active users.";
        }

    }

    public static String getTagText(Collection<Tag> tags) {
        if(tags.size() > 0) {
            StringBuffer tagString = new StringBuffer();
            for(Tag t : tags)
                tagString.append(t.getName() + " ");

            return tagString.toString();
        } else {
            return "No tags associated.";
        }
    }

    public static String getInterestText(Collection<Interest> interests) {
        if(interests.size() > 0) {
            StringBuffer interestBuffer = new StringBuffer();
            for(Interest i : interests)
                interestBuffer.append(i.getName() + " ");

            return interestBuffer.toString();
        } else {
            return "No interests associated";
        }
    }

    public static String getJobCompanyText(String job, String company) {
        if(job == null || job.equals("")) {
            if(company == null || company.equals("")) {
                return "";
            } else {
                return "Works at " + company;
            }
        } else {
            if(company == null || company.equals("")) {
                return job;
            } else {
                return  job + " at " + company;
            }
        }
    }

    public static String getOnlineText(Collection<User> users) {
        return users.size() + " online users";
    }
}

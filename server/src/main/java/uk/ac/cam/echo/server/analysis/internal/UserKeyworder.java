package uk.ac.cam.echo.server.analysis.internal;

import uk.ac.cam.echo.data.Interest;
import uk.ac.cam.echo.data.Tag;
import uk.ac.cam.echo.data.User;

import java.util.*;

/**
 Author: Petar 'PetarV' Veličković

 This class analyses a User object, returning all
 relevant single-word keywords to that user.

 It also assigns a "score" to each keyword, by following this table:
 - First / Last name: 2 pts (reference point)
 - Job title: 1 pt
 - Company: 1 pt
 - Current conversation name: 2 pts
 - Current conversation tags: 1 pt
 - Interests: 4 pts
*/
public class UserKeyworder
{
    /**
     Extracts the keywords for the user and keeps score for each one.

     @param user    The User to extract the keywords from.
     @return        The unordered mapping between the keywords and scores.
    */
    public static Map<String, Integer> extractKeywords(User user)
    {
        Map<String, Integer> ret = new HashMap<String, Integer>();

        if (user.getFirstName() != null)
        {
            String firstNameKwd = user.getFirstName().toLowerCase(Locale.ENGLISH).replaceAll("[^a-zA-Z ]", " ");
            String[] kwds = firstNameKwd.split("\\s+");
            for (String kwd : kwds)
            {
                if (!ret.containsKey(kwd)) ret.put(kwd, 2);
                else
                {
                    int oldValue = ret.get(kwd);
                    ret.put(kwd, oldValue + 2);
                }
            }
        }

        if (user.getLastName() != null)
        {
            String lastNameKwd = user.getLastName().toLowerCase(Locale.ENGLISH).replaceAll("[^a-zA-Z ]"," ");
            String[] kwds = lastNameKwd.split("\\s+");
            for (String kwd : kwds)
            {
                if (!ret.containsKey(kwd)) ret.put(kwd, 2);
                else
                {
                    int oldValue = ret.get(kwd);
                    ret.put(kwd, oldValue + 2);
                }
            }
        }

        if (user.getJobTitle() != null)
        {
            String jobTitleKwd = user.getJobTitle().toLowerCase(Locale.ENGLISH).replaceAll("[^a-zA-Z ]", " ");
            String[] kwds = jobTitleKwd.split("\\s+");
            for (String kwd : kwds)
            {
                if (!ret.containsKey(kwd)) ret.put(kwd, 1);
                else
                {
                    int oldValue = ret.get(kwd);
                    ret.put(kwd, oldValue + 1);
                }
            }
        }

        if (user.getCompany() != null)
        {
            String companyKwd = user.getCompany().toLowerCase(Locale.ENGLISH).replaceAll("[^a-zA-Z ]", " ");
            String[] kwds = companyKwd.split("\\s+");
            for (String kwd : kwds)
            {
                if (!ret.containsKey(kwd)) ret.put(kwd, 1);
                else
                {
                    int oldValue = ret.get(kwd);
                    ret.put(kwd, oldValue + 1);
                }
            }
        }

        if (user.getCurrentConversation() != null)
        {
            String convoNameKwd = user.getCurrentConversation().getName().toLowerCase(Locale.ENGLISH).replaceAll("[^a-zA-Z ]", " ");
            String[] kwds = convoNameKwd.split("\\s+");
            for (String kwd : kwds)
            {
                if (!ret.containsKey(kwd)) ret.put(kwd, 2);
                else
                {
                    int oldValue = ret.get(kwd);
                    ret.put(kwd, oldValue + 2);
                }
            }

            Collection<Tag> tags = user.getCurrentConversation().getTags();
            if (tags != null)
            {
                for (Tag t : tags)
                {
                    String tagNameKwd = t.getName().toLowerCase(Locale.ENGLISH).replaceAll("[^a-zA-Z ]"," ");
                    String[] tkwds = tagNameKwd.split("\\s+");
                    for (String kwd : tkwds)
                    {
                        if (!ret.containsKey(kwd)) ret.put(kwd, 1);
                        else
                        {
                            int oldValue = ret.get(kwd);
                            ret.put(kwd, oldValue + 1);
                        }
                    }
                }
            }
        }

        Collection<Interest> interests = user.getInterests();
        if (interests != null)
        {
            for (Interest I : interests)
            {
                String interestKwd = I.getName().toLowerCase(Locale.ENGLISH).replaceAll("[^a-zA-Z ]", " ");
                String[] ikwds = interestKwd.split("\\s+");
                for (String kwd : ikwds)
                {
                    if (!ret.containsKey(kwd)) ret.put(kwd, 4);
                    else
                    {
                        int oldValue = ret.get(kwd);
                        ret.put(kwd, oldValue + 4);
                    }
                }
            }
        }

        return ret;
    }

    /**
     This method is currently only used for testing, but might be useful.
     It returns a string containing all the keywords extracted from the user,
     repeated as described in the point system.

     @param user    The User to extract the string from.
     @return        The string containing the keywords of the user.
    */
    public static String buildPersonalKeyword(User user)
    {
        Map<String, Integer> keywords = extractKeywords(user);
        String ret = "";
        for (String s : keywords.keySet())
        {
            int val = keywords.get(s);
            while (val > 0)
            {
                ret += s + " ";
                val--;
            }
        }
        return ret;
    }

}

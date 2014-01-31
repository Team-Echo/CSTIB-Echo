package uk.ac.cam.echo.server.analysis;

import uk.ac.cam.echo.data.Conference;
import uk.ac.cam.echo.data.Conversation;
import uk.ac.cam.echo.data.Tag;
import uk.ac.cam.echo.data.User;
import uk.ac.cam.echo.server.analysis.cmp.ConversationComparatorByUserCount;

import java.util.*;

/**
 TODO.
 Author: Petar 'PetarV' Veličković

 An implementation of the ServerDataAnalyst interface.
 Note that many implementations are possible,
 depending on the particular analysis criteria we're after.
*/
public class DataAnalyst implements ServerDataAnalyst
{
    Conference parentConference = null;

    public DataAnalyst(Conference c)
    {
        parentConference = c;
    }

    @Override
    public List<Conversation> search(String keyword, int n)
    {
        return null;
    }

    @Override
    public List<Conversation> onlyTagSearch(String keyword, int n)
    {
        List<Conversation> ret = new LinkedList<Conversation>();

        List<Conversation> matchesByName = new LinkedList<Conversation>();
        List<Conversation> matchesByTag = new LinkedList<Conversation>();

        Set<Conversation> conversations = parentConference.getConversationSet();

        for (Conversation C : conversations)
        {
            if (C.getName().contains(keyword)) matchesByName.add(C);
            else
            {
                Set<Tag> tags = C.getTags();
                for (Tag t : tags)
                {
                    if (t.getName().contains(keyword))
                    {
                        matchesByTag.add(C);
                        break;
                    }
                }
            }
        }

        ListIterator<Conversation> it1 = matchesByName.listIterator();
        ListIterator<Conversation> it2 = matchesByTag.listIterator();

        while (n > 0 && it1.hasNext())
        {
            ret.add(it1.next());
            n--;
        }

        while (n > 0 && it2.hasNext())
        {
            ret.add(it2.next());
            n--;
        }

        return ret;
    }

    @Override
    public List<Conversation> mostUsers(int n)
    {
        List<Conversation> ret = new LinkedList<Conversation>();
        Set<Conversation> conversations = parentConference.getConversationSet();
        PriorityQueue<Conversation> pq = new PriorityQueue<Conversation>(11, new ConversationComparatorByUserCount());

        for (Conversation C : conversations) pq.offer(C);
        while (n > 0 && !pq.isEmpty())
        {
            ret.add(pq.poll());
            n--;
        }

        return ret;
    }

    @Override
    public List<Conversation> mostActiveRecently(int minutes, int n)
    {
        return null;
    }

    @Override
    public List<Conversation> recommend(User user, int n)
    {
        return null;
    }
}

package uk.ac.cam.echo.server.analysis;

import uk.ac.cam.echo.data.*;
import uk.ac.cam.echo.server.analysis.cmp.ConversationComparatorByActivity;
import uk.ac.cam.echo.server.analysis.cmp.ConversationComparatorByMatchFrequency;
import uk.ac.cam.echo.server.analysis.cmp.ConversationComparatorByUserCount;
import uk.ac.cam.echo.server.analysis.internal.DoubleConversationPair;
import uk.ac.cam.echo.server.analysis.internal.IntegerConversationPair;
import uk.ac.cam.echo.server.analysis.internal.MessageLexer;
import uk.ac.cam.echo.server.analysis.internal.StringMatcher;

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
    String dictionary, affix, stopWords;

    public DataAnalyst(Conference c)
    {
        parentConference = c;
        dictionary = this.getClass().getResource("/dictionaries/en_GB/en_GB.dic").getPath();
        affix = this.getClass().getResource("/dictionaries/en_GB/en_GB.aff").getPath();
        stopWords = this.getClass().getResource("/stop_lists/stop_list.txt").getPath();
    }

    @Override
    public List<Conversation> search(String keyword, int n)
    {
        keyword = keyword.toLowerCase(Locale.ENGLISH);
        String[] keywords = keyword.split("\\s+");

        List<Conversation> ret = new LinkedList<Conversation>();

        List<Conversation> matchesByName = new LinkedList<Conversation>();
        List<Conversation> matchesByTag = new LinkedList<Conversation>();

        Set<Conversation> processed = new HashSet<Conversation>();

        Collection<Conversation> conversations = parentConference.getConversationSet();

        for (Conversation C : conversations)
        {
            boolean foundByName = false;
            for (String kwd : keywords)
            {
                if (C.getName().toLowerCase(Locale.ENGLISH).contains(kwd))
                {
                    matchesByName.add(C);
                    processed.add(C);
                    foundByName = true;
                    break;
                }
            }
            if (!foundByName)
            {
                boolean foundByTag = false;
                Collection<Tag> tags = C.getTags();
                for (Tag t : tags)
                {
                    for (String kwd : keywords)
                    {
                        if (t.getName().toLowerCase(Locale.ENGLISH).contains(kwd))
                        {
                            matchesByTag.add(C);
                            processed.add(C);
                            foundByTag = true;
                            break;
                        }
                    }
                    if (foundByTag) break;
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

        if (n > 0)
        {
            PriorityQueue<DoubleConversationPair> pq = new PriorityQueue<DoubleConversationPair>(11, new ConversationComparatorByMatchFrequency());

            for (Conversation C : conversations)
            {
                if (processed.contains(C)) continue;
                Collection<Message> messages = C.getMessages();
                double totalScore = 0.0;
                for (Message msg : messages)
                {
                    List<String> baseWords = MessageLexer.lexAnalyse(msg.getContents(), dictionary, affix, stopWords);
                    double total = (double)baseWords.size();
                    for (String word : baseWords)
                    {
                        for (String kwd : keywords)
                        {
                            totalScore += StringMatcher.Match(kwd, word) / total;
                        }
                    }
                }
                pq.offer(new DoubleConversationPair(totalScore, C));
            }

            while (n > 0 && !pq.isEmpty())
            {
                ret.add(pq.poll().getConvo());
                n--;
            }
        }

        return ret;
    }

    @Override
    public List<Conversation> onlyTagSearch(String keyword, int n)
    {
        keyword = keyword.toLowerCase(Locale.ENGLISH);
        String[] keywords = keyword.split("\\s+");

        List<Conversation> ret = new LinkedList<Conversation>();

        List<Conversation> matchesByName = new LinkedList<Conversation>();
        List<Conversation> matchesByTag = new LinkedList<Conversation>();

        Collection<Conversation> conversations = parentConference.getConversationSet();

        for (Conversation C : conversations)
        {
            boolean foundByName = false;
            for (String kwd : keywords)
            {
                if (C.getName().toLowerCase(Locale.ENGLISH).contains(kwd))
                {
                    matchesByName.add(C);
                    foundByName = true;
                    break;
                }
            }
            if (!foundByName)
            {
                boolean foundByTag = false;
                Collection<Tag> tags = C.getTags();
                for (Tag t : tags)
                {
                    for (String kwd : keywords)
                    {
                        if (t.getName().toLowerCase(Locale.ENGLISH).contains(kwd))
                        {
                            matchesByTag.add(C);
                            foundByTag = true;
                            break;
                        }
                    }
                    if (foundByTag) break;
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
    public List<Conversation> onlyNameSearch(String keyword, int n)
    {
        keyword = keyword.toLowerCase(Locale.ENGLISH);
        String[] keywords = keyword.split("\\s+");

        List<Conversation> ret = new LinkedList<Conversation>();
        Collection<Conversation> conversations = parentConference.getConversationSet();

        for (Conversation C : conversations)
        {
            for (String kwd : keywords)
            {
                if (C.getName().toLowerCase(Locale.ENGLISH).contains(kwd))
                {
                    ret.add(C);
                    break;
                }
            }
            if (ret.size() == n) break;
        }

        return ret;
    }


    @Override
    public List<Conversation> mostUsers(int n)
    {
        List<Conversation> ret = new LinkedList<Conversation>();
        Collection<Conversation> conversations = parentConference.getConversationSet();
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
    public List<Conversation> mostActiveRecently(long minutes, int n)
    {
        List<Conversation> ret = new LinkedList<Conversation>();
        Collection<Conversation> conversations = parentConference.getConversationSet();
        PriorityQueue<IntegerConversationPair> pq = new PriorityQueue<IntegerConversationPair>(11, new ConversationComparatorByActivity());

        long now = new Date().getTime();

        for (Conversation C : conversations)
        {
            int cnt = 0;
            Collection<Message> msgs = C.getMessages();

            // PRECONDITION: msgs is sorted descending by timestamp.

            for (Message M : msgs)
            {
                if ((now - M.getTimeStamp()) / 60000 > minutes) break;
                else cnt++;
            }
            pq.offer(new IntegerConversationPair(cnt, C));
        }

        while (n > 0 && !pq.isEmpty())
        {
            ret.add(pq.poll().getConvo());
            n--;
        }

        return ret;
    }

    @Override
    public List<Conversation> recommend(User user, int n)
    {
        return null;
    }
}

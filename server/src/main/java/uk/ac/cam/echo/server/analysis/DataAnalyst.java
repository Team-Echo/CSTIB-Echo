package uk.ac.cam.echo.server.analysis;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import uk.ac.cam.echo.data.*;
import uk.ac.cam.echo.server.HibernateUtil;
import uk.ac.cam.echo.server.analysis.cmp.*;
import uk.ac.cam.echo.server.analysis.internal.*;
import uk.ac.cam.echo.server.models.ConferenceModel;
import uk.ac.cam.echo.server.models.ConversationModel;
import uk.ac.cam.echo.server.models.MessageModel;
import uk.ac.cam.echo.server.models.UserModel;

import java.util.*;

/**
 Author: Petar 'PetarV' Veličković

 An implementation of the ServerDataAnalyst interface.
 Note that many implementations are possible,
 depending on the particular analysis criteria we're after.
*/
public class DataAnalyst implements ServerDataAnalyst
{
    long parentID;
    String dictionary = this.getClass().getResource("/dictionaries/en_GB/en_GB.dic").getPath();
    String affix = this.getClass().getResource("/dictionaries/en_GB/en_GB.aff").getPath();
    String stopWords = this.getClass().getResource("/stop_lists/stop_list.txt").getPath();

    public DataAnalyst(long conferenceID)
    {
        parentID = conferenceID;
    }

    @Override
    public Map<String, Long> getKeywords(Conversation conversation, long lastTimeStamp)
    {
        List<Message> msgs = (List<Message>)conversation.getSortedMessages();
        Collections.reverse(msgs); // because the query returns them in the opposite order

        Map<String, Long> ret = new HashMap<String, Long>();

        long lastTS = lastTimeStamp;
        for (Message M : msgs)
        {
            long TS = M.getTimeStamp();
            if (TS < lastTimeStamp) break;
            if (TS > lastTS) lastTS = TS;
            List<String> kwds = MessageLexer.lexAnalyse(M.getContents(), dictionary, affix, stopWords);
            for (String kwd : kwds)
            {
                if (!ret.containsKey(kwd)) ret.put(kwd, 1L);
                else
                {
                    long prevValue = ret.get(kwd);
                    ret.put(kwd, prevValue+1);
                }
            }
        }

        ret.put("TS", lastTS);

        return ret;
    }

    @Override
    public Map<String, Long> getKeywords(long lastTimeStamp)
    {
        Conference parentConference = (Conference) HibernateUtil.getTransaction().get(ConferenceModel.class, parentID);
        Collection<Conversation> conversations = parentConference.getConversationSet();

        Map<String, Long> ret = new HashMap<String, Long>();

        long lastTS = lastTimeStamp;

        for (Conversation C : conversations)
        {
            List<Message> msgs = (List<Message>)C.getSortedMessages();
            Collections.reverse(msgs); // because the query returns them in the opposite order
            for (Message M : msgs)
            {
                long TS = M.getTimeStamp();
                if (TS < lastTimeStamp) break;
                if (TS > lastTS) lastTS = TS;
                List<String> kwds = MessageLexer.lexAnalyse(M.getContents(), dictionary, affix, stopWords);
                for (String kwd : kwds)
                {
                    if (!ret.containsKey(kwd)) ret.put(kwd, 1L);
                    else
                    {
                        long prevValue = ret.get(kwd);
                        ret.put(kwd, prevValue+1);
                    }
                }
            }
        }

        ret.put("TS", lastTS);

        return ret;
    }

    @Override
    public void updateGraph()
    {
        Conference parentConference = (Conference) HibernateUtil.getTransaction().get(ConferenceModel.class, parentID);
        Collection<Conversation> conversations = parentConference.getConversationSet();

        long nextLastTS = GraphUtil.lastTS;

        for (Conversation C : conversations)
        {
            List<Message> msgs = (List<Message>)C.getSortedMessages();
            Collections.reverse(msgs); // because the query returns them in the opposite order
            for (Message M : msgs)
            {
                if (M.getTimeStamp() > nextLastTS) nextLastTS = M.getTimeStamp();
                if (M.getTimeStamp() <= GraphUtil.lastTS) break;
                List<String> keywords = MessageLexer.lexAnalyse(M.getContents(), dictionary, affix, stopWords);
                ListIterator<String> it1 = keywords.listIterator();
                while (it1.hasNext())
                {
                    String u = it1.next();
                    String U = u.substring(0, 1).concat(".").concat(u);
                    ListIterator<String> it2 = keywords.listIterator(it1.nextIndex());
                    while (it2.hasNext())
                    {
                        String v = it2.next();
                        String V = v.substring(0, 1).concat(".").concat(v);
                        GraphUtil.addEdge(U, V);
                    }
                }
            }
        }

        GraphUtil.lastTS = nextLastTS;
    }

    @Override
    public void updateFGraph()
    {
        /*
         Multi-pass method.
         1. Get Conference (type 0).
         2. For each Conversation (type 1):
           3. Connect to Conference.
           4. Connect to Tags (type 4).
           5. For each Message (type 2) (**incrementally**):
             6. Connect to Conversation.
             7. Connect to User (type 3).
         8. For each User:
           9. Connect to Conversation.
           10. Connect to Interests (type 5).
        */

        Conference parentConference = (Conference) HibernateUtil.getTransaction().get(ConferenceModel.class, parentID);
        Collection<Conversation> conversations = parentConference.getConversationSet();
        List<User> users = HibernateUtil.getTransaction().createCriteria(UserModel.class).list();

        long nextLastTS = NPForceGraph.lastTS;

        String confName = parentConference.getName();
        long confIid = parentConference.getId();

        NPForceGraph.addNode(confName, 0, confIid);
        for (Conversation C : conversations)
        {
            String convName = C.getName();
            long convIid = C.getId();

            NPForceGraph.addEdge(confName, 0, confIid, convName, 1, convIid);

            Collection<Tag> tags = C.getTags();
            if (tags != null)
            {
                for (Tag T : tags)
                {
                    String tagName = T.getName();
                    long tagIid = T.getId();

                    NPForceGraph.addEdge(convName, 1, convIid, tagName, 4, tagIid);
                }
            }

            List<Message> messages = (List<Message>)C.getSortedMessages();
            Collections.reverse(messages); // because query outputs them in reverse order
            for (Message M : messages)
            {
                if (M.getTimeStamp() > nextLastTS) nextLastTS = M.getTimeStamp();
                if (M.getTimeStamp() <= GraphUtil.lastTS) break;

                String msgFullContents = M.getContents();
                String msgName = msgFullContents.substring(0, Math.min(10, msgFullContents.length())).concat("...");
                long msgIid = M.getId();

                NPForceGraph.addEdge(convName, 1, convIid, msgName, 2, msgIid);

                User sender = M.getSender();
                if (sender != null)
                {
                    String senderName = sender.getUsername();
                    long senderIid = sender.getId();

                    NPForceGraph.addEdge(msgName, 2, msgIid, senderName, 3, senderIid);
                }
            }
        }

        for (User U : users)
        {
            String userName = U.getUsername();
            long userIid = U.getId();

            NPForceGraph.addNode(userName, 3, userIid);

            Conversation convo = U.getCurrentConversation();
            if (convo != null)
            {
                String convoName = convo.getName();
                long convoIid = convo.getId();

                NPForceGraph.addEdge(userName, 3, userIid, convoName, 1, convoIid);
            }

            Collection<Interest> interests = U.getInterests();
            if (interests != null)
            {
                for (Interest I : interests)
                {
                    String interestName = I.getName();
                    long interestIid = I.getId();

                    NPForceGraph.addEdge(userName, 3, userIid, interestName, 5, interestIid);
                }
            }
        }

        NPForceGraph.lastTS = nextLastTS;
    }

    @Override
    public List<Conversation> search(String keyword, int n)
    {
        Conference parentConference = (Conference) HibernateUtil.getTransaction().get(ConferenceModel.class, parentID);

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
                    if (baseWords.isEmpty()) continue;
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
    public List<Conversation> onlyKeywordSearch(String keyword, int n)
    {
        Conference parentConference = (Conference) HibernateUtil.getTransaction().get(ConferenceModel.class, parentID);

        keyword = keyword.toLowerCase(Locale.ENGLISH);
        String[] keywords = keyword.split("\\s+");

        List<Conversation> ret = new LinkedList<Conversation>();

        Collection<Conversation> conversations = parentConference.getConversationSet();

        PriorityQueue<DoubleConversationPair> pq = new PriorityQueue<DoubleConversationPair>(11, new ConversationComparatorByMatchFrequency());

        for (Conversation C : conversations)
        {
            Collection<Message> messages = C.getMessages();
            double totalScore = 0.0;
            for (Message msg : messages)
            {
                List<String> baseWords = MessageLexer.lexAnalyse(msg.getContents(), dictionary, affix, stopWords);
                if (baseWords.isEmpty()) continue;
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

        return ret;
    }

    @Override
    public List<Conversation> onlyTagSearch(String keyword, int n)
    {
        Conference parentConference = (Conference) HibernateUtil.getTransaction().get(ConferenceModel.class, parentID);

        keyword = keyword.toLowerCase(Locale.ENGLISH);
        String[] keywords = keyword.split("\\s+");

        List<Conversation> ret = new LinkedList<Conversation>();

        Collection<Conversation> conversations = parentConference.getConversationSet();

        for (Conversation C : conversations)
        {
            boolean foundByTag = false;
            Collection<Tag> tags = C.getTags();
            for (Tag t : tags)
            {
                for (String kwd : keywords)
                {
                    if (t.getName().toLowerCase(Locale.ENGLISH).contains(kwd))
                    {
                        ret.add(C);
                        n--;
                        foundByTag = true;
                        break;
                    }
                }
                if (foundByTag) break;
            }
            if (n == 0) break;
        }

        return ret;
    }

    @Override
    public List<Conversation> onlyNameSearch(String keyword, int n)
    {
        Conference parentConference = (Conference) HibernateUtil.getTransaction().get(ConferenceModel.class, parentID);

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
    public List<Conversation> nameAndTagSearch(String keyword, int n)
    {
        Conference parentConference = (Conference) HibernateUtil.getTransaction().get(ConferenceModel.class, parentID);

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
    public List<Conversation> mostUsers(int n)
    {
        Conference parentConference = (Conference) HibernateUtil.getTransaction().get(ConferenceModel.class, parentID);

        List<Conversation> ret = new LinkedList<Conversation>();
        Collection<Conversation> conversations = parentConference.getConversationSet();
        PriorityQueue<IntegerConversationPair> pq = new PriorityQueue<IntegerConversationPair>(11, new ConversationComparatorByUserCount());

        for (Conversation C : conversations) pq.offer(new IntegerConversationPair(C.getUsers().size(), C));
        while (n > 0 && !pq.isEmpty())
        {
            ret.add(pq.poll().getConvo());
            n--;
        }

        return ret;
    }

    @Override
    public List<Conversation> mostActiveRecently(long millis, int n)
    {
        Conference parentConference = (Conference) HibernateUtil.getTransaction().get(ConferenceModel.class, parentID);

        List<Conversation> ret = new LinkedList<Conversation>();
        Collection<Conversation> conversations = parentConference.getConversationSet();
        PriorityQueue<IntegerConversationPair> pq = new PriorityQueue<IntegerConversationPair>(11, new ConversationComparatorByActivity());

        long now = new Date().getTime();

        for (Conversation C : conversations)
        {
            int cnt = 0;
            List<Message> msgs = (List<Message>)C.getSortedMessages();
            Collections.reverse(msgs); // because the query returns them in the opposite order

            // PRECONDITION: msgs is sorted descending by timestamp.

            for (Message M : msgs)
            {
                if ((now - M.getTimeStamp()) > millis) break;
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
        Conference parentConference = (Conference) HibernateUtil.getTransaction().get(ConferenceModel.class, parentID);

        Map<String, Integer> keywords = UserKeyworder.extractKeywords(user);
        if (keywords.isEmpty()) return null; // NO DATA TO QUERY UPON; should not happen!

        Collection<Conversation> conversations = parentConference.getConversationSet();
        long now = new Date().getTime();

        PriorityQueue<DoubleConversationPair> pq = new PriorityQueue<DoubleConversationPair>(11, new ConversationComparatorByMatchFrequency());

        List<Conversation> ret = new LinkedList<Conversation>();

        for (Conversation C : conversations)
        {
            String normalisedName = C.getName().toLowerCase(Locale.ENGLISH).replaceAll("[^a-zA-Z ]"," ");
            List<String> normalisedTags = new LinkedList<String>();
            Collection<Tag> tags = C.getTags();
            for (Tag T : tags)
            {
                normalisedTags.add(T.getName().toLowerCase(Locale.ENGLISH).replaceAll("[^a-zA-Z ]"," "));
            }
            double currScore = 0.0;
            double total = (double)normalisedTags.size();
            for (String kwd : keywords.keySet())
            {
                currScore += StringMatcher.Match(kwd, normalisedName) * (double)keywords.get(kwd);
                if (normalisedTags.isEmpty()) continue;
                for (String ntg : normalisedTags)
                {
                    currScore += StringMatcher.Match(kwd, ntg) * (double)keywords.get(kwd) / total;
                }
            }

            // get the amount of minutes since conversation was last active
            double time = (double)(now - ((List<Message>)C.getMessages(1)).get(0).getTimeStamp()) / 60000.0;
            if (time == 0.0) time = 0.0000000001;
            currScore /= time;
            pq.offer(new DoubleConversationPair(currScore, C));
        }

        while (n > 0 && !pq.isEmpty())
        {
            ret.add(pq.poll().getConvo());
            n--;
        }

        return ret;
    }

    @Override
    public Message notify(User user, long currentId, long millis)
    {
        Conference parentConference = (Conference) HibernateUtil.getTransaction().get(ConferenceModel.class, parentID);

        Map<String, Integer> keywords = UserKeyworder.extractKeywords(user);
        if (keywords.isEmpty()) return null; // NO DATA TO QUERY UPON; should not happen!

        Collection<Conversation> conversations = parentConference.getConversationSet();

        long now = new Date().getTime();

        Message ret = null;
        double maxScore = -1.0;

        for (Conversation C : conversations)
        {
            if (C.getId() == currentId) continue;

            List<Message> mostRecent = ((List<Message>)C.getMessages(1));

            if (mostRecent.isEmpty()) continue;

            Message latest = mostRecent.get(0);
            if (now - millis > latest.getTimeStamp()) continue;

            List<String> baseWords = MessageLexer.lexAnalyse(latest.getContents(), dictionary, affix, stopWords);
            if (baseWords.isEmpty()) continue;

            double currScore = 0.0;
            double total = (double)baseWords.size();
            for (String word : baseWords)
            {
                for (String kwd : keywords.keySet())
                {
                    currScore += StringMatcher.Match(kwd, word) * ((double)keywords.get(kwd)) / total;
                }
            }
            if (currScore > maxScore)
            {
                maxScore = currScore;
                ret = latest;
            }
        }

        return ret;
    }

    @Override
    public List<Conversation> mostMessages(int n)
    {
        Conference parentConference = (Conference) HibernateUtil.getTransaction().get(ConferenceModel.class, parentID);

        List<Conversation> ret = new LinkedList<Conversation>();
        Collection<Conversation> conversations = parentConference.getConversationSet();
        PriorityQueue<IntegerConversationPair> pq = new PriorityQueue<IntegerConversationPair>(11, new ConversationComparatorByMessageCount());

        for (Conversation C : conversations) pq.offer(new IntegerConversationPair(C.getMessages().size(), C));
        while (n > 0 && !pq.isEmpty())
        {
            ret.add(pq.poll().getConvo());
            n--;
        }

        return ret;
    }

    @Override
    public List<User> mostActiveUsers(int n)
    {
        List<User> users = HibernateUtil.getTransaction().createCriteria(UserModel.class).list();

        List<User> ret = new LinkedList<User>();

        PriorityQueue<IntegerUserPair> pq = new PriorityQueue<IntegerUserPair>(11, new UserComparatorByActivity());

        for (User U : users)
        {
            int msgCnt = HibernateUtil.getTransaction().createCriteria(MessageModel.class)
                    .add(Restrictions.eq("sender", U)).list().size();
            pq.offer(new IntegerUserPair(msgCnt, U));
        }

        while (n > 0 && !pq.isEmpty())
        {
            ret.add(pq.poll().getUser());
            n--;
        }

        return ret;
    }

    @Override
    public int hail(long millis)
    {
        Conference parentConference = (Conference) HibernateUtil.getTransaction().get(ConferenceModel.class, parentID);

        Collection<Conversation> conversations = parentConference.getConversationSet();
        long now = new Date().getTime();

        int cnt = 0;

        for (Conversation C : conversations)
        {
            List<Message> msgs = (List<Message>) C.getSortedMessages();
            Collections.reverse(msgs);

            for (Message M : msgs)
            {
                if ((now - M.getTimeStamp()) > millis) break;
                else cnt++;
            }
        }

        return cnt;
    }

    @Override
    public double maleToFemaleRatio()
    {
        Conference parentConference = (Conference) HibernateUtil.getTransaction().get(ConferenceModel.class, parentID);
        Collection<Conversation> conversations = parentConference.getConversationSet();

        double maleCount = 0, femaleCount = 0;

        for (Conversation C : conversations)
        {
            Collection<User> users = C.getUsers();
            for (User U : users)
            {
                if (U.getGender() == null) continue;

                if (U.getGender().equals("M") || U.getGender().equals("Male")) maleCount++;
                if (U.getGender().equals("F") || U.getGender().equals("Female")) femaleCount++;
            }
        }

        if (femaleCount == 0.0) return Double.POSITIVE_INFINITY;
        return maleCount / femaleCount;
    }

    @Override
    public double maleToFemaleRatio(Conversation conversation)
    {
        double maleCount = 0, femaleCount=0;
        Collection<User> users = conversation.getUsers();

        for (User U : users)
        {
            if (U.getGender() == null) continue;

            if (U.getGender().equals("M") || U.getGender().equals("Male")) maleCount++;
            if (U.getGender().equals("F") || U.getGender().equals("Female")) femaleCount++;
        }

        if (femaleCount == 0.0) return Double.POSITIVE_INFINITY;
        return maleCount / femaleCount;
    }

    @Override
    public int messageCount(long convoId)
    {
        Conversation convo = (Conversation) HibernateUtil.getTransaction().get(ConversationModel.class, convoId);
        return convo.getMessages().size();
    }

    @Override
    public int messageCount()
    {
        Conference parentConference = (Conference) HibernateUtil.getTransaction().get(ConferenceModel.class, parentID);
        Collection<Conversation> conversations = parentConference.getConversationSet();

        int ret = 0;

        for (Conversation C : conversations)
        {
            ret += C.getMessages().size();
        }

        return ret;
    }

    @Override
    public int userCount(long convoId)
    {
        Conversation convo = (Conversation) HibernateUtil.getTransaction().get(ConversationModel.class, convoId);
        return convo.getUsers().size();
    }

    @Override
    public int userCount()
    {
        Conference parentConference = (Conference) HibernateUtil.getTransaction().get(ConferenceModel.class, parentID);
        Collection<Conversation> conversations = parentConference.getConversationSet();

        int ret = 0;

        for (Conversation C : conversations)
        {
            ret += C.getUsers().size();
        }

        return ret;
    }

    @Override
    public int contributingUsers(long convoId, boolean current)
    {
        Conversation convo = (Conversation) HibernateUtil.getTransaction().get(ConversationModel.class, convoId);

        Set<User> users = (Set<User>)convo.getUsers();
        Collection<Message> msgs = convo.getMessages();

        Set<Long> ret = new HashSet<Long>();

        for (Message M : msgs)
        {
            long id = M.getSender().getId();
            if (!current || users.contains(M.getSender())) ret.add(id);
        }

        return ret.size();
    }

    @Override
    public long lastTimeActive(User user)
    {
        List<Message> sols = HibernateUtil.getTransaction().createCriteria(MessageModel.class)
                .add(Restrictions.eq("sender", user)).addOrder(Order.desc("timeStamp")).list();
        long now = new Date().getTime();
        if (sols == null) return now;
        if (sols.size() == 0) return now;
        return now - sols.get(0).getTimeStamp();
    }

}

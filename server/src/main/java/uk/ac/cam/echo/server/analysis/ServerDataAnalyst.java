package uk.ac.cam.echo.server.analysis;

import uk.ac.cam.echo.data.Conversation;
import uk.ac.cam.echo.data.Message;
import uk.ac.cam.echo.data.User;

import java.util.List;
import java.util.Map;

/**
 Author: Petar 'PetarV' Veličković

 This is the main server Data Analysis module interface.
 It contains the kind of methods that the server will request
 from the analyst, as part of its request multiplexing/replying.
*/

public interface ServerDataAnalyst
{
    /**
     Extracts keywords from messages within a conversation.
     Also returns a special keyword, "TS", which is mapped to the last timestamp processed.

     @param conversation    The Conversation object within which to extract keywords.
     @param lastTimeStamp   The latest message timestamp to look for.
     @return                A mapping of each keyword to its frequency, including a special keyword "TS" as described above.
    */
    public Map<String, Long> getKeywords(Conversation conversation, long lastTimeStamp);

    /**
     Extracts keywords from messages globally.
     Also returns a special keyword, "TS", which is mapped to the last timestamp processed.

     @param lastTimeStamp   The latest message timestamp to look for.
     @return                A mapping of each keyword to its frequency, including a special keyword "TS" as described above.
    */
    public Map<String, Long> getKeywords(long lastTimeStamp);

    /**
     Updates the keyword graph.
    */
    public void updateGraph();

    /**
     Updates the force graph.
    */
    public void updateFGraph();

    /**
     Searches the conversations within the conference, with respect to the given keyword string.
     It searches by names, tags and by extracted keywords, giving priority to names, then tags.

     @param keyword     The string to search by.
     @param n           The maximal amount of solutions to return.
     @return            The list of (up to n) solutions to the search query, sorted descending by relevance.
    */
    public List<Conversation> search(String keyword, int n);

    /**
     Searches the conversations within the conference, with respect to the given keyword string.
     It searches only by the conversation keywords.

     @param keyword     The string to search by.
     @param n           The maximal amount of solutions to return.
     @return            The list of (up to n) solutions to the search query, sorted descending by relevance.
    */
    public List<Conversation> onlyKeywordSearch(String keyword, int n);

    /**
     Searches the conversations within the conference, with respect to the given keyword string.
     It searches only by the conversation tags.

     @param keyword     The string to search by.
     @param n           The maximal amount of solutions to return.
     @return            The list of (up to n) solutions to the search query, sorted descending by relevance.
    */
    public List<Conversation> onlyTagSearch(String keyword, int n);

    /**
     Searches the conversations within the conference, with respect to the given keyword string.
     It searches only by the conversation names.

     @param keyword     The string to search by.
     @param n           The maximal amount of solutions to return.
     @return            The list of (up to n) solutions to the search query, sorted descending by relevance.
    */
    public List<Conversation> onlyNameSearch(String keyword, int n);

    /**
     Searches the conversations within the conference, with respect to the given keyword string.
     It searches only by the conversation names and tags, giving priority to names.

     @param keyword     The string to search by.
     @param n           The maximal amount of solutions to return.
     @return            The list of (up to n) solutions to the search query, sorted descending by relevance.
    */
    public List<Conversation> nameAndTagSearch(String keyword, int n);

    /**
     Displays the most active conversations in terms of user count.

     @param n           The maximal amount of solutions to return.
     @return            The list of (up to n) conversations with the most users, sorted descending by user count.
    */
    public List<Conversation> mostUsers(int n);

    /**
     Displays the most active conversations in terms of message count, in the interval [now-minutes, now].

     @param millis      The size of the interval to consider, in milliseconds.
     @param n           The maximal amount of solutions to return.
     @return            The list of (up to n) conversations that have been most active recently, sorted descending by activity.
    */
    public List<Conversation> mostActiveRecently(long millis, int n);

    /**
     Displays a list of suggested conversation for a user, based on:
      - the user's own interests (prioritised), and his/her message activity;
      - the tags (prioritised) and keywords extracted for each conversation.

     @param user        The User we are querying for.
     @param n           The maximal amount of solutions to return.
     @return            The list of (up to n) conversations that we are suggesting to the user, sorted descending by relevance.
    */
    public List<Conversation> recommend(User user, int n);

    /**
     Returns the latest message in a Conversation
     that would most match some User's interests.

     @param user        The User we are querying for.
     @param currentId   The User's current conversation id (ruled out). Set to -1 to not rule out any conversation.
     @param millis      The size of the latest message interval to consider, in milliseconds.
     @return            The Message that the user should be notified about.
    */
    public Message notify(User user, long currentId, long millis);

    /**
     Displays the most active conversations in terms of total message count.

     @param n           The maximal amount of solutions to return.
     @return            The list of (up to n) conversations with the most messages, sorted by message count.
    */
    public List<Conversation> mostMessages(int n);

    /**
     Displays the most active users of all time, in terms of message count.
     @param n           The maximal amount of solutions to return.
     @return            The list of (up to n) users with the most messages, sorted by message count.
    */
    public List<User> mostActiveUsers(int n);

    /**
     Displays the amount of hail (activity) in the conference.
     It is represented as the amount of messages divided by the interval.

     @param millis      The size of the interval to consider, in milliseconds.
     @return            The hail in the conference over the interval.
    */
    public int hail(long millis);

    /**
     Displays the current M:F ratio in the conference.
     Users are only considered if they are currently in a conversation.

     @return            The M:F ratio.
    */
    public double maleToFemaleRatio();

    /**
     Displays the current M:F ratio in a given conversation.

     @param cnv         The Conversation object in which to calculate the ratio.
     @return            The M:F ratio within the conversation.
    */
    public double maleToFemaleRatio(Conversation cnv);

    /**
     Returns the amount of messages within a particular Conversation.

     @param convoId     The identifier of the Conversation.
     @return            The amount of messages within the conversation.
    */
    public int messageCount(long convoId);

    /**
     Returns the total amount of messages within the conference.

     @return            The amount of messages in the conference.
    */
    public int messageCount();

    /**
     Returns the amount of users within a particular Conversation.

     @param convoId     The identifier of the Conversation.
     @return            The amount of users within the conversation.
    */
    public int userCount(long convoId);

    /**
     Returns the total amount of users within a particular Conference.
     A user is only considered if he is within a Conversation.

     @return            The amount of users within the conference.
    */
    public int userCount();

    /**
     Returns the total amount of contributing users for a conversation.

     @param convoId     The identifier of the Conversation.
     @param current     Whether or not to consider only currently active users.
     @return            The amount of contributing users within the conversation.
    */
    public int contributingUsers(long convoId, boolean current);

    /**
     Returns the difference between the current time and the last time the given User was active.
     @param user        The User we are querying for.
     @return            The desired time difference.
     */
    public long lastTimeActive(User user);
}

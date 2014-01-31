package uk.ac.cam.echo.server.analysis;

import uk.ac.cam.echo.data.Conversation;
import uk.ac.cam.echo.data.User;

import java.util.List;

/**
 TODO.
 Author: Petar 'PetarV' Veličković

 This is the main server Data Analysis module interface.
 It contains the kind of methods that the server will request
 from the analyst, as part of its request multiplexing/replying.
*/

public interface ServerDataAnalyst
{
    /**
     Searches the conversations within the conference, with respect to the given keyword string.
     It searches both by tags and by extracted keywords, giving priority to tags.

     @param keyword     The string to search by.
     @param n           The maximal amount of solutions to return.
     @return            The list of (up to n) solutions to the search query, sorted descending by relevance.
    */
    public List<Conversation> search(String keyword, int n);

    /**
     Displays the most active conversations in terms of user count.

     @param n           The maximal amount of solutions to return.
     @return            The list of (up to n) conversations with the most users, sorted descending by user count.
    */
    public List<Conversation> mostUsers(int n);

    /**
     Displays the most active conversations in terms of message count, in the interval [now-minutes, now].

     @param minutes     The size of the interval to consider.
     @param n           The maximal amount of solutions to return.
     @return            The list of (up to n) conversations that have been most active recently, sorted descending by activity.
    */
    public List<Conversation> mostActiveRecently(int minutes, int n);

    /**
     Displays a list of suggested conversation for a user, based on:
      - the user's own interests (prioritised), and his/her message activity;
      - the tags (prioritised) and keywords extracted for each conversation.

     @param user        The User we are querying for.
     @param n           The maximal amount of solutions to return.
     @return            The list of (up to n) conversations that we are suggesting to the user, sorted descending by relevance.
    */
    public List<Conversation> recommend(User user, int n);

    // TODO: Add more methods as necessary.
}

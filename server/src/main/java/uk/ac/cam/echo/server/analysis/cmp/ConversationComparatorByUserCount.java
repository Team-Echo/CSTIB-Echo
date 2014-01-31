package uk.ac.cam.echo.server.analysis.cmp;

import uk.ac.cam.echo.data.Conversation;

import java.util.Comparator;

/**
 Author: Petar 'PetarV' Veličković

 A Comparator that compares two conversations by user count.
 It is reversed, in order to make our priority queues max-heaps instead of min-heaps.
*/
public class ConversationComparatorByUserCount implements Comparator<Conversation>
{
    @Override
    public int compare(Conversation cnvA, Conversation cnvB)
    {
        int A = cnvA.getUsers().size();
        int B = cnvB.getUsers().size();

        if (A < B) return 1;
        else if (A > B) return -1;
        return 0;
    }
}

package uk.ac.cam.echo.server.analysis.cmp;

import uk.ac.cam.echo.data.Conversation;
import uk.ac.cam.echo.server.analysis.internal.IntegerConversationPair;

import java.util.Comparator;

/**
 Author: Petar 'PetarV' Veličković

 A Comparator that compares two conversations by recent activity (with an interval parameter).
 It is reversed, in order to make our priority queues max-heaps instead of min-heaps.
*/
public class ConversationComparatorByActivity implements Comparator<IntegerConversationPair>
{
    private int minutes;

    public ConversationComparatorByActivity(int minutes)
    {
        this.minutes = minutes;
    }

    @Override
    public int compare(IntegerConversationPair cnvA, IntegerConversationPair cnvB)
    {
        // Immutability of Conversations involved assumed! Only use on snapshots.
        // Otherwise extremely thread-unsafe.

        int iA = cnvA.getInt(), iB = cnvB.getInt();
        if (iA < iB) return 1;
        if (iA > iB) return -1;

        return new ConversationComparatorByUserCount().compare(cnvA.getConvo(), cnvB.getConvo());
    }
}

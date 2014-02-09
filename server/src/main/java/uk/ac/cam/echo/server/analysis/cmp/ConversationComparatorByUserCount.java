package uk.ac.cam.echo.server.analysis.cmp;

import uk.ac.cam.echo.server.analysis.internal.IntegerConversationPair;

import java.util.Comparator;

/**
 Author: Petar 'PetarV' Veličković

 A Comparator that compares two conversations by user count.
 It is reversed, in order to make our priority queues max-heaps instead of min-heaps.
*/
public class ConversationComparatorByUserCount implements Comparator<IntegerConversationPair>
{
    @Override
    public int compare(IntegerConversationPair cnvA, IntegerConversationPair cnvB)
    {
        int iA = cnvA.getInt(), iB = cnvB.getInt();
        if (iA < iB) return 1;
        if (iA > iB) return -1;

        return 0;
    }
}

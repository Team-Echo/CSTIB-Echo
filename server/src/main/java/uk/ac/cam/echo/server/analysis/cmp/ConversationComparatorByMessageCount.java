package uk.ac.cam.echo.server.analysis.cmp;

import uk.ac.cam.echo.server.analysis.internal.IntegerConversationPair;

import java.util.Comparator;

/**
 Author: Petar 'PetarV' Veličković

 A Comparator that compares two conversations by message count.
 It is reversed, in order to make our priority queues max-heaps instead of min-heaps.
*/
public class ConversationComparatorByMessageCount implements Comparator<IntegerConversationPair>
{
    @Override
    public int compare(IntegerConversationPair cnvA, IntegerConversationPair cnvB)
    {
        // Immutability of Conversations involved assumed! Only use on snapshots.
        // Otherwise extremely thread-unsafe.

        double dA = cnvA.getInt(), dB = cnvB.getInt();
        if (dA < dB) return 1;
        if (dA > dB) return -1;

        return 0;
    }
}

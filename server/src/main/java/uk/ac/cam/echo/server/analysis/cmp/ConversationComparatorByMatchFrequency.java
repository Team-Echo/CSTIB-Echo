package uk.ac.cam.echo.server.analysis.cmp;

import uk.ac.cam.echo.server.analysis.internal.DoubleConversationPair;

import java.util.Comparator;

/**
 Author: Petar 'PetarV' Veličković

 A Comparator that compares two conversations by keyword match frequency.
 It is reversed, in order to make our priority queues max-heaps instead of min-heaps.
*/
public class ConversationComparatorByMatchFrequency implements Comparator<DoubleConversationPair>
{
    @Override
    public int compare(DoubleConversationPair cnvA, DoubleConversationPair cnvB)
    {
        // Immutability of Conversations involved assumed! Only use on snapshots.
        // Otherwise extremely thread-unsafe.

        double dA = cnvA.getDouble(), dB = cnvB.getDouble();
        if (dA < dB) return 1;
        if (dA > dB) return -1;

        return 0;
    }
}

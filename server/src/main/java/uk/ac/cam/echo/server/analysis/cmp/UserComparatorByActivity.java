package uk.ac.cam.echo.server.analysis.cmp;

import uk.ac.cam.echo.server.analysis.internal.IntegerUserPair;

import java.util.Comparator;

/**
 Author: Petar 'PetarV' Veličković
 A Comparator that compares two users by activity.
 It is reversed, in order to make our priority queues max-heaps instead of min-heaps.
*/
public class UserComparatorByActivity implements Comparator<IntegerUserPair>
{
    @Override
    public int compare(IntegerUserPair usrA, IntegerUserPair usrB)
    {
        // Immutability of Users involved assumed! Only use on snapshots.
        // Otherwise extremely thread-unsafe.

        int iA = usrA.getInt(), iB = usrB.getInt();
        if (iA < iB) return 1;
        if (iA > iB) return -1;

        return 0;
    }

}

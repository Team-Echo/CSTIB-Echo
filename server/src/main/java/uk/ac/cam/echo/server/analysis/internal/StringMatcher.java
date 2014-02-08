package uk.ac.cam.echo.server.analysis.internal;

/**
 Author: Petar 'PetarV' Veličković

 A class implementing the Knuth-Morris-Pratt (KMP)
 algorithm for fast string matching.

 It uses a slightly modified version that records
 the largest partial match, in the event of no
 full matches being found.
*/
public class StringMatcher
{
    /**
     An implementation of the KMP algorithm, which
     computes matches between two string in linear time.

     @param ndl         The string containing the pattern to match.
     @param hystck      The string in which to look for matches.
     @return            (largest partial match) / (needle length). 1 if full match found.
    */
    public static double Match(String ndl, String hystck)
    {
        int n = hystck.length(), m = ndl.length();

        char[] needle = ndl.toCharArray();
        char[] haystack = hystck.toCharArray();

        int[] P = new int[m];

        for (int i=0;i<m;i++) P[i] = -1;

        for (int i=0,j=-1;i<m;)
        {
            while (j > -1 && needle[i] != needle[j]) j = P[j];
            i++; j++;
            P[i] = j;
        }

        int longestMatch = 0;

        for (int i=0,j=0;i<n;i++)
        {
            while (j > -1 && haystack[i] != needle[j]) j = P[j];
            i++; j++;
            if (j == m) return 1;
            if (j > longestMatch) longestMatch = j;
        }

        return ((double)longestMatch) / ((double)m);
    }
}

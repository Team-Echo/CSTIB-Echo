package uk.ac.cam.echo.server.analysis.porterstemmer;

/**
 Author: Petar 'PetarV' Veličković

 A proxy class for simplifying the communication
 between the analysis module and the stemmer.
*/
public class StemProxy
{
    private static Stemmer stemmer = new Stemmer();

    public static String stem(String word)
    {
        stemmer.add(word.toCharArray(), word.length());
        stemmer.stem();
        return stemmer.toString();
    }
}

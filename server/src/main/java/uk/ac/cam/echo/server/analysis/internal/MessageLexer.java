package uk.ac.cam.echo.server.analysis.internal;

import uk.ac.cam.echo.server.analysis.hunspell.SpellChecker;
import uk.ac.cam.echo.server.analysis.porterstemmer.StemProxy;

import java.io.*;
import java.util.*;

/**
 Author: Petar 'PetarV' Veličković

 This class does lexical analysis on a string message,
 ideally returning a list of base words contained
 within that message.

 Errors are possible, as this is an open problem in NLP.
 However we will treat those errors as correct output
 when doing our keyword search.

 The algorithm is thoroughly described within.
*/
public class MessageLexer
{
    private static SpellChecker checker = null;
    private static Set<String> stopWords = null;
    private static Map<String, String> cache = new HashMap<String, String>();

    /**
     The main method that handles lexical analysis.

     The algorithm can be described in steps as follows:
        1. Convert the entire string to lowercase.
        2. Remove punctuation symbols and other non-letters, replacing them by whitespace.
        3. Split the resulting string into words using whitespace as a delimiter.
        4. Perform a spell-checker pass over the resulting words.
        5. Remove stop-words and single-character words from the list.
        6. Perform a stemming pass over the resulting words, to reduce words to base form.
        7. Perform another spell-checking pass, to try and fix errors made by the stemmer.
        8. Return the obtained list of words.

     @param message     A string containing the message to be decoded.
     @param dictionary  The path to an English dictionary file.
     @param affix       The path to an English dictionary affix file.
     @param stopList    The path to a file containing the list of stop-words.
     @return            The list of base words extracted from the message.
    */
    public static List<String> lexAnalyse(String message, String dictionary, String affix, String stopList)
    {
        // Initialising the necessary resources.
        if (checker == null) checker = new SpellChecker(dictionary, affix);
        if (stopWords == null)
        {
            stopWords = new HashSet<String>();
            try
            {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new BufferedInputStream(new FileInputStream(stopList))));
                String stopWord;
                while ((stopWord = reader.readLine()) != null) stopWords.add(stopWord);

            }
            catch (IOException e)
            {
                e.printStackTrace();
                return new LinkedList<String>();
            }

        }
        List<String> ret = new LinkedList<String>();

        // Step 1.
        message = message.toLowerCase(Locale.ENGLISH);

        // Step 2.
        message = message.replaceAll("[^a-zA-Z ]"," ");

        // Step 3.
        String[] tokens = message.split("\\s+");

        // Steps 4 & 5.
        for (int i=0;i<tokens.length;i++)
        {
            tokens[i] = checker.correct(tokens[i]);
            if (!stopWords.contains(tokens[i]) && tokens[i].length() > 1) ret.add(tokens[i]);
        }

        // Steps 6 & 7.
        ListIterator<String> it = ret.listIterator();
        while (it.hasNext())
        {
            String word = it.next();
            if (cache.containsKey(word)) it.set(cache.get(word));
            else
            {
                String baseWord = checker.correct(StemProxy.stem(word)).toLowerCase(Locale.ENGLISH);
                cache.put(word, baseWord);
                it.set(baseWord);
            }
        }

        // Step 8.
        return ret;
    }
}

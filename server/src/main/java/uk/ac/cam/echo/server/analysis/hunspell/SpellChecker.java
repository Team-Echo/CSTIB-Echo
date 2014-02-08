package uk.ac.cam.echo.server.analysis.hunspell;

import com.atlascopco.hunspell.Hunspell;

import java.util.List;
import java.util.Locale;

/**
 Author: Petar 'PetarV' Veličković

 A class that connects to the Hunspell library
 on behalf of the data analysis module
 in order to simplify the communication.

 Hunspell is released under the LGPL licence,
 a copy of which is provided within this folder.
*/
public class SpellChecker
{
    private Hunspell speller;
    private String dictionaryPath;
    private String affixPath;

    public SpellChecker(String dictionary, String affix)
    {
        this.speller = new Hunspell(dictionary, affix);
        this.dictionaryPath = dictionary;
        this.affixPath = affix;
    }

    private boolean check(String word)
    {
        return speller.spell(word);
    }

    public String correct(String word)
    {
        // Automatically take the first suggestion.
        // If there are no suggestions, add word to dictionary.
        if (check(word)) return word;
        else
        {
            List<String> suggestions = speller.suggest(word);
            if (suggestions.isEmpty())
            {
                speller.add(word);
                return word;
            }
            return suggestions.get(0);
        }
    }

    public void kill()
    {
        speller.close();
    }

}
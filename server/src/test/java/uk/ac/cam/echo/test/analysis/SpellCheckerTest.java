package uk.ac.cam.echo.test.analysis;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uk.ac.cam.echo.server.analysis.hunspell.SpellChecker;

import java.io.File;
import java.net.URL;

/**
 * Created by PetarV on 2/5/14.
 */
public class SpellCheckerTest
{
    private SpellChecker checker;
    private String[] words = new String[]
                    {"teh", "quic", "brown", "foxx", "jmps", "overe", "the", "lzy", "dog"};

    @Before
    public void setUp() throws Exception
    {
        String dictionary = this.getClass().getResource("/dictionaries/en_GB/en_GB.dic").getPath();
        String affix = this.getClass().getResource("/dictionaries/en_GB/en_GB.aff").getPath();
        checker = new SpellChecker(dictionary, affix);
    }

    @After
    public void tearDown() throws Exception
    {
        checker.kill();
    }

    @Test
    public void testSpellChecker()
    {
        for (int i=0;i<words.length;i++)
        {
            System.out.println(checker.correct(words[i]));
        }
    }

}

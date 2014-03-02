package uk.ac.cam.echo.server.analysis;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uk.ac.cam.echo.server.analysis.internal.MessageLexer;

/**
 Author: Petar 'PetarV' Veličković
*/
public class MessageLexerTest
{
    private String message =
            "The current financial crisis is the worst the world has seen since the Great Depression " +
                    "of the 1930s. For younger generations, accustomed to mild recessions of the " +
                    "new phase of globalization, the misery of the Great Depression is hitherto " +
                    "nothing more than a distant legend. However, the collapse of two Bear Stearns " +
                    "Hedge funds in summer of 2007 exposed what came to be known as the subprime " +
                    "mortgage crisis, reintroducing the world to an era of bank failures, " +
                    "a credit crunch, private defaults and massive layoffs. In the new, " +
                    "globalized world of closely interdependent economies, the crisis affected " +
                    "almost every part of the world, receiving extensive coverage in the international " +
                    "media. “In an Interconnected World, American Homeowner Woes Can Be Felt from " +
                    "Beijing to Rio de Janeiro,” observed the International Herald Tribune at the " +
                    "onset of the crisis. “Chinese Steelmakers Shiver, Indian Miners Catch Flu,” " +
                    "noted the Hindustan Times. “US and China Must Tame Imbalances Together,” " +
                    "suggested YaleGlobal, as the frenzied search for a solution continues around the " +
                    "globe.";

    private String message2 =
            "Germany’s surplus has grown steadily larger over the years: from a modest USD40 billion " +
                    "in 2002, it reached a whopping USD248 billion in 2007 and has remained at roughly " +
                    "that level, a reflection not just of Germany’s export success but also its " +
                    "unwillingness to reinvest buoyant export revenues in the German economy. For " +
                    "the first few years, Germany’s rising surplus was recycled into rising deficits " +
                    "in the US and Southern Europe. Its excess savings found their way into American " +
                    "mortgage-backed securities and Southern European government debt, not the wisest " +
                    "investments ever made. Ultimately, Germany’s giant surplus only helped to spur " +
                    "the financial bubble that contributed to the West’s economic downfall.";

    private String dictionary;
    private String affix;
    private String stopWords;

    @Before
    public void setUp() throws Exception
    {
        dictionary = this.getClass().getResource("/dictionaries/en_GB/en_GB.dic").getPath();
        affix = this.getClass().getResource("/dictionaries/en_GB/en_GB.aff").getPath();
        stopWords = this.getClass().getResource("/stop_lists/stop_list.txt").getPath();
    }

    @After
    public void tearDown() throws Exception
    {
        // NOP
    }

    @Test
    public void testMessageLexer()
    {
        System.out.println(MessageLexer.lexAnalyse(message, dictionary, affix, stopWords));
        System.out.println(MessageLexer.lexAnalyse(message2, dictionary, affix, stopWords));
    }
}

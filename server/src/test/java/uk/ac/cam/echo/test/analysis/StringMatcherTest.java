package uk.ac.cam.echo.test.analysis;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uk.ac.cam.echo.server.analysis.internal.StringMatcher;

/**
 Author: Petar 'PetarV' Veličković
*/
public class StringMatcherTest
{
    @Before
    public void setUp() throws Exception
    {
        // NOP
    }

    @After
    public void tearDown() throws Exception
    {
        // NOP
    }

    @Test
    public void testMatcher()
    {
        assert(StringMatcher.Match("bc", "abcabc") == 1.0);
        assert(StringMatcher.Match("troll", "t") == 0.2);
        assert(StringMatcher.Match("xyz", "ijk") == 0.0);
        assert(StringMatcher.Match("", "aoufhas") == 0.0);
        assert(StringMatcher.Match("efgh", "") == 0.0);
    }
}

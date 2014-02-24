package uk.ac.cam.echo.test.analysis;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uk.ac.cam.echo.data.Conversation;
import uk.ac.cam.echo.data.Interest;
import uk.ac.cam.echo.data.Tag;
import uk.ac.cam.echo.data.User;
import uk.ac.cam.echo.server.analysis.internal.UserKeyworder;
import uk.ac.cam.echo.server.models.ConversationModel;
import uk.ac.cam.echo.server.models.InterestModel;
import uk.ac.cam.echo.server.models.TagModel;
import uk.ac.cam.echo.server.models.UserModel;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 Author: Petar 'PetarV' Veličković
*/
public class UserKeyworderTest
{
    private UserModel test_user;
    private ConversationModel test_conversation;
    private Set<Tag> test_tags;
    private Set<Interest> test_interests;

    @Before
    public void setUp() throws Exception
    {
        Tag t1 = new TagModel();
        t1.setName("Tag XYZ");
        Tag t2 = new TagModel();
        t2.setName("ZYX gaT");

        test_tags = new HashSet<Tag>();
        test_tags.add(t1);
        test_tags.add(t2);

        Interest i1 = new InterestModel();
        i1.setName("Interest velickovic");

        test_interests = new HashSet<Interest>();
        test_interests.add(i1);

        test_conversation = new ConversationModel();
        test_conversation.setName("Conversation XYZ");
        test_conversation.setTags(test_tags);

        test_user = new UserModel();
        test_user.setFirstName("Petar");
        test_user.setLastName("Velickovic");
        test_user.setJobTitle("Intern Software Design Engineer");
        test_user.setCompany("MDCS Software");
        test_user.setCurrentConversation(test_conversation);
        test_user.setInterests(test_interests);
    }

    @After
    public void tearDown() throws Exception
    {
        // NOP
    }

    @Test
    public void testKeyworder()
    {
        Map<String, Integer> keywords = UserKeyworder.extractKeywords(test_user);
        assert(keywords.get("petar") == 2);
        assert(keywords.get("velickovic") == 6);
        assert(keywords.get("intern") == 1);
        assert(keywords.get("software") == 2);
        assert(keywords.get("design") == 1);
        assert(keywords.get("engineer") == 1);
        assert(keywords.get("mdcs") == 1);
        assert(keywords.get("conversation") == 2);
        assert(keywords.get("xyz") == 3);
        assert(keywords.get("tag") == 1);
        assert(keywords.get("gat") == 1);
        assert(keywords.get("zyx") == 1);
        assert(keywords.get("interest") == 4);

        System.out.println(UserKeyworder.buildPersonalKeyword(test_user));
    }
}

package uk.ac.cam.echo.test.analysis;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uk.ac.cam.echo.data.Conversation;
import uk.ac.cam.echo.data.Tag;
import uk.ac.cam.echo.data.User;
import uk.ac.cam.echo.server.analysis.internal.UserKeyworder;
import uk.ac.cam.echo.server.models.ConversationModel;
import uk.ac.cam.echo.server.models.TagModel;
import uk.ac.cam.echo.server.models.UserModel;

import java.util.Map;

/**
 Author: Petar 'PetarV' Veličković
*/
public class UserKeyworderTest
{
    private User test_user;
    private Conversation test_conversation;

    @Before
    public void setUp() throws Exception
    {
        test_conversation = new ConversationModel();
        test_conversation.setName("Conversation XYZ");

        test_user = new UserModel();
        test_user.setFirstName("Petar");
        test_user.setLastName("Velickovic");
        test_user.setJobTitle("Intern Software Design Engineer");
        test_user.setCompany("MDCS Software");
        test_user.setCurrentConversation(test_conversation);
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
        assert(keywords.get("velickovic") == 2);
        assert(keywords.get("intern") == 1);
        assert(keywords.get("software") == 2);
        assert(keywords.get("design") == 1);
        assert(keywords.get("engineer") == 1);
        assert(keywords.get("mdcs") == 1);
        assert(keywords.get("conversation") == 2);
        assert(keywords.get("xyz") == 2);

        System.out.println(UserKeyworder.buildPersonalKeyword(test_user));
    }
}

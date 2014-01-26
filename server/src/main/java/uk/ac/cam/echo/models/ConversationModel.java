package uk.ac.cam.echo.models;

import uk.ac.cam.echo.data.Conversation;
import uk.ac.cam.echo.data.Message;
import uk.ac.cam.echo.data.Tag;
import uk.ac.cam.echo.data.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Table(name="Conversation")
public class ConversationModel implements Conversation {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="ConversationIdSeq")
    @SequenceGenerator(name="ConversationIdSeq", sequenceName="Conversation_SEQ", allocationSize=1)
    private long id;

    private String name;

    @ManyToMany(targetEntity = TagModel.class)
    private Set<Tag> tags;
    private Set<Message> messages;

    @OneToMany(targetEntity = UserModel.class, mappedBy = "currentConversation")
    private Set<User> users;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void addUser(User u) {
        users.add(u);
    }

    public void removeUser(User u) {
        if (users.contains(u)) {
            users.remove(u);
        }
    }

    public void addMessage(Message m) {
        messages.add(m);
    }

    public List<Message> getSortedMessages() {
        return new ArrayList<Message>(messages);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

}

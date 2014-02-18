package uk.ac.cam.echo.server.models;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import uk.ac.cam.echo.data.Conversation;
import uk.ac.cam.echo.data.User;
import uk.ac.cam.echo.server.HibernateUtil;

import javax.persistence.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
@Entity
@Table(name="UserTable")
public class UserModel extends BaseModel implements User {

    public UserModel() {

    }

    public static String hashPassword(String password) {

        try {
            byte[] hash = MessageDigest.getInstance("SHA-512").digest(password.getBytes());

            StringBuffer sb = new StringBuffer();
            for(byte b : hash) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

    }

    private static String[] allowed = {
            "username",
            "password",
            "currentConversationId",
            "firstName",
            "lastName",
            "phoneNumber",
            "avatarLink",
            "email",
            "jobTitle",
            "company",
            "gender"
    };

    @JsonCreator
    public UserModel(Map<String, Object> props) {
        super(props, allowed);
    }

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="UserIdSeq")
    @SequenceGenerator(name="UserIdSeq", sequenceName="USER_SEQ", allocationSize=1)
    private long id;
    private String username;
    private String hashedPassword;

    @ManyToOne(targetEntity = ConversationModel.class)
    private Conversation currentConversation;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String avatarLink;
    private String email;
    private String jobTitle;
    private String company;
    private String gender;


    @Transient
    public String getDisplayName() {
        if (getFirstName() == null && getLastName() == null)
            return null;
        if (getFirstName() == null)
            return getLastName();
        if (getLastName() == null)
            return getFirstName();

        return getFirstName() + " " + getLastName();
    }

    /*    Getters and Setters */
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Conversation getCurrentConversation() {
        return currentConversation;
    }

    public void setCurrentConversation(Conversation currentConversation) {
        this.currentConversation = currentConversation;
    }

    public void setCurrentConversationId(Long id) {
        setCurrentConversation((Conversation) HibernateUtil.getSession().load(ConversationModel.class, id));
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAvatarLink() {
        return avatarLink;
    }

    public void setAvatarLink(String avatarLink) {
        this.avatarLink = avatarLink;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @JsonIgnore
    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    @Transient
    @JsonProperty
    public void setPassword(String password) {
        setHashedPassword(hashPassword(password));
    }
}

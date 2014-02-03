package uk.ac.cam.echo.dummy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Conversation {
	
	long id;
	
	public Conversation(long id) {
		this.id = id;
	}
	
	public long getId() {
		return id;
	}
	
	public String getName() {
		StringBuffer name = new StringBuffer("Conversation ");
		name.append(Long.toString(id));
		return name.toString();
	}
	
	public List<Message> getMessages() {
		
		ArrayList<Message> conv = new ArrayList<Message>();
		
		for(int i = 0; i < 40; i++)
			conv.add(new Message(i*i + 2*i + 4));
		
		return conv;
	}
	
	public Set<User> getUsers() {
		HashSet<User> users = new HashSet<User>();
		String[] USER_LIST = {"Petar", "Philip", "Mona", "Alex", "Yojan", "John",
				"George", "Holly", "Mark"};


		for(String u : USER_LIST)
			if(Math.random() < 0.3)
				users.add(new User(u));
		
		return users;
	}
	
	public Set<Tag> getTags() {
		HashSet<Tag> tags = new HashSet<Tag>();
		
		String[] TAG_LIST = {"Cloud", "Quantum", "Robots", "Android", "Windows",
				"Social", "Gaming", "California"};

		for(String s : TAG_LIST)
			if(Math.random() < 0.3)
				tags.add(new Tag(s));
		
		return tags;
	}
	
	public class Message {
		long id;
		private static final String lorem = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibus. Vivamus elementum semper nisi. Aenean vulputate eleifend tellus. Aenean leo ligula, porttitor eu, consequat vitae, eleifend ac, enim. Aliquam lorem ante, dapibus in, viverra quis, feugiat a, tellus. Phasellus viverra nulla ut metus varius laoreet. ";
		public Message(long i) {
			id = i;
		}
		
		public String getContents() {
			int length = (int) (Math.random()*lorem.length());
			return lorem.substring(length);
		}
		
		public User getSender() {
			return new User("TestUser");
		}
		
		public boolean isRemote() {
			if(Math.random() < 0.5) {
				return true;
			}
			return false;
		}
	}
	
	public class User {
		String name;
		
		public User(String n) {
			name = n;
		}
		
		public String getName() {
			return name;
		}
	}
	
	public class Tag {
		String name;
		
		public Tag(String n) {
			name = n;
		}
		
		public String getName() {
			return name;
		}
	}
}



package uk.ac.cam.echo.services;

import java.util.List;

import uk.ac.cam.echo.client.ClientApi;
import uk.ac.cam.echo.data.Conference;
import uk.ac.cam.echo.data.Conversation;
import uk.ac.cam.echo.data.Message;
import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;

public class EchoService extends IntentService {

	private long conferenceId;
	private long conversationId;
	
	private Conference conference;
	private Conversation conversation;
	
	
	private static ClientApi api;
	
	public EchoService(String name) {
		super(name);
		api = new ClientApi("http://echoconf.herokuapp.com/");
	}
	
	public long getConferenceId() {
		return conferenceId;
	}
	
	public long getConversationId() {
		return conversationId;
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	public void sendMessage(String msgContents) {
		Message msg = api.newMessage(conversation);
		msg.setContents(msgContents);
		msg.save();
	}
	
	public boolean joinConference() {
		List<Conference> allConf = api.conferenceResource.getAll();
		if(allConf.size() > 0) {
			conference = allConf.get(0);
			return true;
		}
		return false;
	}
	
	public boolean selectConversation(long conversationId) {
		if(conference != null) {
			conversation = api.conversationResource.get(conversationId);
			return true;
		}
		return false;
	}
	
	public boolean createConversation(String name, String tags) {
		
		Conversation newConversation = api.newConversation();
		newConversation.setConference(conference);
		newConversation.setName(name);
		//newConversation.setTags(tags);
		newConversation.save();
		conversation = newConversation;
		
		return true;
	}
	
	

}

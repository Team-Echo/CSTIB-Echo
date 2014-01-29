package uk.ac.cam.echo;

import java.util.List;
import java.util.Set;

import uk.ac.cam.echo.dummy.Conversation;
import uk.ac.cam.echo.dummy.Conversation.Tag;
import uk.ac.cam.echo.dummy.Conversation.User;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ConversationAdapter extends ArrayAdapter<Conversation> {
	
	Context context;
	int layoutResourceId;
	List<Conversation> data = null;
	
	public ConversationAdapter(Context context, int layoutResourceId,
									List<Conversation> data) {
		
		super(context, layoutResourceId, data);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.data = data;
	}
	
	
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
		
		View row = convertView;
		ConversationHolder holder = null;
		
		if(row == null) {
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			
			holder = new ConversationHolder();
			
			holder.imgIcon = (ImageView)row.findViewById(R.id.rowImage);
			holder.title = (TextView)row.findViewById(R.id.convNameRow);
			holder.users = (TextView)row.findViewById(R.id.convUsersRow);
			holder.tags = (TextView)row.findViewById(R.id.convTagsRow);
			holder.totalOnline = (TextView)row.findViewById(R.id.convOnlineRow);
			
			row.setTag(holder);

		} else {
			holder = (ConversationHolder)row.getTag();
		}
		
		Conversation conversation = data.get(position);
		Set<User> users = conversation.getUsers();
		Set<Tag> tags = conversation.getTags();
		
		//TODO: set imgIcon from the icon resource of conversation
		holder.title.setText(conversation.getName());
		holder.users.setText(getUserText(users));
		holder.tags.setText(getTagText(tags));
		holder.totalOnline.setText(getOnlineText(users));

		return row;
	}
	
	public String getUserText(Set<User> users) {

		if(users.size() > 0) {
			StringBuffer userString = new StringBuffer();
			for(User u : users)
				userString.append(u.getName() +"; ");
			return userString.toString();
		} else {
			return "No active users.";
		}
		
	}
	
	public String getTagText(Set<Tag> tags) {
		if(tags.size() > 0) {
			StringBuffer tagString = new StringBuffer();
			for(Tag t : tags) 
				tagString.append(t.getName() + " ");
			
			return tagString.toString();
		} else {
			return "No tags associated.";
		}
	}
	
	public String getOnlineText(Set<User> users) {
		return users.size() + " online users";
	}
	
	static class ConversationHolder {
		ImageView imgIcon;
		TextView title;
		TextView users;
		TextView tags;
		TextView totalOnline;
	}
	
}

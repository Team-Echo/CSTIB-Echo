package uk.ac.cam.echo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import uk.ac.cam.echo.client.ClientApi;
import uk.ac.cam.echo.client.data.ConversationData;
import uk.ac.cam.echo.data.Conversation;
import uk.ac.cam.echo.data.Message;
import uk.ac.cam.echo.data.Tag;
import uk.ac.cam.echo.data.User;

public class ConversationAdapter extends ArrayAdapter<Conversation> {
	
	private Context context;
	private int layoutResourceId;

    private onListLoadedListener listener; // callback when listview has rendered

	private List<Conversation> data = null;
    private HashMap<Long, Integer> positionMap;
    private HashMap<Long, String> userMap;
    private HashMap<Long, String> onlineMap;
    private HashMap<Long, String> tagMap;
    private HashMap<Long, Bitmap> avatarMap;
    private HashMap<Long, String> messageMap;

    private ClientApi api;

    private int[] avatars;
	
	public ConversationAdapter(Context context, int layoutResourceId,
									List<Conversation> data) {
		
		super(context, layoutResourceId, data);

        positionMap = new HashMap<Long, Integer>();
        tagMap = new HashMap<Long, String>();
        avatarMap = new HashMap<Long, Bitmap>();
        userMap = new HashMap<Long, String>();
        onlineMap = new HashMap<Long, String>();
        messageMap = new HashMap<Long, String>();

		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.data = data;
	}

    public static ConversationAdapter newInstance(Context context, int layoutResourceId,
                                      List<Conversation> data, ClientApi api) {

        ConversationAdapter adapter = new ConversationAdapter(context, layoutResourceId, data);
        adapter.setApi(api);
        return adapter;

    }

	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
		
		View row = convertView;
		ConversationHolder holder;
		Log.d("Adapter", "getting View " + position+"");
		if(row == null) {
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			
			holder = new ConversationHolder();
			
			holder.imgIcon = (ImageView)row.findViewById(R.id.rowImage);
			holder.title = (TextView)row.findViewById(R.id.convNameRow);
			holder.users = (TextView)row.findViewById(R.id.convUsersRow);
			holder.tags = (TextView)row.findViewById(R.id.convTagsRow);
			holder.totalOnline = (TextView)row.findViewById(R.id.convOnlineRow);
            holder.lastMessage = (TextView)row.findViewById(R.id.lastMessage);
			
			row.setTag(holder);

		} else {
			holder = (ConversationHolder)row.getTag();
		}
		
		Conversation conversation = data.get(position);
        long id = conversation.getId();

        // record the position at which the conversation appears on list
        positionMap.put(id, position);

		holder.title.setText(conversation.getName());

        if(tagMap.containsKey(id)) {
            holder.tags.setText(tagMap.get(id));
            holder.users.setText(userMap.get(id));
            holder.totalOnline.setText(onlineMap.get(id));
            holder.imgIcon.setImageBitmap(avatarMap.get(id));
            holder.lastMessage.setText(messageMap.get(id));
        } else {
            new AsyncAdapter().execute(conversation, holder.tags, holder.imgIcon, holder.users, holder.totalOnline, holder.lastMessage);
        }

        if(position == getCount() - 1) {
            listener.onRendered();
        }

		return row;
	}

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    public int getPosition(long id) {
        return positionMap.get(id);
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getId();
    }

    public void updateList(List<Conversation> newData) {
        data = newData;
        notifyDataSetChanged();
    }

    public void setListViewListener(onListLoadedListener l) {
        listener = l;
    }

    private void setApi(ClientApi api) { this.api = api;}

    // ViewHolder to prevent multiple findViewById calls
	static class ConversationHolder {
		ImageView imgIcon;
		TextView title;
		TextView users;
		TextView tags;
		TextView totalOnline;
        TextView lastMessage;
	}

    private class AsyncAdapter extends AsyncTask<Object, Void, String> {
        Conversation conversation;


        TextView tagView;
        TextView userView;
        TextView onlineView;
        TextView lastMessage;
        ImageView imgView;
        Bitmap avatar;

        String onlineText;
        String userText;
        String lastMessageText;

        @Override
        protected String doInBackground(Object... params) {
            conversation = (Conversation)params[0];
            tagView = (TextView)params[1];
            imgView = (ImageView)params[2];
            userView = (TextView)params[3];
            onlineView = (TextView)params[4];
            lastMessage = (TextView)params[5];

            Collection<Tag> tags = conversation.getTags();
            String tagText = ConversationStringUtil.getTagText(tags);

            Message last = api.conversationResource.getMessageResource(conversation.getId()).getRecent(1).get(0);
            lastMessageText = last.getSender().getDisplayName() + ": " + last.getContents();

            Collection<User> users = conversation.getUsers();
            onlineText = ConversationStringUtil.getOnlineText(users);
            userText = ConversationStringUtil.getUserText(users);

            avatar = BitmapUtil.getBitmapFromURL(api.conversationResource.getMessageResource(conversation.getId())
                    .getRecent(1).get(0).getSender().getAvatarLink() + "&s=200");

            return tagText;
        }

        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);
            long id = conversation.getId();

            tagMap.put(id, str);
            tagView.setText(str);

            onlineMap.put(id, onlineText);
            onlineView.setText(onlineText);

            userMap.put(id, userText);
            userView.setText(userText);

            messageMap.put(id, lastMessageText);
            lastMessage.setText(lastMessageText);

            avatarMap.put(id, avatar);
            imgView.setImageBitmap(avatar);
        }
    }
}

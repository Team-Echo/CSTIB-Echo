package uk.ac.cam.echo;

import android.app.Activity;
import android.content.Context;
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
import uk.ac.cam.echo.data.Conversation;
import uk.ac.cam.echo.data.Tag;
import uk.ac.cam.echo.data.User;

public class ConversationAdapter extends ArrayAdapter<Conversation> {
	
	private Context context;
	private int layoutResourceId;

    private onListLoadedListener listener; // callback when listview has rendered

	private List<Conversation> data = null;
    private HashMap<Long, Integer> positionMap;

    private ClientApi api;
	
	public ConversationAdapter(Context context, int layoutResourceId,
									List<Conversation> data) {
		
		super(context, layoutResourceId, data);

        positionMap = new HashMap<Long, Integer>();

		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.data = data;

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
			
			row.setTag(holder);

		} else {
			holder = (ConversationHolder)row.getTag();
		}
		
		Conversation conversation = data.get(position);
		//Collection<User> users = conversation.getUsers();
		//Collection<Tag> tags = api.conversationResource.getTagResource(conversation.getId()).getAll();

        // record the position at which the conversation appears on list
        positionMap.put(conversation.getId(), position);

		//TODO: set imgIcon from the icon resource of conversation
		//holder.imgIcon.setImageBitmap(bm);
		holder.title.setText(conversation.getName());
		//holder.users.setText(getUserText(users));
        holder.users.setText("TestUser");
        new AsyncAdapter().execute(conversation, holder.tags);
		//holder.tags.setText(ConversationStringUtil.getTagText(tags));
        //holder.tags.setText("tag1 tag2 tag3");
		//holder.totalOnline.setText(getOnlineText(users));
        holder.totalOnline.setText("14 users online");

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

    public void setApi(ClientApi api) { this.api = api;}

    // ViewHolder to prevent multiple findViewById calls
	static class ConversationHolder {
		ImageView imgIcon;
		TextView title;
		TextView users;
		TextView tags;
		TextView totalOnline;
	}

    private class AsyncAdapter extends AsyncTask<Object, Void, String> {

        TextView tagView;


        @Override
        protected String doInBackground(Object... params) {
            Conversation conversation = (Conversation)params[0];
             tagView = (TextView)params[1];

            Collection<Tag> tags = conversation.getTags();
            String tagText = ConversationStringUtil.getTagText(tags);

            return tagText;
        }

        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);

            tagView.setText(str);
        }
    }
	
}

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
import uk.ac.cam.echo.data.Conversation;
import uk.ac.cam.echo.data.Tag;

public class ConversationAdapter extends ArrayAdapter<Conversation> {
	
	private Context context;
	private int layoutResourceId;

    private onListLoadedListener listener; // callback when listview has rendered

	private List<Conversation> data = null;
    private HashMap<Long, Integer> positionMap;
    private HashMap<Long, String> tagMap;
    private HashMap<Long, Bitmap> avatarMap;

    private ClientApi api;

    private int[] avatars;
	
	public ConversationAdapter(Context context, int layoutResourceId,
									List<Conversation> data) {
		
		super(context, layoutResourceId, data);

        positionMap = new HashMap<Long, Integer>();
        tagMap = new HashMap<Long, String>();
        avatarMap = new HashMap<Long, Bitmap>();

		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.data = data;

        avatars = getAvatars();

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

        if(tagMap.containsKey(conversation.getId())) {
            holder.tags.setText(tagMap.get(conversation.getId()));
            holder.imgIcon.setImageBitmap(avatarMap.get(conversation.getId()));
        } else {
            new AsyncAdapter().execute(conversation, holder.tags, holder.imgIcon);
        }
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

    private void setApi(ClientApi api) { this.api = api;}

    // ViewHolder to prevent multiple findViewById calls
	static class ConversationHolder {
		ImageView imgIcon;
		TextView title;
		TextView users;
		TextView tags;
		TextView totalOnline;
	}

    private class AsyncAdapter extends AsyncTask<Object, Void, String> {
        Conversation conversation;


        TextView tagView;
        ImageView imgView;
        Bitmap avatar;

        @Override
        protected String doInBackground(Object... params) {
            conversation = (Conversation)params[0];
            tagView = (TextView)params[1];
            imgView = (ImageView)params[2];

            Collection<Tag> tags = conversation.getTags();
            String tagText = ConversationStringUtil.getTagText(tags);

            avatar = BitmapUtil.getBitmapFromURL(api.conversationResource.getMessageResource(conversation.getId())
                    .getRecent(1).get(0).getSender().getAvatarLink() + "&s=200");

            return tagText;
        }

        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);

            tagMap.put(conversation.getId(), str);
            tagView.setText(str);

            avatarMap.put(conversation.getId(), avatar);
            imgView.setImageBitmap(avatar);
        }
    }


    private int[] getAvatars() {
        int[] avatars = {R.drawable.admin, R.drawable.angel, R.drawable.arab_boss, R.drawable.captain, R.drawable.chief,
        R.drawable.devil, R.drawable.engineer, R.drawable.general, R.drawable.judge, R.drawable.king, R.drawable.queen,
        R.drawable.prof, R.drawable.king, R.drawable.superman, R.drawable.wizard, R.drawable.policeman};

        return avatars;
    }
}

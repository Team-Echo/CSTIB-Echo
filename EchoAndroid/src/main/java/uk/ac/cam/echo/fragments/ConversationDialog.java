package uk.ac.cam.echo.fragments;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Collection;

import uk.ac.cam.echo.ConversationStringUtil;
import uk.ac.cam.echo.R;
import uk.ac.cam.echo.activities.ConversationDetailActivity;
import uk.ac.cam.echo.client.ClientApi;
import uk.ac.cam.echo.data.Conversation;
import uk.ac.cam.echo.data.Message;
import uk.ac.cam.echo.data.Tag;
import uk.ac.cam.echo.data.User;

public class ConversationDialog extends DialogFragment implements
									OnClickListener{

	private TextView title;
	private TextView users;
	private TextView tags;
	private TextView online;
    private TextView preview;
	private Button join;
	private ProgressBar progress;
	
	private long id;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.conv_dialog, container, false);
		
		id = getArguments().getLong("_id");
		
		title = (TextView)view.findViewById(R.id.convTitleDialog);
		users = (TextView)view.findViewById(R.id.convUsersDialog);
		tags = (TextView)view.findViewById(R.id.convTagsDialog);
		online = (TextView)view.findViewById(R.id.convOnlineDialog);
        preview = (TextView)view.findViewById(R.id.previewDialog);
		progress = (ProgressBar)view.findViewById(R.id.convProgressDialog);
		join = (Button)view.findViewById(R.id.convJoinDialog);

		join.setOnClickListener(this);
		
		return view;
	}

	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		new GetConversation().execute(id);
	}

	// Factory to create dialog based on id
	public static ConversationDialog newInstance(long id) {
		ConversationDialog cd = new ConversationDialog();
		Bundle args = new Bundle();
		args.putLong("_id", id);
		cd.setArguments(args);
		return cd;
	}

	public long getShownIndex() {
		return getArguments().getLong("_id");
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.convJoinDialog) {
			Intent intent = new Intent(getActivity(), ConversationDetailActivity.class);
			intent.putExtra("_id", id);
			startActivity(intent);
		}
	}

    private class GetConversation extends AsyncTask<Long, Void, Conversation> {

        ClientApi api = new ClientApi("http://echoconf.herokuapp.com/");
        String convName;
        String usersText;
        String tagText;
        String onlineText;
        String previewText;

        @Override
        protected Conversation doInBackground(Long... params) {

            Conversation conversation = api.conversationResource.get(params[0]);

            Collection<Message> messages = conversation.getMessages();
            //Collection<User> users = conversation.getUsers();
            Collection<Tag> tags = conversation.getTags();
            tagText = ConversationStringUtil.getTagText(tags);

            convName = conversation.getName();

//            StringBuilder userBuilder = new StringBuilder();
//            for(User u : users ) {
//                userBuilder.append(u.getUsername() + " ");
//            }
            //usersText = userBuilder.toString();




            //onlineText = users.size() + " users online";

            StringBuilder previewBuilder = new StringBuilder();
            for(Message m : messages) {
                previewBuilder.append(
                        (m.getSender() == null ? "Anonymous" : m.getSender().getUsername()) + ": " +
                        m.getContents() + "\n");
            }
            previewText = previewBuilder.toString();

            return conversation;
        }

        @Override
        protected void onPostExecute(Conversation conversation) {
            super.onPostExecute(conversation);
            if(conversation == null) {
                Log.d("ConvDialog", "No conversation retrieved!");
                return;
            }


            progress.setVisibility(View.GONE);
            preview.setVisibility(View.VISIBLE);
            preview.setText(previewText);

            if(getDialog() != null) {
                getDialog().setTitle(convName);
                title.setVisibility(View.GONE);
            }else {
                title.setText(convName);
            }

            //users.setText(usersText);
            //tags.setText(tagsText);
            users.setText("Yojan Alex Petar Mona Philip");
            tags.setText(tagText);
            online.setText("5 users online");
        }
    }
}

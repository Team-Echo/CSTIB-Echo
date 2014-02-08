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

import uk.ac.cam.echo.ConversationAdapter;
import uk.ac.cam.echo.R;
import uk.ac.cam.echo.activities.ConversationDetailActivity;
import uk.ac.cam.echo.client.ClientApi;
import uk.ac.cam.echo.data.Conversation;
import uk.ac.cam.echo.data.Message;
import uk.ac.cam.echo.data.Tag;
import uk.ac.cam.echo.data.User;

public class ConversationDialog extends DialogFragment implements
									OnClickListener{

	TextView title;
	TextView users;
	TextView tags;
	TextView online;
    TextView preview;
	Button join;
	ProgressBar progress;
	
	long id;
	
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

    // update dialog title
    private void updateDialogView(Conversation c) {

        if(getDialog() != null) {
            getDialog().setTitle(c.getName());
            title.setVisibility(View.GONE);
        }else {
            title.setText(c.getName());
        }

        //Collection<User> userSet = c.getUsers();
       //Collection<Tag> tagSet = c.getTags();
        //users.setText(ConversationAdapter.getUserText(userSet));
        //tags.setText(ConversationAdapter.getTagText(tagSet));
        users.setText("Yojan Alex Petar Mona Philip");
        tags.setText("Echo Multi Touch Conference");
        online.setText("5 users online");


    }

    private class GetConversation extends AsyncTask<Long, Void, Conversation> {

        ClientApi api = new ClientApi("http://echoconf.herokuapp.com/");

        @Override
        protected Conversation doInBackground(Long... params) {
            Log.d("ConvDialog", params[0]+"");
            return api.conversationResource.get(params[0]);
        }

        @Override
        protected void onPostExecute(Conversation conversation) {
            super.onPostExecute(conversation);
            if(conversation == null) {
                Log.d("ConvDialog", "No conversation retrieved!");
                return;
            }
            updateDialogView(conversation);
            Log.d("CONVDIALOG", conversation.getName());
            StringBuilder previewText = new StringBuilder();
            //Collection<Message> messages = conversation.getMessages();

            progress.setVisibility(View.GONE);
            preview.setVisibility(View.VISIBLE);
            preview.setText(previewText.toString());
        }
    }
}

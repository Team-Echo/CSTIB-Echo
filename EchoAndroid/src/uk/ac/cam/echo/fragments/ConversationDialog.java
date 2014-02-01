package uk.ac.cam.echo.fragments;

import java.util.Set;

import uk.ac.cam.echo.ConversationAdapter;
import uk.ac.cam.echo.R;
import uk.ac.cam.echo.activities.ConversationDetailActivity;
import uk.ac.cam.echo.dummy.Conversation;
import uk.ac.cam.echo.dummy.Conversation.Tag;
import uk.ac.cam.echo.dummy.Conversation.User;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ConversationDialog extends DialogFragment implements
									OnClickListener{

	TextView title;
	TextView users;
	TextView tags;
	TextView online;
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
		progress = (ProgressBar)view.findViewById(R.id.convProgressDialog);
		join = (Button)view.findViewById(R.id.convJoinDialog);
		join.setOnClickListener(this);
		return view;
	}
	
	
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		Conversation c = new Conversation(id);
		if(getDialog() != null) {
			getDialog().setTitle("CONVERSATION " + id);
			title.setVisibility(View.GONE);
		}else {
			title.setText(c.getName());
		}
		
		Set<User> userSet = c.getUsers();
		Set<Tag> tagSet = c.getTags();
		users.setText(ConversationAdapter.getUserText(userSet));
		tags.setText(ConversationAdapter.getTagText(tagSet));
		online.setText(userSet.size() + " online users");
		
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
}

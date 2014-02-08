package uk.ac.cam.echo.fragments;

import uk.ac.cam.echo.MessageAdapter;
import uk.ac.cam.echo.R;
import uk.ac.cam.echo.dummy.Conversation;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class ConversationFragment extends Fragment {

	private static final String ID = "_id";
	long id;
	
	Context context;
	ListView listView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
								Bundle savedInstanceState) {
		context = getActivity();
		View view = inflater.inflate(R.layout.message_listview_layout, container, false);
		id = getArguments().getLong(ID);
		
		return view;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		
		listView = (ListView)view.findViewById(R.id.messageListView);
		
		MessageAdapter adapter =
       		 new MessageAdapter(context, R.layout.message_row_remote,
       				 							new Conversation(id).getMessages());
        
        listView.setAdapter(adapter);
		updateViews();
	}
	
	// Factory method to create a fragment based on the conversationID
	public static ConversationFragment newInstance(long id) {
		ConversationFragment cf = new ConversationFragment();
		Bundle args = new Bundle();
		args.putLong(ID, id);
		cf.setArguments(args);
		return cf;
	}
	
	// returns id of the conversation current displayed
	public long getShownIndex() {
		return getArguments().getLong(ID, 0);
	}
	
	// update GUI to reflect conversation
	private void updateViews() {
		//TODO:
		// get previous messages from network
		
		
	}
}

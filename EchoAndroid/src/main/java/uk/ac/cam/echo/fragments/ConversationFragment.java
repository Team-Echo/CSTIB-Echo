package uk.ac.cam.echo.fragments;

import uk.ac.cam.echo.MessageAdapter;
import uk.ac.cam.echo.R;
import uk.ac.cam.echo.client.ClientApi;
import uk.ac.cam.echo.data.Conversation;
import uk.ac.cam.echo.data.Message;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

public class ConversationFragment extends Fragment {

	private static final String ID = "_id";
	long id;
	
	Context context;
	ListView listView;

    List<Message> messageList;

    MessageAdapter adapter;
	
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
        new GetMessage().execute(id);
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

    public MessageAdapter getAdapter() {
        return adapter;
    }
	
	// update GUI to reflect conversation
	private void updateViews() {
		//TODO:
		// get previous messages from network
        //adapter.add();
        //adapter.notifyDataSetChanged();
	}

    private class GetMessage extends AsyncTask<Long, Void, List<Message>> {

        ClientApi api;
        long id;
        List<Message> msgList;

        @Override
        protected List<Message> doInBackground(Long... args) {
            id = args[0];
            api = new ClientApi("http://echoconf.herokuapp.com/");
            msgList = (List)api.conversationResource.get(id).getMessages();
            return msgList;
        }

        @Override
        protected void onPostExecute(List<Message> result) {
            messageList = msgList;
            adapter =
                    new MessageAdapter(context, R.layout.message_row_remote, messageList);
            listView.setAdapter(adapter);
            listView.setSelection(adapter.getCount()-1);
        }
    }
}

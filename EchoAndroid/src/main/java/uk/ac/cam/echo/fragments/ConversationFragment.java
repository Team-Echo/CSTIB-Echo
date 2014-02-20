package uk.ac.cam.echo.fragments;

import uk.ac.cam.echo.ConversationStringUtil;
import uk.ac.cam.echo.MessageAdapter;
import uk.ac.cam.echo.R;
import uk.ac.cam.echo.Toaster;
import uk.ac.cam.echo.client.ClientApi;
import uk.ac.cam.echo.client.data.MessageData;
import uk.ac.cam.echo.data.Conversation;
import uk.ac.cam.echo.data.Message;
import uk.ac.cam.echo.data.async.Handler;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import javax.validation.constraints.Null;
import javax.ws.rs.client.Client;

public class ConversationFragment extends Fragment {

	private static final String ID = "_id";
	long id;
    boolean preview;

    private static ClientApi api;

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

        if(api != null)
            getAndListen();

	}

    public void getAndListen() {
        Log.d("LISTEN", "getAndListen");
        new GetMessage().execute(id);
        new Listen().execute(id);
    }
	
	// Factory method to create a fragment based on the conversationID
	public static ConversationFragment newInstance(long id) {
		ConversationFragment cf = new ConversationFragment();
		Bundle args = new Bundle();
		args.putLong(ID, id);
		cf.setArguments(args);
		return cf;
	}

    public static ConversationFragment newInstance(long id, boolean preview) {
        ConversationFragment cf = new ConversationFragment();
        cf.setIsPreview(preview);
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

    public ListView getListView() { return listView; }

    public void setIsPreview(boolean p) { preview = p; }

    public void setApi(ClientApi clientApi) { api = clientApi; }


    // ASYNCHRONOUS TASKS

    //  Listening to new messages
    private class Listen extends AsyncTask<Long, Message, Void> {
        long id;

        @Override
        protected Void doInBackground(Long... longs) {
            id = longs[0];

            Handler<Message> handler = new Handler<Message>() {
                @Override
                public void handle(Message message) {
                    publishProgress(message);
                    Log.d("LISTEN", "message received");
                }
            };

            api.conversationResource.listenToMessages(id).subscribe(handler);
            return null;
        }

        @Override
        protected void onProgressUpdate(Message... values) {
            super.onProgressUpdate(values);
            adapter.updateMessage(values[0]);
        }
    }

    // Getting previous messages
    private class GetMessage extends AsyncTask<Long, Void, List<Message>> {

        long id;
        Conversation conversation;

        List<Message> msgList;
        public String title;
        String users;

        @Override
        protected List<Message> doInBackground(Long... args) {
            id = args[0];
            conversation = api.conversationResource.get(id);

            title = conversation.getName();
            //users = ConversationStringUtil.getUserText(conversation.getUsers());
            users = "Yojan Alex Petar Mona Philip";
            if(preview) {
                msgList = (List)conversation.getMessages(25);
            } else {
                msgList = (List)conversation.getMessages();
            }


            return msgList;
        }

        @Override
        protected void onPostExecute(List<Message> result) {
            try {
                ActionBar ab = getActivity().getActionBar();
                ab.setTitle(title);
                ab.setSubtitle(users);
            } catch(NullPointerException e) { Log.e("ConversationFrag", e.getMessage()); }
            Toaster.displayShort(getActivity(), users);
            messageList = msgList;
            adapter = MessageAdapter.newInstance(context, R.layout.message_row_remote, messageList, api);
            adapter.setListView(listView);
            adapter.setNotifyOnChange(true);
            listView.setAdapter(adapter);
            listView.setSelection(adapter.getCount() - 1);
        }
    }
}

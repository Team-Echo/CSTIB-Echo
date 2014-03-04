package uk.ac.cam.echo.fragments;

import uk.ac.cam.echo.ConversationStringUtil;
import uk.ac.cam.echo.MessageAdapter;
import uk.ac.cam.echo.R;
import uk.ac.cam.echo.Toaster;
import uk.ac.cam.echo.client.ClientApi;
import uk.ac.cam.echo.client.data.MessageData;
import uk.ac.cam.echo.data.Conversation;
import uk.ac.cam.echo.data.Message;
import uk.ac.cam.echo.data.User;
import uk.ac.cam.echo.data.async.Handler;
import uk.ac.cam.echo.services.EchoService;

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
import java.util.Timer;
import java.util.TimerTask;

import javax.validation.constraints.Null;
import javax.ws.rs.client.Client;

public class ConversationFragment extends Fragment {

	private static final String ID = "_id";
	long id;
    boolean preview;

    private static EchoService echoService;
    private static User user;
    private static ClientApi api;

	Context context;
	ListView listView;

    List<Message> messageList;

    MessageAdapter adapter;
    private static Timer timer;
	
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
        Log.d("LISTEN",(api==null) ? "api is null" : "Not null");
        if(api != null){
            getAndListen();

        }

	}

    @Override
    public void onResume() {
        super.onResume();
        timer = new Timer();
        if(api!=null) callAsynchronousTask(timer);
    }

    @Override
    public void onPause() {
        timer.cancel();
        super.onPause();
    }

    public void getAndListen() {
        Log.d("LISTEN", "getAndListen");
        new GetMessage().execute(id);
        new Listen().execute(id);
    }

    public void onServiceReady() {
        getAndListen();
    }
	
	// Factory method to create a fragment based on the conversationID
    public static ConversationFragment newInstance(long id, boolean preview, EchoService service) {
        ConversationFragment cf = new ConversationFragment();
        cf.setIsPreview(preview);
        if(service != null){
            cf.setService(service);
            if(service.getUser() != null)
                cf.setUser(service.getUser());
        }

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
    public void setUser(User u) { user = u; }
    public void setService(EchoService service) {echoService = service; }


    public void callAsynchronousTask(Timer timer) {
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                Log.d("NOTIF", "asyncupdate called");

                if(user.getCurrentConversation() == null) return;

                Message update = api.conferenceResource.notify(1, user.getId(), user.getCurrentConversation().getId(), 300000);
                echoService.notifyUpdate(update);
            }
        };
        timer.schedule(doAsynchronousTask, 30000, 150000); //execute in every 50000 ms
    }

    // ASYNCHRONOUS TASKS

    //  Listening to new messages
    private class Listen extends AsyncTask<Long, Message, Void> {
        long id;

        @Override
        protected Void doInBackground(Long... longs) {
            id = longs[0];
            //timer = new Timer();

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
            users = ConversationStringUtil.getUserText(conversation.getUsers());
            if(preview) {
                msgList = (List)conversation.getMessages(25);
            } else {
                msgList = (List)conversation.getMessages();
            }
            Log.d("MESSAGES", "loaded all the messages");

            return msgList;
        }

        @Override
        protected void onPostExecute(List<Message> result) {

            messageList = msgList;
            adapter = MessageAdapter.newInstance(context, R.layout.message_row_remote, messageList, api, user);
            adapter.setListView(listView);
            adapter.setNotifyOnChange(true);
            listView.setAdapter(adapter);
            listView.setSelection(adapter.getCount() - 1);
        }
    }

    //private class GetUpdates extends
}

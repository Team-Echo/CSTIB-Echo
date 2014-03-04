package uk.ac.cam.echo.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;

import uk.ac.cam.echo.ConversationStringUtil;
import uk.ac.cam.echo.R;
import uk.ac.cam.echo.Toaster;
import uk.ac.cam.echo.client.ClientApi;
import uk.ac.cam.echo.data.Conversation;
import uk.ac.cam.echo.data.Message;
import uk.ac.cam.echo.fragments.ConversationFragment;
import uk.ac.cam.echo.services.EchoService;

public class ConversationDetailActivity extends Activity implements View.OnClickListener {

    private EchoService echoService;
    private ServiceConnection connection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            echoService = ((EchoService.LocalBinder)service).getService();
            echoService.setNotifEnabled(false);
            api = echoService.getApi();
            cf.setApi(api);
            cf.setService(echoService);
            cf.setUser(echoService.getUser());
            cf.onServiceReady();
            new UpdateActionBar().execute(id);
        }
        public void onServiceDisconnected(ComponentName className) {
            echoService.setNotifEnabled(true);
            echoService = null;
        }
    };

    private static ClientApi api;

    long id;
    ConversationFragment cf;
    EditText input;
    Button send;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_conversation_detail);
		setTitle("");
		id = getIntent().getLongExtra("_id", 0);

        input = (EditText)findViewById(R.id.inputMessage);
        send = (Button)findViewById(R.id.sendButton);
        send.setOnClickListener(this);

        // new ConversationFragment that is not a preview, with the current user
        cf = ConversationFragment.newInstance(id, false, null);

		FragmentManager manager = getFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.replace(R.id.messageFrame, cf);
		transaction.commit();

	}

    @Override
    protected void onResume() {
        super.onResume();
        Intent service = new Intent(this, EchoService.class);
        bindService(service, connection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onPause() {
        super.onPause();
        echoService.setNotifEnabled(true);
        unbindService(connection);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        getMenuInflater().inflate(R.menu.user_list, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, ConversationListActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(i);
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.sendButton) {
            String msgContents = input.getText().toString();
            if(msgContents.equals("")) {
                return;
            }
            send.setVisibility(View.INVISIBLE);
            input.setText("");
            new SendMessage().execute(msgContents);
        }
    }

    public EchoService getService() { return echoService; }




    // Attaching functionality to menu-items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_see_users:
                seeUsers();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void seeUsers() {
        Intent i = new Intent(this, UserListActivity.class);
        i.putExtra("_id", id);
        i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(i);
    }

    private class SendMessage extends AsyncTask<String, Void, Message> {


        Conversation current;

        @Override
        protected Message doInBackground(String... strings) {
            String contents = strings[0];

            current = api.conversationResource.get(id);
            Message msg = api.newMessage(current);
            msg.setContents(contents);
            msg.setSender(echoService.getUser());
            msg.setSenderName(echoService.getUser().getDisplayName());
            msg.save();
            return msg;
        }

        @Override
        protected void onPostExecute(Message newMsg) {
            cf.getListView().setSelection(cf.getAdapter().getCount()-1);
            send.setVisibility(View.VISIBLE);
        }
    }

    private class UpdateActionBar extends AsyncTask<Long,Void,Void> {

        String title;
        String users;

        @Override
        protected Void doInBackground(Long[] args) {
            Conversation conversation = api.conversationResource.get(args[0]);
            title = conversation.getName();
            users = ConversationStringUtil.getUserText(conversation.getUsers());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ActionBar ab = getActionBar();
            if (ab != null)
                ab.setTitle(title);
            if (ab != null)
                ab.setSubtitle(users);
        }
    }
}

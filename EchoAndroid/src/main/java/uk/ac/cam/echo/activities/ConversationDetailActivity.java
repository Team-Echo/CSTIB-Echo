package uk.ac.cam.echo.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import uk.ac.cam.echo.R;
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
            cf.getAndListen();

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

        cf = ConversationFragment.newInstance(id);

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
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.conversation_detail, menu);
		return true;
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

    private class SendMessage extends AsyncTask<String, Void, Message> {


        Conversation current;

        @Override
        protected Message doInBackground(String... strings) {
            String contents = strings[0];

            current = api.conversationResource.get(id);
            Message msg = api.newMessage(current);
            msg.setContents(contents);
            //msg.setSender()

            try {
                msg.save();
                Log.d("ESCAPE", "Escaped");
            } catch(Error e) {
                Log.e("Save", e.getMessage());
                return msg;
            }

                return msg;
        }

        @Override
        protected void onPostExecute(Message newMsg) {
            cf.getListView().setSelection(cf.getAdapter().getCount()-1);
            send.setVisibility(View.VISIBLE);
        }
    }
}
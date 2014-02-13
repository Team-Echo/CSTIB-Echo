package uk.ac.cam.echo.activities;

import uk.ac.cam.echo.R;
import uk.ac.cam.echo.client.ClientApi;
import uk.ac.cam.echo.data.Conversation;
import uk.ac.cam.echo.data.Message;
import uk.ac.cam.echo.fragments.ConversationFragment;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class ConversationDetailActivity extends Activity implements View.OnClickListener {

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

    private class SendMessage extends AsyncTask<String, Void, Message> {

        ClientApi api;
        Conversation current;

        @Override
        protected Message doInBackground(String... strings) {
            String contents = strings[0];
            api = new ClientApi("http://echoconf.herokuapp.com");
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

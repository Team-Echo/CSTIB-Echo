package uk.ac.cam.echo.fragments;

import uk.ac.cam.echo.R;
import uk.ac.cam.echo.Toaster;
import uk.ac.cam.echo.client.ClientApi;
import uk.ac.cam.echo.data.Conference;
import uk.ac.cam.echo.data.Conversation;

import android.app.DialogFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;


public class AddConversationDialog extends DialogFragment implements
        OnClickListener, OnEditorActionListener{

    EditText title;
    EditText tags;
    Button add;
    ProgressBar progress;

    long id;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_conv_dialog, container, false);

        title = (EditText)view.findViewById(R.id.convTitle);
        tags = (EditText)view.findViewById(R.id.convTags);
        progress = (ProgressBar)view.findViewById(R.id.convProgressDialog);
        add = (Button)view.findViewById(R.id.addButton);

        add.setOnClickListener(this);
        tags.setOnEditorActionListener(this);

        // show soft-keyboard when dialog visible
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(getDialog() != null) {
            getDialog().setTitle("Add Conversation");
        }
    }



    // Factory to create dialog based on id
    public static AddConversationDialog newInstance() {
        AddConversationDialog cd = new AddConversationDialog();
        return cd;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.addButton) {

            String titleInput = title.getText().toString();
            if(titleInput.equals("")) {
                Toaster.displayShort(getActivity(), "Please name the conversation");
                return;
            }

            String tagsInput = tags.getText().toString();
            if(tagsInput.equals("")) {
                Toaster.displayShort(getActivity(), "Please enter atleast one tag");
                return;
            }
            Toaster.displayLong(getActivity(), titleInput + " ~ " + tagsInput);
			/*TODO: API calls to add conversation
			 * Conversation c = api.newConversation();
			 * c.setName(titleInput);
			 * c.setTags(tagsInput);
			 * c.save();
			 */
            new AddConversation().execute(titleInput, tagsInput);
        }
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(actionId == EditorInfo.IME_ACTION_DONE) {
            add.performClick();
            return true;
        }
        return false;
    }

    private class AddConversation extends AsyncTask<String, Void, Conversation> {

        ClientApi api = new ClientApi("http://echoconf.herokuapp.com/");
        Conference conference;

        @Override
        protected Conversation doInBackground(String... params) {
            String convName = params[0];
            String convTags = params[1];

            conference = api.conferenceResource.getAll().get(0);
            Conversation newConv = api.newConversation();
            newConv.setName(convName);
            newConv.setConference(conference);

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            newConv.save();
            Log.d("ESCAPE", "Escaped");
            return newConv;
        }
    }

}
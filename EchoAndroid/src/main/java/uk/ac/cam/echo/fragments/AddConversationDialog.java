package uk.ac.cam.echo.fragments;

import android.app.DialogFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import uk.ac.cam.echo.R;
import uk.ac.cam.echo.Toaster;
import uk.ac.cam.echo.client.ClientApi;
import uk.ac.cam.echo.data.Conference;
import uk.ac.cam.echo.data.Conversation;
import uk.ac.cam.echo.data.Tag;


public class AddConversationDialog extends DialogFragment implements
        OnClickListener, OnEditorActionListener{

    EditText title;
    EditText tags;
    Button add;
    ProgressBar progress;

    private static ClientApi api;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_conv_dialog, container, false);

        title = (EditText)view.findViewById(R.id.convTitle);
        tags = (EditText)view.findViewById(R.id.convTags);
        progress = (ProgressBar)view.findViewById(R.id.addProgress);
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

    public void setApi(ClientApi clientApi) { api = clientApi; }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.addButton) {

            add.setVisibility(View.GONE);
            progress.setVisibility(View.VISIBLE);

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


        Conference conference;

        @Override
        protected Conversation doInBackground(String... params) {
            String convName = params[0];
            String convTags = params[1];

            conference = api.conferenceResource.getAll().get(0);
            Conversation newConv = api.newConversation();
            newConv.setName(convName);
            newConv.setConference(conference);

            newConv.save();
            //newConv = api.conversationResource.get(convName); //temp hack to load information from db

            String[] tags = convTags.split(" ");

            for(String tagName : tags) {
               Tag t = api.newTag(api.conversationResource.get(newConv.getId()));
               t.setName(tagName);
               api.conversationResource.getTagResource(newConv.getId()).create(t);
            }

            return newConv;
        }

        @Override
        protected void onPostExecute(Conversation result) {
            getDialog().dismiss();

        }
    }

}
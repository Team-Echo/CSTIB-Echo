package uk.ac.cam.echo.fragments;

import java.nio.channels.AsynchronousCloseException;
import java.util.ArrayList;
import java.util.List;

import uk.ac.cam.echo.ConversationAdapter;
import uk.ac.cam.echo.R;
import uk.ac.cam.echo.Toaster;
import uk.ac.cam.echo.dummy.Conversation;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;

public class ConversationListFragment extends Fragment implements
											OnItemClickListener {

	private Context context;
	private Communicator comm;
	private ConversationAdapter adapter;
	
	private ListView listView;
	private ProgressBar listProgress;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		context = getActivity();
		return inflater.inflate(R.layout.conv_listview_layout, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		listView = (ListView)getView().findViewById(R.id.convListView);
		listProgress = (ProgressBar)getView().findViewById(R.id.listProgress);
		
		new PerformSearch().execute();
	}
	
	public void performSearch(String query) {
		Toaster.displayLong(getActivity(), query);
		new PerformSearch().execute(query);
	}

	public ConversationAdapter getAdapter() {
		return adapter;
	}

	public List<Conversation> getDummyConversations() {
		ArrayList<Conversation> cs = new ArrayList<Conversation>();

		for(int i = 0; i < 20; i++) {
			Conversation c = new Conversation(i);
			cs.add(c);
		}

		return cs;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		comm.respond(id);
	}

	public void openConversation(int id) {
		listView.performItemClick(
				listView.getAdapter().getView(id, null, null),
				id,
				listView.getAdapter().getItemId(id));
	}

	public interface Communicator {
		public void respond(long id);
	}

	public void setCommunicator(Communicator comm) {
		this.comm = comm;
	}
	
	private class PerformSearch extends AsyncTask<String, Void, List<Conversation>> {

		@Override
		protected List<Conversation> doInBackground(String... params) {
			
			if(params.length == 0) {
				// display all conversations
			} else {
				String query = params[0];
				// TODO: perform search for name using api.conferenceResource
			}
			
			try {
				//simulating network call for now
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			
			return getDummyConversations();
		}

		@Override
		protected void onPostExecute(List<Conversation> result) {
			// update listview
			super.onPostExecute(result);
			
			if(adapter == null) {
				adapter = new ConversationAdapter(context, R.layout.conv_list_row, result);
				listView.setAdapter(adapter);
				listView.setVisibility(View.VISIBLE);
				listView.setOnItemClickListener(ConversationListFragment.this);
				listProgress.setVisibility(View.GONE);
			} else {
				adapter.updateList(result);
			}
		}
		
		
		
	}
}

package uk.ac.cam.echo.fragments;

import java.util.ArrayList;
import java.util.List;

import uk.ac.cam.echo.ConversationAdapter;
import uk.ac.cam.echo.R;
import uk.ac.cam.echo.Toaster;
import uk.ac.cam.echo.dummy.Conversation;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ConversationListFragment extends Fragment implements
		OnItemClickListener {
		
	Context context;
	ListView listView;
	Communicator comm;
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
             
             
             ConversationAdapter adapter =
            		 new ConversationAdapter(context, R.layout.conv_list_row,
            				 							getDummyConversations());
             
             listView.setAdapter(adapter);
             listView.setOnItemClickListener(this);
     }
	 
	 private List<Conversation> getDummyConversations() {
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
		Toaster.displayShort(getActivity(), position + " clicked..");

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
}

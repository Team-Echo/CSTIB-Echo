package uk.ac.cam.echo.activities;

import uk.ac.cam.echo.R;
import uk.ac.cam.echo.fragments.ConversationDialog;
import uk.ac.cam.echo.fragments.ConversationListFragment;
import uk.ac.cam.echo.fragments.ConversationListFragment.Communicator;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class ConversationListActivity extends Activity implements Communicator {
	
	private static final String ID = "_id";
	
	FragmentManager manager;
	boolean dualPane; //to manage orientations/different screensizes
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.conv_list_detail_layout);
            
            manager = getFragmentManager();
            
            View detailsFrame = findViewById(R.id.convFrame);
            dualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;
                       
            ConversationListFragment clf = (ConversationListFragment) manager.findFragmentById(R.id.convListFragment);
            clf.setCommunicator(this);
            
    }
	
	/* if current view is dual-pane, update the fragment, otherwise
	 * start the stand-alone conversation activity.
	 */
	@Override
	public void respond(long id) {
		
		if(dualPane) {
			
			ConversationDialog convFrag =
					(ConversationDialog)manager.findFragmentById(R.id.convFrame);
			if(convFrag == null || convFrag.getShownIndex() != id) {
				convFrag = ConversationDialog.newInstance(id);
				FragmentTransaction ft = manager.beginTransaction();
				ft.replace(R.id.convFrame, convFrag);
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				ft.commit();
			}
		} else {
	
			ConversationDialog cd = ConversationDialog.newInstance(id);
			cd.show(manager, "conversation_info");
			
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		getMenuInflater().inflate(R.menu.list_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_search:
	        	openSearch();
	            return true;
	        case R.id.action_scan:
	            openScan();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	private void openScan() {
		Intent intent = new Intent(this, ScanActivity.class);
		startActivity(intent);
	}
	
	private void openSearch() {
		// TODO: search client api call
	}
	
	
	
	
}

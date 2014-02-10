package uk.ac.cam.echo.activities;

import java.util.List;

import uk.ac.cam.echo.R;
import uk.ac.cam.echo.Toaster;
import uk.ac.cam.echo.dummy.Conversation;
import uk.ac.cam.echo.fragments.AddConversationDialog;
import uk.ac.cam.echo.fragments.ConversationDialog;
import uk.ac.cam.echo.fragments.ConversationListFragment;
import uk.ac.cam.echo.fragments.ConversationListFragment.Communicator;
import uk.ac.cam.echo.onListLoadedListener;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.SearchView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ConversationListActivity extends Activity
        implements Communicator, onListLoadedListener {

    private Menu menu;

	private FragmentManager manager;
	private boolean dualPane; //to manage orientations/different screensizes

    private ConversationListFragment clf;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setProgressBarIndeterminateVisibility(true);
        setContentView(R.layout.conv_list_detail_layout);

        manager = getFragmentManager();
        View detailsFrame = findViewById(R.id.convFrame);
        dualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

        clf = (ConversationListFragment) manager.findFragmentById(R.id.convListFragment);

        clf.setCommunicator(this);


        handleIntent(getIntent());
    }
	
	// If current view is dual-pane, update the fragment,
	// otherwise start the stand-alone conversation activity
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

    // Set up action-bar and Search functionality
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

        this.menu = menu;

		// Inflate the menu items for use in the action bar
		getMenuInflater().inflate(R.menu.list_activity_actions, menu);
		
		// Associate searchable configuration with the SearchView
		SearchManager searchManager =
				(SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView =
				(SearchView) menu.findItem(R.id.action_search).getActionView();
		searchView.setSearchableInfo(
				searchManager.getSearchableInfo(getComponentName()));
		
		return true;
	}

    // Attaching functionality to menu-items
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_scan:
	            openScan();
	            return true;
	        case R.id.action_add_conv:
	        	addConversation();
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
    }

    // Methods to handle search queries to the same activity
    @Override
    protected void onNewIntent(Intent intent) {
        Log.d("SEARCH", "onNewIntent");
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if(Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            clf.performSearch(query);
            Log.d("SEARCH", "handleIntent");
        }
    }

	// Opening a Dialog form to add new Conversation
	private void addConversation() {
		AddConversationDialog addDialog = AddConversationDialog.newInstance();
		addDialog.show(manager, "add_conv");
	}


    // Methods to handle QR code scanning and retrieving results
    private void openScan() {
        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        scanIntegrator.initiateScan();
    }
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		//retrieve scan result
		IntentResult scanningResult =
				IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		if(scanningResult != null) {
			String scanContent = scanningResult.getContents();
			//String scanFormat = scanningResult.getFormatName();
			
			int position = -1;
			try {
				position = Integer.parseInt(scanContent);
			}catch(Exception e) {
				Log.e("CLF", e.getMessage());
			}
			if (position != -1) 
				clf.openConversation(position);
		}else {
			Toaster.displayLong(this, "No scan data received!");
		}
    }

    // Callback method when ListView has fully rendered
    @Override
    public void onRendered() {
        setProgressBarIndeterminateVisibility(false);
        Toaster.displayLong(this, "toggleeee");
    }
}

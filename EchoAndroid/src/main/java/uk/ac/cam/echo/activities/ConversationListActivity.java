package uk.ac.cam.echo.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import uk.ac.cam.echo.R;
import uk.ac.cam.echo.Toaster;
import uk.ac.cam.echo.client.ClientApi;
import uk.ac.cam.echo.fragments.AddConversationDialog;
import uk.ac.cam.echo.fragments.ConversationDialog;
import uk.ac.cam.echo.fragments.ConversationListFragment;
import uk.ac.cam.echo.fragments.ConversationListFragment.Communicator;
import uk.ac.cam.echo.onListLoadedListener;
import uk.ac.cam.echo.services.EchoService;

public class ConversationListActivity extends Activity
        implements Communicator, onListLoadedListener {

    private EchoService echoService;
    private ServiceConnection connection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            echoService = ((EchoService.LocalBinder)service).getService();
            api = echoService.getApi();
            clf.setApi(api);
            if(!isSearch) clf.getAllConversations();
            openDialog();
        }

        public void onServiceDisconnected(ComponentName className) {
            echoService = null;
        }
    };
    private static ClientApi api;
    private MenuItem qrScan;

	private FragmentManager manager;
	private boolean dualPane; //to manage orientations/different screensizes

    private ConversationListFragment clf;

    private boolean isSearch;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conv_list_detail_layout);

        manager = getFragmentManager();
        View detailsFrame = findViewById(R.id.convFrame);
        dualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

        clf = (ConversationListFragment) manager.findFragmentById(R.id.convListFragment);

        clf.setCommunicator(this);

        Log.d("SEARCH", "onCreate");
        isSearch = false;
        handleIntent(getIntent());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("SEARCH", "binding again");
        Intent service = new Intent(this, EchoService.class);
        bindService(service, connection, Context.BIND_AUTO_CREATE);

    }

    private void openDialog() {
        long id = getIntent().getLongExtra("_id", -1L);
        if(id != -1){
            respond(id);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(connection);
    }

    public EchoService getService() { return echoService; }

    // If current view is dual-pane, update the fragment,
	// otherwise start the stand-alone conversation activity
	@Override
	public void respond(long id) {
		
		if(dualPane) {
			Log.d("RESPOND", "respond called " + id);
			ConversationDialog convFrag =
					(ConversationDialog)manager.findFragmentById(R.id.convFrame);
			if(convFrag == null || convFrag.getShownIndex() != id) {
				convFrag = ConversationDialog.newInstance(id, getService());
                convFrag.setApi(getService().getApi());
                Log.d("RESPOND", "new instance created");
				FragmentTransaction ft = manager.beginTransaction();
				ft.replace(R.id.convFrame, convFrag);
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				ft.commit();
			}
		} else {
            if(id != getService().getConversationId()) {
                getService().listenToConversation(id);
            }
            Intent intent = new Intent(this, ConversationDetailActivity.class);
            intent.putExtra("_id", id);
            startActivity(intent);
        }

	}

    // Set up action-bar and Search functionality
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
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
            case R.id.action_user:
                openUserSettings();
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
            isSearch = true;
            clf.performSearch(query);
            Log.d("SEARCH", "handleIntent");
        }
    }

	// Opening a Dialog form to add new Conversation
	private void addConversation() {
		AddConversationDialog addDialog = AddConversationDialog.newInstance();
        addDialog.setApi(getService().getApi());
		addDialog.show(manager, "add_conv");


	}


    // Methods to handle QR code scanning and retrieving results
    private void openScan() {
        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        scanIntegrator.initiateScan();
    }

    private void openUserSettings() {
        Intent intent = new Intent(this, UserSettingsActivity.class);
        startActivity(intent);
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
        if(dualPane) respond(clf.getAdapter().getItem(0).getId());
    }
}

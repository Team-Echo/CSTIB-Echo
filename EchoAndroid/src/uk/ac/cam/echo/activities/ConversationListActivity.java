package uk.ac.cam.echo.activities;

import uk.ac.cam.echo.R;
import uk.ac.cam.echo.fragments.ConversationListFragment;
import uk.ac.cam.echo.fragments.ConversationListFragment.Communicator;
import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;

public class ConversationListActivity extends Activity implements Communicator {
	
	FragmentManager manager;
	boolean dualPane; //to manage orientations/different screensizes
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.conv_list_detail_layout);
            
            manager = getFragmentManager();
           // View detailsFrame = findViewById(R.id.detailFrame);
           // dualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;
            
            ConversationListFragment clf = (ConversationListFragment) manager.findFragmentById(R.id.convListFragment);
            clf.setCommunicator(this);
    }

	@Override
	public void respond(long id) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}

package uk.ac.cam.echo.activities;

import uk.ac.cam.echo.R;
import uk.ac.cam.echo.Toaster;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class MainActivity extends Activity
				implements OnEditorActionListener {
	TextView title;
	EditText username;
	EditText password;
	Button login;
	ProgressBar progress;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        title = (TextView)findViewById(R.id.titleLogin);
        username = (EditText)findViewById(R.id.usernameLogin);
        password = (EditText)findViewById(R.id.passwordLogin);
        login = (Button)findViewById(R.id.buttonLogin);
        progress = (ProgressBar)findViewById(R.id.progressLogin);
        
        password.setOnEditorActionListener(this);
        
        Typeface flex = Typeface.createFromAsset(getAssets(), "fonts/roboto_light_italic.ttf");
        title.setTypeface(flex);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void loginUser(View v) {
    	String user = username.getText().toString();
    	String pass = password.getText().toString();
    	
    	/* basic validation, display loading bar
    	 * perform API call for login authentication */
    	if(!user.equals("") && !pass.equals("")) {
    		toggleButton();
    		
    		if(user.equals("user") && pass.equals("pass")) { //userAuntheticated(user, pass) == ClientAPI.AUTH_SUCCESS
    			Intent i = new Intent(this, ConversationListActivity.class);
    			startActivity(i);
  
    		} else {
    			Toaster.displayShort(this, "authentication failed");
    			toggleButton();
    		}
    		
    		
    	} else {
    		Toaster.displayShort(this, "username/password blank");
    	}
    }


	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if(actionId == EditorInfo.IME_ACTION_DONE) {
			login.performClick();
			return true;
		}
		return false;
	}
    
	private void toggleButton() {
		if(login.getVisibility() == View.GONE) {
			login.setVisibility(View.VISIBLE);
			progress.setVisibility(View.GONE);
		} else {
			login.setVisibility(View.GONE);
			progress.setVisibility(View.VISIBLE);
		}
	}
    
}

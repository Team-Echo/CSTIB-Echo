package uk.ac.cam.echo.activities;

import uk.ac.cam.echo.R;
import uk.ac.cam.echo.Toaster;
import uk.ac.cam.echo.client.ClientApi;
import uk.ac.cam.echo.services.EchoService;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.IBinder;
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

    private EchoService echoService;
    private ServiceConnection connection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            echoService = ((EchoService.LocalBinder)service).getService();
            api = echoService.getApi();
        }
        public void onServiceDisconnected(ComponentName className) {
            echoService = null;
        }
    };

    ClientApi api;

	TextView title;
	EditText username;
	EditText password;
    TextView registerScreen;
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
        registerScreen = (TextView)findViewById(R.id.registerScreen);
        password.setOnEditorActionListener(this);
        
        Typeface flex = Typeface.createFromAsset(getAssets(), "fonts/roboto_light_italic.ttf");
        title.setTypeface(flex);

        startService(new Intent(this, EchoService.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent service = new Intent(this, EchoService.class);
        bindService(service, connection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onPause() {
        super.onPause();
        echoService.setNotifEnabled(true);
        unbindService(connection);
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
    		

    	    Intent i = new Intent(this, ConversationListActivity.class);
    		startActivity(i);

    		
    		
    	} else {
    		Toaster.displayShort(this, "username/password blank");
    	}
    }

    public void registerScreen(View v) {
        Intent i = new Intent(this, RegisterActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(i);
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
            registerScreen.setVisibility(View.VISIBLE);
		} else {
			login.setVisibility(View.GONE);
			progress.setVisibility(View.VISIBLE);
            registerScreen.setVisibility(View.GONE);
		}
	}
    
}

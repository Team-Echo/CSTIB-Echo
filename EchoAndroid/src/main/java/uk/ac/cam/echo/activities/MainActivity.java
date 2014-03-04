package uk.ac.cam.echo.activities;

import uk.ac.cam.echo.ConnectionDetector;
import uk.ac.cam.echo.R;
import uk.ac.cam.echo.Toaster;
import uk.ac.cam.echo.client.ClientApi;
import uk.ac.cam.echo.data.User;
import uk.ac.cam.echo.services.EchoService;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
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
            onServiceReady();

        }
        public void onServiceDisconnected(ComponentName className) {
            echoService = null;
        }
    };

    public static final String LOGGED_IN = "uk.ac.cam.echo.loggedIn";
    boolean isConnected;
    ConnectionDetector detector;

    ClientApi api;

	TextView title;
	EditText username;
	EditText password;
    TextView registerScreen;
	Button login;
	ProgressBar progress;

    SharedPreferences prefs;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        detector = new ConnectionDetector(getApplicationContext());
        isConnected = detector.isConnectingToInternet();

        startService(new Intent(this, EchoService.class));
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

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

        if(!isConnected) {
            showAlertDialog();
            finish();
        }
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
        unbindService(connection);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void onServiceReady() {
        if(echoService.getUser()!=null && prefs.getBoolean(LOGGED_IN, false)) {
            Intent intent = new Intent(this, ConversationListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        }
    }

    public void loginUser(View v) {

    	String user = username.getText().toString();
    	String pass = password.getText().toString();
    	
    	/* basic validation, display loading bar
    	 * perform API call for login authentication */
    	if(!user.equals("") && !pass.equals("")) {
    		toggleButton();
            new AsyncTask<String, Void, User>() {

                @Override
                protected User doInBackground(String... args) {
                    Log.d("Login", args[0] + " - " + args[1]);
                    User ret = api.userResource.authenticate(args[0], args[1]);
                    return ret;
                }
                @Override
                protected void onPostExecute(User user) {
                    super.onPostExecute(user);
                    if(user == null) {
                        Toaster.displayLong(MainActivity.this, "Username or Password incorrect");
                        toggleButton();
                    } else {
                        echoService.setUser(user);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean(LOGGED_IN, true);
                        editor.commit();
                        Intent i = new Intent(MainActivity.this, ConversationListActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(i);
                    }
                }
            }.execute(user, pass);
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

    private void showAlertDialog() {
        Context context = getApplicationContext();

        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("No Internet Connection");
        alertDialog.setMessage("Please connect to the internet");
        alertDialog.setIcon(android.R.drawable.stat_sys_warning);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
    
}

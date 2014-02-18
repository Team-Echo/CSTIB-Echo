package uk.ac.cam.echo.activities;

import uk.ac.cam.echo.R;
import uk.ac.cam.echo.Toaster;
import uk.ac.cam.echo.client.ClientApi;
import uk.ac.cam.echo.data.User;
import uk.ac.cam.echo.services.EchoService;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Typeface;
import android.os.AsyncTask;
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

public class RegisterActivity extends Activity
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

    EditText username;
    EditText password;
    EditText passwordVerify;
    EditText firstName;
    EditText lastName;
    TextView loginScreen;
    Button register;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        username = (EditText)findViewById(R.id.usernameInput);
        password = (EditText)findViewById(R.id.passwordInput1);
        passwordVerify = (EditText)findViewById(R.id.passwordInput2);
        firstName = (EditText)findViewById(R.id.firstName);
        lastName = (EditText)findViewById(R.id.lastName);
        register = (Button)findViewById(R.id.buttonRegister);
        progress = (ProgressBar)findViewById(R.id.progressLogin);
        loginScreen = (TextView)findViewById(R.id.loginScreen);

        password.setOnEditorActionListener(this);

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

    public void registerUser(View v) {

        String usernameText = username.getText().toString();
        if(usernameText.equals("")) {
            Toaster.displayLong(this, "username must not be empty");
            return;
        }

        String passwordText = password.getText().toString();
        String passwordVerifyText = passwordVerify.getText().toString();
        if(!passwordText.equals(passwordVerifyText)){
            Toaster.displayLong(this, "Passwords do not match");
            return;
        }

        toggleButton();

        String firstText = firstName.getText().toString();
        String lastText = lastName.getText().toString();

        if(firstText.equals("") || lastText.equals("")) {
            Toaster.displayLong(this, "Please enter your name");
            return;
        }

        Toaster.displayShort(this, api == null ? "api is null" : "api not null");
        Toaster.displayShort(this, echoService == null ? "service is null" : "service not null");

        new RegisterUser().execute(usernameText, passwordText, firstText, lastText);

    }

    public void loginScreen(View v) {
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(i);
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(actionId == EditorInfo.IME_ACTION_DONE) {
            register.performClick();
            return true;
        }
        return false;
    }

    private void toggleButton() {
        if(register.getVisibility() == View.GONE) {
            register.setVisibility(View.VISIBLE);
            loginScreen.setVisibility(View.VISIBLE);
            progress.setVisibility(View.GONE);
            loginScreen.setVisibility(View.VISIBLE);
        } else {
            register.setVisibility(View.GONE);
            progress.setVisibility(View.VISIBLE);
            loginScreen.setVisibility(View.GONE);
        }
    }

    private class RegisterUser extends AsyncTask<String, Void, User> {

        @Override
        protected User doInBackground(String... args) {
            String usernameText = args[0];
            String passwordText = args[1];
            String firstText = args[2];
            String lastText = args[3];

            User user = api.newUser();
            user.setUsername(usernameText);
            user.setPassword(passwordText);
            user.setFirstName(firstText);
            user.setLastName(lastText);

            echoService.setUser(user);
            user.save();

            Intent i = new Intent(getApplicationContext(), ConversationListActivity.class);
            startActivity(i);

            return user;
        }
    }

}

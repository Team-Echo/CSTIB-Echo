package uk.ac.cam.echo.activities;

import android.app.Activity;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import uk.ac.cam.echo.BitmapUtil;
import uk.ac.cam.echo.R;
import uk.ac.cam.echo.Toaster;
import uk.ac.cam.echo.client.ClientApi;
import uk.ac.cam.echo.client.data.UserData;
import uk.ac.cam.echo.data.User;
import uk.ac.cam.echo.services.EchoService;

public class UserSettingsActivity extends Activity implements View.OnClickListener {

    private EchoService echoService;
    private ServiceConnection connection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            echoService = ((EchoService.LocalBinder)service).getService();
            api = echoService.getApi();
            user = echoService.getUser();
            Log.d("USER", "user is now " + user.getUsername());
            onServiceReady();
        }
        public void onServiceDisconnected(ComponentName className) {
            echoService = null;
        }
    };

    ClientApi api;
    User user;

    Bitmap avatarBM;

    private ImageView avatar;
    private EditText display;
    private EditText email;
    private EditText phone;
    private EditText job;
    private EditText company;
    private Button update;

    // Original attribute values to compare
    private String displayText;
    private String emailText;
    private String phoneText;
    private String jobText;
    private String companyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        avatar = (ImageView)findViewById(R.id.avatarImg);
        display = (EditText)findViewById(R.id.dispNameInput);
        email = (EditText)findViewById(R.id.emailInput);
        phone = (EditText)findViewById(R.id.phoneInput);
        job = (EditText)findViewById(R.id.jobInput);
        company = (EditText)findViewById(R.id.companyInput);
        update = (Button)findViewById(R.id.updateUserButton);

        update.setOnClickListener(this);

        Log.d("USER", "onCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent service = new Intent(this, EchoService.class);
        Log.d("UserSettingsActivity", "onResume - binding EchoService");
        bindService(service, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(connection);
    }

    private void onServiceReady() {
        getActionBar().setTitle(user.getUsername());

        new AsyncTask<Void,Void,Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... voids) {
                displayText = user.getDisplayName();
                emailText = user.getEmail();
                phoneText = user.getPhoneNumber();
                jobText = user.getJobTitle();
                companyText = user.getCompany();

               if(user.getAvatarLink() != null) {
                    Bitmap bimage =  BitmapUtil.getBitmapFromURL(user.getAvatarLink() + "&s=200");
                    Log.d("BITMAP", (bimage==null) + "");
                    return bimage;
                } return null;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);

                if(!displayText.equals("")) display.setText(displayText);
                if(!emailText.equals("")) email.setText(emailText);
                if(!phoneText.equals("")) phone.setText(phoneText);
                if(!jobText.equals("")) job.setText(jobText);
                if(!companyText.equals("")) company.setText(companyText);

                avatar.setImageBitmap(bitmap);
                //avatarBM = bitmap;
            }
        }.execute();

        Log.d("USER", "onServerReady");
    }

    // Set up action-bar and Search functionality
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        getMenuInflater().inflate(R.menu.user_profile, menu);
        return true;
    }

    // Attaching functionality to menu-items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_update:
                update.performClick();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    // Update the user account
    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.updateUserButton) {

            new AsyncTask<String, Void, Boolean>() {

                @Override
                protected Boolean doInBackground(String... args) {
                    Log.d("USER", ""+(user == null));
                    if(!args[0].equals(displayText))
                        ((UserData)user).setDisplayName(args[0]);

                    if(!args[1].equals(emailText))
                        user.setEmail(args[1]);

                    if(!args[2].equals(phoneText)) {
                        user.setPhoneNumber(args[2]);
                        Log.d("USER", "setting phone number " + args[2]);
                    }

                    if(!args[3].equals(jobText))
                        user.setJobTitle(args[3]);

                    if(!args[4].equals(companyText))
                        user.setCompany(args[4]);

                    user.save();

                    return true;
                }

                @Override
                protected void onPostExecute(Boolean aBoolean) {
                    super.onPostExecute(aBoolean);
                    Toaster.displayShort(getApplicationContext(), "User updated!");
                }
            }.execute(display.getText().toString(),     //args[0]
                      email.getText().toString(),       //args[1]
                      phone.getText().toString(),       //args[2]
                      job.getText().toString(),         //args[3]
                      company.getText().toString());    //args[4]
        }
    }




}

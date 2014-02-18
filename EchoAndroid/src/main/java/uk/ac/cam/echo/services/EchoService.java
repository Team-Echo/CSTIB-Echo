package uk.ac.cam.echo.services;

import java.util.List;

import uk.ac.cam.echo.R;
import uk.ac.cam.echo.activities.ConversationDetailActivity;
import uk.ac.cam.echo.client.ClientApi;
import uk.ac.cam.echo.data.Conference;
import uk.ac.cam.echo.data.Conversation;
import uk.ac.cam.echo.data.Message;
import uk.ac.cam.echo.data.User;
import uk.ac.cam.echo.data.async.Handler;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class EchoService extends Service {

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder binder = new LocalBinder();

	private long conferenceId;
	private long conversationId;
	
	private Conference conference;
	private Conversation conversation;

	private static ClientApi api;
    private static User user;


    private NotificationManager notificationManager;
    private static boolean notifEnabled;
    private int numMessages;


    @Override
    public void onCreate() {
        super.onCreate();
        api = new ClientApi("http://echoconf.herokuapp.com/");
        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public ClientApi getApi() {return api; }

    public void setUser(User u) { user = u; }
    public User getUser() { return user; }

     public void notify(Message message) {
         if(notifEnabled) {
             Intent intent = new Intent(EchoService.this, ConversationDetailActivity.class);
             intent.putExtra("_id", 5);
             PendingIntent pIntent = PendingIntent.getActivity(EchoService.this, 0, intent, 0);

             Notification.Builder notifBuilder = new Notification.Builder(EchoService.this)
                     .setContentTitle("New message in ")
                     .setContentIntent(pIntent)
                     .setSmallIcon(R.drawable.devil)
                     .addAction(R.drawable.admin, "See", pIntent)
                     .setAutoCancel(true)
                     .setContentText("Anon" + ": " + message.getContents());
             notificationManager.notify(0,
                     notifBuilder.build());
         }
    }

   public void setNotifEnabled(boolean enabled) {
       notifEnabled = enabled;
   }

   public void listenToConversation(long id) {
       Log.d("EchoListen", "listening to " + id);
       if(conversationId != id) {

           new AsyncTask<Long, Void, Void>() {
               @Override
               protected Void doInBackground(Long... args) {
                   getUser().setCurrentConversation(api.conversationResource.get(args[0]));
                   return null;
               }
           }.execute(id);

           conversationId = id;
           Handler<Message> handler = new Handler<Message>() {
               @Override
               public void handle(Message message) {
                   EchoService.this.notify(message);
               }
           };
           api.conversationResource.listenToMessages(id).subscribe(handler);
       }
   }

	public long getConversationId() {
        return conversationId;
	}




    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder {
        public EchoService getService() {
            return EchoService.this;
        }
    }
	

}

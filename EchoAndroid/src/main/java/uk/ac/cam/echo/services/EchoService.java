package uk.ac.cam.echo.services;

import java.util.List;

import uk.ac.cam.echo.BitmapUtil;
import uk.ac.cam.echo.R;
import uk.ac.cam.echo.activities.ConversationDetailActivity;
import uk.ac.cam.echo.activities.ConversationListActivity;
import uk.ac.cam.echo.activities.MainActivity;
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
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;

public class EchoService extends Service {

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder binder = new LocalBinder();

    private static Vibrator v;

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
        v = (Vibrator)getSystemService(VIBRATOR_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }




    @Override
    public void onDestroy() {
        Log.d("SERVICE", "onDestroy()");
        if(getUser() != null) {
            getUser().setCurrentConversation(null);
            getUser().save();
        }
        super.onDestroy();
    }

    public ClientApi getApi() {return api; }

    public void setUser(User u) { user = u; }
    public User getUser() { return user; }

     public void notify(Message message) {
         Log.d("NOTIF", "notify called " + notifEnabled);
         if(notifEnabled) {
             new AsyncTask<Message, Void, Notification.Builder>(){
                 @Override
                 protected Notification.Builder doInBackground(Message... args) {
                     Message msg = args[0];
                     Context context = getApplicationContext();
                     Intent intent = new Intent(context, ConversationListActivity.class);
                     intent.putExtra("_id", msg.getConversation().getId());
                     PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);

                     String user = msg.getSender().getFirstName();

                     Bitmap avatar = BitmapUtil.getBitmapFromURL(msg.getSender().getAvatarLink()+"&s=200");

                     long[] vibratePattern = {500,0,0,500};

                     Notification.Builder notifBuilder = new Notification.Builder(context)
                             .setContentTitle(conversation.getName())
                             .setContentIntent(pIntent)
                             .setSmallIcon(android.R.drawable.ic_dialog_email)
                             .setLargeIcon(avatar)
                             .setAutoCancel(true)
                             .setContentText(user + ": " + msg.getContents())
                             .setVibrate(vibratePattern)
                             .setTicker(user + ": " + msg.getContents());

                     return notifBuilder;
                 }

                 @Override
                 protected void onPostExecute(Notification.Builder nb) {
                     super.onPostExecute(nb);
                     notificationManager.notify(0, nb.build());
                 }
             }.execute(message);
         }
    }

    public void notifyUpdate(Message message) {
        Log.d("NOTIF", "notifyUpdate");
        if(message == null) return;

        new AsyncTask<Message, Void, Notification.Builder>(){
            @Override
            protected Notification.Builder doInBackground(Message... args) {
                Message msg = args[0];
                Context context = getApplicationContext();
                Conversation msgConv = msg.getConversation();

                Intent intent = new Intent(context, ConversationListActivity.class);
                intent.putExtra("_id", msgConv.getId());
                Log.d("LISTEN",""+msgConv.getId());
                PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);
                Log.d("NOTIFY", msg == null ? "null" : "not null");
                String user = msg.getSender().getFirstName();

                Bitmap avatar = BitmapUtil.getBitmapFromURL(msg.getSender().getAvatarLink()+"&s=200");

                long[] vibratePattern = {250,0,250};
                Notification.Builder notifBuilder = new Notification.Builder(context)
                        .setTicker("Overheard " + msg.getSender().getDisplayName() + " in " + msgConv.getName())
                        .setContentTitle("Overheard " + msgConv.getName())
                        .setContentIntent(pIntent)
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .setLargeIcon(avatar)
                        .setVibrate(vibratePattern)
                        .setAutoCancel(true)
                        .setContentText(user + ": " + msg.getContents());

                return notifBuilder;
            }

            @Override
            protected void onPostExecute(Notification.Builder nb) {
                super.onPostExecute(nb);
                Notification n = nb.build();
                notificationManager.notify(1, n);
            }
        }.execute(message);
     }


   public void setNotifEnabled(boolean enabled) {
       Log.d("NOTIF", "notifications are now " + enabled);
       notifEnabled = enabled;
   }

   public void listenToConversation(long id) {

       if(conversationId != id) {

           new AsyncTask<Long, Void, Conversation>() {

               @Override
               protected Conversation doInBackground(Long... args) {
                   Conversation c = api.conversationResource.get(args[0]);
                   getUser().setCurrentConversation(c);
                   getUser().save();
                   return c;
               }

               @Override
               protected void onPostExecute(Conversation conversation) {
                   super.onPostExecute(conversation);
                   setCurrentConversation(conversation);
               }
           }.execute(id);
           Log.d("Notif", conversation == null ? " conv is null " : "conv is not null");
           conversationId = id;
           Handler<Message> handler = new Handler<Message>() {
               @Override
               public void handle(Message message) {
                   Log.d("NOTIF", "new notif received");
                   EchoService.this.notify(message);
               }
           };
           Log.d("NOTIF", "listening for notifs to " + id);
           api.conversationResource.listenToMessages(id).subscribe(handler);
       }
   }

    public void setCurrentConversation(Conversation newConv) {conversation = newConv; }

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

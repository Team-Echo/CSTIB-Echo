package uk.ac.cam.echo;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.sql.Date;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import uk.ac.cam.echo.activities.UserListActivity;
import uk.ac.cam.echo.client.ClientApi;
import uk.ac.cam.echo.data.Conversation;
import uk.ac.cam.echo.data.Interest;
import uk.ac.cam.echo.data.User;


public class UserAdapter extends BaseExpandableListAdapter {

    private int layoutResourceId;
    private Activity activity;
    private HashMap<Long, UserCache> userMap;
    private Context context;
    private ClientApi api;
    private List<User> data;

    private static String conversationName, loggedInUserName;

    public UserAdapter(Context context, int layoutResourceId,
                               List<User> data) {

        this.context = context;
        userMap = new HashMap<Long, UserCache>();
        this.layoutResourceId = layoutResourceId;
        this.data = data;
    }

    public static UserAdapter newInstance(Activity activity, int layoutResourceId,
                                                  List<User> data, ClientApi api) {
        UserAdapter adapter = new UserAdapter(activity.getBaseContext(), layoutResourceId, data);
        adapter.setActivity(activity);
        adapter.setApi(api);
        return adapter;
    }


    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        View row = convertView;
        UserHolder holder;

        if(row == null) {
            LayoutInflater inflater = (LayoutInflater)context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.user_child_item, parent, false);

            holder = new UserHolder();
            holder.avatar = (ImageView)row.findViewById(R.id.avatar);
            holder.user = (TextView)row.findViewById(R.id.username);
            holder.jobAndCompany = (TextView)row.findViewById(R.id.jobAndCompany);
            holder.interests = (TextView)row.findViewById(R.id.interests);
            holder.phone = (TextView)row.findViewById(R.id.phone);
            holder.email = (TextView)row.findViewById(R.id.email);
            holder.phoneButton = (ImageButton)row.findViewById(R.id.phoneUser);
            holder.emailButton = (ImageButton)row.findViewById(R.id.emailUser);

            row.setTag(holder);
        } else {
            holder = (UserHolder)row.getTag();
        }

        final User user = data.get(groupPosition);

        if(userMap.get(user.getId()).hasAttributes()) {
            final UserCache userCache = userMap.get(user.getId());
            holder.avatar.setImageBitmap(userCache.avatar);
            holder.user.setText(userCache.user);
            holder.jobAndCompany.setText(userCache.jobAndCompany);
            holder.interests.setText(userCache.interests);
            holder.phone.setText(userCache.phone);
            holder.email.setText(userCache.email);

            holder.phoneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String uri = "tel:" + userCache.phone.trim();
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse(uri));
                    context.startActivity(intent);
                }
            });

            holder.emailButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto", userCache.email, null));
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, user.getCurrentConversation().getName());
                    emailIntent.setType("plain/text");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "Dear " + user.getDisplayName() +",\n\n" );
                    emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(Intent.createChooser(emailIntent, "Send Email using: "));
                }
            });

        } else {
            new AsyncAdapter().execute(user, holder.avatar, holder.user, holder.jobAndCompany,
                    holder.interests, holder.phone, holder.email, holder.phoneButton, holder.emailButton);
        }
        return row;
    }

    @Override
    public boolean isChildSelectable(int i, int i2) {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        View row = convertView;

        User user = data.get(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.user_group_item, parent, false);
        }

        TextView userDisplay = (TextView) row.findViewById(R.id.userFullName);
        userDisplay.setText(user.getDisplayName());
        TextView lastSeen = (TextView)row.findViewById(R.id.lastSeenText);
        if(userMap.containsKey(user.getId())) {
            lastSeen.setText(userMap.get(user.getId()).lastActive);
            lastSeen.setTextColor(context.getResources().getColor(userMap.get(user.getId()).colour));
        } else {
            new AsyncTask<Object, Void, String>() {
                User user;
                TextView lastSeen;
                int colour;

                @Override
                protected String doInBackground(Object... args) {

                    user = (User)args[0];
                    lastSeen = (TextView)args[1];

                    long timestamp = api.conferenceResource.lastTimeActive(1, user.getId());
                    colour = R.color.darkRed;


                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date(timestamp));

                    if(calendar.get(Calendar.MONTH) > 1)
                        return calendar.get(Calendar.MONTH) + " months";
                    else if(calendar.get(Calendar.DAY_OF_YEAR)-1 > 1) {
                        colour = R.color.midOrange;
                        return calendar.get(Calendar.DAY_OF_YEAR)-1 + " days";
                    }
                    else if(calendar.get(Calendar.HOUR_OF_DAY) > 1){
                        colour = R.color.darkGreen;
                        return calendar.get(Calendar.HOUR_OF_DAY)-1 + " hours";
                    }
                    else {
                        colour = R.color.green;
                        return calendar.get(Calendar.MINUTE)-1 + " mins";
                    }
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    lastSeen.setText(s);
                    userMap.put(user.getId(), new UserCache(s,colour));
                    lastSeen.setTextColor(context.getResources().getColor(colour));
                }
            }.execute(user, lastSeen);
        }
        return row;
    }

    public void setApi(ClientApi clientApi) { api = clientApi; }
    public void setActivity(Activity activity) {this.activity = activity;}

    public void updateList(List<User> newData) {
        data = newData;
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return data.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int i2) {
        return data.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return data.get(groupPosition).getId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return data.get(groupPosition).getId();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    static class UserHolder {
        ImageView avatar;
        TextView user;
        TextView jobAndCompany;
        TextView interests;
        TextView phone;
        TextView email;
        ImageButton phoneButton;
        ImageButton emailButton;
    }

    private class AsyncAdapter extends AsyncTask<Object, Void, String> {
        User user;
        String userDisplay;

        ImageView imgView;
        TextView username;
        TextView jobAndCompany;
        TextView interests;
        TextView phone;
        TextView email;
        Bitmap avatar;

        ImageButton phoneButton;
        ImageButton emailButton;

        String usernameText;
        String jobAndCompanyText;
        String interestsText;
        String phoneText;
        String emailText;


        @Override
        protected String doInBackground(Object... params) {
            user = (User)params[0];
            imgView = (ImageView)params[1];
            username = (TextView)params[2];
            jobAndCompany = (TextView)params[3];
            interests = (TextView)params[4];
            phone = (TextView)params[5];
            email = (TextView)params[6];
            phoneButton = (ImageButton)params[7];
            emailButton = (ImageButton)params[8];

            if(conversationName == null || conversationName.equals(""))
                conversationName = user.getCurrentConversation().getName();

            if(loggedInUserName == null || loggedInUserName.equals(""))
                loggedInUserName = ((UserListActivity)activity).getService().getUser().getDisplayName();

            userDisplay = user.getDisplayName();

            usernameText = user.getUsername();
            Collection<Interest> interests = user.getInterests();
            interestsText = ConversationStringUtil.getInterestText(interests);
            jobAndCompanyText = ConversationStringUtil.getJobCompanyText(user.getJobTitle(), user.getCompany());
            phoneText = user.getPhoneNumber();
            emailText = user.getEmail();
            avatar = BitmapUtil.getBitmapFromURL(user.getAvatarLink() + "&s=90");

            return null;
        }

        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);

            username.setText(usernameText);

            jobAndCompany.setText(jobAndCompanyText);
            interests.setText(interestsText);

            phone.setText(phoneText);
            email.setText(emailText);
            imgView.setImageBitmap(avatar);

            if(phoneText!=null && !phoneText.equals("")){
                phoneButton.setVisibility(View.VISIBLE);
                phoneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String uri = "tel:" + phoneText.trim();
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse(uri));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);

                    }
                });
            }

            if(emailText!=null && !emailText.equals("")){
             emailButton.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     Intent emailIntent = new Intent(Intent.ACTION_SEND);
                     emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                     emailIntent.setType("message/rfc822");
                     emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailText});

                     emailIntent.putExtra(Intent.EXTRA_SUBJECT, conversationName + " - a personal message from " + loggedInUserName);
                     emailIntent.putExtra(Intent.EXTRA_TEXT, "Dear " + userDisplay + ",\n\n\n" + loggedInUserName);
                     activity.startActivity(Intent.createChooser(emailIntent, "Choose an email client: "));
                 }
             });
            }

            userMap.put(user.getId(), userMap.get(user.getId()).setAttributes(avatar, usernameText, jobAndCompanyText,
                    interestsText, phoneText, emailText));

        }
    }
}

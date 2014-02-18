package uk.ac.cam.echo;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import uk.ac.cam.echo.data.Message;

public class MessageAdapter extends ArrayAdapter<Message> {
	
	Context context;
	int layoutResourceId;
	LayoutInflater inflater;
    ListView listView;
	
	static List<Message> data = null;
	
	// for scaling message TextViews
	int width;
	int height;
	
	public MessageAdapter(Context context, int layoutResourceId,
			List<Message> data) {
		
		super(context, layoutResourceId, data);
		
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.data = data;
        this.inflater = ((Activity)context).getLayoutInflater();
		
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		width = metrics.widthPixels;
		height = metrics.heightPixels;
	}

	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
		
		View row = convertView;
		MessageHolder holder = null;
		int type = getItemViewType(position);
		
		if(row == null) {

			if(type == 0) { // home
				row = inflater.inflate(R.layout.message_row_home, parent, false);
			} else { // remote
				row = inflater.inflate(R.layout.message_row_remote, parent, false);
			}
			
			holder = new MessageHolder();
			holder.user = (TextView)row.findViewById(R.id.userText);
			holder.time = (TextView)row.findViewById(R.id.dateText);
			holder.contents = (TextView)row.findViewById(R.id.message);
			
			row.setTag(holder);
		} else {
			holder = (MessageHolder)row.getTag();
		}
		
		Message message = data.get(position);

		//String user = message.getSender() == null ? "Anonymous" : message.getSender().getUsername();
        String user = "Anon";

		long time = message.getTimeStamp();
        Date date = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM\nHH:mm");
        String timeString = formatter.format(date);

		String contents = message.getContents();
		
		holder.contents.setMaxWidth((int) (width*0.66));
		holder.contents.setMinWidth(width/3);
		holder.user.setText(user);
		holder.contents.setText(contents);
		
		holder.time.setText(timeString);

		return row;
	}

    public void updateMessage(Message m) {
        add(m);
        notifyDataSetChanged();
        listView.setSelection(getCount());
        Log.d("MessageList", "done..." + m.getContents());
    }

    public void setListView(ListView lv) { listView = lv; }

	@Override
	public int getViewTypeCount() {
		return 2;
	}
	
	@Override
	public int getItemViewType(int position) {
        // data.get(position).getSender().getUsername() == username ? 0 : 1;
        return Math.random() < 0.5 ? 1 : 0;
	}
	
	@Override
	public int getCount() {
		return data == null ? 0 : data.size();
	}
	
	static class MessageHolder {
		ImageView img;
		TextView user;
		TextView time;
		TextView contents;
	}


}

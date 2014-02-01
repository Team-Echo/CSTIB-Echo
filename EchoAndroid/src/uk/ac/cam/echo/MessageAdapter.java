package uk.ac.cam.echo;

import java.util.List;

import uk.ac.cam.echo.dummy.Conversation.Message;
import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MessageAdapter extends ArrayAdapter<Message> {
	
	Context context;
	int layoutResourceId;
	LayoutInflater inflater;
	
	List<Message> data = null;
	
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
		Toaster.displayShort(context, width+"");
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
		String user = message.getSender().getName();
		String time = "4:20";
		String contents = message.getContents();
		Log.d("ADAPTER", user);
		// holder.textView.setMaxWidth(width*0.66);

		holder.user.setText(user);
		holder.contents.setText(contents);
		
		holder.time.setText(time);

		return row;
	}
	
	@Override
	public int getViewTypeCount() {
		return 2;
	}
	
	@Override
	public int getItemViewType(int position) {
		return data.get(position).isRemote() ? 1 : 0;
	}
	
	@Override
	public int getCount() {
		return data.size();
	}
	
	static class MessageHolder {
		ImageView img;
		TextView user;
		TextView time;
		TextView contents;
	}
}

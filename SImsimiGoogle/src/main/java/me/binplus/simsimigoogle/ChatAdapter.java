package me.binplus.simsimigoogle;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by bjzhou on 13-5-30.
 */
public class ChatAdapter extends BaseAdapter {

    private List<MessageBean> list;
    private Activity activity;

    public ChatAdapter(Activity activity, List<MessageBean> list) {
        this.list = list;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        if (list != null)
            return list.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (i % 2 == 0)
            view = activity.getLayoutInflater().inflate(R.layout.list_item_right, null);
        else
            view = activity.getLayoutInflater().inflate(R.layout.list_item_left, null);

        TextView tv = (TextView) view.findViewById(R.id.tv_msg);
        tv.setText(list.get(i).getMsg());
        return view;

    }
}

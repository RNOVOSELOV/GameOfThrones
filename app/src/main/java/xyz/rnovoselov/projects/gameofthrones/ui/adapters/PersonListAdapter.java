package xyz.rnovoselov.projects.gameofthrones.ui.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import xyz.rnovoselov.projects.gameofthrones.R;
import xyz.rnovoselov.projects.gameofthrones.data.storage.models.Titles;

/**
 * Created by roman on 15.10.16.
 */

public class PersonListAdapter extends BaseAdapter {

    private List<String> mTitles;
    private LayoutInflater mInflater;
    private Context mContext;

    public PersonListAdapter(Context context, List<String> list) {
        this.mTitles = list;
        mInflater = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        mContext = context;
    }

    @Override
    public int getCount() {
        return mTitles.size();
    }

    @Override
    public Object getItem(int position) {
        return mTitles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = mInflater.inflate(R.layout.item_repo_list, parent, false);
        }

        TextView editText = ((TextView) itemView);
        editText.setText(mTitles.get(position));
        return itemView;
    }
}
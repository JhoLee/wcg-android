package me.jho.wcg.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import me.jho.wcg.R;

public class FontSpinnerAdapter extends ArrayAdapter<String> {
    private String[] fontNames;
    private Context mContext;

    public FontSpinnerAdapter(@NonNull Context context, String[] fontNames) {
        super(context, R.layout.spinner_font_row);

        this.fontNames = fontNames;
        this.mContext = context;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    public int getCount() {
        return fontNames.length;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder mViewHolder = new ViewHolder();

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.spinner_font_row, parent, false);
            mViewHolder.mName = (TextView) convertView.findViewById(R.id.textview_spinner_font);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        mViewHolder.mName.setText(fontNames[position]);

        return convertView;

    }

    private static class ViewHolder {
        TextView mName;
    }
}

package igotplaced.com.layouts.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import igotplaced.com.layouts.Model.SpinnerWithCheckBox;
import igotplaced.com.layouts.R;

/**
 * Created by Ashith VL on 5/8/2017.
 */

public class SpinnerCheckBoxAdapter extends ArrayAdapter<SpinnerWithCheckBox> {
    private Context mContext;
    private ArrayList<SpinnerWithCheckBox> spinnerWithCheckBoxArrayList;
    private SpinnerCheckBoxAdapter spinnerCheckBoxAdapter;
    private boolean isFromView = false;

    public SpinnerCheckBoxAdapter(Context context, int resource, List<SpinnerWithCheckBox> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.spinnerWithCheckBoxArrayList = (ArrayList<SpinnerWithCheckBox>) objects;
        this.spinnerCheckBoxAdapter = this;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(mContext);
            convertView = layoutInflator.inflate(R.layout.spinner_with_check_box, null);
            holder = new ViewHolder();
            holder.mTextView = (TextView) convertView.findViewById(R.id.text_view);
            holder.mCheckBox = (CheckBox) convertView.findViewById(R.id.checkbox_spinner);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mTextView.setText(spinnerWithCheckBoxArrayList.get(position).getTitle());

        // To check weather checked event fire from getview() or user input
        isFromView = true;
        holder.mCheckBox.setChecked(spinnerWithCheckBoxArrayList.get(position).isSelected());
        isFromView = false;

        if ((position == 0)) {
            holder.mCheckBox.setVisibility(View.INVISIBLE);
        } else {
            holder.mCheckBox.setVisibility(View.VISIBLE);
        }
        holder.mCheckBox.setTag(position);
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int getPosition = (Integer) buttonView.getTag();
            }
        });
        return convertView;
    }

    private class ViewHolder {
        private TextView mTextView;
        private CheckBox mCheckBox;
    }
}
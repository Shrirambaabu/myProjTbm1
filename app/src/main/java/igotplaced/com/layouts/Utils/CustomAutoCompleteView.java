package igotplaced.com.layouts.Utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

/**
 * Created by Ashith VL on 5/16/2017.
 */

public class CustomAutoCompleteView extends android.support.v7.widget.AppCompatAutoCompleteTextView {

    public CustomAutoCompleteView(Context context) {
        super(context);
    }

    public CustomAutoCompleteView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomAutoCompleteView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void performFiltering(CharSequence text, int keyCode) {
        String filterText = "";
        super.performFiltering(filterText, keyCode);
    }

    /*
    * After a selection, capture the new value and append to the existing 
    * text 
    */

    @Override
    protected void replaceText(CharSequence text) {
        super.replaceText(text);
    }
}

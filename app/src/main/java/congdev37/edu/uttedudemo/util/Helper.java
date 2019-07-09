package congdev37.edu.uttedudemo.util;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;

/**
 * Mục đính của methob:
 * @Create_by: trand
 * @Date: 7/9/2019
 * @param
 * @return
 */
public class Helper {
    // hàm lấy activity từ context
    public static Activity getActivity(Context context)
    {
        if (context == null)
        {
            return null;
        }
        else if (context instanceof ContextWrapper)
        {
            if (context instanceof Activity)
            {
                return (Activity) context;
            }
            else
            {
                return getActivity(((ContextWrapper) context).getBaseContext());
            }
        }

        return null;
    }
}

package congdev37.edu.uttedudemo.admin.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import congdev37.edu.uttedudemo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubjectManagerFragment extends Fragment {


    public SubjectManagerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manager_subject, container, false);
    }

}

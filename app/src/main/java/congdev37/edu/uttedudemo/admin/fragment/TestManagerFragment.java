package congdev37.edu.uttedudemo.admin.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import congdev37.edu.uttedudemo.MainActivity;
import congdev37.edu.uttedudemo.R;

public class TestManagerFragment extends Fragment {

    View view;

    public TestManagerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_test_manager, container, false);
        return view;
    }


}

package com.bupt.booktrade.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bupt.booktrade.R;

public class PersonalHomeFragment extends Fragment {

    public PersonalHomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_personal_home, container, false);

        return rootView;
    }
}

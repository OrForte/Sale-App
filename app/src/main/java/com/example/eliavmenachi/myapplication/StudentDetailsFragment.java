package com.example.eliavmenachi.myapplication;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StudentDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StudentDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudentDetailsFragment extends Fragment {
    private static final String ARG_PARAM1 = "studentId";
    String studentId;

    //private OnFragmentInteractionListener mListener;

    public StudentDetailsFragment() {
    }


//    public static StudentDetailsFragment newInstance(String studentId) {
//        StudentDetailsFragment fragment = new StudentDetailsFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, studentId);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            studentId = getArguments().getString(ARG_PARAM1);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student_details, container, false);
        TextView title = view.findViewById(R.id.st_details_title);
        title.setText(studentId);

        return view;
    }

}

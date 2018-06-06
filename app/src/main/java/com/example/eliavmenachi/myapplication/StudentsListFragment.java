package com.example.eliavmenachi.myapplication;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.eliavmenachi.myapplication.Model.Model;
import com.example.eliavmenachi.myapplication.Model.Student;

import java.util.LinkedList;
import java.util.List;


public class StudentsListFragment extends Fragment {
    //private OnFragmentInteractionListener mListener;

    ListView list;
    MyAdapter myAdapter = new MyAdapter();;
    StudentListViewModel dataModel;

    public static StudentsListFragment newInstance() {
        StudentsListFragment fragment = new StudentsListFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataModel = ViewModelProviders.of(this).get(StudentListViewModel.class);
        dataModel.getData().observe(this, new Observer<List<Student>>() {
            @Override
            public void onChanged(@Nullable List<Student> students) {
                myAdapter.notifyDataSetChanged();
                Log.d("TAG","notifyDataSetChanged" + students.size());
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Model.instance.cancellGetAllStudents();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_students_list, container, false);

        list = view.findViewById(R.id.studentslist_list);
        list.setAdapter(myAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("TAG","item selected:" + i);
            }
        });
        return view;
    }



    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }


    class MyAdapter extends BaseAdapter {
        public MyAdapter(){
        }

        @Override
        public int getCount() {
            Log.d("TAG","list size:" + dataModel.getData().getValue().size());

            return dataModel.getData().getValue().size();

        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            if (view == null){
                view = LayoutInflater.from(getActivity()).inflate(R.layout.student_list_item,null);
                final CheckBox cb = view.findViewById(R.id.stListItem_check_cb);
                cb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int index = (int) cb.getTag();
                        Student s = dataModel.getData().getValue().get(index);
                        s.checked = !s.checked;
                    }
                });
            }

            final Student s = dataModel.getData().getValue().get(i);

            TextView nameTv = view.findViewById(R.id.stListItem_name_tv);
            TextView idTv = view.findViewById(R.id.stListItem_id_tv);
            CheckBox cb = view.findViewById(R.id.stListItem_check_cb);
            final ImageView avatarView = view.findViewById(R.id.stListItem_avatar);

            cb.setTag(i);

            nameTv.setText(s.name);
            idTv.setText(s.id);
            cb.setChecked(s.checked);
            avatarView.setImageResource(R.drawable.avatar);
            avatarView.setTag(s.id);
            if (s.avatar != null){
                Model.instance.getImage(s.avatar, new Model.GetImageListener() {
                    @Override
                    public void onDone(Bitmap imageBitmap) {
                        if (s.id.equals(avatarView.getTag()) && imageBitmap != null) {
                            avatarView.setImageBitmap(imageBitmap);
                        }
                    }
                });
            }
            return view;
        }
    }
}

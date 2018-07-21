package com.example.eliavmenachi.myapplication;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.eliavmenachi.myapplication.Model.Model;
import com.example.eliavmenachi.myapplication.Model.Student;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewStudentFragment extends Fragment {
    EditText nameEt;
    EditText idEt;
    EditText dateEt;
    ImageView avatar;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    ProgressBar progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_student, container, false);

        nameEt = view.findViewById(R.id.new_student_name);
        idEt = view.findViewById(R.id.new_student_id);
        dateEt = view.findViewById(R.id.new_student_bdate);
        progress = view.findViewById(R.id.new_student_progress);
        progress . setVisibility(View.GONE);

        Button save = view.findViewById(R.id.new_student_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress . setVisibility(View.VISIBLE);

                final Student st = new Student();
                st.name = nameEt.getText().toString();
                st.id = idEt.getText().toString();

                //save image
                if (imageBitmap != null) {
                    Model.instance.saveImage(imageBitmap, new Model.SaveImageListener() {
                        @Override
                        public void onDone(String url) {
                            //save student obj
                            st.avatar = url;
                            Model.instance.addStudent(st);
                            progress . setVisibility(View.GONE);
                            //getActivity().getSupportFragmentManager().popBackStack();
                        }
                    });
                }
            }
        });

        Button cancel = view.findViewById(R.id.new_student_cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        Button editImage = view.findViewById(R.id.new_student_img_btn);
        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open camera
                Intent takePictureIntent = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });
        avatar = view.findViewById(R.id.new_student_image);
        return view;
    }

    Bitmap imageBitmap;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE &&
                resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            avatar.setImageBitmap(imageBitmap);
        }
    }
}

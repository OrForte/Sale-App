package com.example.eliavmenachi.myapplication.Fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Bitmap;
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

import com.example.eliavmenachi.myapplication.Entities.CityMallStoreDetails;
import com.example.eliavmenachi.myapplication.Entities.Sale;
import com.example.eliavmenachi.myapplication.Model.Model;
import com.example.eliavmenachi.myapplication.Models.MainModel;
import com.example.eliavmenachi.myapplication.Models.SaleListViewModel;
import com.example.eliavmenachi.myapplication.R;

import java.util.List;

public class SalesListFragment extends Fragment {

    ListView list;
    SalesListFragment.ListAdapter listAdapter = new SalesListFragment.ListAdapter();;
    SaleListViewModel dataModel;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        dataModel = ViewModelProviders.of(this).get(SaleListViewModel.class);
        dataModel.getData().observe(this, new Observer<List<Sale>>() {
            @Override
            public void onChanged(@Nullable List<Sale> sales) {
                listAdapter.notifyDataSetChanged();
                Log.d("TAG","notifyDataSetChanged" + sales.size());
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // todo: cahnge to ssales
        Model.instance.cancellGetAllStudents();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sale_list, container, false);

        list = view.findViewById(R.id.lvSaleList);
        list.setAdapter(listAdapter);
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


    class ListAdapter extends BaseAdapter {
        public ListAdapter(){
        }

        @Override
        public int getCount() {
            int nCount = 0;
            if (dataModel != null)
            {
                if (dataModel.getData() != null)
                {
                    if (dataModel.getData().getValue() != null)
                    {
                        nCount = dataModel.getData().getValue().size();
                    }
                }
            }
            return nCount;
        }

        // TODO:
        @Override
        public Object getItem(int i) {
            return null;
        }

        // TODO:
        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null){
                view = LayoutInflater.from(getActivity()).inflate(R.layout.sale_list_item,null);
//                final CheckBox cb = view.findViewById(R.id.stListItem_check_cb);
//                cb.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        int index = (int) cb.getTag();
//                        Student s = dataModel.getData().getValue().get(index);
//                        s.checked = !s.checked;
//                    }
//                });
            }

            final Sale currentSale = dataModel.getData().getValue().get(i);

            TextView tvCity = view.findViewById(R.id.tvCity);
            TextView tvMall = view.findViewById(R.id.tvMall);
            TextView tvStore = view.findViewById(R.id.tvStore);
            final ImageView imSalePic = view.findViewById(R.id.ivSalePic);

            String strStoreId = currentSale.storeId +"";

            if (currentSale != null) {
                MainModel.instance.GetDetailsByStoreId(currentSale.storeId, new MainModel.GetDetailsByStoreIdListener() {
                    @Override
                    public void onGetDetailsByStoreIdResults(CityMallStoreDetails data) {

                    }
                });

                tvCity.setText(strStoreId);
                tvMall.setText(strStoreId);
                tvStore.setText(strStoreId);
            }

            //imSalePic.setImageResource(R.drawable.avatar);
            //imSalePic.setTag(currentSale.id);
            /*
            if (currentSale.getPictureUrl() != null) {
                Model.instance.getImage(currentSale.getPictureUrl(), new Model.GetImageListener() {
                    @Override
                    public void onDone(Bitmap imageBitmap) {
                        if (currentSale.id.equals(imSalePic.getTag()) && imageBitmap != null) {
                            imSalePic.setImageBitmap(imageBitmap);
                        }
                    }
                });
            }*/
            return view;
//
//            TextView nameTv = view.findViewById(R.id.stListItem_name_tv);
//            TextView idTv = view.findViewById(R.id.stListItem_id_tv);
//            CheckBox cb = view.findViewById(R.id.stListItem_check_cb);
//            final ImageView avatarView = view.findViewById(R.id.stListItem_avatar);
//
//            cb.setTag(i);
//
//            nameTv.setText(s.name);
//            idTv.setText(s.id);
//            cb.setChecked(s.checked);
//            avatarView.setImageResource(R.drawable.avatar);
//            avatarView.setTag(s.id);
//            if (s.avatar != null){
//                Model.instance.getImage(s.avatar, new Model.GetImageListener() {
//                    @Override
//                    public void onDone(Bitmap imageBitmap) {
//                        if (s.id.equals(avatarView.getTag()) && imageBitmap != null) {
//                            avatarView.setImageBitmap(imageBitmap);
//                        }
//                    }
//                });
//            }
//            return view;
        }
    }
}

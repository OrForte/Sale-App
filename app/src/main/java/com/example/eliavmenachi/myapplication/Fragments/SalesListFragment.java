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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.eliavmenachi.myapplication.Entities.ListData;
import com.example.eliavmenachi.myapplication.Entities.Sale;
import com.example.eliavmenachi.myapplication.Entities.Store;
import com.example.eliavmenachi.myapplication.Entities.Mall;
import com.example.eliavmenachi.myapplication.Entities.City;
import com.example.eliavmenachi.myapplication.Models.ImageModel;
import com.example.eliavmenachi.myapplication.Models.MainModel;
import com.example.eliavmenachi.myapplication.Models.SaleListViewModel;
import com.example.eliavmenachi.myapplication.R;

import junit.framework.Test;

import java.util.List;

public class SalesListFragment extends Fragment {

    ListView list;
    SalesListFragment.ListAdapter listAdapter = new SalesListFragment.ListAdapter();;
    SaleListViewModel dataModel;
    ListData listData;
    ImageView imSalePic;

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
        //Model.instance.cancellGetAllStudents();
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
            final int copyI = i;
            final View copyView = view;
            final TextView tvCity = view.findViewById(R.id.tvCity);
            final TextView tvMall = view.findViewById(R.id.tvMall);
            final TextView tvStore = view.findViewById(R.id.tvStore);
            final TextView tvDesc = view.findViewById(R.id.tvDescription);
            tvDesc.setText(currentSale.description);

            String strStoreId = currentSale.storeId +"";
            tvCity.setText(currentSale.cityName);
            tvMall.setText(currentSale.mallName);
            tvStore.setText(currentSale.storeName);

            //final ImageView imSalePic = view.findViewById(R.id.ivSalePic);
            /*
            if (listData == null) {
                MainModel.instance.GetListOfCitiesMallsAndStores(new MainModel.GetListOfCitiesMallsAndStoresListener() {
                    @Override
                    public void onGetListOfCitiesMallsANdStoresResults(ListData data) {

                        if (listData == null) {
                            listData = data;
                        }

                        SettingData(copyI,copyView,currentSale,tvCity,tvMall,tvStore);
                    }
                });
            }
            else {
                if (listData != null) {
                    this.SettingData(i, view, currentSale, tvCity, tvMall, tvStore);
                }
            }*/

            imSalePic = view.findViewById(R.id.ivSalePic);
            imSalePic.setImageResource(R.drawable.avatar);
            imSalePic.setTag(currentSale.id);

            if (currentSale.getPictureUrl() != null) {
                ImageModel.instance.getImage(currentSale.getPictureUrl(), new ImageModel.GetImageListener() {
                    @Override
                    public void onDone(Bitmap imageBitmap) {
                        if (currentSale.id.equals(imSalePic.getTag()) && imageBitmap != null) {
                            imSalePic.setImageBitmap(imageBitmap);
                        }
                    }
                });
            }
            return view;

        }


        public void SettingData(int i, View view, Sale currentSale, TextView tvCity, TextView tvMall, TextView tvStore)
        {
            Store storeData = new Store();
            Mall malData = new Mall();
            City cityData = new City();
            for(int nIndex = 0; nIndex < listData.stores.size();nIndex++)
            {
                if (currentSale.storeId == listData.stores.get(nIndex).id)
                {
                    storeData = listData.stores.get(nIndex);
                }
            }

            for(int nIndex = 0; nIndex < listData.malls.size();nIndex++)
            {
                if (storeData.mallId == listData.malls.get(nIndex).id)
                {
                    malData = listData.malls.get(nIndex);
                }
            }

            for(int nIndex = 0; nIndex < listData.cities.size();nIndex++)
            {
                if (malData.cityId == listData.cities.get(nIndex).id)
                {
                    cityData = listData.cities.get(nIndex);
                }
            }

            tvCity.setText(cityData.name);
            tvMall.setText(malData.name);
            tvStore.setText(storeData.name);
        }
    }
}

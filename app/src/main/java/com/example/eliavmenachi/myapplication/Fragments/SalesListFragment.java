package com.example.eliavmenachi.myapplication.Fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.eliavmenachi.myapplication.Entities.Consts;
import com.example.eliavmenachi.myapplication.Entities.ListData;
import com.example.eliavmenachi.myapplication.Entities.Sale;
import com.example.eliavmenachi.myapplication.Entities.Store;
import com.example.eliavmenachi.myapplication.Entities.Mall;
import com.example.eliavmenachi.myapplication.Entities.City;
import com.example.eliavmenachi.myapplication.Models.Image.ImageModel;
import com.example.eliavmenachi.myapplication.ViewModels.SaleListViewModel;
import com.example.eliavmenachi.myapplication.R;

import java.util.List;

public class SalesListFragment extends Fragment {
    // Data Members
    ListView list;
    SalesListFragment.ListAdapter listAdapter = new SalesListFragment.ListAdapter();;
    SaleListViewModel dataModel;
    ListData listData;
    ImageView imSalePic;
    String m_selectedStore = "-1";
    boolean m_bGetAllSales = true;
    int nCounterQuery = 0;
    View rlProgressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sale_list, container, false);
        nCounterQuery = 0;
        list = view.findViewById(R.id.lvSaleList);
        list.setAdapter(listAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("TAG","item selected:" + i);
            }
        });
        rlProgressBar = view.findViewById(R.id.fragment_sale_list_rlProgressBar);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Sale selectedSaleItem = dataModel.getDataByStoreId(m_bGetAllSales,m_selectedStore).getValue().get(i);

                int nId = 1;
                if (nId == 1) {
                    NewSaleFragment fragment = new NewSaleFragment();
                    Bundle args = new Bundle();
                    args.putString("SALE_ID", selectedSaleItem.id);
                    args.putString(Consts.instance.SALE_LIST_TYPE, Consts.instance.ALL);
                    fragment.setArguments(args);
                    FragmentTransaction tran = getActivity().getSupportFragmentManager().beginTransaction();
                    tran.replace(R.id.main_container, fragment);
                    tran.addToBackStack(null);
                    tran.commit();
                }
                else if (nId == 2) {
                    SaleDetailsFragment fragment = new SaleDetailsFragment();
                    Bundle args = new Bundle();
                    args.putString("SALE_ID", selectedSaleItem.id);
                    fragment.setArguments(args);
                    FragmentTransaction tran = getActivity().getSupportFragmentManager().beginTransaction();
                    tran.replace(R.id.main_container, fragment);
                    tran.addToBackStack(null);
                    tran.commit();
                }
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (getArguments() != null){
            m_selectedStore = getArguments().getString("STORE_ID");
            m_bGetAllSales = false;
        }
        else
        {
            m_selectedStore = "-1";
            m_bGetAllSales = true;
        }

        dataModel = ViewModelProviders.of(this).get(SaleListViewModel.class);
        dataModel.getDataByStoreId(m_bGetAllSales,m_selectedStore).observe(this, new Observer<List<Sale>>() {
            @Override
            public void onChanged(@Nullable List<Sale> sales) {
                //nCounterQuery++;
                //if (nCounterQuery >= 2) {
                    listAdapter.notifyDataSetChanged();
                    Log.d("TAG", "notifyDataSetChanged" + sales.size());
                    rlProgressBar.setVisibility(View.GONE);
                //}
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
            //if (nCounterQuery >= 2) {
                if (dataModel != null) {
                    LiveData<List<Sale>> data = dataModel.getDataByStoreId(m_bGetAllSales, m_selectedStore);
                    if (data != null) {
                        if (data.getValue() != null) {
                            nCount = data.getValue().size();
                        }
                    }
                }
            //}
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
            }
            //if (nCounterQuery >= 2) {
                final Sale currentSale = dataModel.getDataByStoreId(m_bGetAllSales, m_selectedStore).getValue().get(i);
                final int copyI = i;
                final View copyView = view;
                final TextView tvCity = view.findViewById(R.id.tvCity);
                final TextView tvMall = view.findViewById(R.id.tvMall);
                final TextView tvStore = view.findViewById(R.id.tvStore);
                final TextView tvDesc = view.findViewById(R.id.tvDescription);
                tvDesc.setText(currentSale.description);

                String strStoreId = currentSale.storeId + "";
                tvCity.setText(currentSale.cityName);
                tvMall.setText(currentSale.mallName);
                tvStore.setText(currentSale.storeName);

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
            //}

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

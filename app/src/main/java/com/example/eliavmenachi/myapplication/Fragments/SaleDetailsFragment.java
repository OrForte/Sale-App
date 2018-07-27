package com.example.eliavmenachi.myapplication.Fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.eliavmenachi.myapplication.Entities.City;
import com.example.eliavmenachi.myapplication.Entities.ListData;
import com.example.eliavmenachi.myapplication.Entities.Mall;
import com.example.eliavmenachi.myapplication.Entities.Sale;
import com.example.eliavmenachi.myapplication.Entities.Store;
import com.example.eliavmenachi.myapplication.Models.Image.ImageModel;
import com.example.eliavmenachi.myapplication.R;
import com.example.eliavmenachi.myapplication.ViewModels.CityMallAndStoreViewModel;
import com.example.eliavmenachi.myapplication.ViewModels.SaleListViewModel;

public class SaleDetailsFragment extends Fragment {

    ImageView imageSale;
    TextView etDescription;
    TextView etEndDate;
    TextView etTitle;
    TextView etCity;
    TextView etMall;
    TextView etStore;

    ListView list;
    CityMallAndStoreViewModel dataModel;
    SaleListViewModel dataModelSale;
    boolean bIsOccur = false;
    Sale currSale;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataModel = ViewModelProviders.of(this).get(CityMallAndStoreViewModel.class);
        dataModelSale = ViewModelProviders.of(this).get(SaleListViewModel.class);
        dataModel.getData().observe(this, new Observer<ListData>() {
            @Override
            public void onChanged(@Nullable ListData listData) {
                if (!bIsOccur) {
                    if (listData != null) {
                        if (listData.cities.size() != 0 && listData.malls.size() != 0&& listData.stores.size() != 0) {
                            bIsOccur = true;
                        }
                    }
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sale_details, container, false);

        imageSale = view.findViewById(R.id.sale_details_image);
        etDescription = view.findViewById(R.id.fragment_sale_details_etDescription);
        etEndDate = view.findViewById(R.id.fragment_sale_Details_etEndDate);
        etTitle = view.findViewById(R.id.fragment_sale_details_tvRegister);
        etCity = view.findViewById(R.id.fragment_sale_details_etCity);
        etMall = view.findViewById(R.id.fragment_sale_details_etMall);
        etStore = view.findViewById(R.id.fragment_sale_details_etStore);

        String nId ="";
        if (getArguments() != null){
            nId = getArguments().getString("SALE_ID");
            dataModelSale.GetSaleBySaleId(nId).observe(this, new Observer<Sale>() {
                @Override
                public void onChanged(@Nullable Sale sale) {
                    currSale = sale;

                    etTitle.setText("sale " + currSale.id);

                    // populate the data
                    PopulateTheView();
                }
            });
        }


        return view;
    }

    public void PopulateTheView() {
        etDescription.setText(currSale.description);
        etEndDate.setText(currSale.endDate);
        etCity.setText(currSale.cityName);
        etMall.setText(currSale.mallName);
        etStore.setText(currSale.storeName);

        imageSale.setImageResource(R.drawable.avatar);
        imageSale.setTag(currSale.id);
        if (currSale.getPictureUrl() != null) {
            ImageModel.instance.getImage(currSale.getPictureUrl(), new ImageModel.GetImageListener() {
                @Override
                public void onDone(Bitmap imageBitmap) {
                    if (currSale.id.equals(imageSale.getTag()) && imageBitmap != null) {
                        imageSale.setImageBitmap(imageBitmap);
                    }
                }
            });
        }
    }
}

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

import com.example.eliavmenachi.myapplication.Entities.ListData;
import com.example.eliavmenachi.myapplication.Entities.Sale;
import com.example.eliavmenachi.myapplication.Entities.User;
import com.example.eliavmenachi.myapplication.Entities.UserPreview;
import com.example.eliavmenachi.myapplication.Models.Image.ImageModel;
import com.example.eliavmenachi.myapplication.R;
import com.example.eliavmenachi.myapplication.ViewModels.CityMallAndStoreViewModel;
import com.example.eliavmenachi.myapplication.ViewModels.SaleListViewModel;
import com.example.eliavmenachi.myapplication.ViewModels.UserViewModel;

public class SaleDetailsFragment extends Fragment {

    public int HEIGHT = 600;
    public int WIDTH = 600;
    ImageView imageSale;
    TextView etDescription;
    TextView etEndDate;
    TextView etTitle;
    TextView etCity;
    TextView etMall;
    TextView etStore;
    TextView etUser;


    ListView list;
    CityMallAndStoreViewModel dataModel;
    SaleListViewModel dataModelSale;
    UserViewModel userDataModel;
    boolean bIsOccur = false;
    Sale currSale;
    View rlProgressBar;
    int nCounterQuery = 0;
    UserPreview currentUser;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataModel = ViewModelProviders.of(this).get(CityMallAndStoreViewModel.class);
        dataModelSale = ViewModelProviders.of(this).get(SaleListViewModel.class);
        userDataModel = ViewModelProviders.of(this).get(UserViewModel.class);
        dataModel.getData().observe(this, new Observer<ListData>() {
            @Override
            public void onChanged(@Nullable ListData listData) {
                if (!bIsOccur) {
                    if (listData != null) {
                        if (listData.cities.size() != 0 && listData.malls.size() != 0 && listData.stores.size() != 0) {
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
        nCounterQuery = 0;
        currSale = new Sale();
        imageSale = view.findViewById(R.id.sale_details_image);
        etDescription = view.findViewById(R.id.fragment_sale_details_etDescription);
        etEndDate = view.findViewById(R.id.fragment_sale_Details_etEndDate);
        etTitle = view.findViewById(R.id.fragment_sale_details_tvRegister);
        etCity = view.findViewById(R.id.fragment_sale_details_etCity);
        etMall = view.findViewById(R.id.fragment_sale_details_etMall);
        etStore = view.findViewById(R.id.fragment_sale_details_etStore);
        rlProgressBar = view.findViewById(R.id.fragment_sale_details_rlProgressBar);
        etUser = view.findViewById(R.id.fragment_sale_details_etUser);
        rlProgressBar.setVisibility(View.VISIBLE);
        String nId = "";
        if (getArguments() != null) {
            nId = getArguments().getString("SALE_ID");
            dataModelSale.GetSaleBySaleId(nId).observe(this, new Observer<Sale>() {
                @Override
                public void onChanged(@Nullable final Sale sale) {
                    nCounterQuery++;
                    if (nCounterQuery >= 2) {
                        if (sale != null) {
                            if (sale.id != null) {
                                LoadUserAndPopulateData(sale);
                            }
                        }
                    }
                }
            });
        }


        return view;
    }

    public void LoadUserAndPopulateData(final Sale sale)
    {
        /*
        userDataModel.getUserByUserId(sale.userId).observe( SaleDetailsFragment.this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                if (nCounterQuery >= 2) {
                    currSale = sale;
                    currentUser = user;
                    etTitle.setText("sale " + currSale.id);

                    // populate the data
                    PopulateTheView();
                }
            }
        });*/

        userDataModel.getUsersPreviewByUserId(sale.userId).observe(SaleDetailsFragment.this, new Observer<UserPreview>() {
            @Override
            public void onChanged(@Nullable UserPreview userPreview) {
                if (nCounterQuery >= 2) {
                    currSale = sale;
                    currentUser = userPreview;
                    etTitle.setText("sale " + currSale.id);

                    // populate the data
                    PopulateTheView();
                }
            }
        });
    }

    public void PopulateTheView() {
        if (currSale != null) {
            if (currSale.id != null) {
                etDescription.setText(currSale.description);
                etEndDate.setText(currSale.endDate);
                etCity.setText(currSale.cityName);
                etMall.setText(currSale.mallName);
                etStore.setText(currSale.storeName);
                if (currentUser != null) {
                    etUser.setText(currentUser.username);
                }
                else
                {
                    etUser.setText("");
                }

                imageSale.setImageResource(R.drawable.avatar);
                imageSale.setTag(currSale.id);
                rlProgressBar.setVisibility(View.GONE);
                if (currSale.getPictureUrl() != null) {
                    ImageModel.instance.getImage(currSale.getPictureUrl(), new ImageModel.GetImageListener() {
                        @Override
                        public void onDone(Bitmap imageBitmap) {
                            if (currSale.id.equals(imageSale.getTag()) && imageBitmap != null) {
                                imageBitmap = Bitmap.createScaledBitmap(imageBitmap, WIDTH, HEIGHT, true);
                                imageSale.setImageBitmap(imageBitmap);
                            }
                        }
                    });
                }
            }
        }
    }
}

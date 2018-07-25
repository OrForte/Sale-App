package com.example.eliavmenachi.myapplication.Fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.example.eliavmenachi.myapplication.Entities.City;
import com.example.eliavmenachi.myapplication.Entities.Consts;
import com.example.eliavmenachi.myapplication.Entities.ListData;
import com.example.eliavmenachi.myapplication.Entities.Mall;
import com.example.eliavmenachi.myapplication.Entities.Sale;
import com.example.eliavmenachi.myapplication.Entities.Store;
import com.example.eliavmenachi.myapplication.ViewModels.CityMallAndStoreViewModel;
import com.example.eliavmenachi.myapplication.Models.Image.ImageModel;
import com.example.eliavmenachi.myapplication.Models.Sale.SaleModel;
import com.example.eliavmenachi.myapplication.Models.ViewModels.SaleListViewModel;
import com.example.eliavmenachi.myapplication.R;

import android.widget.ArrayAdapter;

import static android.app.Activity.RESULT_OK;

public class NewSaleFragment extends Fragment {

    ListData listData = new ListData();
    Spinner dropDownCities;
    List<String> citiesNames;
    List<String> mallNames;
    List<String> storeNames;
    Spinner dropDownMalls;
    Spinner dropDownStores;
    Button btnSave;
    Button btnEditImage;
    int storeId;
    int mallId;
    int cityId;
    String selectedMallName;
    String selectedCityName;
    String selectedStoreName;
    Sale newSale;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Bitmap imageBitmap;
    ImageView imageSale;
    TextView etDescription;
    TextView etEndDate;
    ArrayAdapter<String> adapterCities;
    ArrayAdapter<String> adapterMalls;
    ArrayAdapter<String> adapterStores;
    boolean IsNewSale = true;

    ListView list;
    CityMallAndStoreViewModel dataModel;
    SaleListViewModel dataModelSale;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataModel = ViewModelProviders.of(this).get(CityMallAndStoreViewModel.class);
        dataModelSale = ViewModelProviders.of(this).get(SaleListViewModel.class);
        dataModel.getData().observe(this, new Observer<ListData>() {
            @Override
            public void onChanged(@Nullable ListData listData) {
                if (IsNewSale == true) {
                    SetListOfCities(listData);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_sale, container, false);
        dropDownCities = view.findViewById(R.id.fragment_new_sale_etCity);
        dropDownMalls = view.findViewById(R.id.fragment_new_sale_etMall);
        dropDownStores = view.findViewById(R.id.fragment_new_sale_etStore);
        btnSave = view.findViewById(R.id.fragment_new_sale_btnSaveSale);
        imageSale = view.findViewById(R.id.new_sale_image);
        etDescription = view.findViewById(R.id.fragment_new_sale_etDescription);
        etEndDate = view.findViewById(R.id.fragment_new_sale_etEndDate);

        String nId ="";
        if (getArguments() != null){
            nId = getArguments().getString("SALE_ID");
            IsNewSale = false;
            dataModelSale.GetSaleBySaleId(nId).observe(this, new Observer<Sale>() {
                @Override
                public void onChanged(@Nullable Sale sale) {
                    newSale = sale;

                    // populate the data
                    PopulateTheView();
                }
            });
        }

        btnSave.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view) {
                // TODO: need to save the data to firebase
                // TODO: add spinner to the loading of save
                if (newSale == null)
                {
                    newSale = new Sale();
                    newSale.id = "0";


                    String SeqName = "saleSeq";
                    SaleModel.instance.GetNextSequenceSale(SeqName, new SaleModel.GetNextSequenceListener() {
                        @Override
                        public void onGetNextSeq(String p_next) {
                            AddNewSaleToFireBase(p_next);
                        }
                    });
                }
                else{
                    int nId = 1;
                    //AddNewSaleToFireBase(newSale.id);
                }
            }
        });

        btnEditImage = view.findViewById(R.id.new_sale_img_btn);

        btnEditImage.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                // TODO: need to edit image
                //open camera
                Intent takePictureIntent = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);

                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE &&
                resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            imageSale.setImageBitmap(imageBitmap);
        }
    }

    public void GetToSaleListFragments()
    {
        FragmentManager fragmentManager = getFragmentManager();
        SalesListFragment fragment = new SalesListFragment();
        FragmentTransaction tran = getActivity().getSupportFragmentManager().beginTransaction();
        tran.replace(R.id.main_container, fragment);
        tran.addToBackStack(Consts.instance.TAG_SALES);
        tran.commit();
    }

    public void SetListOfCities(ListData data)
    {
        listData = new ListData();
        listData = data;

        // get the ciry names
        citiesNames = dataModel.GetCityNames(listData);

        // set the adaper
        adapterCities = SetAdapter(citiesNames);

        // set the drop down cities
        dropDownCities.setAdapter(adapterCities);

        dropDownCities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                OnSelectedCity(adapterView,view,position,l);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public void AddNewSaleToFireBase(String newId)
    {
        newSale.id = newId;
        // setting the details of sale
        newSale.description = etDescription.getText().toString();
        newSale.endDate = etEndDate.getText().toString();
        newSale.createdDate = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        newSale.storeId = storeId;
        newSale.cityId = cityId;
        newSale.mallId = mallId;
        newSale.mallName = selectedMallName;
        newSale.cityName = selectedCityName;
        newSale.storeName = selectedStoreName;
        //newSale.id = java.util.UUID.randomUUID().toString();

        // setting image details
        if (imageBitmap != null) {
            ImageModel.instance.saveImage(imageBitmap, new ImageModel.SaveImageListener() {
                @Override
                public void onDone(String url) {
                    newSale.pictureUrl = url;
                    SaleModel.instance.addPost(newSale);
                    GetToSaleListFragments();
                }
            });
        }
        else
        {
            SaleModel.instance.addPost(newSale);
            GetToSaleListFragments();
        }
    }

    public void OnSelectedCity(AdapterView<?> adapterView, View view, final int position, long l)
    {
        mallNames = new ArrayList<>();
        storeNames = new ArrayList<>();
        selectedCityName = adapterView.getItemAtPosition(position).toString();
        City selectedCity = dataModel.GetCityByCityName(selectedCityName, listData);
        if (selectedCity != null)
        {
            cityId = selectedCity.id;
        }
        mallNames = dataModel.GetMallNamesByCityId(selectedCity.id, listData);
        adapterMalls = SetAdapter(mallNames);
        dropDownMalls.setAdapter(adapterMalls);
        dropDownMalls.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                OnSelectedMall(adapterView,view,i,l);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public void OnSelectedMall(AdapterView<?> adapterView, View view, int position, long l)
    {
        storeNames = new ArrayList<>();
        selectedMallName = adapterView.getItemAtPosition(position).toString();
        Mall selectedMall = dataModel.GetMallByMallName(selectedMallName, listData);
        if (selectedMall != null)
        {
            mallId = selectedMall.id;
        }
        storeNames = dataModel.GetStoreNamesByMallId(selectedMall.id, listData);
        adapterStores = SetAdapter(storeNames);
        dropDownStores.setAdapter(adapterStores);
        dropDownStores.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                OnSelectedStore(adapterView,view,i,l);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public void OnSelectedStore(AdapterView<?> adapterView, View view, int position, long l)
    {
        selectedStoreName = adapterView.getItemAtPosition(position).toString();
        Store selectedStore = dataModel.GetStoreByStoreName(selectedStoreName,listData);
        if (selectedStore != null)
        {
            storeId = selectedStore.id;
        }
    }

    public ArrayAdapter<String> SetAdapter(List<String> collection)
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, collection);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        return adapter;
    }

    public void PopulateTheView()
    {
        // setting the desctiption
        etDescription.setText(newSale.description);

        // setting the end date
        etEndDate.setText(newSale.endDate);

        dataModel.getData().observe(this, new Observer<ListData>() {
            @Override
            public void onChanged(@Nullable ListData data) {
                //listData = new ListData();
                //listData = data;
                if (newSale != null) {
                    City city = dataModel.GetCityByCityId(newSale.cityId, data);
                    if (city != null) {
                        selectedCityName = city.name;
                    }
                    Mall mall = dataModel.GetMallByMallId(newSale.mallId, data);
                    if (mall != null) {
                        selectedMallName = mall.name;
                    }
                    Store store = dataModel.GetStoreByStoreId(newSale.storeId, data);
                    if (store != null) {
                        selectedStoreName = store.name;
                    }

                    if (selectedCityName != null && selectedStoreName != null && selectedMallName != null) {
                        SetListOfCities(data);
                    }
                    /*
                    citiesNames = dataModel.GetCityNames(listData);
                    adapterCities = SetAdapter(citiesNames);
                    dropDownCities.setAdapter(adapterCities);
                    int positionCity = adapterCities.getPosition(selectedCityName);
                    dropDownCities.setSelection(positionCity);

                    if (city != null) {
                        mallNames = dataModel.GetMallNamesByCityId(city.id, listData);
                        adapterMalls = SetAdapter(mallNames);
                        dropDownMalls.setAdapter(adapterMalls);
                        int positionMall = adapterMalls.getPosition(selectedMallName);
                        dropDownMalls.setSelection(positionMall);
                    }

                    if (store != null) {
                        storeNames = dataModel.GetStoreNamesByMallId(mall.id, listData);
                        adapterStores = SetAdapter(storeNames);
                        dropDownStores.setAdapter(adapterStores);
                        int positionStore = adapterStores.getPosition(selectedStoreName);
                        dropDownStores.setSelection(positionStore);
                    }
                    if (selectedCityName != null && selectedStoreName != null && selectedMallName != null) {
                        dropDownCities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                                OnSelectedCity(adapterView, view, position, l);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                            }
                        });
                    }*/
                }
            }
        });
    }
}

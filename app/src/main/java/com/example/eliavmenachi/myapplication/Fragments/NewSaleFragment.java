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
import java.util.Iterator;
import java.util.List;

import com.example.eliavmenachi.myapplication.Entities.City;
import com.example.eliavmenachi.myapplication.Entities.ListData;
import com.example.eliavmenachi.myapplication.Entities.Mall;
import com.example.eliavmenachi.myapplication.Entities.Sale;
import com.example.eliavmenachi.myapplication.Entities.Store;
import com.example.eliavmenachi.myapplication.Models.CityMallAndStore.CityMallAndStoreModel;
import com.example.eliavmenachi.myapplication.Models.ViewModels.CityMallAndStoreViewModel;
import com.example.eliavmenachi.myapplication.Models.Image.ImageModel;
import com.example.eliavmenachi.myapplication.Models.Sale.SaleModel;
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

    ListView list;
    CityMallAndStoreViewModel dataModel;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataModel = ViewModelProviders.of(this).get(CityMallAndStoreViewModel.class);
        dataModel.getData().observe(this, new Observer<ListData>() {
            @Override
            public void onChanged(@Nullable ListData listData) {
                SetListOfCities(listData);
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
        String nId ="";
        if (getArguments() != null){
            nId = getArguments().getString("SALE_ID");
        }

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_sale, container, false);
        dropDownCities = view.findViewById(R.id.fragment_new_sale_etCity);
        dropDownMalls = view.findViewById(R.id.fragment_new_sale_etMall);
        dropDownStores = view.findViewById(R.id.fragment_new_sale_etStore);
        btnSave = view.findViewById(R.id.fragment_new_sale_btnSaveSale);
        imageSale = view.findViewById(R.id.new_sale_image);
        etDescription = view.findViewById(R.id.fragment_new_sale_etDescription);
        etEndDate = view.findViewById(R.id.fragment_new_sale_etEndDate);

        btnSave.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view) {
                // TODO: need to save the data to firebase
                // TODO: add spinner to the loading of save
                newSale = new Sale();

                String SeqName = "saleSeq";
                SaleModel.instance.GetNextSequenceSale(SeqName, new SaleModel.GetNextSequenceListener() {
                    @Override
                    public void onGetNextSeq(String p_next) {
                        AddNewSaleToFireBase(p_next);
                    }
                });
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

    public void SetListOfCities(ListData data)
    {
        listData = new ListData();
        listData = data;

        // get the ciry names
        citiesNames = CityMallAndStoreModel.instance.GetCityNames(listData);

        // set the adaper
        ArrayAdapter<String> adapter = SetAdapter(citiesNames);

        // set the drop down cities
        dropDownCities.setAdapter(adapter);

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
                }
            });
        }
        else
        {
            SaleModel.instance.addPost(newSale);
        }
    }

    public void OnSelectedCity(AdapterView<?> adapterView, View view, final int position, long l)
    {
        mallNames = new ArrayList<>();
        storeNames = new ArrayList<>();
        selectedCityName = adapterView.getItemAtPosition(position).toString();
        City selectedCity = CityMallAndStoreModel.instance.GetCityByCityName(selectedCityName, listData);
        if (selectedCity != null)
        {
            cityId = selectedCity.id;
        }
        mallNames = CityMallAndStoreModel.instance.GetMallNamesByCityId(selectedCity.id, listData);
        ArrayAdapter<String> adapter = SetAdapter(mallNames);
        dropDownMalls.setAdapter(adapter);
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
        Mall selectedMall = CityMallAndStoreModel.instance.GetMallByMallName(selectedMallName, listData);
        if (selectedMall != null)
        {
            mallId = selectedMall.id;
        }
        storeNames = CityMallAndStoreModel.instance.GetStoreNamesByMallId(selectedMall.id, listData);
        ArrayAdapter<String> adapter = SetAdapter(storeNames);
        dropDownStores.setAdapter(adapter);
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
        Store selectedStore = CityMallAndStoreModel.instance.GetStoreByStoreName(selectedStoreName,listData);
        if (selectedStore != null)
        {
            storeId = selectedStore.id;
        }
    }

    /*
    public List<String> GetCityNames()
    {
        List<String> cities = new ArrayList<>();
        for (Iterator iterator = listData.cities.iterator(); iterator.hasNext();)
        {
            cities.add(((City) iterator.next()).name);
        }
        return cities;
    }

    public List<String> GetMallNamesByCityId(int cityId)
    {
        List<String> malls = new ArrayList<>();
        for (Iterator iterator = listData.malls.iterator(); iterator.hasNext();)
        {
            Mall mall = (Mall)iterator.next();
            if (mall.cityId == cityId)
            {
                malls.add(mall.name);
            }
        }
        return malls;
    }

    public List<String> GetStoreNamesByMallId(int mallId)
    {
        List<String> stores = new ArrayList<>();
        for (Iterator iterator = listData.stores.iterator(); iterator.hasNext();)
        {
            Store store = (Store)iterator.next();
            if (store.mallId == mallId)
            {
                stores.add(store.name);
            }
        }
        return stores;
    }

    public City GetCityByCityName(String selectedCityName)
    {
        for (Iterator iterator = listData.cities.iterator(); iterator.hasNext();)
        {
            City city = (City) iterator.next();
            if (selectedCityName == city.name)
            {
                return city;
            }
        }
        return null;
    }

    public Mall GetMallByMallName(String selectedMallName)
    {
        for (Iterator iterator = listData.malls.iterator(); iterator.hasNext();)
        {
            Mall mall = (Mall) iterator.next();
            if (selectedMallName == mall.name)
            {
                return mall;
            }
        }
        return null;
    }

    public Store GetStoreByStoreName(String selectedStoreName)
    {
        for (Iterator iterator = listData.stores.iterator(); iterator.hasNext();)
        {
            Store store = (Store) iterator.next();
            if (selectedStoreName == store.name)
            {
                return store;
            }
        }
        return null;
    }*/

    public ArrayAdapter<String> SetAdapter(List<String> collection)
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, collection);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        return adapter;
    }
}

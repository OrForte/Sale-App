package com.example.eliavmenachi.myapplication.Fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eliavmenachi.myapplication.Entities.City;
import com.example.eliavmenachi.myapplication.Entities.Consts;
import com.example.eliavmenachi.myapplication.Entities.ListData;
import com.example.eliavmenachi.myapplication.Entities.Mall;
import com.example.eliavmenachi.myapplication.Entities.Sale;
import com.example.eliavmenachi.myapplication.Entities.Store;
import com.example.eliavmenachi.myapplication.Entities.User;
import com.example.eliavmenachi.myapplication.Models.Image.ImageModel;
import com.example.eliavmenachi.myapplication.Models.Sale.SaleModel;
import com.example.eliavmenachi.myapplication.Models.User.UserModel;
import com.example.eliavmenachi.myapplication.R;
import com.example.eliavmenachi.myapplication.ViewModels.CityMallAndStoreViewModel;
import com.example.eliavmenachi.myapplication.ViewModels.SaleListViewModel;
import com.example.eliavmenachi.myapplication.ViewModels.UserViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class NewSaleFragment extends Fragment {

    public int HEIGHT = 600;
    public int WIDTH = 600;
    ListData listData = new ListData();
    Spinner dropDownCities;
    List<String> citiesNames;
    List<String> mallNames;
    List<String> storeNames;
    Spinner dropDownMalls;
    Spinner dropDownStores;
    Button btnSave;
    Button btnEditImage;
    Button btnCancelOrDelete;
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
    TextView title;
    ArrayAdapter<String> adapterCities;
    ArrayAdapter<String> adapterMalls;
    ArrayAdapter<String> adapterStores;
    boolean m_bIsChangedImage = false;

    ListView list;
    CityMallAndStoreViewModel dataModel;
    SaleListViewModel dataModelSale;
    UserViewModel userViewModel;
    boolean bIsOccur = false;
    boolean bUpdateMode = false;
    String m_SaleListTypeParams = "";
    int nCounterQuery = 0;
    View rlProgressBar;
    ProgressBar progressBarSaveNewSale;
    User currentUser;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        UserModel.instance.getCurrentUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                currentUser = user;
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

        bIsOccur = false;
        View view = inflater.inflate(R.layout.fragment_new_sale, container, false);
        dropDownCities = view.findViewById(R.id.fragment_new_sale_etCity);
        dropDownMalls = view.findViewById(R.id.fragment_new_sale_etMall);
        dropDownStores = view.findViewById(R.id.fragment_new_sale_etStore);
        btnSave = view.findViewById(R.id.fragment_new_sale_btnSaveSale);
        btnCancelOrDelete = view.findViewById(R.id.fragment_new_sale_btnCancelSale);
        imageSale = view.findViewById(R.id.new_sale_image);
        etDescription = view.findViewById(R.id.fragment_new_sale_etDescription);
        title = view.findViewById(R.id.fragment_register_tvRegister);
        rlProgressBar = view.findViewById(R.id.fragment_new_sale_rlProgressBar);
        dataModel = ViewModelProviders.of(this).get(CityMallAndStoreViewModel.class);
        dataModelSale = ViewModelProviders.of(this).get(SaleListViewModel.class);

        etEndDate = view.findViewById(R.id.fragment_new_sale_etEndDate);

        progressBarSaveNewSale = view.findViewById(R.id.fragment_new_sale_progress);
        progressBarSaveNewSale.setVisibility(View.GONE);
        dataModel.getData().observe(this, new Observer<ListData>() {
            @Override
            public void onChanged(@Nullable ListData listData) {
                if (!bIsOccur) {
                    if (listData != null) {
                        if (listData.cities.size() != 0 && listData.malls.size() != 0 && listData.stores.size() != 0) {
                            bIsOccur = true;
                            SetListOfCities(listData);
                        }
                    }
                }
            }
        });

        String nId = "";
        if (getArguments() != null) {
            bUpdateMode = true;
            nId = getArguments().getString("SALE_ID");
            m_SaleListTypeParams = getArguments().getString(Consts.instance.SALE_LIST_TYPE);
            dataModelSale.GetSaleBySaleId(nId).observe(this, new Observer<Sale>() {
                @Override
                public void onChanged(@Nullable Sale sale) {
                    nCounterQuery++;
                    if (nCounterQuery >= 2) {
                        newSale = sale;

                        title.setText("sale " + newSale.id);
                        btnSave.setText("update");
                        btnCancelOrDelete.setText("delete");
                        // populate the data
                        PopulateTheView();
                    }
                }
            });
        } else {
            rlProgressBar.setVisibility(View.GONE);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // TODO: need to save the data to firebase
                // TODO: add spinner to the loading of save
                progressBarSaveNewSale.setVisibility(View.VISIBLE);
                if (newSale == null) {
                    newSale = new Sale();
                    newSale.id = "0";

                    String SeqName = "saleSeq";
                    dataModelSale.GetNextSequenceSale(SeqName, new SaleModel.GetNextSequenceListener() {
                        @Override
                        public void onGetNextSeq(String p_next) {
                            AddNewSaleToFireBase(p_next);
                        }
                    });
                } else {
                    int nId = 1;
                    AddNewSaleToFireBase(newSale.id);
                }
            }
        });

        btnEditImage = view.findViewById(R.id.new_sale_img_btn);

        btnEditImage.setOnClickListener(new View.OnClickListener() {
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

        btnCancelOrDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (bUpdateMode == true) {
                    newSale.active = false;
                    dataModelSale.deleteLogicSale(newSale, new SaleModel.deleteLogicSaleListener() {
                        @Override
                        public void onDeleteLogicSale(boolean b_isDelete) {
                            if (b_isDelete == true) {
                                Toast.makeText(getActivity(), "delete sale successfully",
                                        Toast.LENGTH_LONG).show();
                                GetToSaleListFragments();
                            }
                        }
                    });
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE &&
                resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            imageBitmap = Bitmap.createScaledBitmap(imageBitmap, WIDTH, HEIGHT, true);
            imageSale.setImageBitmap(imageBitmap);
            m_bIsChangedImage = true;
        }
    }

    public void GetToSaleListFragments() {
        progressBarSaveNewSale.setVisibility(View.GONE);
        if (!bUpdateMode || m_SaleListTypeParams.equals(Consts.instance.ALL)) {
            FragmentManager fragmentManager = getFragmentManager();
            SalesListFragment fragment = new SalesListFragment();
            FragmentTransaction tran = getActivity().getSupportFragmentManager().beginTransaction();
            tran.replace(R.id.main_container, fragment);
            tran.addToBackStack(null);
            tran.commit();
        } else if (m_SaleListTypeParams.equals(Consts.instance.BY_USER)) {
            FragmentManager fragmentManager = getFragmentManager();
            UserSalesListFragment fragment = new UserSalesListFragment();
            FragmentTransaction tran = getActivity().getSupportFragmentManager().beginTransaction();
            tran.replace(R.id.main_container, fragment);
            tran.addToBackStack(null);
            tran.commit();
        }
    }

    public void SetListOfCities(ListData data) {
        listData = new ListData();
        listData = data;

        // get the ciry names
        citiesNames = dataModel.GetCityNames(listData);

        // set the adaper
        adapterCities = SetAdapter(citiesNames);

        // set the drop down cities
        dropDownCities.setAdapter(adapterCities);

        int positionCity = adapterCities.getPosition(selectedCityName);
        dropDownCities.setSelection(positionCity);

        dropDownCities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                OnSelectedCity(adapterView, view, position, l);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public void AddNewSaleToFireBase(String newId) {

        SetSaleData(newId);

        // TODO: need to change to view model...
        if (currentUser != null) {
            newSale.userId = currentUser.id;
        }
        // setting image details
        if (imageBitmap != null) {
            ImageModel.instance.saveImage(imageBitmap, new ImageModel.SaveImageListener() {
                @Override
                public void onDone(String url) {
                    newSale.pictureUrl = url;
                    addSaleToFireBase();
                }
            });
        } else {
            addSaleToFireBase();
        }
    }

    public void SetSaleData(String newId)
    {
        newSale.id = newId;
        newSale.description = etDescription.getText().toString();
        newSale.endDate = etEndDate.getText().toString();
        newSale.createdDate = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
        newSale.storeId = storeId;
        newSale.cityId = cityId;
        newSale.mallId = mallId;
        newSale.mallName = selectedMallName;
        newSale.cityName = selectedCityName;
        newSale.storeName = selectedStoreName;
        newSale.active = true;
    }

    public void addSaleToFireBase()
    {
        dataModelSale.addOrUpdateNewSale(newSale, new SaleModel.addOrUpdateNewSaleListener() {
            @Override
            public void onAddOrUpdateNewSaleResults(Sale SaleToReturn) {
                GetToSaleListFragments();
            }
        });
    }

    public void OnSelectedCity(AdapterView<?> adapterView, View view, final int position, long l) {
        mallNames = new ArrayList<>();
        storeNames = new ArrayList<>();
        selectedCityName = adapterView.getItemAtPosition(position).toString();
        City selectedCity = dataModel.GetCityByCityName(selectedCityName, listData);
        if (selectedCity != null) {
            cityId = selectedCity.id;
        }
        mallNames = dataModel.GetMallNamesByCityId(selectedCity.id, listData);
        adapterMalls = SetAdapter(mallNames);
        dropDownMalls.setAdapter(adapterMalls);
        int positionMall = adapterMalls.getPosition(selectedMallName);
        dropDownMalls.setSelection(positionMall);
        dropDownMalls.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                OnSelectedMall(adapterView, view, i, l);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public void OnSelectedMall(AdapterView<?> adapterView, View view, int position, long l) {
        storeNames = new ArrayList<>();
        selectedMallName = adapterView.getItemAtPosition(position).toString();
        Mall selectedMall = dataModel.GetMallByMallName(selectedMallName, listData);
        if (selectedMall != null) {
            mallId = selectedMall.id;
            storeNames = dataModel.GetStoreNamesByMallId(selectedMall.id, listData);
        }
        adapterStores = SetAdapter(storeNames);
        dropDownStores.setAdapter(adapterStores);
        int positionStore = adapterStores.getPosition(selectedStoreName);
        dropDownStores.setSelection(positionStore);
        dropDownStores.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                OnSelectedStore(adapterView, view, i, l);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public void OnSelectedStore(AdapterView<?> adapterView, View view, int position, long l) {
        selectedStoreName = adapterView.getItemAtPosition(position).toString();
        Store selectedStore = dataModel.GetStoreByStoreName(selectedStoreName, listData);
        if (selectedStore != null) {
            storeId = selectedStore.id;
        }
    }

    public ArrayAdapter<String> SetAdapter(List<String> collection) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, collection);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        return adapter;
    }

    public void PopulateTheView() {
        // setting the desctiption
        etDescription.setText(newSale.description);

        // setting the end date
        etEndDate.setText(newSale.endDate);

        if (!m_bIsChangedImage) {
            imageSale.setImageResource(R.drawable.avatar);
            imageSale.setTag(newSale.id);
            if (newSale.getPictureUrl() != null) {
                ImageModel.instance.getImage(newSale.getPictureUrl(), new ImageModel.GetImageListener() {
                    @Override
                    public void onDone(Bitmap imageBitmap) {
                        if (newSale.id.equals(imageSale.getTag()) && imageBitmap != null) {
                            imageBitmap = Bitmap.createScaledBitmap(imageBitmap, WIDTH, HEIGHT, true);
                            imageSale.setImageBitmap(imageBitmap);
                        }
                    }
                });
            }
        }

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

                    rlProgressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}

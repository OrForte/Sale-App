package com.example.eliavmenachi.myapplication.Fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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

    private static final String END_DATE = "END_DATE";
    private static final String TITLE = "TITLE";
    private static final String DESCRIPTION = "REPEAT_PASSWORD";
    private static final String PICTURE = "PICTURE";
    private static final String URL = "InstanceState.png";
    private static final String CITY = "CITY";
    private static final String MALL = "MALL";
    private static final String STORE = "STORE";

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
    TextView etTitle;
    View mainLayout;
    Bundle m_savedInstanceState;
    boolean bIsInstanceState = false;

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
        etTitle = view.findViewById(R.id.fragment_new_sale_etTitle);

        mainLayout = view.findViewById(R.id.fragment_new_sale_layout);

        progressBarSaveNewSale = view.findViewById(R.id.fragment_new_sale_progress);
        progressBarSaveNewSale.setVisibility(View.GONE);

        m_savedInstanceState = savedInstanceState;

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
                    if (sale != null) {
                        if (!m_bIsChangedImage && !bIsInstanceState) {
                            //nCounterQuery++;
                            //if (nCounterQuery >= 2) {
                            newSale = sale;

                            title.setText("sale " + newSale.id);
                            btnSave.setText("update");
                            btnCancelOrDelete.setText("delete");
                            // populate the data
                            PopulateTheView();
                            LoadDataAfterInstanceState(m_savedInstanceState);
                            //}
                        }
                    }
                }
            });
        } else {
            btnCancelOrDelete.setText("Cancel");
            rlProgressBar.setVisibility(View.GONE);
            LoadDataAfterInstanceState(savedInstanceState);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // TODO: need to save the data to firebase
                // TODO: add spinner to the loading of save

                progressBarSaveNewSale.setVisibility(View.VISIBLE);
                if (isConnected()) {
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
                    if (isConnected()) {
                        dataModelSale.deleteLogicSale(newSale, new SaleModel.deleteLogicSaleListener() {
                            @Override
                            public void onDeleteLogicSale(boolean b_isDelete) {
                                if (b_isDelete == true) {
                                    Toast.makeText(getActivity(), "Sale deleted successfully.",
                                            Toast.LENGTH_LONG).show();
                                    GetToSaleListFragments();
                                }
                            }
                        });
                    }
                } else {
                    getFragmentManager().popBackStack();
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
        try {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);

        } catch (Exception e) {

        }

        progressBarSaveNewSale.setVisibility(View.GONE);

        getFragmentManager().popBackStack();
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

        if (m_savedInstanceState != null) {
            String cityString = m_savedInstanceState.getString(CITY);
            if (cityString != null) {
                int cityInt = Integer.parseInt(cityString);
                if (cityInt >= dropDownCities.getAdapter().getCount()) {
                    cityInt = 0;
                }

                dropDownCities.setSelection(cityInt, true);
            }
        }
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

    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            try {
                return true;
            } catch (Exception e) {
                Toast.makeText(getActivity(), "internet not connected",
                        Toast.LENGTH_LONG).show();
                return false;
            }
        } else {
            Toast.makeText(getActivity(), "internet not connected",
                    Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public void SetSaleData(String newId) {
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
        newSale.title = etTitle.getText().toString();
    }

    public void addSaleToFireBase() {
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

        if (m_savedInstanceState != null) {
            String mallString = m_savedInstanceState.getString(MALL);
            if (mallString != null) {
                int mallInt = Integer.parseInt(mallString);
                if (mallInt >= dropDownMalls.getAdapter().getCount()) {
                    mallInt = 0;
                }

                dropDownMalls.setSelection(mallInt, true);
            }
        }
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

        if (m_savedInstanceState != null) {
            String storeString = m_savedInstanceState.getString(STORE);
            if (storeString != null) {
                int storeInt = Integer.parseInt(storeString);
                if (storeInt >= dropDownStores.getAdapter().getCount()) {
                    storeInt = 0;
                }

                dropDownStores.setSelection(storeInt, true);
            }
        }
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

        etTitle.setText(newSale.title);

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

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString(TITLE, etTitle.getText().toString());
        bundle.putString(DESCRIPTION, etDescription.getText().toString());
        bundle.putString(END_DATE, etEndDate.getText().toString());
        bundle.putString(CITY, dropDownCities.getSelectedItemPosition() + "");
        bundle.putString(MALL, dropDownMalls.getSelectedItemPosition() + "");
        bundle.putString(STORE, dropDownStores.getSelectedItemPosition() + "");

        ImageModel.instance.saveImageToFile(imageBitmap, URL);
    }

    public void LoadDataAfterInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            String title = savedInstanceState.getString(TITLE);
            if (title != null) {
                etTitle.setText(title);
            }
            String desc = savedInstanceState.getString(DESCRIPTION);
            if (desc != null) {
                etDescription.setText(desc);
            }
            String endDate = savedInstanceState.getString(END_DATE);
            if (endDate != null) {
                etEndDate.setText(endDate);
            }
            Bitmap bitMap = ImageModel.instance.loadImageFromFile(URL);
            if (bitMap != null) {
                imageSale.setImageBitmap(bitMap);
                imageBitmap = bitMap;
                ImageModel.instance.DeleteImage(URL);
            }

            m_savedInstanceState = savedInstanceState;
            bIsInstanceState = true;
        }
    }
}

package com.promoteprovider.demotodayvalue;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.SyncStateContract;
import android.view.View;

import com.google.android.gms.common.internal.Constants;
import com.google.firebase.firestore.FirebaseFirestore;
import com.promoteprovider.demotodayvalue.Storages.MySharedPreferences;
import com.promoteprovider.demotodayvalue.databinding.ActivityUserBinding;

public class User_Activity extends AppCompatActivity {

    private ActivityUserBinding binding;
    private PreferenceManager preferenceManager;
    @SuppressWarnings("MoveFieldAssignmentToInitializer")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }





    private void showErrorMessage(){

        binding.textErrorMessage.setText(String.format("%s","No User available"));
        binding.textErrorMessage.setVisibility(View.VISIBLE);
    }

    private void loading(Boolean isLoading){
        if(isLoading){
            binding.progressBAr.setVisibility(View.VISIBLE);
        }else {
            binding.progressBAr.setVisibility(View.INVISIBLE);
        }
    }
}
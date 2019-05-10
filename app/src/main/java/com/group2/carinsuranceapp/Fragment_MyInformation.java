package com.group2.carinsuranceapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Fragment_MyInformation extends Fragment {

    private TextView name;
    private TextView surname;
    private TextView birthdate;
    private TextView sex;
    private Button editButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_information,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        name = view.findViewById(R.id.text_first_name_myinfo_changable);
        surname = view.findViewById(R.id.text_last_name_myinfo_changeable);
        birthdate = view.findViewById(R.id.text_birthdate_myinfo_changeable);
        sex = view.findViewById(R.id.text_sex_myinfo_changeable);
        editButton = view.findViewById(R.id.edit_button);
    }
}

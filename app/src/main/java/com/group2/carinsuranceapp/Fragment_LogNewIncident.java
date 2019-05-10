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

public class Fragment_LogNewIncident extends Fragment {

    private Button submitButton;
    private EditText incidentDateField;
    private EditText incidentDescriptionField;
    private EditText incidentTimeField;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_log_new_incident,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        submitButton = view.findViewById(R.id.b_submit);
        incidentDateField = view.findViewById(R.id.field_incident_date);
        incidentDescriptionField = view.findViewById(R.id.field_incident_description);
        incidentTimeField= view.findViewById(R.id.field_incident_time);
        //TODO location input field

    }
}

/*
    Copyright 2019 Sebastian Merkt

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

package io.github.sebmerkt.everydayconverter;

import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;


public class ConvertUnitsLength extends ConvertUnitsBase implements AdapterView.OnItemSelectedListener {

    UsersAdapter adapter;
    ListView listView;

    //Conversion factors to go from {nm -> um, um -> mm, mm -> cm, cm -> m, m -> km, km -> in, in -> ft, ft -> yd, yd -> mi}
    double[] convFactors = {1000.0, 1000.0, 10.0, 100.0, 1000.0, 0.0000254, 12.0, 3.0, 1760.0};

    //Size of conversion matrix
    int convMatrixDim = convFactors.length+1;
    double[][] convMatrix = new double[convMatrixDim][convMatrixDim];

    //Default EditText value
    double inputValue = 1.0;
    //Default output unit
    int selectedUnit = 0;
    //Results to be displayed
    double[] outputValues = new double[convMatrixDim];

    //Initialize unit data
    UnitData[] lengthData;
    String[] units = new String[]{""};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.convert_units_base);

        Resources res = getResources();
        if(useAbbrev) {
            units = res.getStringArray(R.array.length_units_abbrev);
        } else {
            units = res.getStringArray(R.array.length_units);
        }
        //Initialize length units
        lengthData = new UnitData[units.length];
        //Initialize conversion
        updateOutputValues(outputValues, inputValue, convMatrix, selectedUnit);
        fillMatrix (convMatrix, convFactors);

        // Construct the data source
        ArrayList<UnitData> arrayOfItems = new ArrayList<>();
        // Create the adapter to convert the array to views
        adapter = new UsersAdapter(this, arrayOfItems);
        // Attach the adapter to a ListView
        listView = findViewById(R.id.lv_convert_units_results);
        updateUnitData(units, lengthData, adapter, outputValues);
        listView.setAdapter(adapter);

        //Initialize EditText; to input values
        final EditText editText = findViewById(R.id.et_conv_number);
        editText.setText(String.valueOf(inputValue));
        editText.setSelection(editText.getText().length());



        // EditText: Listen for user input of the EditText and update the results list
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // clear the current view
                adapter.clear();
                // If EditText is empty, display zero
                // If EditText is not empty, calculate new result and fill list of results
                if(editText.getText().toString().trim().equals("") || editText.getText().toString().trim().equals(".") || editText.getText().toString().trim().equals(",") || editText.getText().toString().trim().equals("-")) {
                    outputValues = resetOutputValues(outputValues, convMatrixDim);
                } else {
                    // Get value of EditText
                    inputValue = Double.valueOf(editText.getText().toString());
                    // Update all result values
                    updateOutputValues(outputValues, inputValue, convMatrix, selectedUnit);
                }
                updateUnitData(units, lengthData, adapter, outputValues);
                listView.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

        });

        int stringArrayUnits;
        if(useAbbrev) {
            stringArrayUnits = R.array.length_units_abbrev;
        } else{
            stringArrayUnits = R.array.length_units;
        }

        String metric_string=getString(R.string.string_metric);
        String unitSelector =
                PreferenceManager.getDefaultSharedPreferences(ConvertUnitsLength.this)
                        .getString("default_units", metric_string);

        int stringLengthDefault;
        if(unitSelector.equals(metric_string)){
            if(useAbbrev) {
                stringLengthDefault = R.string.string_meter_abbrev;
            }
            else {
                stringLengthDefault = R.string.string_meter;
            }
        }
        else {
            if(useAbbrev) {
                stringLengthDefault = R.string.string_foot_abbrev;
            }
            else {
                stringLengthDefault = R.string.string_foot;
            }
        }



        // Spinner for base unit selection
        Spinner spinner = findViewById(R.id.spinner_select_unit);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                stringArrayUnits, R.layout.unit_spinner_item);
        // Specify the layout to use when the list of choices appears
//        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAdapter.setDropDownViewResource(R.layout.unit_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(spinnerAdapter);


        spinner.setSelection(getIndex(spinner, getString(stringLengthDefault)));
        spinner.setOnItemSelectedListener(this);

    }

    //Update spinner information:
    // User selects different unit
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // Clear results list
        adapter.clear();
        // Update selected unit
        selectedUnit = pos;
        // Update results list
        updateOutputValues(outputValues, inputValue, convMatrix, selectedUnit);
        updateUnitData(units, lengthData, adapter, outputValues);
    }

    public void onNothingSelected(AdapterView<?> parent) {
    }



}

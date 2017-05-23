package com.safeandsound.app.safeandsound.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.safeandsound.app.safeandsound.R;
import com.safeandsound.app.safeandsound.controller.database.SQLiteHandler;
import com.safeandsound.app.safeandsound.controller.ruleengine.IfStatement;
import com.safeandsound.app.safeandsound.controller.ruleengine.Rule;
import com.safeandsound.app.safeandsound.controller.ruleengine.ThenStatement;
import com.safeandsound.app.safeandsound.model.User;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by louisapabst on 16.05.17.
 */

public class RuleActivity extends FragmentActivity {

    private Button btnAddRule;
    private SQLiteHandler db;
    private int countID = 0;
    private String userid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rules);

        // Android interne SQLite Datenbank wird instanziiert
        db = new SQLiteHandler(getApplicationContext());

        HashMap<String, String> user = db.getUserDetails();
        userid = user.get("user_id");
        getUserRules(userid);

        //Get Add Sensor button
        btnAddRule = (Button) findViewById(R.id.btn_addRule);

        btnAddRule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set components for view to get user input
                final LinearLayout ll = new LinearLayout(RuleActivity.this);
                ll.setOrientation(LinearLayout.VERTICAL);
                ll.setPadding(50, 50, 50, 50);

                LayoutInflater lii = LayoutInflater.from(RuleActivity.this);
                final LinearLayout ll2 = (LinearLayout) lii.inflate(R.layout.addrule, null);
                ll.addView(ll2);

                final Spinner spinner = (Spinner) ll2.findViewById(R.id.comparisonTypeSpinner);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selectedType = spinner.getSelectedItem().toString();
                        LayoutInflater li = LayoutInflater.from(RuleActivity.this);
                        if(selectedType.equals("is equal") || selectedType.equals("is greater") || selectedType.equals("is smaller")){
                            EditText btn = (EditText) ll2.findViewById(R.id.secondComparisonData);
                            btn.setVisibility(View.INVISIBLE);
                        }else if(selectedType.equals("is between")) {
                            EditText btn = (EditText) ll2.findViewById(R.id.secondComparisonData);
                            btn.setVisibility(View.VISIBLE);
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                AlertDialog.Builder dialog = new AlertDialog.Builder(RuleActivity.this);
                dialog.setTitle("Add a new rule");
                dialog.setView(ll);
                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Spinner spDataType = (Spinner) ll2.findViewById(R.id.dataTypeSpinner);
                        String dataType = spDataType.getSelectedItem().toString();

                        Spinner spComparisonType = (Spinner) ll2.findViewById(R.id.comparisonTypeSpinner);
                        String comparisonType = spComparisonType.getSelectedItem().toString();

                        EditText ed_firstComparisonData = (EditText) ll2.findViewById(R.id.firstComparisonData);
                        String comparisonData = ed_firstComparisonData.getText().toString();

                        if(comparisonType.equals("is between")){
                            EditText ed_secondComparisonData = (EditText) ll2.findViewById(R.id.secondComparisonData);
                            String secondComparisonData = ed_secondComparisonData.getText().toString();
                            comparisonData = comparisonData + "," + secondComparisonData;
                        }
                        IfStatement newIfStatement = new IfStatement(dataType, comparisonType, comparisonData);

                        EditText ed_actionText = (EditText) ll2.findViewById(R.id.actionText);
                        String thenText = ed_actionText.getText().toString();
                        ThenStatement newThenStatement = new ThenStatement(thenText, null);
                        Rule newRule = new Rule(newIfStatement, newThenStatement);
                        //TODO: PHP File für Datenbank Speicherung auf RP
                        db.addIfStatement(countID, dataType, comparisonType, comparisonData);
                        db.addThenStatement(countID, thenText, null);
                        countID++;
                    }
                })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // Do nothing.
                            }
                        }).show();
            }
        });
        }

    private void getUserRules(String userID){
        List<Rule> rules = db.getRules(userID);
    }
    }


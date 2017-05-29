package com.safeandsound.app.safeandsound.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.safeandsound.app.safeandsound.R;
import com.safeandsound.app.safeandsound.model.database.SQLiteHandler;
import com.safeandsound.app.safeandsound.model.ruleengine.IfStatement;
import com.safeandsound.app.safeandsound.model.ruleengine.Rule;
import com.safeandsound.app.safeandsound.model.ruleengine.ThenStatement;

import java.util.ArrayList;
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
    int count = 0;
    //private int countID = 0;
    private String userid;
    private HashMap<String, String> map_comparisonTypes;
    private HashMap<String, String> map_conjunctionTypes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rules);

        // Android interne SQLite Datenbank wird instanziiert
        db = new SQLiteHandler(getApplicationContext());

        HashMap<String, String> user = db.getUserDetails();
        userid = user.get("user_id");
        List<Rule> rules = getUserRules(userid);

        //Get Add Sensor button
        btnAddRule = (Button) findViewById(R.id.btn_addRule);

        setComparisonHashMap();
        setConjunctionHashMap();

        showAllRules(rules);

        btnAddRule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = 0;
                final HashMap<Integer, LinearLayout > map_layout = new HashMap<Integer, LinearLayout>();

                // Set components for view to get user input
                final LinearLayout ll = new LinearLayout(RuleActivity.this);
                ll.setOrientation(LinearLayout.VERTICAL);
                ll.setPadding(50, 50, 50, 50);

                LayoutInflater lii = LayoutInflater.from(RuleActivity.this);
                final LinearLayout ll2 = (LinearLayout) lii.inflate(R.layout.addrule, null);
                final ImageButton button_addIf = (ImageButton) ll2.findViewById(R.id.button_addif);
                final LinearLayout ll_addIf = (LinearLayout) ll2.findViewById(R.id.llifs);
                button_addIf.setVisibility(View.VISIBLE);
                map_layout.put(count, ll_addIf);
                count++;
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

                button_addIf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LayoutInflater li = LayoutInflater.from(RuleActivity.this);
                        LinearLayout ll = (LinearLayout) ll2.findViewById(R.id.llifs);
                        LinearLayout ll_addif = (LinearLayout) li.inflate(R.layout.addif, null);
                        map_layout.put(count, ll_addif);
                        ll.addView(ll_addif);
                        count++;
                        if (count == 3){
                            button_addIf.setVisibility(View.INVISIBLE);
                        }
                    }
                });

                AlertDialog.Builder dialog = new AlertDialog.Builder(RuleActivity.this);
                dialog.setTitle("Add a new rule");
                dialog.setView(ll);
                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        List<IfStatement> ifStatements = new ArrayList<IfStatement>();
                        List<ThenStatement> thenStatements = new ArrayList<ThenStatement>();
                        String conjunction = "";
                        for(int i = 0; i < count; i++) {
                            LinearLayout linearLayout = map_layout.get(i);
                            Spinner spDataType = (Spinner) linearLayout.findViewById(R.id.dataTypeSpinner);
                            String dataType = spDataType.getSelectedItem().toString().toLowerCase();

                            Spinner spComparisonType = (Spinner) linearLayout.findViewById(R.id.comparisonTypeSpinner);
                            String comparisonType = spComparisonType.getSelectedItem().toString();

                            EditText ed_firstComparisonData = (EditText) linearLayout.findViewById(R.id.firstComparisonData);
                            String comparisonData = ed_firstComparisonData.getText().toString();

                            if (comparisonType.equals("is between")) {
                                EditText ed_secondComparisonData = (EditText) linearLayout.findViewById(R.id.secondComparisonData);
                                String secondComparisonData = ed_secondComparisonData.getText().toString();
                                comparisonData = comparisonData + ";" + secondComparisonData;
                            }
                            if(i > 0){
                                Spinner spConjunction = (Spinner) linearLayout.findViewById(R.id.conjunctionSpinner);
                                conjunction = spConjunction.getSelectedItem().toString();
                                conjunction = map_conjunctionTypes.get(conjunction);
                            }
                            comparisonType = map_comparisonTypes.get(comparisonType);
                            IfStatement newIfStatement = new IfStatement(dataType, comparisonType, comparisonData, conjunction);
                            ifStatements.add(newIfStatement);
                        }
                        EditText ed_actionText = (EditText) ll2.findViewById(R.id.actionText);
                        String thenText = ed_actionText.getText().toString();
                        ThenStatement newThenStatement = new ThenStatement(thenText, null, null);
                        thenStatements.add(newThenStatement);
                        Rule newRule = new Rule(ifStatements, thenStatements);
                        //TODO: PHP File für Datenbank Speicherung auf RP
                        db.addRule(userid, ifStatements, thenStatements);
                        //countID++;
                        finish();
                        startActivity(getIntent());
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

    private List<Rule> getUserRules(String userID){
        List<Rule> rules = db.getRules(userID);
        return rules;
    }

    /**
     * Alle Regeln werden auf dem User Interface angezeigt
     * @param rules
     */
    public void showAllRules(List<Rule> rules){
        LinearLayout ll = (LinearLayout) findViewById(R.id.ruleLayout);
        for (int r = 0; r < rules.size(); r++){
            Rule currentRule = rules.get(r);
            String s_rule = "If ";
            List<IfStatement> ifStatements = currentRule.getIfStatements();

            for (int i = 0; i < ifStatements.size(); i++ ){
                IfStatement currentIfStatement = ifStatements.get(i);
                String conjunction = currentIfStatement.getConjunction();
                String comparisiontype = currentIfStatement.getComparisonType();
                String comparisondata = currentIfStatement.getComparisonData();
                Iterator itConjunction = map_conjunctionTypes.entrySet().iterator();
                while (itConjunction.hasNext()) {
                    Map.Entry entry = (Map.Entry) itConjunction.next();
                    if(entry.getValue().equals(conjunction)){
                        conjunction = entry.getKey().toString();
                    }

                }
                Iterator itComparison = map_comparisonTypes.entrySet().iterator();
                while (itComparison.hasNext()) {
                    Map.Entry entry = (Map.Entry) itComparison.next();
                    if(entry.getValue().equals(comparisiontype)){
                        comparisiontype = entry.getKey().toString();
                    }

                }
                if(comparisondata.contains(";")){
                    String[] c = comparisondata.split(";");
                    comparisondata = c[0] + " and " + c[1];
                }
                s_rule += conjunction + " " + currentIfStatement.getDataType() + " " + comparisiontype + " " + comparisondata + " \r\n";
            }
            s_rule += "then ";
            List<ThenStatement> thenStatements = currentRule.getThenStatements();
            for (int i = 0; i < thenStatements.size(); i++ ){
                ThenStatement currentThenStatement = thenStatements.get(i);
                s_rule += currentThenStatement.getThenType() + " " + currentThenStatement.getThenText() + " " + currentThenStatement.getConjunction() + " \r\n";
            }
            s_rule = s_rule.replace("null", "");
            LayoutInflater lii = LayoutInflater.from(RuleActivity.this);
            LinearLayout ll2 = (LinearLayout) lii.inflate(R.layout.singlerule, null);
            TextView tv = (TextView) ll2.findViewById(R.id.ruleText);
            tv.setText(s_rule);
            ll.addView(ll2);
        }

    }

    public void setComparisonHashMap(){
        map_comparisonTypes = new HashMap<String, String>();
        map_comparisonTypes.put("is equal", "==");
        map_comparisonTypes.put("is greater", ">");
        map_comparisonTypes.put("is smaller", "<");
        map_comparisonTypes.put("is between", ">;<");
    }
    public void setConjunctionHashMap(){
        map_conjunctionTypes = new HashMap<String, String>();
        map_conjunctionTypes.put("AND", "&&");
        map_conjunctionTypes.put("OR", "||");
    }
}


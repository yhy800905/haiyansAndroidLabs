package algonquin.cst2335.haiyan0348.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import algonquin.cst2335.haiyan0348.R;
import algonquin.cst2335.haiyan0348.data.MainViewModel;
import algonquin.cst2335.haiyan0348.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private MainViewModel model;
    private ActivityMainBinding variableBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new ViewModelProvider(this).get(MainViewModel.class);

        variableBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(variableBinding.getRoot());

        model.editString.observe(this, s ->
                variableBinding.textview.setText("Your edit text has" + s));

        variableBinding.myButton.setOnClickListener( click ->
                model.editString.postValue(variableBinding.myEdittext.getText().toString()));




//        TextView theText = variableBinding.textview;
//        Button myButton = variableBinding.myButton;
//        EditText myEdit = variableBinding.myEdittext;
//        CheckBox myCheckbox = variableBinding.myCheckbox;
//        Switch mySwitch = variableBinding.mySwitch;

//        myCheckbox.setOnCheckedChangeListener((a, b) -> {
//            theText.setText("The switch is on?" + b);
//
//        });

//        theText.setText(model.editString);
//        myButton.setText(model.buttonText);


        //variableBinding.myEdittext.setText(model.editString);




       // TextView myText = findViewById(R.id.textview);
        //Button btn = findViewById(R.id.myButton);
        //EditText myEdit = findViewById(R.id.myEdittext);

    }
}
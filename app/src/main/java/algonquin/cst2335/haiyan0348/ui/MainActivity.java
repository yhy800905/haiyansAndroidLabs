package algonquin.cst2335.haiyan0348.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

        //variableBinding.myEdittext.setText(model.editString);

        variableBinding.myButton.setOnClickListener( (click) -> {
            //model.editString.postValue(variableBinding.myEdittext.getText().toString());

            model.editString.observe(this, s -> {

            variableBinding.myEdittext.setText("Your edit text has" + s);

                });
            //variableBinding.myEdittext.setText("Your edit text is:" +model.editString);
        } );

       // TextView myText = findViewById(R.id.textview);

        //Button btn = findViewById(R.id.myButton);

        //EditText myEdit = findViewById(R.id.myEdittext);

    }
}
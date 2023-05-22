package algonquin.cst2335.haiyan0348.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.os.VibrationAttributes;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import algonquin.cst2335.haiyan0348.R;
import algonquin.cst2335.haiyan0348.data.MainViewModel;
import algonquin.cst2335.haiyan0348.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private MainViewModel model;
    private ActivityMainBinding variableBinding;
    private boolean selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new ViewModelProvider(this).get(MainViewModel.class);

        variableBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(variableBinding.getRoot());

        model.editString.observe(this, s ->
                variableBinding.textview.setText("Your edit text has" + s));

        variableBinding.myButton.setOnClickListener(click ->
                model.editString.postValue(variableBinding.myEdittext.getText().toString()));

        model.isSelected.observe(this, selected -> {
            variableBinding.myCheckbox.setChecked(selected);
            variableBinding.myRadioButton.setChecked(selected);
            variableBinding.mySwitch.setChecked(selected);
        });

        variableBinding.myCheckbox.setOnCheckedChangeListener((btn, isChecked) -> {
            model.isSelected.postValue(isChecked);
            Toast.makeText(getApplicationContext(), "The value is now: " + isChecked, Toast.LENGTH_SHORT).show();
        });

        variableBinding.myRadioButton.setOnCheckedChangeListener((btn, isChecked) -> {
            model.isSelected.postValue(isChecked);
        });

        variableBinding.mySwitch.setOnCheckedChangeListener((btn, isChecked) -> {
            model.isSelected.postValue(isChecked);
        });

        variableBinding.myImageView.setOnClickListener(click -> {
            variableBinding.textview.setText("You clicked the picture");
        });

        variableBinding.myImageButton.setOnClickListener(click -> {
            Toast.makeText(getApplicationContext(), "The width = " + variableBinding.myImageButton.getWidth() +
                    "The height = " + variableBinding.myImageButton.getHeight(), Toast.LENGTH_SHORT).show();

        });
    }
}



package algonquin.cst2335.haiyan0348;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import algonquin.cst2335.haiyan0348.databinding.ActivitySecondBinding;

public class SecondActivity extends AppCompatActivity {
    private ActivitySecondBinding binding;
    private ImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySecondBinding.inflate(getLayoutInflater());
        //loads the XML file  /res/layout/activity_second.xml
        setContentView(binding.getRoot());


        Intent fromPrevious = getIntent();
        //null if EMAIL is not found
        String emailAddress = fromPrevious.getStringExtra("EmailAddress");
        String day = fromPrevious.getStringExtra("DAY");
        int age = fromPrevious.getIntExtra("AGE", 0);
        int something = fromPrevious.getIntExtra("SOMETHING", 0);//default is for when SOMETHING is not there

        binding.textView3.setText("Welcome back " + emailAddress);


        binding.callNumberButton.setOnClickListener((v) -> {
            String phoneNumber = ""; // Get the phone number from the EditText or any other source
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(callIntent);
        });


        ActivityResultLauncher<Intent> cameraResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override //the camera has disappeared
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData(); // our picture is in here

                            Bitmap thumbnail = data.getParcelableExtra("data");

                            FileOutputStream fOut = null;

                            File file = new File( getFilesDir(), "Picture.png");

                            if(file.exists())
                            {
                                int i = 0;
                            }

                            try {
                                fOut = openFileOutput("Picture.png", Context.MODE_PRIVATE);

                                thumbnail.compress(Bitmap.CompressFormat.PNG, 100, fOut);

                                fOut.flush();

                                fOut.close();

                            }
                            catch(IOException ioe) {}

                            int i = 0;
                        }
                    }
                }
        );
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        binding.changePictureButton.setOnClickListener( click -> {
            //use apps on the phone
            cameraResult.launch(cameraIntent);
            //should return a picture
        });




        binding.goBackButton.setOnClickListener((v) -> {
            //opposite of startActivity()
            finish(); //go back to previous
        });

    }
}
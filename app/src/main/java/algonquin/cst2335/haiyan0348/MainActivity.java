package algonquin.cst2335.haiyan0348;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/** This is the main activity of the application.
 * This activity allows users to enter a password and checks its complexity against specific requirements.
 * If the password meets the requirements, a success message is displayed. Otherwise, an error message is shown.
 * @author Haiyan Yang
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    /**
     * This holds the "Type your Password" text view
     */
    protected TextView theText;


    /**
     * This holds the "Login" button
     */
    protected Button myButton;


    /**
     * This holds the edit text for typing into
     */
    protected EditText theEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //calling onCreate from parent class
        super.onCreate(savedInstanceState);
        //loads an XML file on the page
        setContentView(R.layout.activity_main);

        theText = findViewById(R.id.textView);
        myButton = findViewById(R.id.button);
        theEditText = findViewById(R.id.theEditText);

        myButton.setOnClickListener(click -> {
            String password = theEditText.getText().toString();

            checkPasswordComplexity(password);
        });
    }

    /**
     * Checks the complexity of a password.
     *
     * @param pw The string object that we are checking
     * @return {@code true} if the password meets the complexity requirements, {@code false} otherwise.
     */
    boolean checkPasswordComplexity(String pw) {

        boolean foundUpperCase = false;
        boolean foundLowerCase = false;
        boolean foundNumber = false;
        boolean foundSpecial = false;

        //String password = theEditText.getText().toString();
        for (int i = 0; i < pw.length(); i++) {
            char c = pw.charAt(i);

            if (Character.isUpperCase(c)) {
                foundUpperCase = true;
            } else if (Character.isLowerCase(c)) {
                foundLowerCase = true;
            } else if (Character.isDigit(c)) {
                foundNumber = true;
            } else if (isSpecialCharacter(c)) {
                foundSpecial = true;
            }
        }

        if (!foundUpperCase) {
            Toast.makeText(this, "Your password does not have an upper case letter", Toast.LENGTH_SHORT).show();
            theText.setText("You shall not pass!");
            return false;
        } else if (!foundLowerCase) {
            Toast.makeText(this, "Your password does not have a lower case letter", Toast.LENGTH_SHORT).show();
            theText.setText("You shall not pass!");
            return false;
        } else if (!foundNumber) {
            Toast.makeText(this, "Your password does not have a number", Toast.LENGTH_SHORT).show();
            theText.setText("You shall not pass!");
            return false;
        } else if (!foundSpecial) {
            Toast.makeText(this, "Your password does not have a special symbol", Toast.LENGTH_SHORT).show();
            theText.setText("You shall not pass!");
            return false;
        } else {
            Toast.makeText(this, "Password is valid", Toast.LENGTH_SHORT).show();
            theText.setText("Your password meets the requirements");
            return true;
        }
    }

    /**
     * Checks if a character is a special character.
     *
     * @param c The character to be checked.
     * @return {@code true} if the character is a special character, {@code false} otherwise.
     */
    private boolean isSpecialCharacter(char c) {
        //return true if c is one of: #$%^&*!@?
        //return false otherwise
        switch (c) {
            case '#':
            case '?':
            case '*':
                return true;
            default:
                return false;
        }
    }
}
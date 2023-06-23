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

    /** This holds the text at the centre of the screen*/
    TextView tv = null;
    /** This holds the edit text for typing into*/
    EditText et = null;
    /** This holds the "Login" button*/
    Button btn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.textView);
        et = findViewById(R.id.editText);
        btn = findViewById(R.id.button);

        btn.setOnClickListener( clk ->{
            String password = et.getText().toString();

            if (checkPasswordComplexity(password)) {
                tv.setText("Your password meets the requirements");
            } else {
                tv.setText("You shall not pass!");
            }
        });
    }

    /** This function checks if the password string is too simple or not.
     *
     * @param pw The String object that we are checking
     * @return Returns true if this string has an Upper Case letter, a lower case letter, a number, and a special symbol; returns false otherwise.
     */
    boolean checkPasswordComplexity(String pw) {
        boolean foundUpperCase, foundLowerCase, foundNumber, foundSpecial;

        foundUpperCase = foundLowerCase = foundNumber = foundSpecial = false;

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

        if(!foundUpperCase)
        {

            Toast.makeText(MainActivity.this, "Missing an upper case letter", Toast.LENGTH_SHORT).show();// Say that they are missing an upper case letter;

            return false;

        }

        else if( ! foundLowerCase)
        {
            Toast.makeText(MainActivity.this, "Missing an lower case letter", Toast.LENGTH_SHORT).show(); // Say that they are missing a lower case letter;

            return false;

        }

        else if( ! foundNumber)
        {
            Toast.makeText(MainActivity.this, "Missing an number", Toast.LENGTH_SHORT).show(); // Say that they are missing a number;

            return false;
        }

        else if(! foundSpecial)
        {
            Toast.makeText(MainActivity.this, "Missing a special character", Toast.LENGTH_SHORT).show(); // Say that they are missing a special character;

            return false;
        }

        else

            return true; //only get here if they're all true
    }

    /** This function checks if a character is a special character.
     *
     * @param c The character to check.
     * @return Returns true if the character is one of #$%^&*!@?; returns false otherwise.
     */
    private boolean isSpecialCharacter(char c) {
        switch (c) {
            case '#':
            case '$':
            case '%':
            case '^':
            case '&':
            case '*':
            case '!':
            case '@':
            case '?':
                return true;
            default:
                return false;
        }
    }
}
package com.example.testtaskone.validators;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.regex.Pattern;

public class StringValidator {
    private Context context;

    public StringValidator(Context context) {
        this.context = context;
    }

    public void isEmpty(String value, View view) throws Exception {
        if(value.length() <= 0) {
            String errorText = "Please, enter " + view.getTag();
            Log.d("ErrorEmpty", errorText);
            throw new Exception(errorText);
        }
    }

    public void isOnlySpace(String value) throws Exception {
        if(value.matches("^\\s+$")) {
            String errorText = "The field cannot contains only spaces";
            Log.d("ErrorSpaces", errorText);
            throw new Exception(errorText);
        }
    }

    public void isRightEmail(String value) throws Exception {
        Pattern emailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
                Pattern.CASE_INSENSITIVE);
        if(!emailPattern.matcher(value).find()) {
            String errorText = "Incorrect e-mail";
            Log.d("ErrorEmail", errorText);
            throw new Exception(errorText);
        }
    }

    public void isRightPhoneFormat(String value) throws Exception {
        if(!value.matches("^\\(([0-9]{3})\\)-([0-9]{3})-([0-9]{4})$")) {
            String errorText = "Incorrect phone number format";
            Log.d("ErrorPhone", errorText);
            throw new Exception(errorText);
        }
    }
}

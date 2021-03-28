package com.example.testtaskone;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.testtaskone.adapters.SavedUsersDatabaseAdapter;
import com.example.testtaskone.entities.User;
import com.example.testtaskone.helpers.PhoneTextWatcher;
import com.example.testtaskone.helpers.SavedUsersDatabaseHelper;
import com.example.testtaskone.validators.StringValidator;

public class EditUserProfileActivity extends AppCompatActivity implements View.OnFocusChangeListener {
    private static final int PICK_IMAGE = 100;

    private ImageButton avatarBtn;
    private EditText firstNameBox, lastNameBox, emailBox, phoneBox;
    private StringValidator stringValidator;
    private Uri imageUri;
    private long userId;
    private SavedUsersDatabaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);

        avatarBtn = (ImageButton) findViewById(R.id.avatarBtn);
        firstNameBox = (EditText) findViewById(R.id.editTextFirstName);
        lastNameBox = (EditText) findViewById(R.id.editTextLastName);
        emailBox = (EditText) findViewById(R.id.editTextEmail);
        phoneBox = (EditText) findViewById(R.id.editTextPhone);
        phoneBox.addTextChangedListener(new PhoneTextWatcher(phoneBox, "(###)-###-####"));
        adapter = new SavedUsersDatabaseAdapter(this);

        firstNameBox.setOnFocusChangeListener(this);
        lastNameBox.setOnFocusChangeListener(this);
        emailBox.setOnFocusChangeListener(this);
        phoneBox.setOnFocusChangeListener(this);

        firstNameBox.setTag("first name");
        lastNameBox.setTag("last name");
        emailBox.setTag("e-mail");
        phoneBox.setTag("phone");

        stringValidator = new StringValidator(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Bundle arguments = getIntent().getExtras();
        if(arguments != null) {
            userId = arguments.getLong("id");
            firstNameBox.setText(arguments.getString("firstName"));
            lastNameBox.setText(arguments.getString("lastName"));
            emailBox.setText(arguments.getString("email"));
            phoneBox.setText(arguments.getString("phone"));
            Bitmap pictureImage = (Bitmap) arguments.getParcelable("picture");
            if(pictureImage != null)
                avatarBtn.setImageBitmap(pictureImage);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getSupportActionBar().setTitle("Edit user profile");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_user_profile_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getBack();
                return true;
            case R.id.saveBtn:
                save();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        EditText textBox = (EditText) v;
        try {
            if(hasFocus) {
                textBox.setTextColor(getResources().getColor(R.color.black));
            } else {
                textBox.setTextColor(getResources().getColor(R.color.light_gray));
                stringValidator.isEmpty(textBox.getText().toString(), textBox);
                stringValidator.isOnlySpace(textBox.getText().toString());
                if(textBox.getTag().equals("e-mail")) {
                    stringValidator.isRightEmail(textBox.getText().toString());
                }
                if(textBox.getTag().equals("phone")) {
                    stringValidator.isRightPhoneFormat(textBox.getText().toString());
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG);
        }
    }

    public void openGallery(View view) {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            avatarBtn.setImageURI(imageUri);
        }
    }

    public void getBack() {
        this.finish();
    }

    public void save() {
        try{
            long userId = this.userId;
            String firstName = firstNameBox.getText().toString();
            String lastName = lastNameBox.getText().toString();
            String email = emailBox.getText().toString();
            String phone = phoneBox.getText().toString();
            Bitmap photo = ((BitmapDrawable)avatarBtn.getDrawable()).getBitmap();
            User user = new User(userId, photo, firstName, lastName, email, phone);

            String[] values = { firstName, lastName, email, phone };
            EditText[] views = { firstNameBox, lastNameBox, emailBox, phoneBox };
            for(int i=0; i<values.length;i++) {
                stringValidator.isEmpty(values[i], views[i]);
                stringValidator.isOnlySpace(values[i]);
            }
            stringValidator.isRightEmail(email);
            stringValidator.isRightPhoneFormat(phone);

            adapter.open();
            if (userId > 0) {
                adapter.update(user);
            } else {
                adapter.insert(user);
            }
            adapter.close();
            getToSaved();
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
        }
    }

    private void getToSaved() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("saved", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
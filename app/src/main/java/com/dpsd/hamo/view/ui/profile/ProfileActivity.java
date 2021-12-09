package com.dpsd.hamo.view.ui.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dpsd.hamo.R;
import com.dpsd.hamo.controllers.Updater;
import com.dpsd.hamo.dbmodel.DatabaseHandle;
import com.dpsd.hamo.dbmodel.UsersCollection;
import com.dpsd.hamo.dbmodel.dbhelpers.LocalStorage;

public class ProfileActivity extends AppCompatActivity implements Updater
{

    EditText fullnameTbx, establishmentTbx;
    Button saveBtn;
    CheckBox updateLocationChx;
    UsersCollection usersCollection;

    String userId = LocalStorage.getValue("userId", this);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2);

        fullnameTbx = (EditText) findViewById(R.id.fullnameTbx);
        establishmentTbx = (EditText) findViewById(R.id.establishmentTbx);
        saveBtn = (Button) findViewById(R.id.saveBtn);
        updateLocationChx = (CheckBox) findViewById(R.id.updateLocationChx);

        //populate profile
        usersCollection = new UsersCollection(DatabaseHandle.db);
        usersCollection.loadProfile(userId, fullnameTbx, establishmentTbx);

        //add event for button
        saveBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (fullnameTbx.getText().toString().trim().equals(""))
                {
                    Toast.makeText(ProfileActivity.this, "Full name cannot be empty.", Toast.LENGTH_SHORT).show();
                    return;
                }

                usersCollection.updateProfile(userId, fullnameTbx.getText().toString(),
                        establishmentTbx.getText().toString(), updateLocationChx.isChecked(),
                        ProfileActivity.this);
            }
        });

    }

    @Override
    public void showProfileUpdateSuccess()
    {
        Toast.makeText(this, "Profile successfully updated.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProfileUpdateFailure()
    {
        Toast.makeText(this, "Error updating profile.", Toast.LENGTH_SHORT).show();
    }
}
package com.dpsd.hamo.view.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.dpsd.hamo.controllers.Updater;
import com.dpsd.hamo.databinding.FragmentCommunityRepProfileBinding;
import com.dpsd.hamo.dbmodel.DatabaseHandle;
import com.dpsd.hamo.dbmodel.UsersCollection;
import com.dpsd.hamo.dbmodel.dbhelpers.LocalStorage;

public class CommunityRepProfileFragment extends Fragment implements Updater
{
    EditText fullnameTbx, establishmentTbx;
    Button saveBtn;
    CheckBox updateLocationChx;
    UsersCollection usersCollection;
    String userId;

    private FragmentCommunityRepProfileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        binding = FragmentCommunityRepProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        userId = LocalStorage.getValue("userId", getContext());

        fullnameTbx = binding.fullnameTbx;
        establishmentTbx = binding.establishmentTbx;
        saveBtn = binding.saveBtn;
        updateLocationChx = binding.updateLocationChx;

        //populate profile
        usersCollection = new UsersCollection(DatabaseHandle.db);
        usersCollection.loadProfile(userId, fullnameTbx, establishmentTbx);
        Toast.makeText(getContext(), "UserId" + userId, Toast.LENGTH_SHORT).show();

        //add event for button
        saveBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (fullnameTbx.getText().toString().trim().equals(""))
                {
                    Toast.makeText(getContext(), "Full name cannot be empty.", Toast.LENGTH_SHORT).show();
                    return;
                }

                usersCollection.updateProfile(userId, fullnameTbx.getText().toString(),
                        establishmentTbx.getText().toString(), updateLocationChx.isChecked(),
                        CommunityRepProfileFragment.this);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void showProfileUpdateSuccess()
    {
        Toast.makeText(getContext(), "Profile successfully updated.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProfileUpdateFailure()
    {
        Toast.makeText(getContext(), "Error updating profile.", Toast.LENGTH_SHORT).show();
    }
}
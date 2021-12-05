package com.dpsd.hamo.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.dpsd.hamo.R;
import com.dpsd.hamo.view.login.LoginFragment;

public class LoginActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.nav_fragment_activity_login, LoginFragment.class, null)
                    .commit();
        }
    }
}
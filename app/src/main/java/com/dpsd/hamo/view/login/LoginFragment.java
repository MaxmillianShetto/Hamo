package com.dpsd.hamo.view.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dpsd.hamo.R;
import com.dpsd.hamo.controllers.Login;
import com.dpsd.hamo.databinding.FragmentLoginBinding;
import com.dpsd.hamo.dbmodel.DatabaseHandle;
import com.dpsd.hamo.dbmodel.UsersCollection;
import com.dpsd.hamo.view.UserActivityFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment implements Login, AdapterView.OnItemSelectedListener
{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentLoginBinding binding;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public TextView signUp;
    public TextView forgotPassword;
    public TextView errorEmailOrPhoneTextView;
    public TextView errorPasswordTextView;
    public Button loginButton;
    public EditText emailOrPhoneNumberEditText;
    public EditText passwordEditText;

    private String role = "";
    private Map<String, String> roleMap;



    public Spinner roleSpinner;

    public LoginFragment()
    {
        // Required empty public constructor
    }

    public static LoginFragment newInstance(String param1, String param2)
    {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        signUp = binding.joinUs;
        loginButton = binding.loginButton;
        forgotPassword = binding.forgotPassword;
        emailOrPhoneNumberEditText = binding.emailOrPasswordEditText;
        passwordEditText = binding.passwordEditText;
        errorEmailOrPhoneTextView = binding.errorEmailOrPhoneTextView;
        errorPasswordTextView = binding.errorPasswordTextView;

        roleSpinner = binding.roleLoginSpinner;
        roleSpinner.setOnItemSelectedListener(this);

        signUp.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                loadSignUp(v);
            }
        });


        forgotPassword.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                loadForgotPassword(v);
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                UsersCollection collection = new UsersCollection(DatabaseHandle.db);
                String userNameOrPhoneNumber = emailOrPhoneNumberEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                if(role.trim().equals(""))
                {
                    role = roleMap.get(roleSpinner.getSelectedItem().toString());
                }
                collection.isUser(userNameOrPhoneNumber, password
                            , role.trim(), LoginFragment.this);
                Log.i("Login", "here");
            }
        });

        roleMap = new HashMap<String, String>();
        roleMap.put(getString(R.string.giver), getString(R.string.roleGiver));
        roleMap.put(getString(R.string.community_rep), getString(R.string.roleRep));
        roleMap.put(getString(R.string.admin), getString(R.string.roleAdmin));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.role_array , android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);

        return root;
    }

    public void loadSignUp(View view)
    {
        Fragment signUpFragment = new SignUpFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_fragment_activity_login, signUpFragment)
                .addToBackStack(null)
                .commit();
    }

    public void loadForgotPassword(View view)
    {
        Fragment forgotPasswordFragment = new ForgotPasswordFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_fragment_activity_login, forgotPasswordFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void proceedToHomePage(String role)
    {
        Toast.makeText(getContext(),"successful", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), (Class<?>) UserActivityFactory.loadActivity(role));
        intent.putExtra("role", role);
        startActivity(intent);
    }

    @Override
    public void fieldsEmptyErrorMessage()
    {
        String emailOrPhoneNumber = emailOrPhoneNumberEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if(emailOrPhoneNumber.trim().equals(""))
            errorEmailOrPhoneTextView.setText(R.string.email_phone_empty);

        if(password.trim().equals(""))
            errorPasswordTextView.setText(R.string.password_empty);
    }

    @Override
    public void invalidLoginErrorMessage()
    {
        Toast.makeText(getContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void connectionErrorMessage()
    {
        Toast.makeText(getContext(), R.string.connection_error, Toast.LENGTH_SHORT).show();
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        role = roleMap.get(roleSpinner.getSelectedItem().toString());
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}
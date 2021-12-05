package com.dpsd.hamo.view.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.dpsd.hamo.R;
import com.dpsd.hamo.controllers.SignUp;
import com.dpsd.hamo.databinding.FragmentSignUpBinding;
import com.dpsd.hamo.dbmodel.DatabaseHandle;
import com.dpsd.hamo.dbmodel.UsersCollection;
import com.dpsd.hamo.dbmodel.dbhelpers.GpsLocation;
import com.dpsd.hamo.view.UserActivityFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment implements SignUp, AdapterView.OnItemSelectedListener
{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentSignUpBinding binding;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String role = "";
    private Map<String, String> roleMap;

    public TextView loginTransitionTextView;
    public EditText emailOrPhoneNumberEditText;
    public EditText passwordEditText;
    public EditText confirmPasswordEditText;
    public EditText fullNameEditText;
    public Button singUpButton;
    public Spinner roleSpinner;


    public SignUpFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignUpFragment newInstance(String param1, String param2)
    {
        SignUpFragment fragment = new SignUpFragment();
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
        binding = FragmentSignUpBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        loginTransitionTextView = binding.loginTransitionTextView;

        emailOrPhoneNumberEditText = binding.emailOrPhoneNumberEditTextSignUp;
        passwordEditText = binding.passwordEditTextSignUp;
        confirmPasswordEditText = binding.confirmPasswordEditText;
        fullNameEditText = binding.fullNameEditText;

        singUpButton = binding.signUpButton;

        roleSpinner = binding.roleSpinner;
        roleSpinner.setOnItemSelectedListener(this);

        loginTransitionTextView.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                loadLoginPage(v);
            }
        });

        singUpButton.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                UsersCollection collection = new UsersCollection(DatabaseHandle.db);
                String fullName = fullNameEditText.getText().toString();
                String userEmailOrPhoneNumber = emailOrPhoneNumberEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                if(role.trim().equals(""))
                {
                    role = roleMap.get(roleSpinner.getSelectedItem().toString());
                }
                ArrayList<GpsLocation> locale = new ArrayList<GpsLocation>();

                collection.addUser(userEmailOrPhoneNumber, fullName, password, role,
                        locale, SignUpFragment.this);
            }
        });

        roleMap = new HashMap<String, String>();
        roleMap.put(getString(R.string.giver), getString(R.string.roleGiver));
        roleMap.put(getString(R.string.community_rep), getString(R.string.roleRep));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.role_array , android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);

        return root;
    }

    public void loadLoginPage(View view)
    {
        Fragment loginFragment = new LoginFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_fragment_activity_login, loginFragment)
                .addToBackStack(null)
                .commit();
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        role = roleMap.get(roleSpinner.getSelectedItem().toString());
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    @Override
    public void proceedToHomePage(String role)
    {
        Intent intent = new Intent(getActivity(), (Class<?>) UserActivityFactory.loadActivity(role));
        startActivity(intent);
    }

    @Override
    public void showErrorMessage()
    {

    }
}
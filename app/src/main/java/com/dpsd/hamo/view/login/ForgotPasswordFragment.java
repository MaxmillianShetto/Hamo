package com.dpsd.hamo.view.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dpsd.hamo.R;
import com.dpsd.hamo.controllers.EmailSender;
import com.dpsd.hamo.controllers.Messenger;
import com.dpsd.hamo.databinding.FragmentForgotPasswordBinding;

import java.util.Random;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForgotPasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForgotPasswordFragment extends Fragment
{
    public TextView loginTransitionTextView;
    public EditText emailOrPhoneEditText;
    public Button sendCodeButton;
    private FragmentForgotPasswordBinding binding;

    public ForgotPasswordFragment()
    {
        // Required empty public constructor
    }

    public static ForgotPasswordFragment newInstance(String param1, String param2)
    {
        ForgotPasswordFragment fragment = new ForgotPasswordFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        loginTransitionTextView = binding.loginTextViewForgotPassword;
        emailOrPhoneEditText = binding.emailOrPasswordEditTextForgotPass;
        sendCodeButton = binding.sendCodeButton;

        loginTransitionTextView.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                loadLoginPage(v);
            }
        });

        sendCodeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String value = emailOrPhoneEditText.getText().toString();
                if (!value.trim().equals(""))
                {
                    int resetCode = randomValueGenerator();

                    sendNotification(value, resetCode);

                }
                else
                {

                }
            }
        });

        return root;
    }

    public int randomValueGenerator()
    {
        Random random = new Random();
        int number = random.nextInt(999999);
        return number;
    }

    public void sendNotification(String value, int resetCodeProvided)
    {
        String regexEmail = "^(.+)@(.+)$";
        String regexPhoneNumber = "^\\d{10}$";
        Pattern pattern = Pattern.compile(regexEmail);

        StringBuilder message = new StringBuilder();
        message.append("Here is your reset code: ");
        message.append(resetCodeProvided);

        Bundle resetDetails = new Bundle();
        resetDetails.putString("resetCode", Integer.toString(resetCodeProvided));


        if (pattern.matcher(value).matches())
        {
            EmailSender newEmail = new EmailSender();
            try
            {
                newEmail.sendSignUpEmail("Reset Password", message.toString(), value);
                resetDetails.putString("email", value);

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else if (pattern.compile(regexPhoneNumber).matcher(value).matches())
        {
            Messenger messenger = new Messenger();
            messenger.sendMessage(getContext(), getActivity(), value, message.toString());
            resetDetails.putString("email", value);
        }
        loadResetPassword(resetDetails);
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

    public void loadResetPassword(Bundle bundle)
    {
        Fragment resetPasswordFragment = new ResetPasswordFragment();
        resetPasswordFragment.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_fragment_activity_login, resetPasswordFragment)
                .addToBackStack(null)
                .commit();
    }
}
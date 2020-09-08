package com.example.jome17wave.jome_loginRegister;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.jome17wave.R;

public class LoginFragment extends Fragment {
    private static final String TAG = "LoginFragment";
    private Activity activity;
    private ImageButton ibtLogin;
    private ImageButton ibtforgetPw;
    private ImageButton ibtRegister;
    private ImageButton ibtFbLogin;
    private ImageButton ibtGoogleLogin;
    private EditText etLoginAc;
    private EditText etLoginPw;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ibtLogin = view.findViewById(R.id.ibtLogin);
        ibtforgetPw = view.findViewById(R.id.ibtforgetPw);
        ibtRegister = view.findViewById(R.id.ibtRegister);
        ibtFbLogin = view.findViewById(R.id.ibtFbLogin);
        ibtGoogleLogin = view.findViewById(R.id.ibtGoogleLogin);
        etLoginAc = view.findViewById(R.id.etLoginAc);
        etLoginPw = view.findViewById(R.id.etLoginPw);

        View.OnClickListener loginBtOnclick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.ibtLogin:
                        break;
                    case R.id.ibtforgetPw:
                        Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_forgetPwFragment);
                        break;
                    case R.id.ibtRegister:
                        Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerMemberFragment);
                        break;
                    case R.id.ibtGoogleLogin:
                        break;
                    case R.id.ibtFbLogin:
                        break;
                    case R.id.etLoginAc:
                        break;
                    case R.id.etLoginPw:
                        break;
                    default:
                        break;
                }
            }
        };
        ibtLogin.setOnClickListener(loginBtOnclick);
        ibtforgetPw.setOnClickListener(loginBtOnclick);
        ibtRegister.setOnClickListener(loginBtOnclick);
        ibtFbLogin.setOnClickListener(loginBtOnclick);
        ibtGoogleLogin.setOnClickListener(loginBtOnclick);
    }
}
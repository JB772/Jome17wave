package com.example.jome17wave.jome_loginRegister;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.jome17wave.R;
import com.example.jome17wave.jome_member.JomeMember;

public class RegisterMemberFragment extends Fragment {
    private static final String TAG = "RegisterMemberFragment";
    private Activity activity;
    private EditText etRegisterMail;
    private EditText etRegisterPw;
    private EditText etRegisterCheckPw;
    private EditText etRegisterNn;
    private EditText etRegisterPh;
    private EditText etRegisterPhV;
    private ImageButton ibtRegister;
    private RadioGroup radGGender;
    private RadioButton radBtMale, radBtFemale, radBtThird;
    private JomeMember jomeMember = new JomeMember();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register_member, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etRegisterMail = view.findViewById(R.id.etRegisterMail);
        etRegisterPw = view.findViewById(R.id.etRegisterPw);
        etRegisterCheckPw = view.findViewById(R.id.etRegisterCheckPw);
        etRegisterNn = view.findViewById(R.id.etRegisterNn);
        etRegisterPh = view.findViewById(R.id.etRegisterPh);
        etRegisterPhV = view.findViewById(R.id.etRegisterPhV);
        ibtRegister = view.findViewById(R.id.ibtRegister);
        radGGender = view.findViewById(R.id.radGGender);
        radBtMale = view.findViewById(R.id.radBtMale);
        radBtFemale = view.findViewById(R.id.radBtFemale);
        radBtThird = view.findViewById(R.id.radBtThird);

        View.OnClickListener btonclick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int genderCode = -1;
                switch (v.getId()){
                    case R.id.ibtRegister:
                        break;
                    case R.id.radBtMale:
                        genderCode = 1;
                        break;
                    case R.id.radBtFemale:
                        genderCode = 2;
                        break;
                    case R.id.radBtThird:
                        genderCode = 3;
                        break;
                    default:
                        break;
                }
                jomeMember.setGender(genderCode);
            }
        };
    }
}
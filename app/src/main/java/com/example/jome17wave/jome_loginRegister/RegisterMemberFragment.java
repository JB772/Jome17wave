package com.example.jome17wave.jome_loginRegister;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.jome17wave.Common;
import com.example.jome17wave.R;
import com.example.jome17wave.jome_member.JomeMember;
import com.example.jome17wave.task.CommonTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.concurrent.ExecutionException;

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
    private JomeMember jomeMember ;
    private int genderCode = -1;
    private CommonTask registerTask;

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

                switch (v.getId()){
                    case R.id.ibtRegister:
                        if (registerSubmit()){
                            String url = Common.URL_SERVER + "jome_member/RegisterServlet";
                            JsonObject jsonObject = new JsonObject();
                            jsonObject.addProperty("action", "register");
                            jsonObject.addProperty("jomeMember", new Gson().toJson(jomeMember));
                            registerTask = new CommonTask(url, jsonObject.toString());
                            int resultCode = -1;
                            try {
                                String jsonIn = registerTask.execute().get();
                                jsonObject = new Gson().fromJson(jsonIn, JsonObject.class);
                                resultCode = jsonObject.get("resultCode").getAsInt();
                            } catch (Exception e) {
                                Log.d(TAG, e.toString());
                            }
                            if (resultCode == 1){
                                Navigation.findNavController(ibtRegister).popBackStack();
                                Common.showToast(activity, R.string.successRegister);
                            }else {
                                Common.showToast(activity, R.string.no_network_connection_available);
                            }
                        }
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
            }
        };
        radBtMale.setOnClickListener(btonclick);
        radBtFemale.setOnClickListener(btonclick);
        radBtThird.setOnClickListener(btonclick);
        ibtRegister.setOnClickListener(btonclick);
    }

    private boolean registerSubmit() {
        String account = etRegisterMail.getText().toString().trim();
        String password = etRegisterPw.getText().toString().trim();
        String checkPw = etRegisterCheckPw.getText().toString().trim();
        String nickname = etRegisterNn.getText().toString().trim();
        String phoneNumber = etRegisterPh.getText().toString().trim();
        if (account.length() <= 0 || password.length() <= 0 || checkPw.length() <= 0 || nickname.length() <=0 || genderCode <= 0 || phoneNumber.length()!=10){
            Common.showToast(activity, R.string.unuseAcOrPw);
            return false;
        }else {
            if (password.equals(checkPw)){
                jomeMember = new JomeMember(Common.getDateTimeId() ,account, password, genderCode, phoneNumber, nickname);
                return true;
            }else {
                Common.showToast(activity, R.string.passwordIsError);
                return false;
            }
        }
    }
}
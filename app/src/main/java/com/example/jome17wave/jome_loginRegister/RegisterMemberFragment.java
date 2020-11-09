package com.example.jome17wave.jome_loginRegister;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.jome17wave.Common;
import com.example.jome17wave.R;
import com.example.jome17wave.jome_Bean.JomeMember;
import com.example.jome17wave.task.CommonTask;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.concurrent.TimeUnit;

public class RegisterMemberFragment extends Fragment {
    private static final String TAG = "RegisterMemberFragment";
    private Activity activity;
    private PhoneAuthProvider.ForceResendingToken resendToken;
    private FirebaseAuth auth;
    private String verificationId;
    private EditText etRegisterMail, etRegisterPw, etRegisterCheckPw, etRegisterNn, etRegisterPh, etRegisterPhV;
    private TextView tvRegisterPV;
    private ImageButton ibtRegister, btRegisterVCode;
    private RadioGroup radGGender;
    private RadioButton radBtMale, radBtFemale, radBtThird;
    private JomeMember jomeMember;
    private int genderCode = -1;
    int verifyResult = 0;
    private CommonTask registerTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        auth = FirebaseAuth.getInstance();
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
        etRegisterPhV.setVisibility(View.GONE);
        tvRegisterPV = view.findViewById(R.id.tvRegisterPV);
        tvRegisterPV.setVisibility(View.GONE);
        ibtRegister = view.findViewById(R.id.ibtRegister);
        btRegisterVCode = view.findViewById(R.id.btRegisterVCode);
        radGGender = view.findViewById(R.id.radGGender);
        radBtMale = view.findViewById(R.id.radBtMale);
        radBtFemale = view.findViewById(R.id.radBtFemale);
        radBtThird = view.findViewById(R.id.radBtThird);
        View.OnClickListener btOnClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {
                    case R.id.btRegisterVCode:
                        String phone = "+886" + etRegisterPh.getText().toString().trim();
                        if (phone.isEmpty() || phone.equals("+886")) {
                            etRegisterPh.setError(getString(R.string.unuseAcOrPw));
                            return;
                        }else{
Log.d(TAG, "phone: " + phone);
                            sendVerificationCode(phone);
                        }

                        break;
                    case R.id.ibtRegister:
                        if (registerSubmit() == 1) {
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
                            if (resultCode == 1) {
                                Navigation.findNavController(ibtRegister).popBackStack();
                                Common.showToast(activity, R.string.successRegister);
                            } else {
                                Common.showToast(activity, R.string.accountIsExist);
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
        btRegisterVCode.setOnClickListener(btOnClick);
        radBtMale.setOnClickListener(btOnClick);
        radBtFemale.setOnClickListener(btOnClick);
        radBtThird.setOnClickListener(btOnClick);
        ibtRegister.setOnClickListener(btOnClick);
    }

    private void sendVerificationCode(String phone) {
        etRegisterPhV.setVisibility(View.VISIBLE);
        tvRegisterPV.setVisibility(View.VISIBLE);
        auth.setLanguageCode("zh-Hant");
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(phone)                      // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS)   // Timeout and unit
                        .setActivity(activity)                     // Activity (for callback binding)
                        .setCallbacks(verifyCallbacks)           // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks verifyCallbacks
            = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        /** This callback will be invoked in two situations:
         1 - Instant verification. In some cases the phone number can be instantly
         verified without needing to send or enter a verification code.
         2 - Auto-retrieval. On some devices Google Play services can automatically
         detect the incoming verification SMS and perform verification without
         user action. */
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
            Log.d(TAG, "onVerificationCompleted: " + credential);
        }

        /**
         * 發送驗證碼填入的電話號碼格式錯誤，或是使用模擬器發送都會產生發送錯誤，
         * 使用模擬器發送會產生下列執行錯誤訊息：
         * App validation failed. Is app running on a physical device?
         */
        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Log.e(TAG, "onVerificationFailed: " + e.getMessage());

        }

        /**
         * The SMS verification code has been sent to the provided phone number,
         * we now need to ask the user to enter the code and then construct a credential
         * by combining the code with a verification ID.
         */
        @Override
        public void onCodeSent(@NonNull String id, @NonNull PhoneAuthProvider.ForceResendingToken token) {
            Log.d(TAG, "onCodeSent: " + id);
            verificationId = id;
            resendToken = token;
            // 顯示填寫驗證碼版面
            etRegisterPhV.setVisibility(View.VISIBLE);
            tvRegisterPV.setVisibility(View.VISIBLE);
        }
    };

    private int registerSubmit() {
        String account = etRegisterMail.getText().toString().trim();
        String password = etRegisterPw.getText().toString().trim();
        String checkPw = etRegisterCheckPw.getText().toString().trim();
        String nickname = etRegisterNn.getText().toString().trim();
        String phoneNumber = etRegisterPh.getText().toString().trim();
        String verificationCode = etRegisterPhV.getText().toString().trim();
        if (verificationCode.isEmpty()) {
            etRegisterPhV.setError(getString(R.string.unuseAcOrPw));
            verifyResult = -1;
        }
        if (account.length() <= 0 || password.length() <= 0 || checkPw.length() <= 0 || nickname.length() <= 0 || genderCode <= 0 || phoneNumber.length() != 10) {
            Common.showToast(activity, R.string.unuseAcOrPw);
            verifyResult = -1;
        }
        if (password.equals(checkPw)) {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, verificationCode);
            auth.signInWithCredential(credential)
                    .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                jomeMember = new JomeMember(Common.getDateTimeId(), account, password, genderCode, phoneNumber, nickname);
                                verifyResult = 1;
                            } else {
                                Exception exception = task.getException();
                                String message = exception == null ? "Sign in fail." : exception.getMessage();
                                Log.d(TAG, message);
                                if (exception instanceof FirebaseAuthInvalidCredentialsException) {
//                                    etRegisterPhV.setError(getString(R.string.textInvalidCode));
                                    Common.showToast(activity, R.string.unuseAcOrPw);
                                    verifyResult = -1;
                                }
                            }
                        }
                    });
        } else {
            Common.showToast(activity, R.string.passwordIsError);
            verifyResult = -1;
        }
        return verifyResult;
    }
}
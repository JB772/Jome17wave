package com.example.jome17wave.jome_loginRegister;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;


public class ForgetPwFragment extends Fragment {
    private static final String TAG = "ForgetPwFragment";
    private final String url = Common.URL_SERVER + "jome_member/LoginServlet";
    private Activity activity;
    private ConstraintLayout clVerifyingFP, clNewPassword, clNewPasswordCheck;
    private ImageButton ibtSendVerifyCodeFP, ibtReSendVerifyCodeFP, ibtEnterVerifyFP, ibtSendResetPassword;
    private Button btSend;
    private EditText etAccountFP, etPhoneFP, etVerifyCodeFP, etNewPassword, etNewPasswordCheck;
    private PhoneAuthProvider.ForceResendingToken resendToken;
    private CommonTask checkAccountTask, resetPasswordTask;
    private FirebaseAuth auth;
    private String verificationId;
    private JomeMember selfMember;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_forget_pw, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        clVerifyingFP = view.findViewById(R.id.clVerifyingFP);
        clVerifyingFP.setVisibility(View.GONE);
        clNewPassword = view.findViewById(R.id.clNewPassword);
        clNewPassword.setVisibility(View.GONE);
        clNewPasswordCheck = view.findViewById(R.id.clNewPasswordCheck);
        clNewPasswordCheck.setVisibility(View.GONE);

        ibtSendVerifyCodeFP = view.findViewById(R.id.ibtSendVerifyCodeFP);
        ibtSendVerifyCodeFP.setVisibility(View.GONE);
        ibtReSendVerifyCodeFP = view.findViewById(R.id.ibtReSendVerifyCodeFP);
        ibtReSendVerifyCodeFP.setVisibility(View.GONE);

        ibtEnterVerifyFP = view.findViewById(R.id.ibtEnterVerifyFP);

        ibtSendResetPassword = view.findViewById(R.id.ibtSendResetPassword);
        ibtSendResetPassword.setVisibility(View.GONE);
        btSend = view.findViewById(R.id.btSend);
        etAccountFP = view.findViewById(R.id.etAccountFP);
        etPhoneFP = view.findViewById(R.id.etPhoneFP);
        etVerifyCodeFP = view.findViewById(R.id.etVerifyCodeFP);
        etNewPassword = view.findViewById(R.id.etNewPassword);
        etNewPasswordCheck = view.findViewById(R.id.etNewPasswordCheck);

        View.OnClickListener buttonClickForgetPassword = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btSend:
                        String account = etAccountFP.getText().toString().trim();
                        String phoneNumber = etPhoneFP.getText().toString().trim();
                        if (account.length() <= 0 || phoneNumber.length() <= 0 || phoneNumber.length() != 10){
                            if (account.length() <= 0){
                                etAccountFP.setError(getString(R.string.unuseAcOrPw));
                            }else {
                                etPhoneFP.setError(getString(R.string.phoneIsError));
                            }
                        }else {
                            if (sendAccountPhone(account, phoneNumber)){
                                ibtSendVerifyCodeFP.setVisibility(View.VISIBLE);
                                etAccountFP.setEnabled(false);
                                etPhoneFP.setEnabled(false);
                            }else {
                                etAccountFP.setError(getString(R.string.textIsError));
                                etPhoneFP.setError(getString(R.string.textIsError));
                            }
                        }
                        break;

                    case R.id.ibtSendVerifyCodeFP:
                        // 電話號碼格式要符合E.164，要加上country code，台灣為+886
                        String phone = "+886" + etPhoneFP.getText().toString().trim();
                        if (phone.isEmpty()) {
                            etPhoneFP.setError(getString(R.string.textIsError));
                            return;
                        }
                        sendVerificationCode(phone);
                        break;

                    case R.id.ibtReSendVerifyCodeFP:
                        break;

                    case R.id.ibtEnterVerifyFP:
                        String verificationCode = etVerifyCodeFP.getText().toString().trim();
                        if (verificationCode.isEmpty()) {
                            etVerifyCodeFP.setError(getString(R.string.unuseAcOrPw));
                            return;
                        }
                        verifyPhoneNumberWithCode(verificationId, verificationCode);
                        break;

                    case R.id.ibtSendResetPassword:
                        String newPassword = etNewPassword.getText().toString().trim();
                        String newPasswordCheck = etNewPasswordCheck.getText().toString().trim();
                        if (newPassword.length() != newPasswordCheck.length() || newPassword.length()<=0 ){
                            etNewPassword.setError(getString(R.string.textIsError));
                            etNewPasswordCheck.setError(getString(R.string.textIsError));
                        }else {
                            if (newPassword.equals(newPasswordCheck)){
                                if (resetPassword(newPassword)){
                                    Common.showToast(activity, R.string.passwordResetFinish);
                                    Navigation.findNavController(etNewPasswordCheck).popBackStack();
                                }
                            }else {
                                etNewPassword.setError(getString(R.string.textIsError));
                                etNewPasswordCheck.setError(getString(R.string.textIsError));
                            }

                        }

                        break;
                }
            }
        };
        btSend.setOnClickListener(buttonClickForgetPassword);
        ibtSendVerifyCodeFP.setOnClickListener(buttonClickForgetPassword);
        ibtReSendVerifyCodeFP.setOnClickListener(buttonClickForgetPassword);
        ibtEnterVerifyFP.setOnClickListener(buttonClickForgetPassword);
        ibtSendResetPassword.setOnClickListener(buttonClickForgetPassword);
    }

    private void sendVerificationCode(String phone) {
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

    private void resendVerificationCode(String phone, PhoneAuthProvider.ForceResendingToken resendToken) {
        auth.setLanguageCode("zh-Hant");
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(phone)                      // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS)   // Timeout and unit
                        .setActivity(activity)                     // Activity (for callback binding)
                        .setCallbacks(verifyCallbacks)           // OnVerificationStateChangedCallbacks
                        .setForceResendingToken(resendToken)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void verifyPhoneNumberWithCode(String verificationId, String verificationCode) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, verificationCode);
        firebaseAuthWithPhoneNumber(credential);
    }

    private void firebaseAuthWithPhoneNumber(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            ibtSendVerifyCodeFP.setVisibility(View.GONE);
                            btSend.setVisibility(View.GONE);
                            clVerifyingFP.setVisibility(View.GONE);
                            clNewPassword.setVisibility(View.VISIBLE);
                            clNewPasswordCheck.setVisibility(View.VISIBLE);
                            ibtSendResetPassword.setVisibility(View.VISIBLE);
                        } else {
                            ibtSendVerifyCodeFP.setVisibility(View.GONE);
                            ibtReSendVerifyCodeFP.setVisibility(View.VISIBLE);
                            Exception exception = task.getException();
                            String message = exception == null ? "Sign in fail." : exception.getMessage();
                            Log.d(TAG, message);
                            if (exception instanceof FirebaseAuthInvalidCredentialsException) {
                                etVerifyCodeFP.setError(getString(R.string.textInvalidCode));
                            }
                        }
                    }
                });
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
            clVerifyingFP.setVisibility(View.VISIBLE);
        }
    };

    private boolean sendAccountPhone(String account, String phoneNumber){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "forgetPassword");
        jsonObject.addProperty("account", account);
        jsonObject.addProperty("phone", phoneNumber);
        String jsonInStr = "";
        checkAccountTask = new CommonTask(url, jsonObject.toString());
        try {
            jsonInStr = checkAccountTask.execute().get();
            jsonObject = new Gson().fromJson(jsonInStr, JsonObject.class);
        } catch (ExecutionException e) {
            Log.e(TAG, e.toString());
        } catch (InterruptedException e) {
            Log.e(TAG, e.toString());
        }
        if (jsonObject.get("checkResult").getAsInt() == 1){
            selfMember = new Gson().fromJson(jsonObject.get("selfMember").getAsString(), JomeMember.class);
            return true;
        }

        return false;
    }

    private boolean resetPassword(String newPassword){
        selfMember.setPassword(newPassword);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "update");
        jsonObject.addProperty("memberUp", new Gson().toJson(selfMember));
        jsonObject.addProperty("imageBase64", "noImage");
        String jsonInStr = "";
        resetPasswordTask = new CommonTask(url, jsonObject.toString());
        try {
            jsonInStr = resetPasswordTask.execute().get();
            jsonObject = new Gson().fromJson(jsonInStr, JsonObject.class);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (jsonObject.get("resultCode").getAsInt() == 1){
            return true;
        }
        return  false;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (checkAccountTask != null){
            checkAccountTask.cancel(true);
            checkAccountTask = null;
        }
        if (resetPasswordTask != null){
            resetPasswordTask.cancel(true);
            resetPasswordTask = null;
        }
    }
}
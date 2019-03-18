package com.kwame.dietitian.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.kwame.dietitian.R;
import com.kwame.dietitian.activity.MainActivity;
import com.kwame.dietitian.util.UserPref;

public class LoginFragment extends Fragment {
    private EditText email, password;
    private FirebaseAuth auth;
    private UserPref pref;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        auth = FirebaseAuth.getInstance();
        pref = new UserPref(getActivity());

        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        Button login = view.findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    private void loginUser() {
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();

        if (TextUtils.isEmpty(userEmail) || TextUtils.isEmpty(userPassword))
            Toast.makeText(getActivity(), "Email and Password Required", Toast.LENGTH_SHORT).show();
        else{
            final ProgressDialog dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Authenticating, please wait...");
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            auth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        pref.saveUserDetails(auth.getCurrentUser());
                        dialog.hide();
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        getActivity().finish();
                    }else{
                        dialog.hide();
                        Toast.makeText(getActivity(), "Authentication error, please retry", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}

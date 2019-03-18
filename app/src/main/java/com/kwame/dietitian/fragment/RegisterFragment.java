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

public class RegisterFragment extends Fragment {
    private EditText email, password, passwordConfirmation;
    private FirebaseAuth auth;
    private UserPref pref;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        auth = FirebaseAuth.getInstance();
        pref = new UserPref(getActivity());
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        passwordConfirmation = view.findViewById(R.id.password_confirmation);
        Button register = view.findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }


    private void registerUser() {
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();
        String userPasswordConfirmation = passwordConfirmation.getText().toString();

        if (TextUtils.isEmpty(userEmail) || TextUtils.isEmpty(userPassword) || TextUtils.isEmpty(userPasswordConfirmation))
            Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
        else if(!TextUtils.equals(userPassword, userPasswordConfirmation))
            Toast.makeText(getActivity(), "Password dd not match", Toast.LENGTH_SHORT).show();
        else {
            final ProgressDialog dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Registering, please wait...");
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            auth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        pref.saveUserDetails(auth.getCurrentUser());
                        dialog.hide();
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        getActivity().finish();
                    }else{
                        dialog.hide();
                        Toast.makeText(getActivity(), "Error creating account, please retry", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}

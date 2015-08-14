package com.example.samples2facebook;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class LoginFragment extends Fragment {
	CallbackManager cm;
	LoginButton login;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_main, container, false);
		cm = CallbackManager.Factory.create();
		login = (LoginButton)view.findViewById(R.id.btn_login);
		login.setFragment(this);
		login.registerCallback(cm, new FacebookCallback<LoginResult>() {
			
			@Override
			public void onSuccess(LoginResult result) {
				Toast.makeText(getActivity(), "success",Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onError(FacebookException error) {
				Toast.makeText(getActivity(), "error",Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onCancel() {
				Toast.makeText(getActivity(), "cancel",Toast.LENGTH_SHORT).show();
			}
		});
		return view;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		cm.onActivityResult(requestCode, resultCode, data);
	}
}

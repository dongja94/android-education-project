package com.example.samples2facebook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;


public class MainActivity extends ActionBarActivity {

	LoginButton login;
	CallbackManager callbackManager;
	LoginManager mLM;
	Button btnLogin;
	AccessTokenTracker tracker;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mLM = LoginManager.getInstance();
        
        login = (LoginButton)findViewById(R.id.btn_login);
        callbackManager = CallbackManager.Factory.create();
        login.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
			
			@Override
			public void onSuccess(LoginResult result) {
				AccessToken token = AccessToken.getCurrentAccessToken();
				
				Toast.makeText(MainActivity.this, "login success : " + token.getUserId(), Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onError(FacebookException error) {
				Toast.makeText(MainActivity.this, "login error", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onCancel() {
				Toast.makeText(MainActivity.this, "login cancel", Toast.LENGTH_SHORT).show();
			}
		});
        
        Button btn =(Button)findViewById(R.id.btn_other);
        btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, OtherActivity.class));
			}
		});
        
        btnLogin = (Button)findViewById(R.id.btn_login2);
        AccessToken token = AccessToken.getCurrentAccessToken();
        if (token == null) {
        	btnLogin.setText("login");
        } else {
        	btnLogin.setText("logout");
        }
        
        btnLogin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AccessToken token = AccessToken.getCurrentAccessToken();
				if (token == null) {
					mLM.logInWithReadPermissions(MainActivity.this, null);
				} else {
					mLM.logOut();
				}
			}
		});
        
        tracker = new AccessTokenTracker() {
			
			@Override
			protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
					AccessToken currentAccessToken) {
				if (currentAccessToken != null) {
					btnLogin.setText("logout");
				} else {
					btnLogin.setText("login");
				}
			}
		};
    }

    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	tracker.stopTracking();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

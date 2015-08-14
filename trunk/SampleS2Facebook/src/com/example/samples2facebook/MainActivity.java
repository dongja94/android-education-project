package com.example.samples2facebook;

import java.util.Arrays;

import org.json.JSONObject;

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
import com.facebook.GraphRequest;
import com.facebook.GraphRequest.Callback;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;


public class MainActivity extends ActionBarActivity {

	LoginButton login;
	CallbackManager callbackManager;
	LoginManager mLM;
	Button btnLogin;
	AccessTokenTracker tracker;
	enum ActionState {
		NOT_ACTION,
		POST,
		READ
	}
	ActionState state = ActionState.NOT_ACTION;
	
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
				if (state == ActionState.POST) {
					sendPost();
					state = ActionState.NOT_ACTION;
				} if (state == ActionState.READ) {
					readPost();
					state = ActionState.NOT_ACTION;
				}
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
        
        
        btn = (Button)findViewById(R.id.btn_post);
        btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AccessToken token = AccessToken.getCurrentAccessToken();
				if (token != null) {
					if (token.getPermissions().contains("publish_actions")) {
						sendPost();
						return;
					}
				}
				state = ActionState.POST;
				mLM.logInWithPublishPermissions(MainActivity.this, Arrays.asList("publish_actions"));
			}
		});
        
        btn = (Button)findViewById(R.id.btn_home);
        btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AccessToken token = AccessToken.getCurrentAccessToken();
				if (token != null) {
					if (token.getPermissions().contains("user_posts")) {
						readPost();
						return;
					}
				}
				state = ActionState.READ;
				mLM.logInWithReadPermissions(MainActivity.this, Arrays.asList("user_posts"));
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

    private void sendPost() {
    	AccessToken token = AccessToken.getCurrentAccessToken();
    	String path = "/me/feed";
    	Bundle params = new Bundle();
    	params.putString("message", "facebook post test");
		params.putString("link", "http://developers.facebook.com/docs/android");
        params.putString("picture", "https://scontent.xx.fbcdn.net/hphotos-xpa1/t39.2178-6/851567_1813371155468532_1182857230_n.png");
        params.putString("name", "Hello Facebook");
        params.putString("description", "The 'Hello Facebook' sample  showcases simple …");

        
    	GraphRequest request = new GraphRequest(token, path, params, HttpMethod.POST, new Callback() {
			
			@Override
			public void onCompleted(GraphResponse response) {
				JSONObject obj = response.getJSONObject();
				if (obj != null) {
					Toast.makeText(MainActivity.this, "id : " + obj.optString("id"), Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(MainActivity.this, "errof : " + response.getError().getErrorMessage(), Toast.LENGTH_SHORT).show();
				}
			}
		});
    	
    	request.executeAsync();
    }
    
    private void readPost() {
    	AccessToken token = AccessToken.getCurrentAccessToken();
    	String path = "/me/feed";
    	GraphRequest request = new GraphRequest(token, path, null, HttpMethod.GET, new Callback() {
			
			@Override
			public void onCompleted(GraphResponse response) {
				JSONObject obj = response.getJSONObject();
				if (obj != null) {
					Toast.makeText(MainActivity.this, "result : " + obj.toString(), Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(MainActivity.this, "errof : " + response.getError().getErrorMessage(), Toast.LENGTH_SHORT).show();
				}
			}
		});
    	request.executeAsync();
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

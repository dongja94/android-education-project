package com.example.samples2database;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class DBAddActivity extends ActionBarActivity {

	EditText nameView, ageView, addressView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dbadd);
		nameView = (EditText)findViewById(R.id.edit_name);
		ageView = (EditText)findViewById(R.id.edit_age);
		addressView = (EditText)findViewById(R.id.edit_address);
		Button btn = (Button)findViewById(R.id.btn_add);
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Person p = new Person();
				p.name = nameView.getText().toString();
				p.age = Integer.parseInt(ageView.getText().toString());
				p.address = addressView.getText().toString();
				DBManager.getInstance().addPerson(p);
				nameView.setText("");
				ageView.setText("0");
				addressView.setText("");
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dbadd, menu);
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

package com.example.samples2database;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.samples2database.DBConstant.PersonTable;


public class MainActivity extends ActionBarActivity {

	ListView listView;
//	ArrayAdapter<Person> mAdapter;
	SimpleCursorAdapter mAdapter;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView)findViewById(R.id.listView1);
//        mAdapter = new ArrayAdapter<Person>(this, android.R.layout.simple_list_item_1);
        String[] from = {PersonTable.NAME, PersonTable.ADDRESS};
        int[] to = {R.id.text_name, R.id.text_address};
        mAdapter = new SimpleCursorAdapter(this, R.layout.view_item, null, from, to, 0);
        
        listView.setAdapter(mAdapter);
        
        Button btn = (Button)findViewById(R.id.btn_add);
        btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, DBAddActivity.class);
				startActivity(intent);
			}
		});
    }
    
    @Override
    protected void onResume() {
    	super.onResume();

    	Cursor c = DBManager.getInstance().getPersonCursor(null);
    	mAdapter.changeCursor(c);
    	
//    	mAdapter.clear();
//    	List<Person> list = DBManager.getInstance().getPerson(null);
//    	for (Person p : list) {
//    		mAdapter.add(p);
//    	}
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

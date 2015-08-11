package com.example.samples2image;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;

public class PickActivity extends ActionBarActivity implements LoaderCallbacks<Cursor> {

	GridView gridView;
	SimpleCursorAdapter mAdapter;
	String[] columns = {MediaStore.Images.Media._ID, Images.Media.DISPLAY_NAME, Images.Media.DATA };
	String sortOrder = Images.Media.DATE_ADDED + " ASC";
	int idColumn = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pick);
		gridView = (GridView)findViewById(R.id.gridView1);
		String[] from = {Images.Media._ID, Images.Media.DISPLAY_NAME};
		int[] to = {R.id.image_icon , R.id.text_display};
		mAdapter = new SimpleCursorAdapter(this, R.layout.view_image, null, from, to, 0);
		mAdapter.setViewBinder(new ViewBinder() {
			
			@Override
			public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
				if (idColumn == columnIndex) {
					ImageView iv = (ImageView)view;
					long id = cursor.getLong(columnIndex);
					Bitmap bm = Images.Thumbnails.getThumbnail(getContentResolver(), id, Images.Thumbnails.MINI_KIND, null);
					iv.setImageBitmap(bm);
					return true;
				}
				return false;
			}
		});
		gridView.setAdapter(mAdapter);
		
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Uri uri = ContentUris.withAppendedId(Images.Media.EXTERNAL_CONTENT_URI, id);
				Intent data = new Intent();
				data.setData(uri);
				Cursor c = (Cursor)gridView.getItemAtPosition(position);
				String path = c.getString(c.getColumnIndex(Images.Media.DATA));
				data.putExtra("path", path);
				setResult(Activity.RESULT_OK, data);
				finish();
			}
		});
		getSupportLoaderManager().initLoader(0, null, this);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pick, menu);
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

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new CursorLoader(this, Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, sortOrder);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		idColumn = data.getColumnIndex(Images.Media._ID);
		mAdapter.swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mAdapter.swapCursor(null);
	}
}

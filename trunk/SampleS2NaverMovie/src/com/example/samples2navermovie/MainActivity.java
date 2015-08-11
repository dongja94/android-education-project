package com.example.samples2navermovie;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.begentgroup.xmlparser.XMLParser;
import com.example.samples2navermovie.NetworkManager.OnResultListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class MainActivity extends ActionBarActivity {

	ListView listView;
	// ArrayAdapter<MovieItem> mAdapter;
	MyAdapter mAdapter;
	EditText inputView;
	boolean isUpdate = false;
	PullToRefreshListView refreshView;

	Handler mHandler = new Handler(Looper.getMainLooper());

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		inputView = (EditText) findViewById(R.id.edit_input);

		refreshView = (PullToRefreshListView) findViewById(R.id.listView1);

		refreshView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> rv) {
				String keyword = mAdapter.getKeyword();
				if (!TextUtils.isEmpty(keyword)) {
					int startIndex = mAdapter.getStartOffset();
					if (startIndex != MyAdapter.INVAILD_START_INDEX) {
						NetworkManager.getInstance().getNaverMovie(
								MainActivity.this, keyword, startIndex, 20,
								new OnResultListener<NaverMovie>() {

									@Override
									public void onSuccess(final NaverMovie result) {
										mHandler.postDelayed(new Runnable() {
											
											@Override
											public void run() {
												mAdapter.addAll(result.items);
												refreshView.onRefreshComplete();
											}
										}, 2000);
									}

									@Override
									public void onFail(int code) {
										refreshView.onRefreshComplete();
									}

								});
						return;
					}
				}
				mHandler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						refreshView.onRefreshComplete();
					}
				}, 20);
				// mHandler.postDelayed(new Runnable() {
				//
				// @Override
				// public void run() {
				// refreshView.onRefreshComplete();
				// }
				// }, 2000);

			}
		});

		listView = refreshView.getRefreshableView();
		// mAdapter = new ArrayAdapter<MovieItem>(this,
		// android.R.layout.simple_list_item_1);
		mAdapter = new MyAdapter();
		listView.setAdapter(mAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				MovieItem item = (MovieItem) listView
						.getItemAtPosition(position);
				Intent intent = new Intent(MainActivity.this,
						BrowserActivity.class);
				intent.setData(Uri.parse(item.link));
				startActivity(intent);
			}
		});

		// listView.setOnScrollListener(new OnScrollListener() {
		//
		// @Override
		// public void onScrollStateChanged(AbsListView view, int scrollState) {
		//
		// }
		//
		// @Override
		// public void onScroll(AbsListView view, int firstVisibleItem,
		// int visibleItemCount, int totalItemCount) {
		// if (firstVisibleItem + visibleItemCount >= totalItemCount) {
		// String keyword = mAdapter.getKeyword();
		// if (!TextUtils.isEmpty(keyword)) {
		// int startIndex = mAdapter.getStartOffset();
		// if (startIndex != MyAdapter.INVAILD_START_INDEX) {
		// if (!isUpdate) {
		// isUpdate = true;
		// NetworkManager.getInstance().getNaverMovie(
		// MainActivity.this, keyword, startIndex,
		// 20, new OnResultListener<NaverMovie>() {
		//
		// @Override
		// public void onSuccess(
		// NaverMovie result) {
		// mAdapter.addAll(result.items);
		// isUpdate = false;
		// }
		//
		// @Override
		// public void onFail(int code) {
		// isUpdate = false;
		// }
		//
		// });
		// }
		// }
		// }
		// }
		// }
		// });
		Button btn = (Button) findViewById(R.id.btn_search);
		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				final String keyword = inputView.getText().toString();
				if (!TextUtils.isEmpty(keyword)) {
					// new MovieTask().execute(keyword);
					NetworkManager.getInstance().getNaverMovie(
							MainActivity.this, keyword, 1, 20,
							new OnResultListener<NaverMovie>() {

								@Override
								public void onSuccess(NaverMovie result) {
									mAdapter.clear();
									mAdapter.addAll(result.items);
									mAdapter.setKeyword(keyword);
									mAdapter.setTotalCount(result.total);
								}

								@Override
								public void onFail(int code) {
									Toast.makeText(MainActivity.this,
											"fail....", Toast.LENGTH_SHORT)
											.show();
								}
							});
					// NetworkManager.getInstance().getNaverMovie(keyword, new
					// OnResultListener<NaverMovie>() {
					//
					// @Override
					// public void onSuccess(NaverMovie result) {
					// // for (MovieItem item : result.items) {
					// // mAdapter.add(item);
					// // }
					// mAdapter.addAll(result.items);
					// }
					//
					// @Override
					// public void onFail(int code) {
					// Toast.makeText(MainActivity.this, "fail...",
					// Toast.LENGTH_SHORT).show();
					// }
					// });
				}
			}
		});
	}

	class MovieTask extends AsyncTask<String, Integer, NaverMovie> {
		@Override
		protected NaverMovie doInBackground(String... params) {
			String format = "http://openapi.naver.com/search?key=55f1e342c5bce1cac340ebb6032c7d9a&query=%s&display=10&start=1&target=movie";
			String keyword = params[0];
			try {
				String urlText = String.format(format,
						URLEncoder.encode(keyword, "utf-8"));
				URL url = new URL(urlText);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				int code = conn.getResponseCode();
				if (code == HttpURLConnection.HTTP_OK) {
					InputStream is = conn.getInputStream();
					XMLParser parser = new XMLParser();
					NaverMovie movie = parser.fromXml(is, "channel",
							NaverMovie.class);
					return movie;
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(NaverMovie result) {
			super.onPostExecute(result);
			if (result != null) {
				mAdapter.addAll(result.items);
				// for (MovieItem item : result.items) {
				// mAdapter.add(item);
				// }
			}
		}
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

package com.example.samples2tmap;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapData.FindAllPOIListenerCallback;
import com.skp.Tmap.TMapData.FindPathDataListenerCallback;
import com.skp.Tmap.TMapMarkerItem;
import com.skp.Tmap.TMapPOIItem;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapPolyLine;
import com.skp.Tmap.TMapView;
import com.skp.Tmap.TMapView.OnCalloutRightButtonClickCallback;
import com.skp.Tmap.TMapView.OnClickListenerCallback;


public class MainActivity extends ActionBarActivity {

	TMapView mapView;
	LocationManager mLM;
	String mProvider = LocationManager.GPS_PROVIDER;
	EditText keywordView;
	ListView listView;
	ArrayAdapter<POIItem> mAdapter;
	RadioGroup group;
	
	TMapPoint startPoint, endPoint;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        keywordView = (EditText)findViewById(R.id.edit_keyword);
        listView = (ListView)findViewById(R.id.listView1);
        group = (RadioGroup)findViewById(R.id.radioGroup1);
        mAdapter = new ArrayAdapter<POIItem>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				POIItem item = (POIItem)listView.getItemAtPosition(position);
				moveMap(item.poi.getPOIPoint().getLatitude(), item.poi.getPOIPoint().getLongitude());
			}
		});
        mapView = (TMapView)findViewById(R.id.mapView);
        mLM = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        
        new InitTask().execute();
        
        Button btn = (Button)findViewById(R.id.btn_marker);
        btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				addMarker();
			}
		});
        
        btn = (Button)findViewById(R.id.btn_search);
        btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String keyword = keywordView.getText().toString();
				if (!TextUtils.isEmpty(keyword)) {
					searchKeyword(keyword);
				}
			}

		});
        
        btn = (Button)findViewById(R.id.btn_route);
        btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (startPoint != null && endPoint != null) {
					route(startPoint, endPoint);
				} else {
					Toast.makeText(MainActivity.this, "start or end null",Toast.LENGTH_SHORT).show();
				}
			}
		});
    }
    
    private void route(TMapPoint start, TMapPoint end) {
    	TMapData data = new TMapData();
    	data.findPathData(start, end, new FindPathDataListenerCallback() {
			
			@Override
			public void onFindPathData(final TMapPolyLine path) {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						mapView.addTMapPath(path);
					}
				});
			}
		});
    }
	private void searchKeyword(String keyword) {
		TMapData data = new TMapData();
		data.findAllPOI(keyword, new FindAllPOIListenerCallback() {
			
			@Override
			public void onFindAllPOI(final ArrayList<TMapPOIItem> poilist) {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						mapView.addTMapPOIItem(poilist);
						for (TMapPOIItem poi : poilist) {
							POIItem item = new POIItem();
							item.poi = poi;
							mAdapter.add(item);
						}
					}
				});
			}
		});
	}
    
    int id = 0;
    private void addMarker() {
    	TMapPoint point = mapView.getCenterPoint();
//    	TMapPoint pt = new TMapPoint(point.getLatitude(), point.getLongitude());
    	TMapMarkerItem item = new TMapMarkerItem();
    	
    	item.setTMapPoint(point);
    	Bitmap bitmap = ((BitmapDrawable)getResources().getDrawable(R.drawable.ic_launcher)).getBitmap();
    	item.setIcon(bitmap);
    	item.setPosition(0.5f, 1);
    	item.setCalloutTitle("my marker");
    	item.setCalloutSubTitle("marker subtitle");
    	item.setCanShowCallout(true);
    	Bitmap left = ((BitmapDrawable)getResources().getDrawable(android.R.drawable.ic_dialog_info)).getBitmap();
    	item.setCalloutLeftImage(left);
    	
    	Bitmap right = ((BitmapDrawable)getResources().getDrawable(android.R.drawable.ic_input_get)).getBitmap();
    	item.setCalloutRightButtonImage(right);
    	
    	mapView.addMarkerItem("id" + id++, item);
    }
    
    class InitTask extends AsyncTask<String, Integer, Boolean> {
    	@Override
    	protected Boolean doInBackground(String... params) {
    		mapView.setSKPMapApiKey("458a10f5-c07e-34b5-b2bd-4a891e024c2a");    		
    		return true;
    	}
    	
    	@Override
    	protected void onPostExecute(Boolean result) {
    		super.onPostExecute(result);
    		if (result == true) {
    			setupMap();
    		}
    	}
    }
    
    private void setupMap() {
    	mapView.setTrafficInfo(true);
    	mapView.setMapType(TMapView.MAPTYPE_STANDARD);
    	mapView.setLanguage(TMapView.LANGUAGE_KOREAN);
    	mapView.setOnCalloutRightButtonClickListener(new OnCalloutRightButtonClickCallback() {
			
			@Override
			public void onCalloutRightButton(TMapMarkerItem item) {
				Toast.makeText(MainActivity.this, "item : " + item.getCalloutTitle(), Toast.LENGTH_SHORT).show();
			}
		});
    	
    	mapView.setOnClickListenerCallBack(new OnClickListenerCallback() {
			
			@Override
			public boolean onPressUpEvent(ArrayList<TMapMarkerItem> arg0,
					ArrayList<TMapPOIItem> arg1, TMapPoint arg2, PointF arg3) {
				if (arg1 != null && arg1.size() > 0) {
					TMapPoint point = arg1.get(0).getPOIPoint();
					switch(group.getCheckedRadioButtonId()) {
					case R.id.radio_start :
						startPoint = point;
						break;
					case R.id.radio_end :
						endPoint = point;
						break;
					}
				}
				return false;
			}
			
			@Override
			public boolean onPressEvent(ArrayList<TMapMarkerItem> arg0,
					ArrayList<TMapPOIItem> arg1, TMapPoint arg2, PointF arg3) {
				Toast.makeText(MainActivity.this, "press", Toast.LENGTH_SHORT).show();
				return false;
			}
		});
    }

    LocationListener mListener = new LocationListener() {
		
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			
		}
		
		@Override
		public void onProviderEnabled(String provider) {
			
		}
		
		@Override
		public void onProviderDisabled(String provider) {
			
		}
		
		@Override
		public void onLocationChanged(Location location) {
			moveMap(location.getLatitude(), location.getLongitude());
			mLM.removeUpdates(this);
		}
	};
	
	private void moveMap(double lat, double lng) {
		mapView.setCenterPoint(lng, lat);
		mapView.setZoom(17);
	}
	
    @Override
    protected void onStart() {
    	super.onStart();
    	Location last = mLM.getLastKnownLocation(mProvider);
    	if (last != null) {
    		mListener.onLocationChanged(last);
    	}
    	mLM.requestLocationUpdates(mProvider, 0, 0, mListener);
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    	mLM.removeUpdates(mListener);
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

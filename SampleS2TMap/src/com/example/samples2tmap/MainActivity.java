package com.example.samples2tmap;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.skp.Tmap.TMapView;


public class MainActivity extends ActionBarActivity {

	TMapView mapView;
	LocationManager mLM;
	String mProvider = LocationManager.GPS_PROVIDER;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mapView = (TMapView)findViewById(R.id.mapView);
        mLM = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        
        new InitTask().execute();
        
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

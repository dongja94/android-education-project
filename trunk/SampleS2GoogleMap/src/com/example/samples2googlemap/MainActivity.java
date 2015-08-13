package com.example.samples2googlemap;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends ActionBarActivity implements
		GoogleMap.OnMapClickListener,
		GoogleMap.OnMarkerClickListener,
		GoogleMap.OnInfoWindowClickListener {

	GoogleMap mMap;
	LocationManager mLM;
	String mProvider = LocationManager.GPS_PROVIDER;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setupMapIfNeeded();
		mLM = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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
			mLM.removeUpdates(mListener);
		}
	};

	private void moveMap(double lat, double lng) {
		if (mMap != null) {
			CameraPosition.Builder builder = new CameraPosition.Builder();
			builder.target(new LatLng(lat, lng));
			builder.zoom(17f);
			// builder.bearing(30);
			// builder.tilt(30);
			CameraPosition postion = builder.build();

			CameraUpdate update = CameraUpdateFactory
					.newCameraPosition(postion);

			mMap.moveCamera(update);
		}
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
	protected void onResume() {
		super.onResume();
		setupMapIfNeeded();
	}

	private void setupMapIfNeeded() {
		if (mMap == null) {
			SupportMapFragment f = (SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.fragment1);
			mMap = f.getMap();
			if (mMap != null) {
				setupMap();
			}
		}
	}

	private void setupMap() {
		mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		mMap.getUiSettings().setZoomControlsEnabled(true);
		mMap.setMyLocationEnabled(true);
		mMap.setOnMapClickListener(this);
		mMap.setOnMarkerClickListener(this);
		mMap.setOnInfoWindowClickListener(this);
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

	@Override
	public void onMapClick(LatLng latLng) {
		addMarker(latLng.latitude, latLng.longitude);
	}

	private void addMarker(double lat, double lng) {
		MarkerOptions options = new MarkerOptions();
		options.position(new LatLng(lat, lng));
		options.icon(BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
		options.anchor(0.5f, 1);
		options.title("My Marker");
		options.snippet("description...");
		options.draggable(true);
		Marker marker = mMap.addMarker(options);
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		Toast.makeText(this, marker.getTitle(), Toast.LENGTH_SHORT).show();
		marker.showInfoWindow();
		return true;
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		Toast.makeText(this, "infowindow : " + marker.getTitle(), Toast.LENGTH_SHORT).show();
		marker.hideInfoWindow();
	}
}

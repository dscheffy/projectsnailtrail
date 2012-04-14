package com.hello.jefferson;

import org.projectsnailtrail.writable.TrackPoint;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class TrackPointViewerActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setListAdapter(new ArrayAdapter<TrackPoint>(this, R.layout.location_list_item, TrackPointManager.getInstance().getAllPoints()));
		ListView lv = getListView();
		lv.setStackFromBottom(true);
//		String[] bla = {"hello","world","you","cruel","beast","oh","how","I","love","you"};
//		setListAdapter(new ArrayAdapter<String>(this, R.layout.location_list_item, bla));

	}
	

}

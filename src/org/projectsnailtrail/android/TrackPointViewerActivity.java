package org.projectsnailtrail.android;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TrackPointViewerActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		try{
			File[] files = TrackPointManager.getInstance().getTrailFileNames();
		setListAdapter(new ArrayAdapter<File>(this, R.layout.location_list_item, files));
		
		}catch (IOException ioe){
			throw new RuntimeException(ioe);
			//bla bla bla TODO have to do something here...
		}
//		String[] bla = {"hello","world","you","cruel","beast","oh","how","I","love","you"};
//		setListAdapter(new ArrayAdapter<String>(this, R.layout.location_list_item, bla));

	}
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        File name = (File) l.getItemAtPosition(position);

        Intent intent = new Intent(MapViewActivity.ACTION_MAIN, Uri.fromFile(name));
//        intent.setClass(this, MapViewActivity.class);
        startActivity(intent);
    }


}

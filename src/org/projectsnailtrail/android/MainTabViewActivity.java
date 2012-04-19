package org.projectsnailtrail.android;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class MainTabViewActivity extends TabActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.main);

	    Resources res = getResources(); // Resource object to get Drawables
	    TabHost tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab

	    // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, ProjectSnailTrailActivity.class);

	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec("main").setIndicator("Configure",
	                      res.getDrawable(R.drawable.snail_tab))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    // Do the same for the other tabs
	    intent = new Intent().setClass(this, TrackPointViewerActivity.class);
	    spec = tabHost.newTabSpec("list").setIndicator("History",
                res.getDrawable(R.drawable.snail_tab))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    // Do the same for the other tabs
	    intent = new Intent().setClass(this, MapViewActivity.class);
	    spec = tabHost.newTabSpec("map").setIndicator("Map",
                res.getDrawable(R.drawable.snail_tab))
	                  .setContent(intent);
	    tabHost.addTab(spec);


	    tabHost.setCurrentTab(0);
	}	

}
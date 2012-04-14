package com.hello.jefferson;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class TrackPointViewerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TextView textView = new TextView(this);
		textView.setText("this page intentionally left blank =)");
		setContentView(textView);
	}
	

}

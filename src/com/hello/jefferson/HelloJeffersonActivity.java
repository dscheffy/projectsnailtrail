package com.hello.jefferson;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HelloJeffersonActivity extends Activity implements OnClickListener {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config_tab);
    	Button buttonOn = (Button)findViewById(R.id.buttonOn);
    	buttonOn.setOnClickListener(this);
    	Button buttonOff = (Button)findViewById(R.id.buttonOff);    
    	buttonOff.setOnClickListener(this);
    }

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
			case R.id.buttonOff: {
				WakeUpBroadcastReceiver.disable(this);
				break;
			}
			case R.id.buttonOn: {
				WakeUpBroadcastReceiver.enable(this);
				break;
			}
		}
		
	}
    
}
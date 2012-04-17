package com.hello.jefferson;

import java.util.ArrayList;

import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class TrackPointOverlay extends ItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> overlays = new ArrayList<OverlayItem>();
	public TrackPointOverlay(Drawable defaultMarker) {
		super(boundCenter(defaultMarker));
		// TODO Auto-generated constructor stub
	}
	@Override
	protected OverlayItem createItem(int i) {
		return overlays.get(i);
	}
	@Override
	public int size() {
		// TODO Auto-generated method stub
		return overlays.size();
	}
	public void addOverlay(OverlayItem overlay){
		overlays.add(overlay);
		populate();
	}

	
}

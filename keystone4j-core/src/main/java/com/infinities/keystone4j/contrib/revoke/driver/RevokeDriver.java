package com.infinities.keystone4j.contrib.revoke.driver;

import java.util.Calendar;
import java.util.List;

import com.infinities.keystone4j.contrib.revoke.model.RevokeEvent;

public interface RevokeDriver {

	// lastFetch=null
	List<RevokeEvent> getEvents(Calendar lastFetch);

	void revoke(RevokeEvent event);

}

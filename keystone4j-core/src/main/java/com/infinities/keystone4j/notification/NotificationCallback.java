package com.infinities.keystone4j.notification;

import com.infinities.keystone4j.notification.Notifications.Payload;

public interface NotificationCallback {

	// String getName();

	void invoke(String service, String resourceType, Actions operation, Payload payload) throws Exception;

}

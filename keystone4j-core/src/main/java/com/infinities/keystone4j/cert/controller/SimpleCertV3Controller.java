package com.infinities.keystone4j.cert.controller;

import javax.ws.rs.core.Response;

public interface SimpleCertV3Controller {

	Response getCaCertificate();

	Response listCertificate();
}

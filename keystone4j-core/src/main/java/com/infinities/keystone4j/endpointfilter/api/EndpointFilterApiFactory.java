//package com.infinities.keystone4j.endpointfilter.api;
//
//import javax.inject.Inject;
//
//import org.glassfish.hk2.api.Factory;
//
//import com.infinities.keystone4j.endpointfilter.EndpointFilterApi;
//import com.infinities.keystone4j.endpointfilter.EndpointFilterDriver;
//import com.infinities.keystone4j.extension.ExtensionApi;
//
//public class EndpointFilterApiFactory implements Factory<EndpointFilterApi> {
//
//	private final EndpointFilterDriver endpointFilterDriver;
//	private final ExtensionApi extensionApi;
//
//
//	@Inject
//	public EndpointFilterApiFactory(EndpointFilterDriver endpointFilterDriver, ExtensionApi extensionApi) {
//		this.endpointFilterDriver = endpointFilterDriver;
//		this.extensionApi = extensionApi;
//	}
//
//	@Override
//	public void dispose(EndpointFilterApi arg0) {
//
//	}
//
//	@Override
//	public EndpointFilterApi provide() {
//		return new EndpointFilterApiImpl(endpointFilterDriver, extensionApi);
//	}
//
// }

package edu.mayo.cts2.framework.service.provider;

import edu.mayo.cts2.framework.service.profile.Cts2Profile;

public class EmptyServiceProvider implements ServiceProvider {

	public <T extends Cts2Profile> T getService(Class<T> serviceClass) {
		return null;
	}

}

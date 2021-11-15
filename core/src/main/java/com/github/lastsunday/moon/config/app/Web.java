package com.github.lastsunday.moon.config.app;

public class Web {

	private Root root = new Root();
	private Resource app = new Resource();
	private Resource miniApp = new Resource();
	private Resource admin = new Resource();
	private Resource repository = new Resource();

	public Root getRoot() {
		return root;
	}

	public void setRoot(Root root) {
		this.root = root;
	}

	public Resource getApp() {
		return app;
	}

	public void setApp(Resource app) {
		this.app = app;
	}

	public Resource getMiniApp() {
		return miniApp;
	}

	public void setMiniApp(Resource miniApp) {
		this.miniApp = miniApp;
	}

	public Resource getAdmin() {
		return admin;
	}

	public void setAdmin(Resource admin) {
		this.admin = admin;
	}

	public Resource getRepository() {
		return repository;
	}

	public void setRepository(Resource repository) {
		this.repository = repository;
	}

	public class Resource {
		private boolean enable = true;

		public boolean isEnable() {
			return enable;
		}

		public void setEnable(boolean enable) {
			this.enable = enable;
		}

	}

	public class Root{
		private String redirect = "/app";

		public String getRedirect() {
			return redirect;
		}

		public void setRedirect(String redirect) {
			this.redirect = redirect;
		}
	}


}

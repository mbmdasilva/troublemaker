(function (TR, jQuery) {

	function ServiceClient() {
		this._configuration = {
			"headers": {
				"Accept" : "application/json; charset=utf-8",
				"Content-Type": "application/json; charset=utf-8"
			}
		};
	}

	ServiceClient.prototype._configuration = null;

	ServiceClient.prototype.getAllNamespaces = function () {
		if (TR.useMockData) {
			return Promise.resolve(TR.mockData.namespaces);
		} else {

		}
	};

	ServiceClient.prototype.getAplicationsForNamespace = function (namespace) {
		if (TR.useMockData) {
			return Promise.resolve(TR.mockData.applications[namespace]);
		} else {

		}
	};

	ServiceClient.prototype.getServersForNamespaceAndApplication = function (namespace, application) {
		if (TR.useMockData) {
			return Promise.resolve(TR.mockData.servers[namespace][application]);
		} else {

		}
	};

	ServiceClient.prototype.sendActionToServer = function (server, action) {
		if (TR.useMockData) {
			return Promise.resolve(TR.mockData.actionResponse[server][action]);
		} else {

		}
	};

	ServiceClient.prototype.getLogs = function (server, application) {
		if (TR.useMockData) {
			return Promise.resolve(TR.mockData.logs[server][application]);
		} else {

		}
	};

	TR.serviceClient = new ServiceClient();
})(TR, jQuery);
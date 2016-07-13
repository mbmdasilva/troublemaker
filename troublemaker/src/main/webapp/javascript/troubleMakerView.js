(function (TR, jQuery) {

	function TroubleMakerView () {}


	/************************** PROPERTY DECLARATION *********************************/

	TroubleMakerView.prototype._dApplications = null;
	TroubleMakerView.prototype._dNamespacesSearch = null;
	TroubleMakerView.prototype._dActions = null;
	TroubleMakerView.prototype._dLogs = null;
	TroubleMakerView.prototype._dLogsName = null;
	TroubleMakerView.prototype._dActionStatus = null;

	TroubleMakerView.prototype._namespaceSelected = null;
	TroubleMakerView.prototype._applicationSelected = null;
	TroubleMakerView.prototype._serverSelected = null;



	/******************************** INITIALISATION *********************************/

	TroubleMakerView.prototype.initialise = function () {
		this._cashDomElements();
		this._attachDomEvents();

		TR.serviceClient.getAllNamespaces().then(this._setNamespacesSearchHandler.bind(this));
	};

	TroubleMakerView.prototype._cashDomElements = function () {
		this._dApplications = jQuery("#applications");
		this._dActions = jQuery("#actions");
		this._dLogs = jQuery("#logs");
		this._dNamespacesSearch = jQuery("#namespacesSearch");
		this._dLogsName = jQuery("#logsName");
		this._dActionStatus = jQuery("#actionStatus");
	};

	TroubleMakerView.prototype._attachDomEvents = function () {
		this._dApplications.on("click", "button", this._onServerSelected.bind(this));
		this._dActions.on("click", "button", this._onAction.bind(this));
	};

	TroubleMakerView.prototype._setNamespacesSearchHandler = function (namespaces) {
		this._dNamespacesSearch.autocomplete({
			source: namespaces,
			select: this._onNamespaceSelected.bind(this)
		});
	};


	/************************** INTERACTION HANDLERS *********************************/

	TroubleMakerView.prototype._onNamespaceSelected = function (event, selected) {
		this._namespaceSelected = selected.item.value;

		TR.serviceClient.getAplicationsForNamespace(this._namespaceSelected)
			.then(this._renderApplications.bind(this));
	};

	TroubleMakerView.prototype._onApplicationSelected = function (event, selected) {
		if(this._dApplications.accordion("option", "active") !== false) {
			this._applicationSelected = selected.newHeader.attr("id");

			TR.serviceClient.getServersForNamespaceAndApplication(
					this._namespaceSelected,
					this._applicationSelected)
				.then(this._renderServers.bind(this, selected.newPanel));
		} else {
			this._dLogs.empty();
			this._dLogsName.text("");
		}
	};

	TroubleMakerView.prototype._onServerSelected = function (event) {
		this._serverSelected = jQuery(event.target).attr("id");
		this._dActions.removeClass("disableActions");
		TR.serviceClient.getLogs(this._serverSelected, this._applicationSelected)
			.then(this._renderLogs.bind(this));
	};

	TroubleMakerView.prototype._onAction = function (event) {
		var action = jQuery(event.target).attr("id");

		TR.serviceClient.sendActionToServer(this._serverSelected, action)
			.then(this._renderActionStatus.bind(this, action));
	};



	/************************************* RENDERERS *********************************/

	TroubleMakerView.prototype._renderApplications = function (applications) {
		if (this._dApplications.children().length) {
			this._dApplications.empty();
			this._dApplications.accordion("destroy");
		}

		applications.forEach(function (appName) {
			this._dApplications.append(jQuery("<label>").text(appName).attr("id", appName));
			this._dApplications.append(jQuery("<ul>"));
		}, this);

		this._dApplications.accordion({
			"active": false,
			"collapsible": true,
			"activate": this._onApplicationSelected.bind(this)
		});
	};

	TroubleMakerView.prototype._renderServers = function (dContainer, servers) {
		var dListElem;

		//Has not been rendered yet
		if(dContainer.children().length === 0 ) {
			servers.forEach(function (serverName) {
				dListElem = jQuery("<li>").addClass("server");
				dListElem.append(jQuery("<button>").text(serverName).attr("id", serverName));
				dContainer.append(dListElem);
			});
		}

		dContainer.selectable();
	};

	TroubleMakerView.prototype._renderLogs = function (logs) {
		this._dLogs.empty();

		this._dLogsName.html("namespace <span>" + this._namespaceSelected
			+ "</span>, application <span>" + this._applicationSelected
			+ "</span>, server <span>" + this._serverSelected
			+ "</span>");

		logs.split("\n").forEach(function (line) {
			jQuery("<p>").text(line).appendTo(this._dLogs);
		}, this);
	};

	TroubleMakerView.prototype._renderActionStatus = function (action, actionStatus) {
		this._dActionStatus.html("<span>" + actionStatus + "</span> for action <span>" + action);
	};

	TR.troubleMakerView = new TroubleMakerView();
})(TR, jQuery);

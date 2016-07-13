(function (TR) {

	TR.mockData = {
		"namespaces": ["namespace1", "namespace2", "namespace3"],
		"applications": {
			"namespace1": ["np1-app1", "np1-app2", "np1-app3"],
			"namespace2": ["np2-app1", "np2-app2", "np2-app3"],
			"namespace3": [
				"np3-app1", "np3-app2", "np3-app3",
				"np3-app4", "np3-app5", "np3-app6",
				"np3-app7", "np3-app8", "np3-app9",
				"np3-app10", "np3-app11", "np3-app12",
				"np3-app13", "np3-app14", "np3-app15"
			],
		},
		"servers": {
			"namespace1": {
				"np1-app1": ["serverA", "serverB"],
				"np1-app2": ["serverA", "serverB"],
				"np1-app3": ["serverA", "serverB"]
			},
			"namespace2": {
				"np2-app1": ["serverC", "serverA"],
				"np2-app2": ["serverC", "serverA"],
				"np2-app3": ["serverC", "serverA"]
			},
			"namespace3": {
				"np3-app1": ["serverB", "serverD"],
				"np3-app2": ["serverB", "serverD"],
				"np3-app3": ["serverB", "serverD"]
			}
		},
		"actionResponse": {
			"serverA": {
				"stop": "Server stopped",
				"start": "Server not started correctly",
				"restart": "Server restarted properly"
			},
			"serverB": {
				"stop": "Server could not stop",
				"start": "Server started",
				"restart": "Server restarted"
			},
			"serverC": {
				"stop": "Server could not stop",
				"start": "Server started",
				"restart": "Server restarted"
			},
			"serverD": {
				"stop": "Server could not stop",
				"start": "Server started",
				"restart": "Server restarted"
			}
		},
		"logs": {
			"serverA": {
				"np1-app1" : " This is a long \n text in few lines",
				"np1-app2" : " This is a long \n text in few lines",
				"np1-app3" : " This is a long \n text in few lines",
				"np2-app1" : " This is a long \n text in few lines",
				"np2-app2" : " This is a long \n text in few lines",
				"np2-app3" : " This is a long \n text in few lines"
			},
			"serverB": {
				"np1-app1" : " This is a long \n text in few lines",
				"np1-app2" : " This is a long \n text in few lines",
				"np1-app3" : " This is a long \n text in few lines",
				"np3-app1" : " This is a long \n text in few lines",
				"np3-app2" : " This is a long \n text in few lines",
				"np3-app3" : " This is a long \n text in few lines"
			},
			"serverC": {
				"np2-app1" : " This is a long \n text in few lines",
				"np2-app2" : " This is a long \n text in few lines",
				"np2-app3" : " This is a long \n text in few lines"
			},
			"serverD": {
				"np3-app1" : " This is a long \n text in few lines",
				"np3-app2" : " This is a long \n text in few lines",
				"np3-app3" : " This is a long \n text in few lines"
			}
		}
	}
})(TR);
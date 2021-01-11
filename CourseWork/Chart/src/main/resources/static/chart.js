	let OPEARATIONS = ['RegisterClient','UserData','FilteredUsersData','SaveUser', 'DeleteUser', 'OperationStatus'];
	let color = Chart.helpers.color;
	let barChartData = {
		labels: ['RegisterClient', 'UserData', 'FilteredUsersData', 'SaveUser', 'DeleteUser', 'OperationStatus'],
		datasets: []
	};

	const connect = () => {
		console.log('In Connected function');
		stompClient = Stomp.over(new SockJS('/gs-chart-websocket'));
		console.log('After Stomp.over');
		stompClient.connect({}, (frame) => {
			console.log('Connected: ' + frame);
			stompClient.subscribe('/topic/chartdata', (operationsDataJson) => fillChart(JSON.parse(operationsDataJson.body)));
		});
	}

	const sendGetChartDataMsg = () => {
		stompClient.send("/chartdata/askchartdata", {}, {});
	}

	let colorNames = Object.keys(window.chartColors);
	const fillChart = (operationsData) => {
		console.log("fillChart param: "+JSON.stringify(operationsData));
		console.log("fillChart operationsData.length = " + operationsData.length);
		barChartData.datasets = [];

		for(let opIdx = 0; opIdx < operationsData.length; opIdx++ ) {
			console.log("opIdx = " + opIdx);
			let colorName = colorNames[opIdx];
			let dsColor = window.chartColors[colorName];
			let newDataset = {
				label: operationsData[opIdx].moduleName,
				backgroundColor: color(dsColor).alpha(0.5).rgbString(),
				borderColor: dsColor,
				borderWidth: 1,
				data: []
			};

			let arr = new Array(barChartData.labels.length);
			for(const [key, value] of Object.entries(operationsData[opIdx].chartData)) {
				for (let index = 0; index < barChartData.labels.length; index++) {
					if (barChartData.labels[index] === key) {
						arr[index] = value;
					}
				}
			}
			newDataset.data = arr;
			barChartData.datasets.push(newDataset);
		}
		window.myBar.update();
	}

	window.onload = function() {
		let ctx = document.getElementById('canvas').getContext('2d');
		window.myBar = new Chart(ctx, {
			type: 'bar',
			data: barChartData,
			options: {
				responsive: true,
				legend: {
					position: 'top',
				},
				title: {
					display: true,
					text: 'Online operations observer'
				},
				scales: {
					yAxes: [{
						ticks: {
							beginAtZero: true
						}
					}]
				}
			}
		});
		connect();
	};

	document.getElementById("reconnect").addEventListener('click', function(){
		connect();
	});

	document.getElementById('getData').addEventListener('click', function() {
		console.log("getData");
		sendGetChartDataMsg();
	});

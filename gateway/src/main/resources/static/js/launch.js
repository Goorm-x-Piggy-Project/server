var global = {
    mobileClient: false,
    savePermit: true,
    usd: 0,
    krw: 0
};

/**
 * Oauth2
 */

function requestOauthToken(username, password) {

	var success = false;

	$.ajax({
		url: 'oauth2/token',
		datatype: 'json',
		type: 'post',
		headers: {'Authorization': 'Basic YnJvd3Nlcjpicm93c2VyLXBhc3N3b3Jk'},
		async: false,
		data: {
			scope: 'ui',
			username: username,
			password: password,
			grant_type: 'password'
		},
		success: function (data) {
			localStorage.setItem('token', data.access_token);
			success = true;
		},
		error: function () {
			removeOauthTokenFromStorage();
		}
	});

	return success;
}

function getOauthTokenFromStorage() {
	return localStorage.getItem('token');
}

function removeOauthTokenFromStorage() {
    return localStorage.removeItem('token');
}

/**
 * Current account
 */

function getCurrentAccount() {

	var token = getOauthTokenFromStorage();
	var account = null;

	if (token) {
		$.ajax({
			url: 'api/v1/account/current',
			datatype: 'json',
			type: 'get',
			headers: {'Authorization': 'Bearer ' + token},
			async: false,
			success: function (data) {
				account = data;
			},
			error: function () {
				removeOauthTokenFromStorage();
			}
		});
	}

	return account;
}

$(window).load(function(){

	if(/Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent) ) {
		FastClick.attach(document.body);
        global.mobileClient = true;
	}

    $.getJSON("api/v1/statistics/rates/default", function( data ) {
        global.krw = 1 / data.KRW;
        global.usd = 1 / data.USD;
    });

	var account = getCurrentAccount();

	if (account) {
		showGreetingPage(account);
	} else {
		showLoginForm();
	}
});

function showGreetingPage(account) {
    initAccount(account);
	var userAvatar = $("<img />").attr("src","images/userpic.jpg");
	$(userAvatar).load(function() {
		setTimeout(initGreetingPage, 500);
	});
}

function showLoginForm() {
	$("#loginpage").show();
	$("#frontloginform").focus();
	setTimeout(initialShaking, 700);
}
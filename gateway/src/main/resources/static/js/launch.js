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
			localStorage.setItem('access_token', data.access_token);
			if (data.refresh_token) {
				localStorage.setItem('refresh_token', data.refresh_token);
				console.log("리프레시 토큰 저장");
			}
			success = true;
		},
		error: function () {
			removeOauthTokenFromStorage();
		}
	});

	return success;
}

function isTokenExpired(token) {
	var payload = JSON.parse(atob(token.split('.')[1])); // JWT payload 디코딩
	var currentTime = Math.floor(Date.now() / 1000);
	console.log("액세스 토큰 만료 체크: 만료 시간: " + payload.exp + ", 현재 시간: " + currentTime);
	return payload.exp < currentTime;
}

function refreshAccessToken() {
	var success = false;
	var refreshToken = localStorage.getItem('refresh_token');

	if (!refreshToken) {
		// console.error('리프레시 토큰이 없습니다.');
		return success;
	}

	$.ajax({
		url: 'oauth2/token',
		datatype: 'json',
		type: 'post',
		headers: {'Authorization': 'Basic YnJvd3Nlcjpicm93c2VyLXBhc3N3b3Jk'}, // clientId:clientSecret
		async: false,
		data: {
			grant_type: 'refresh_token',
			username: username,
			password: password,
			refresh_token: refreshToken,
			scope: 'ui'
		},
		success: function (data) {
			// 새 액세스 토큰 및 리프레시 토큰 저장
			localStorage.setItem('access_token', data.access_token);
			if (data.refresh_token) {
				localStorage.setItem('refresh_token', data.refresh_token);
				// console.log("저장된 refresh token: " + data.refresh_token); // 추후 제거
			}
			success = true;
		},
		error: function () {
			console.error('리프레시 토큰으로 새 액세스 토큰을 갱신하지 못했습니다.');
			removeOauthTokenFromStorage();
		}
	});

	return success;
}

function getAccessTokenFromStorage() {
	var accessToken = localStorage.getItem('access_token');
	if(!accessToken) {
		return null;
	} else if (isTokenExpired(accessToken)) {
		if (!refreshAccessToken()) {
			return null;
		}
		accessToken = localStorage.getItem('access_token');
	}

	return accessToken;
}

function removeOauthTokenFromStorage() {
	localStorage.removeItem('refresh_token');
    return localStorage.removeItem('access_token');
}

/**
 * Current account
 */

function getCurrentAccount() {

	var token = getAccessTokenFromStorage();
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
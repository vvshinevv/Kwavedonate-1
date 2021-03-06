/* Theme Name: KWAVE D Web Service
 * Author: Kim Da Hye
 * Author URI: http://kwavedonate.com
 * Author e-mail: dahye432111@kwavedonate.com
 * Created: Jan 2017
 * File Description: Custom scripts
 */

window.fbAsyncInit =function(){
	FB.init({
		appId:'453712568293707',
		cookie:true,
		xfbml:true,
		version:'v2.8'
	});
};

(function(d, s, id){
	var js, fjs = d.getElementsByTagName(s)[0];
	if(d.getElementById(id)){return;}
	js = d.createElement(s); js.id=id;
	js.src = "//connect.facebook.net/ko_KR/sdk.js";
	fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));


	if($("#facebookLoginBtn").length>0) {
		$("#facebookLoginBtn").click(function(event){
		
			FB.getLoginStatus(function(response) {
				 FB.login(function(response) {
				  if (response.status === 'connected') {
				    console.log(response.authResponse.accessToken);
				    FB.api('/me', {fields: 'name,email'}, function(user){
				    	if(user.email == null){
				    		alert(redirectSignIn);
				    		location.replace("/signin");
				    	}else{
				    		$.ajax({
			                    type: "POST",
			                    url: "/FacebookValidate", 
			                    data: {
			                        "userEmail" : user.email,
			                        "userName" : user.name
			                    },
			                    dataType: "json",
			                    success: function(data) {
			                       if(data.KEY == "SUCCESS"){
			                    	   wrapWindowByMask();
			                    	   //처음 페이스북 로그인시
			                    	   $.ajax({
			                           	type: "POST",
			                               url: "/j_spring_security_check",
			                               data: {
			                                   "username": user.email,
			                                   "password":user.email+user.name+"1@#$@#!$$$#@"
			                               },
			                               dataType: "json",
			                               success: function(data) {
			                               	//성공 시 데이터 처리 
			                            	   if(data.KEY=="SUCCESS") {
			                               			if(data.RETURNURI.substring(0, 5)=="?lang") {
			                               				location.replace("/");
			                               			} else {
			                               				location.replace(data.RETURNURI);
			                               			}
			                                   } else if(data.KEY=="SUCCESS_ADMIN") {
			                                   	  	location.replace(data.RETURNURI);
			                                   } else {
			                                	   alert(failToLogin);
			                                	   location.replace("/login?fail");
			                                   }
			                                }
			                            });
			                       }else{
	   		                          alert(alreadyRegister);
	   		                          location.replace("/login");
			                       }
			                    }
			                });
				    	}
				    });
				  } 
				},{scope: 'public_profile, email'});
			});
		});
	}

function wrapWindowByMask() {
    //화면의 높이와 너비를 구한다.
    var maskHeight = $(document).height(); 
    var maskWidth = window.document.body.clientWidth;
     
    var mask = "<div id='mask' style='position:absolute; z-index:9000; background-color:#000000; display:none; left:0; top:0;'></div>";
    var loadingImg = '';
     
    loadingImg += "<div id='loadingImg' style='position:absolute; left:50%; top:40%; display:none; z-index:10000;'>";
    loadingImg += " <img src='resources/images/viewLoading.gif'/>";
    loadingImg += "</div>";  
 
    //화면에 레이어 추가
    $('body')
        .append(mask)
        .append(loadingImg)
       
    //마스크의 높이와 너비를 화면 것으로 만들어 전체 화면을 채운다.
    $('#mask').css({
            'width' : maskWidth
            , 'height': maskHeight
            , 'opacity' : '0.3'
    }); 
 
    //마스크 표시
    $('#mask').show();   
 
    //로딩중 이미지 표시
    $('#loadingImg').show();
}

function closeWindowByMask() {
    $('#mask, #loadingImg').hide();
    $('#mask, #loadingImg').remove();  
}




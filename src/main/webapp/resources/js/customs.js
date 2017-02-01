/* Theme Name: KWAVE D Web Service
 * Author: Choi Hong Hee(Adler)
 * Author URI: http://kwavedonate.com
 * Author e-mail: vvshinevv@naver.com
 * Created: Jan 2017
 * File Description: Custom scripts
 */
$(document).ready(function() {


    /* banner custom */
    if($(".campaigns-banner").length>0) {
        if (Modernizr.touch) {
            $(".campaigns-banner").vide({
                poster: "resources/images/campaigns_banner.jpg"
            }, {
                position: "50% 50%", 
                posterType: "jpg", 
                resizing: true
            });
        } else {
            $(".campaigns-banner").vide({
                poster: "resources/images/campaigns_banner.jpg"
            }, {
                position: "50% 50%",
                posterType: "jpg", 
                resizing: true
            });
        };
    };


    if($(".aboutUs-banner").length>0) {
        if (Modernizr.touch) {
            $(".aboutUs-banner").vide({
                poster: "resources/images/aboutUs_banner.jpg"
            }, {
                position: "50% 50%", 
                posterType: "jpg", 
                resizing: true
            });
        } else {
            $(".aboutUs-banner").vide({
                poster: "resources/images/aboutUs_banner.jpg"
            }, {
                position: "50% 50%", 
                posterType: "jpg", 
                resizing: true
            });
        };
    };


    /* fixed aside custom */
    if($("aside.campaignEnterNow-web").length>0) {
        $("aside.campaignEnterNow-web").scrollToFixed({  
            
            marginTop: $('header').outerHeight(true) + 10,
            limit: function() {
                return $('.campaign-perks').offset().top - $(this).outerHeight(true);
            },
            zIndex: 999
        });
    }

    if($("div.campaignEnterNow-mobile").length>0) {
        $("div.campaignEnterNow-mobile-fixed").scrollToFixed({
            bottom: 0,
            limit: function() {
                return $('.campaign-perks').offset().top;
            },
            zIndex: 999
        });
    }
        

	/* swiper customize */
    var swiper = new Swiper('.swiper-container', {
    	initialSlide: 2,
        pagination: '.swiper-pagination',
        nextButton: '.swiper-button-next',
        prevButton: '.swiper-button-prev',
        slidesPerView: 1,
        centeredSlides: true,
        paginationClickable: true,
        spaceBetween: 10,
        breakpoints: {
        	320: {
        		slidesPerView: 1, 
        		spaceBetween: 10
        	},
        	480: {
        		slidesPerView: 1,
        		spaceBetween: 10
        	},
        	640: {
        		slidesPerView: 1,
        		spaceBetween: 10
        	},
        	767: {
        		slidesPerView: 1,
        		spaceBetween: 10
        	},
        	991: {
        		slidesPerView: 1,
        		spaceBetween: 10
        	},
        	1199: {
        		slidesPerView: 1,
        		spaceBetween: 10
        	},
            1280: {
                slidesPerView: 2,
                spaceBetween: 20
            },

        	1320: {
        		slidesPerView: 2,
        		spaceBetween: 20
        	},
        	1640: {
				slidesPerView: 2,
        		spaceBetween: 20
        	},
        	2000: {
        		slidesPerView: 3,
        		spaceBetween: 30
        	},
        	3000: {
				slidesPerView: 3,
        		spaceBetween: 30
        	},
        	4000: {
				slidesPerView: 3,
        		spaceBetween: 30
        	},
        	6000: {
        		slidesPerView: 5,
        		spaceBetween: 30
        	}
        }
    });

    /* Contact form validation customize*/

    // 회원가입 validation
    if($("#validateSignIn").length>0) {
        $("#validateSignIn").validate({
           
            submitHandler: function(form) {   
                // 데이터 베이스에 저장 ajax 사용
                $.ajax({
                    type: "POST",
                    url: "/kwaveweb/insertUser",
                    data: {
                        "userEmail": $("#userEmail").val(),
                        "userPassword": $("#userPassword").val(),
                        "userName": $("#userName").val()
                    },
                    dataType: "json",
                    success: function(data) {
                       if(data.KEY == "SUCCESS"){
                          alert("회원가입을 축하드립니다");
                         window.location = "http://localhost:8181/kwaveweb/login";
                       }else{
                          alert("회원가입에 실패하셨습니다.");
                       }
                    }
                });
            },
            errorPlacement: function(error, element) {  
                error.appendTo(element.parent());  
            },
            onkeyup: false,
            onclick: false,
            rules: {
                userEmail: {
                    required: true,
                    email: true
                },
                userPassword: {
                    required: true,
                    minlength: 6
                },
                userPasswordConfirm: {
                    required: true,
                    minlength: 6,
                    equalTo: "#userPassword"
                },
                userName: { 
                    required: true,
                    minlength: 1
                },
                signInAgreement: {
                    required: true
                }
            },
            messages: {
                userEmail: {
                    required: "We need your email address to contact you.",
                    email: "Please enter a valid email address."
                },
                userPassword: {
                    required: "Please enter a password.",
                    minlength: "Your password must be at least 6 charaters long."
                },
                userPasswordConfirm: {
                    required: "Please confirm the password.",
                    minlength: "Your password must be at least 6 charaters long.",
                    equalTo: "Please enter the same password as above."
                },
                userName: {
                    required: "Please enter your name.",
                    minlength: "Your name must be at least 1 charaters long."
                },
                signInAgreement: {
                    required: "Please accept our policy."
                }
            },
            errorElement: "span",
            highlight: function (element) {
                $(element).parent().removeClass("has-success").addClass("has-error");
                $(element).siblings("label").addClass("hide");
            },
            success: function (element) {
                $(element).parent().removeClass("has-error").addClass("has-success");
                $(element).siblings("label").removeClass("hide");
            }
        });
    }

    // 로그인 validation
    if($("#validateLogin").length>0) {
        $("#validateLogin").validate({
            submitHandler: function(form) {   
                // 데이터 베이스에 저장 ajax 사용
                $.ajax({
                    type: "POST",
                    url: "/",
                    data: {
                        "userEmail": $("#userEmail").val(),
                        "userPassword": $("#userPassword").val()
                    },
                    dataType: "json",
                    success: function(data) {
                        //성공 시 데이터 처리 
                    }
                });
            },
            errorPlacement: function(error, element) {  
                error.appendTo(element.parent());  
            },
            onkeyup: false,
            onclick: false,
            rules: {
                userEmail: {
                    required: true,
                    email: true
                },
                userPassword: {
                    required: true,
                    minlength: 6
                }
            },
            messages: {
                userEmail: {
                    required: "Please enter your email address.",
                    email: "Please enter a valid email address."
                },
                userPassword: {
                    required: "Please enter your password.",
                    minlength: "Your password must be at least 6 charaters long."
                }
            },
            errorElement: "span",
            highlight: function (element) {
                $(element).parent().removeClass("has-success").addClass("has-error");
                $(element).siblings("label").addClass("hide");
            },
            success: function (element) {
                $(element).parent().removeClass("has-error").addClass("has-success");
                $(element).siblings("label").removeClass("hide");
            }
        });
    }

    // 비밀번호 찾기 validation
    if($("#validateFindPassword").length>0) {
        $("#validateFindPassword").validate({
            submitHandler: function(form) {   
                // 데이터 베이스에 저장 ajax 사용
                $.ajax({
                    type: "POST",
                    url: "/",
                    data: {
                        "userEmail": $("#userEmail").val()
                    },
                    dataType: "json",
                    success: function(data) {
                        //성공 시 데이터 처리 
                    }
                });
            },
            errorPlacement: function(error, element) {  
                error.appendTo(element.parent());  
            },
            onkeyup: false,
            onclick: false,
            rules: {
                userEmail: {
                    required: true,
                    email: true
                }
            },
            messages: {
                userEmail: {
                    required: "Please enter your email address.",
                    email: "Please enter a valid email address."
                }
            },
            errorElement: "span",
            highlight: function (element) {
                $(element).parent().removeClass("has-success").addClass("has-error");
                $(element).siblings("label").addClass("hide");
            },
            success: function (element) {
                $(element).parent().removeClass("has-error").addClass("has-success");
                $(element).siblings("label").removeClass("hide");
            }
        });
    }

    // 비밀번호 변경 validation
    if($("#validateChangePassword").length>0) {
        $("#validateChangePassword").validate({
            submitHandler: function(form) {   
                // 데이터 베이스에 저장 ajax 사용
                $.ajax({
                    type: "POST",
                    url: "/",
                    data: {
                        "currentPassword": $("#currentPassword").val(),
                        "newPassword": $("#newPassword").val()
                    },
                    dataType: "json",
                    success: function(data) {
                        //성공 시 데이터 처리 
                    }
                });
            },
            errorPlacement: function(error, element) {  
                error.appendTo(element.parent());  
            },
            onkeyup: false,
            onclick: false,
            rules: {
                currentPassword: {
                    required: true,
                    minlength: 6
                },
                newPassword: {
                    required: true,
                    minlength: 6
                },
                newPasswordConfirm: {
                    required: true,
                    minlength: 6,
                    equalTo: "#newPassword"
                }
            },
            messages: {
                currentPassword: {
                    required: "Please enter a older password.",
                    minlength: "Your password must be at least 6 charaters long."
                },
                newPassword: {
                    required: "Please enter a new password.",
                    minlength: "Your password must be at least 6 charaters long."
                },
                newPasswordConfirm: {
                    required: "Please confirm the password.",
                    minlength: "Your password must be at least 6 charaters long.",
                    equalTo: "Please enter the same password as above."
                }
            },
            errorElement: "span",
            highlight: function (element) {
                $(element).parent().removeClass("has-success").addClass("has-error");
                $(element).siblings("label").addClass("hide");
            },
            success: function (element) {
                $(element).parent().removeClass("has-error").addClass("has-success");
                $(element).siblings("label").removeClass("hide");
            }
        });
    }

    // 개인 정보 변경 validation
    if($("#validateAboutYou").length>0) {
        $("#validateAboutYou").validate({
            submitHandler: function(form) {   
                // 데이터 베이스에 저장 ajax 사용
                $.ajax({
                    type: "POST",
                    url: "/",
                    data: {
                        "userEmail": $("#userEmail").val(),
                        "userName": $("#userName").val(),
                        "nation": $("#nation").val()
                    },
                    dataType: "json",
                    success: function(data) {
                        //성공 시 데이터 처리 
                    }
                });
            },
            errorPlacement: function(error, element) {  
                error.appendTo(element.parent());  
            },
            onkeyup: false,
            onclick: false,
            rules: {
                userEmail: {
                    required: true,
                    email: true
                },
                userName: {
                    required: true,
                    minlength: 1
                },
                nation: {
                    required: true
                }
            },
            messages: {
                userEmail: {
                    required: "Please enter your email address.",
                    email: "Please enter a valid email address."
                },
                userName: {
                    required: "Please enter your name.",
                    minlength: "Your name must be at least 1 charaters long."
                },
                nation: {
                    required: "You have to select nation."
                }
            },
            errorElement: "span",
            highlight: function (element) {
                $(element).parent().removeClass("has-success").addClass("has-error");
                $(element).siblings("label").addClass("hide");
            },
            success: function (element) {
                $(element).parent().removeClass("has-error").addClass("has-success");
                $(element).siblings("label").removeClass("hide");
            }
        });
    }

    // 배송정보 validation
    if($("#validateDeliveryInfo").length>0) {
        $("#validateDeliveryInfo").validate({
            submitHandler: function(form) {   
                // Iamport 결제 모듈 연동
                $.ajax({
                    type: "POST",
                    url: "/",
                    data: {
                        
                    },
                    dataType: "json",
                    success: function(data) {
                        
                    }
                });
            },
            errorPlacement: function(error, element) {  
                error.appendTo(element.parent());  
            },
            onkeyup: false,
            onclick: false,
            rules: {
                //단순 입력이 되는 값에 한정함
                //넘어오는 값들(ex. rewardNum, proejctName, rewardAmount, shippingAmount...)은 rules에 담지 않았음
                userEmail: {
                    required: true,
                    email: true,
                },
                userName: {
                    required: true,
                    minlength: 1
                },
                phone: {
                    required: true
                },
                zipCode: {
                    required: true
                },
                address1: {
                    required: true
                },
                city: {
                    required: true
                },
                region: {
                    required: true
                },
                country: {
                    required: true
                },
                shippingMethod: {
                    required: true
                }
            },
            messages: {
                userEmail: {
                    required: "Please enter your email address.",
                    email: "Please enter a valid email address."
                },
                userName: {
                    required: "Please enter your name.",
                    minlength: "Your name must be at least 1 charaters long."
                },
                phone: {
                    required: "Please enter your phone number."
                },
                zipCode: {
                    required: "Please enter your zip code number."
                },
                address1: {
                    required: "Please enter your address."
                },
                city: {
                    required: "Please enter your city."
                },
                region: {
                    required: "Please enter your region."
                },
                country: {
                    required: "Please select your country. This depend on shipping amount."
                },
                shippingMethod: {
                    required: "Please enter your shipping method"
                }
            },
            errorElement: "span",
            highlight: function (element) {
                $(element).parent().removeClass("has-success").addClass("has-error");
                $(element).siblings("label").addClass("hide");
            },
            success: function (element) {
                $(element).parent().removeClass("has-error").addClass("has-success");
                $(element).siblings("label").removeClass("hide");
            }
        });
    }
});
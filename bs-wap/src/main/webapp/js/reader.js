/*
 * reader-html
 *
 * Copyright (c) 2014 liangqi
 * Licensed under the MIT license.
 */
var isLoading = false;
var page = 2;
Zepto(function ($) {
    // 检测非首页，使用全局延迟加载，首页存在轮播图，延迟加载需要额外定制
    if($('.home').length <= 0){
        octopus.lazyImg({ el: document.body });
    }

    //通用点击效果
    var selector = ".menu>a,#btnSingleOrder,.order_buttons>a,.order_header .recharge,.result_buttons>a, .u-6>div>a,.btn-list-inner>a,.comment-links>a span,.u-5-item>div,.u-3,.u-1 .more,.nav_ >div> a,.nav_item,.u-4 > a,.type_ a,.enter_,.space-v td,.clickEvent td,.rank-tab td,.recharge_phone_message_input_submit";
    $(document).on('touchstart', selector, function (e) {
        var that = $(this);
        that.addClass('link-ef');
        setTimeout(function () {
            that.removeClass('link-ef');
        }, 100);
    });

    // 图封点击效果
    var imgSelector = ".u-2 img,.u-3>img";
    $(document).on('touchstart', imgSelector, function (e) {
        var that = $(this);
        that.css('opacity', 0.8);
        setTimeout(function () {
            that.css('opacity', 1);
        }, 100);
    });

    // 详情页tab切换
    if ($('.desc_').length) {
        var tab = $('.tabs');
        var tabs = tab.find('a');

        tab.on('click', 'a', function (e) {
            e.preventDefault();
            if (!$(this).hasClass('hover')) {
                var index = $(this).data('index');
                showTabContent(index);
                toggleTab(index, tabs);
            }
        });

        function toggleTab(index, tabs) {
            $.each(tabs, function (id, elem) {
                if (id == index && !$(elem).hasClass('hover')) {
                    $(elem).addClass('hover');
                } else {
                    $(elem).removeClass('hover');
                }
            });
        }

        function showTabContent(index) {
            var tabContents = $('.tab-content').find('.tab-item');
            $.each(tabContents, function (id, elem) {
                if (id == index) {
                    $(elem).show();
                } else {
                    $(elem).hide();
                }
            })
        }
    }





    /**
     * 下载页面加载
     */
    if ($('#ajaxList').length) {

        // 初始化页面，主动出发一次页面滚动事件，避免当点击返回键时，页面已经再页面底部，而不能再向下滚动页面
        window.scrollTo(0,0);
        (function () {
            var loading = $(".loading")[0];
            $(window).on("scroll", function (e) {
                var screen = window.innerHeight || document.documentElement.clientHeight, contentHeight = document.body.scrollHeight, scroll = Math.max(window.pageYOffset, document.body.scrollTop);
                var n = (contentHeight - (screen + scroll));
                if ((!isLoading) && n < 10) {
                    isLoading = true;
                    $(loading).addClass("show");
                    $.ajax({
                        type: 'GET',
                        url: $_list.url + page,
                        dataType: 'json',
                        timeout: 20000,
                        context: $('body'),
                        success: function (data) {
                            listAppend(data, "#ajaxList", $_list.pref_str);
                        },
                        error: function (xhr, type) {

                        },
                        complete: function (xhr, status) {
                            $(loading).removeClass("show");
                            page++;
                            isLoading = false;
                        }
                    });
                }
            }, false);
        })();

        function listAppend(jsonData, targetId, prefStr) {
            var list = eval(jsonData);
            var strTmp = '';
            var elem = document.createElement("div");
            for (var i = 0; i < list.length; i++) {
                strTmp += singleData(list[i], list[i].sourceType, prefStr);
            }
            if (strTmp) {
                $(targetId).append(strTmp);
            }
        }

        /*
         * 标题模式上拉刷新后传回的JSON数据
         * @jsonData 单条数据
         */
        function singleData(jsonData, source, prefStr) {
            if (!(jsonData || source)) {
                return;
            }
            //Object { author: "作者2", id: 2, memo: "描述2", name: "书2", smallPic: "/static/res/cover-2.jpg" }
            var html;
            var url = prefStr + '/book/show.do?id=' + jsonData.id;
            var img = '<img src="' + jsonData.smallPic + '" style="background: url(data:image/jpg;base64,/9j/4QAYRXhpZgAASUkqAAgAAAAAAAAAAAAAAP/sABFEdWNreQABAAQAAABQAAD/4QMsaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLwA8P3hwYWNrZXQgYmVnaW49Iu+7vyIgaWQ9Ilc1TTBNcENlaGlIenJlU3pOVGN6a2M5ZCI/PiA8eDp4bXBtZXRhIHhtbG5zOng9ImFkb2JlOm5zOm1ldGEvIiB4OnhtcHRrPSJBZG9iZSBYTVAgQ29yZSA1LjUtYzAxNCA3OS4xNTE0ODEsIDIwMTMvMDMvMTMtMTI6MDk6MTUgICAgICAgICI+IDxyZGY6UkRGIHhtbG5zOnJkZj0iaHR0cDovL3d3dy53My5vcmcvMTk5OS8wMi8yMi1yZGYtc3ludGF4LW5zIyI+IDxyZGY6RGVzY3JpcHRpb24gcmRmOmFib3V0PSIiIHhtbG5zOnhtcD0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLyIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bXA6Q3JlYXRvclRvb2w9IkFkb2JlIFBob3Rvc2hvcCBDQyAoTWFjaW50b3NoKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpDMDkzRTE0ODI1QzkxMUU0ODVGOTk5RkE4MjE1QkQ4RiIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpDMDkzRTE0OTI1QzkxMUU0ODVGOTk5RkE4MjE1QkQ4RiI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOkMwOTNFMTQ2MjVDOTExRTQ4NUY5OTlGQTgyMTVCRDhGIiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOkMwOTNFMTQ3MjVDOTExRTQ4NUY5OTlGQTgyMTVCRDhGIi8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+/+4AJkFkb2JlAGTAAAAAAQMAFQQDBgoNAAALkgAAEc8AACDXAAA4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);background-size:100% 100%" ';
            var name = '<p>' + jsonData.name + '</p>';
            var author = '<p class="font_size_small">' + jsonData.author + '</p>';
            var memo = '<p class="font_size_small">' + jsonData.shortMemo + '</p>';
            html = ['<a href="' + url + '" class="u-3 clear">',
                img,
                '<aside>',
                name,
                author,
                memo,
                '</aside>',
                '</a>'].join("");
            return html;
        }

    }


    /**
     * 用户中心js
     *
     */

    // 积分签到
    var signUp = $('#sign-up');
    if (signUp.length) {
        signUp.on('click', function () {
            if (!signUp.hasClass('hover')) {
                signUp.addClass('hover');
                signUp.find('span').html('已签到');
                // TODO 服务端签到统计url
                $.ajax({
                    type: 'GET',
                    url: $_list.url,
                    dataType: 'json',
                    timeout: 200000,
                    context: $('body'),
                    success: function (data) {
                        // TODO 更改积分状态,临时设为700
                        var resp = eval(data);
                        console.log(data)
                        if (resp.success) {
                            signUp.siblings('.user-info-main').find('.user-score em').html(resp.exp);
                            showToastDialog("签到成功");
                        }
                    },
                    error: function (xhr, type) {
                        showToastDialog("签到失败");
                        signUp.removeClass('hover');
                        signUp.find('span').html('签到');
                    }
                });
            }
        });
    }

    function showToastDialog(stringMsg) {
        try {
            window._cordovaNative.exec("ReadBookPlugin", "showToast", " ", '[{"msg":' + stringMsg + '}]');
        } catch (e) {
        }
    }

    function showLoadingDialog() {
        try {
            window._cordovaNative.exec("ReadBookPlugin", "showLoadingDialog", '', '[{"event":"showLoadingDialog","msg":"正在提交数据"}]');
        } catch (e) {
        }
    }

    function hideLoadingDialog() {
        try {
            window._cordovaNative.exec("ReadBookPlugin", "hideLoadingDialog", '', '[{"event":"hideLoadingDialog"}]');
        } catch (e) {
        }
    }

    $('#recharge').on('click',function(event){
        event.preventDefault();
        try{
            console.log("recharge" + Math.random());
            window._cordovaNative.recharge('goToRecharge','','');
        }catch (e){

        }
    });

    // 提交个人资料
    $('#userData').find('input[type="text"]').on('focus', function () {
        var error = $(this).parent().siblings('span.error');
        if (error.length) {
            error.remove();
        }
    });

    var oldUserName = $('input[name="nickName"]').val(),
        oldUserPhone = $('input[name="mobile"]').val(),
        oldUserBirthday = $('input[name="birthday"]').val();

    $('#btn-submit-user-profile').on('click', function (e) {
        e.preventDefault();
        var errors = [],
            uid = $('input[name="id"]').val(),
            userName = $('input[name="nickName"]'),
            userPhone = $('input[name="mobile"]'),
            userBirthday = $('input[name="birthday"]'),

            userSex = $('#sex-sel').find('span.hover').data('index'),
            userPhoneVal = $.trim(userPhone.val()),
            userBirthdayVal = $.trim(userBirthday.val()),
            userNameVal = $.trim(userName.val());

        // 2-10个中英文、数字、下划线
        if(userNameVal.length > 0){
            if (!/^[\u4e00-\u9fa5_a-zA-Z0-9_]{2,10}$/.test(userNameVal)) {
                addError(userName, "请正确输入昵称");
                errors.push(userName.name);
            }
        }

        if(userPhoneVal.length > 0){
            if (!/^\d{11}$/.test(userPhoneVal)) {
                addError(userPhone, "请正确输入手机号")
                errors.push(userPhone.name);
            }
        }

        if(userBirthdayVal.length > 0){
            if (!/^\d{8}$/.test(userBirthdayVal)) {
                addError(userBirthday, "请正确输入生日");
                errors.push(userBirthday.name);
            }
        }

        if (errors.length < 1) {
            if(!userNameVal && oldUserName){
                userNameVal = oldUserName;
            }

            if(!userBirthdayVal && oldUserBirthday){
                userBirthdayVal = oldUserBirthday;
            }

            if(!userPhoneVal && oldUserPhone){
                userPhoneVal = oldUserPhone;
            }

            var userData = JSON.stringify({"id": parseInt(uid), "nickName": userNameVal, "sex": userSex, "mobile": userPhoneVal, "birthday": userBirthdayVal});
            showLoadingDialog();

            $.ajax({
                type: 'POST',
                url: $_list.url,
                data: userData,
                contentType: "application/json;charset=utf-8",
                timeout: 200000,
                success: function (data) {
                    if (data.status == 1) {
                        showToastDialog(data.msg);
                        saveUserData(userData);
                    } else {
                        showToastDialog(data.msg);
                    }
                },
                error: function (xhr, type) {
                    showToastDialog("数据提交出错啦，重新试试！");
                },
                complete: function (xhr, status) {
                    hideLoadingDialog();
                }
            });
        }
    });

    function saveUserData(userData) {
        try {
            // 不需要任何数据，也不需要回调函数
            window._cordovaNative.updateUserData(userData);
        } catch (e) {

        }
    }

    // 修改密码，调用客户端页面
    $('#mod-pwd').on('click', function (e) {
        e.preventDefault();
        try {
            // 不需要任何数据，也不需要回调函数
            window._cordovaNative.exec("ReadBookPlugin", "startPage", '', '[{"pageName":"passwordEdit","position":1}]');
        } catch (e) {

        }
    });

    // 性别选择
    $('#sex-sel').on('click', 'span', function () {
        if (!$(this).hasClass('hover')) {
            $(this).siblings('.hover').removeClass('hover');
            $(this).addClass('hover');
        }
    });

    function addError(elem, errMsg) {
        elem.parent().parent().append('<span class="error">' + errMsg + '</span> ');
    }


    // 猜你喜欢
    $('#bookLikeList').on('click', 'a', function (e) {
        e.preventDefault();
        var bookId = $(this).data('book-id');
        try {
            window._cordovaNative.exec("ReadBookPlugin", "startRead", " ", '[' + JSON.stringify($_book) + ']');
        } catch (e) {
        }
    });


    /**
     * js ~ 客户端接口
     *
     */
    var readBook = $('#readBook'),
        bookList = $('#bookList'),
        addFav = $('#addFav'),
        readNewChapter = $('#readBookNewChapter'),
        btnComment = $('#btnComment'),
        commentList = $('#commentList');

    // 立即阅读
    if (readBook.length) {
        readBook.on('click', function (e) {
            e.preventDefault();
            var bookId = $(this).data('book-id');
            try {
                $.ajax({
                    type: 'GET',
                    url: $_book.staturl,
                    timeout: 5000,
                    complete: function (xhr, status) {
                        window._cordovaNative.exec("ReadBookPlugin", "startRead", " ", '[' + JSON.stringify($_book) + ']');
                    }
                });

            } catch (e) {
            }

        });
    }

    // 查看目录
    if (bookList.length) {
        bookList.on('click', function (e) {
            e.preventDefault();
            var bookId = $(this).data('book-id');
            try {
                window._cordovaNative.exec("ReadBookPlugin", "bookList", " ", '[' + JSON.stringify($_book) + ']');
            } catch (e) {
            }
        });
    }

    // 添加收藏 & 取消收藏
    if (addFav.length) {
        addFav.on('click', function (e) {
            e.preventDefault();
            var bookId = $(this).data('book-id');
            try {
                var em = addFav.find('em');
                //if (!em.hasClass('hover')) {
                //em.addClass('hover');
                //}
                window._cordovaNative.exec("ReadBookPlugin", "addFav", addFav.id, '[' + JSON.stringify($_book) + ']');
            } catch (e) {
            }
        });
    }

    // 阅读新章节
    if (readNewChapter.length) {
        readNewChapter.on('click', function (e) {
            e.preventDefault();
            var bookId = $(this).data('book-id'),
                chapterId = $(this).data('chapter-id');
            try {
                $_bookchap = {};
                $_bookchap = $.extend(true,$_bookchap,$_book);
                $_bookchap.chapterId = chapterId;
                console.log("oldChapterId:" + $_book.chapterId + ";new ChapterId" + $_bookchap.chapterId);
                window._cordovaNative.exec("ReadBookPlugin", "startRead", "", '[' + JSON.stringify($_bookchap) + ']');
            } catch (e) {
            }
        });
    }

    if (btnComment.length) {
        btnComment.on('click', function (e) {
            e.preventDefault();
            var bookId = $(this).data('book-id');
            try {
                window._cordovaNative.exec("ReadBookPlugin", "postComment", "", '[' + JSON.stringify($_book) + ']');
            } catch (e) {
            }
        });
    }

    if (commentList.length) {
        commentList.on('click', function (e) {
            e.preventDefault();
            var bookId = $(this).data('book-id');
            try {
                window._cordovaNative.exec("ReadBookPlugin", "commentList", '', '[' + JSON.stringify($_book) + ']');
            } catch (e) {
            }
        });
    }

});

function Shelfadd(bid,cid){
	$.ajax({
				url:	 '/index.php/Book/BookshelfAdd',
				type:	 'POST',
				dataType: 'json',
				data:	  'bid='+bid+'&cid='+cid,
				error:function(json){
					alert('追书失败！');
				},
				success:function(json){
					if(json.result=='nologin'){
							$('#login').css('display','block');
						}else if(json.result=='success'){
							 alert('追书成功！');
						}else if(json.result=='again'){
							alert('已追书成功，不可重复！');
						}else if(json.result=='againSuccess'){
							alert('添加成功！');
						}else{
							alert('追书失败！');
						}
				}
			});
}
function htmlDecode(value){
	return $('<div/>').html(value).text();
}
function content(){
	var bid	=	$('#bookId').val();
	var cid	=	$('#chapterId').val();
	$.ajax({
		url:	'/chapter/fetch.do',
		type:	'POST',
		dataType: 'json',
		data:	'bid='+bid+'&cid='+cid,
		error: function(json){
			alert('数据错误，请刷新重试！');
		},
		success:function(json){
			if(json.result=='success'){
				var content = htmlDecode(json.content);
				$('#content').html(content);
			}else if(json.result=='login'){
				$('#login').css('display','block');
			}else if(json.result=='consume'){
				$('#consume').css('display','block');
			}
		}
	});
}

window.onload = content;

function closePage(){
	$('#xiao').css('display','none');
	$('#ku').css('display','none');
}
function consume(){
	var uid	=	$('#userId').val();
	var bid	=	$('#bookId').val();
	var cid	=	$('#chapterId').val();

	$.ajax({
		url:	'/pay/consume.do',
		type:	'POST',
		dataType: 'json',
		data:	'bid='+bid+'&cid='+cid+'&uid='+uid,
		error: function(json){
			alert('数据错误，请刷新重试！');
		},
		success:function(json){
			if(json.result=='success'){
				var content = htmlDecode(json.content);
				$('#content').html(content);
				$('#consume').css('display','none');
				$('#xiao').css('display','block');
				window.setTimeout("closePage()",2000);
			} else if(json.result=='login'){
				$('#consume').css('display','none');
				$('#login').css('display','block');
			} else if(json.result=='coin'){
				$('#consume').css('display','none');
				$('#ku').css('display','block');
				window.setTimeout("closePage()",2000);
				window.location = '/recharge/entry.do';
			}
		}
	});
}

function batchCosume(){
	var uid	=	$('#userId').val();
	var bid	=	$('#bookId').val();
	var cid	=	$('#chapterId').val();
	$.ajax({
		url:	'/pay/batchConsume.do',
		type:	'POST',
		dataType: 'json',
		data:	'bid='+bid+'&cid='+cid+'&uid='+uid,
		error: function(json){
			alert('数据错误，请刷新重试！');
		},
		success:function(json){
			if(json.result=='success'){
				var content = htmlDecode(json.content);
				$('#content').html(content);
				$('#consume').css('display','none');
				$('#xiao').css('display','block');
				window.setTimeout("closePage()",2000);
			}else if(json.result=='login'){
				$('#login').css('display','block');
			}else if(json.result=='consume'){
				$('#consume').css('display','block');
			}else if(json.result=='coin'){
				$('#consume').css('display','none');
				$('#ku').css('display','block');
				window.location = '/recharge/entry.do';
			}
		}
	});
}
function closePay(){
	$('#consume').css('display','none');
	$('#login').css('display','none');
}

function changeModel(id){
	$.ajax({
			url:	'/index.php/Book/readSession',
			type:	'POST',
			dataType: 'json',
			data:	'dayNight='+id,
			error: function(json){
			},
			success:function(json){
				if(json.result=='nologin'){
					$('#login').css('display','block');
				}else if(json.result=='success'){
					var bodyStyle		=	$('#bodyStyle').val();
					var contentStyle	=	$('#contentStyle').val();
					$('#styles').css('display','none');
					if(id==2){
						$('#containers').removeClass(bodyStyle);
						$('#content').removeClass(contentStyle);
						$('#containers').addClass('containhei');
						$('#cTitle').addClass('title');
						$('#content').addClass('zhangjie');
						$('#bodyStyle').val('containhei');
						$('#contentStyle').val('zhangjie');
						$('#daytime').css('display','block');
						$('#night').css('display','none');
					}else if(id==3){
						$('#containers').removeClass(bodyStyle);
						$('#content').removeClass(contentStyle);
						$('#containers').addClass('bgo');
						$('#content').addClass('bgo-font');
						$('#bodyStyle').val('bgo');
						$('#contentStyle').val('bgo-font');
						$('#night').css('display','block');
						$('#daytime').css('display','none');
					}else if(id==4){
						$('#containers').removeClass(bodyStyle);
						$('#content').removeClass(contentStyle);
						$('#containers').addClass('bgt');
						$('#content').addClass('bgt-font');
						$('#bodyStyle').val('bgt');
						$('#contentStyle').val('bgt-font');
						$('#night').css('display','block');
						$('#daytime').css('display','none');
					}else if(id==5){
						$('#containers').removeClass(bodyStyle);
						$('#content').removeClass(contentStyle);
						$('#containers').addClass('bgr');
						$('#content').addClass('bgr-font');
						$('#bodyStyle').val('bgr');
						$('#contentStyle').val('bgr-font');
						$('#night').css('display','block');
						$('#daytime').css('display','none');
					}else {
						$('#containers').removeClass('containhei');
						$('#cTitle').removeClass('title');
						$('#content').removeClass('zhangjie');
						$('#night').css('display','block');
						$('#daytime').css('display','none');
					}
				}
			}
		});
}
function fontPlus(){
	var fSize	=	$('#fontSize').val();
	if(fSize<22){
		var plusSize	=	parseInt(fSize)+parseInt(2);
		var nowSize		=	plusSize+'px';
		$.ajax({
			url:	'/index.php/Book/readSession',
			type:	'POST',
			dataType: 'json',
			data:	'fontSize='+plusSize,
			error: function(json){
			},
			success:function(json){
				if(json.result=='nologin'){
					$('#login').css('display','block');
				}else if(json.result=='success'){
					$('#content').css('font-size',nowSize);
					$('#fontSize').val(plusSize);
				}
			}
		});
	}
}
function fontMinus(){
	var fSize	=	$('#fontSize').val();
	if(fSize>12){
		var plusSize	=	parseInt(fSize)-parseInt(2);
		var nowSize		=	plusSize+'px';
		$.ajax({
			url:	'/index.php/Book/readSession',
			type:	'POST',
			dataType: 'json',
			data:	'fontSize='+plusSize,
			error: function(json){
			},
			success:function(json){
				if(json.result=='nologin'){
					$('#login').css('display','block');
				}else if(json.result=='success'){
					$('#content').css('font-size',nowSize);
					$('#fontSize').val(plusSize);
				}
			}
		});
	}
}

function commentAdd(id,cid,type){
	$.ajax({
		url:	'/index.php/Book/loginAjax',
				type:	 'POST',
				dataType: 'json',
				data:	  '',
				error:function(json){
					alert('系统错误！');
				},
				success:function(json){
					if(json.result=='nologin'){
						$('#login').css('display','block');
					 }else if(json.result=='success'){
						 if(type==1){
							 	window.location = '/index.php/Book/CommentAdd/bid/'+id+'/cid/'+cid;
						  }else if(type==2){
						 	 window.location = '/index.php/Book/Comment/bid/'+id+'/cid/'+cid;
						  }
					}
			  }
	});
}

function chargeStyle(){
	$('#styles').css('display','block');
}

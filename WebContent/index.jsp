<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="jquery-1.9.1.min.js"></script>
<script type="text/javascript">

	$(function(){
		$("button").click(function(){
			var url = "GetTicket";
			$.get(url, function(ticket){
				var src = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + ticket;
				$("img").attr("src",src);
			});
			
		})
		
	})

</script>
</head>
<body>

<button>生成二维码</button>

<img alt="" src="">

</body>
</html>
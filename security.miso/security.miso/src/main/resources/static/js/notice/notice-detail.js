
let noticeCode = location.pathname.substring(location.pathname.lastIndexOf("/") +1);

load("/api/v1/notice/");
function load(uri) {
	$.ajax({
		async:false ,
		type: "get" ,
		url : uri +noticeCode ,
		dataType: "json" ,
		success : (response) => {
			
		},
		error : (error) => {
			console.log(error);
		}
		
	})
}


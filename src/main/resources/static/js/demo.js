/**
 * 
 */

"use strict"




function search(){
	
	console.log("search");
	var port = $("#port").val();
	console.log(port);
		var callback = function(data){
			console.log(data);
			if(data.name != "" && data.name != undefined){
				$("#send").attr("disabled",false);
				$("#owner").text(data.name);
				$("#id").val(data.id);
//				$("#email").text(data.email);
				$("#email").val(data.email);
			}else{
				$("#owner").text('Port is not found!');
			}
		}
		var data = {"port": port};
		jQuery.ajax({
	        url: "/searchPort",
	        type: "POST",
	        contentType:"application/json; charset=utf-8",
	        dataType: "json",
	        data: JSON.stringify(data),
	        success: callback
	    });

//		window.location.reload();
		
//	}else{
		
//	}
	
}
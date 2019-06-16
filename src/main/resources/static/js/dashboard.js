jQuery(document).ready(function($) {
	
	/*
	 * Facilitate one-off queries for statistic data based on UUIDs.
	 * Ajax-based, formats results as table rows.
	 */
	$("#stat-search").submit(function(event) {

		var uuid = $("#uuid").val();
		
		$.ajax({
			type : "POST",
			contentType : "application/json",
			url : "http://localhost:8080/",
			data : uuid,
			dataType : 'json',
			timeout : 10000,
			statusCode : {
				302: function(data) {
					
					var row = "<tr><td>" + data.responseJSON.requestUuid 
					+ "</td><td><a href=\"" + data.responseJSON.url + "\">" + data.responseJSON.url + "</a>"  
					+ "</td><td>" + data.responseJSON.responseSize + "</td></tr>";
					
					$("#results").append(row);
					$("#error").text("");
				},
				404 : function(e) {
					$("#error").text(e.responseText);
				}
			}
		})
		.done(function(e) {
			console.log("DONE");
		});
		
		// Prevent default event handling.
		event.preventDefault();
	});
});
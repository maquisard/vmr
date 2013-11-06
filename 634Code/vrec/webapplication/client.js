/*
 * Please put any code that hit the web service in a function
 * in this file
 */

var service_base_url = "http://localhost:8080/vrecservice/";
var num_of_random_users = 6;
var current_response = null;
var current_callback = null;
var request_count = 0;



function load_watched_movies( )
{
	var url = service_base_url + 'movie/watched/1/15';
	send_get_request(url);
}

function retrieve_random_users( )
{
	var url = service_base_url + "user/random/" + num_of_random_users;
	current_callback = function( response )
	{
		var user_select = document.getElementById("random_user_list");
		for(var i = 0; i < user_select.options.length; i++)
		{
			user_select.options[i] = null;
		}
		
		for(var i = 0; i < response.entities.length; i++)
		{
			var entity = response.entities[i];
			var option = document.createElement("option");
			option.text = entity.name;
			option.value = entity.ml_id;
			user_select.appendChild(option);
		}
		set_current_user_in_session( );
	}
	send_get_request(url);
}

function set_current_user_in_session()
{
	var user_select = document.getElementById("random_user_list");
	var current_user_id = user_select.options[user_select.selectedIndex].value;
	var url = service_base_url + "user/set/" + current_user_id;
	current_callback = function( response )
	{
		var user = response.entity;
		var imagepath = get_user_profile(user.age, user.gender);
		
		document.getElementById("profile_image").src = imagepath;
		document.getElementById("user_name_value").innerHTML = user.name;
		document.getElementById("user_age_value").innerHTML = user.age;
		document.getElementById("user_gender_value").innerHTML = user.gender === "F" ? "Female" : "Male";
		document.getElementById("user_occupation_value").innerHTML = user.occupation.occupation;
		document.getElementById("user_zipcode_value").innerHTML = user.zipcode;
		
		load_watched_movies( );
	}
	send_get_request(url);
}

function get_user_profile(age, gender)
{
	if(age > 40) //old person
	{
		if( gender === "F")
		{
			return "images/profiles/old-woman.png";
		}
		else
		{
			return "images/profiles/old-guy.png"
		}
	}
	else
	{
		if( gender === "F")
		{
			return "images/profiles/young-woman.png";
		}
		else
		{
			return "images/profiles/young-guy.png"
		}
	}
}


function emphasize(img, flag)
{
	if(flag)
	{
		img.style.width = '90px';
		img.style.height = '140px';
		img.style.border = 'solid orange 8px';
	}
	else
	{
		img.style.width = '80px';
		img.style.height = '120px';
		img.style.border = '';
	}
}

function watched_callback( response )
{
	alert("Bitch! - " + response.entities.length);
	if(response.entities.length == 0)
	{
		var html = "<div style='font-size: 2em;'>";
		html += "No movie was retrieved.</div>";
		document.getElementById("tabs-2").innerHTML = html;
	}
	else
	{
		var html = "<div style='overflow:auto; overflow-y: hidden;'><table><tr>";
		for(var i = 0; i < response.entities.length; i++)
		{
			var entity = response.entities[i];
			html += "<td><img onmouseover='emphasize(this, true)' onmouseout='emphasize(this, false)' id='" + entity.id + "'style='width: 80px; height: 120px;' alt='" + 
					entity.title + "' src='images/movieitem/" + entity.id + ".jpg' /></td>";
		}
		html += "</tr></table></div>";
		document.getElementById("tabs-2").innerHTML = html;
	}
	hideGlassPane();
}

function jsonCallBack( data )
{
	if(data.status)
	{
		current_callback(data);
	}
	else
	{
		alert(data.message);
	}
	hideGlassPane();
}

function send_get_request( url )
{
	showGlassPane();
	
    var _url = url + "?callback=?";
    $.ajax({
       type: 'GET',
        url: _url,
        async: true,
        contentType: "application/json",
        dataType: 'jsonp',
    });	
}

function showGlassPane()
{
	if(request_count == 0)
	{
		$("#glass_pane").show();
	}
	request_count++;
}

function hideGlassPane()
{
	request_count--;
	if(request_count <= 0)
	{ 
		request_count = 0;
		$("#glass_pane").hide();
	}
}


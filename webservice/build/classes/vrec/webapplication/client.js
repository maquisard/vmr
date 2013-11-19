/*
 * Please put any code that hit the web service in a function
 * in this file
 */


var service_base_url = "http://localhost:8080/vrecservice/";

var num_of_random_users = 6;
var num_of_recommendations = 14;

var current_response = null;
var current_movie = null;
var current_callback = null;

var request_count = 0;

var recommendations = null;

var watched = null;
var queue = null;
var browsed = null;

var attribute_matrix = null;
var particleSystem = null;
var selectedNode = null;
var hoverNode = null;
var traversed = new Array();

var selected_key = "genre";
var selected_value = "";

var grow_by_attribute = false;

var sys = arbor.ParticleSystem(10000, 600, 0.5);

function load_movie_queue( )
{
	var url = service_base_url + 'movie/queue/0/10';
	send_get_request(url);
}

function load_browsed_movies( )
{
	var url = service_base_url + 'movie/browsed';
	send_get_request(url);
}

function load_watched_movies( )
{
	var url = service_base_url + 'movie/watched/0/10';
	send_get_request(url);
}

function add_movie_to_queue( movieid )
{
	var url = service_base_url + 'movie/add/queue/' + movieid;
	send_get_request(url);
}

function add_movie_to_browsed( movieid )
{
	var url = service_base_url + 'movie/add/browsed/' + movieid;
	send_get_request(url);
}

function post_rating( value )
{
	if(current_movie != null)
	{
		var url = service_base_url + 'movie/rating/' + value + '/' + current_movie.id;
		send_get_request(url);
		current_movie.userrating = value;
	}
}

function load_recommendations( )
{
	var url = service_base_url + 'movie/recommend/' + num_of_recommendations;
	send_get_request(url);
}

function load_recommendations( movieid )
{
	clear_movies_from_canvas( );
	var url = service_base_url + 'movie/recommend/' + movieid + '/' + num_of_recommendations;
	send_get_request(url);
}

function update_user_rating( value )
{
	$("#movie_user_value").show();
	$("#movie_user_value").html(" - " + value + "/10");
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
		load_movie_queue( );
		load_browsed_movies( );
		load_recommendations( );
		$("#movie_profile_container").hide();

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


function find_movie_by_id( movieid, movie_list )
{
	for(var i = 0; i < movie_list.length; i++)
	{
		if(movie_list[i].id  == movieid)
		{
			return movie_list[i];
		}
	}
	return null;
}

function is_movie_in_queue( movieid )
{
	return document.getElementById("q-" + movieid) != null;
}

function load_movie_from_browsed( movieid )
{
	var movie = find_movie_by_id( movieid, browsed );
	current_movie = movie;
	
	load_movie( movie );
	selectedNode = null;
}
function load_movie_from_queue( movieid )
{
	var movie = find_movie_by_id( movieid, queue );
	current_movie = movie;
	
	load_movie( movie );
	selectedNode = null;
}

function load_movie_from_watched( movieid )
{
	var movie = find_movie_by_id( movieid, watched );
	current_movie = movie;
	
	load_movie( movie );
	selectedNode = null;
}

function load_movie_from_recommendations( movieid )
{
	var movie = find_movie_by_id( movieid, recommendations );
	current_movie = movie;
	
	load_movie( movie );
}

function load_movie( movie )
{
	
	$("#movie_profile_container").show();

	
	$("#movie_profile_image").attr({
		src : "images/movieitem/" + movie.id + ".jpg",
		title: movie.title,
		alt : movie.title
	});
	
	$("#movie_title").html(movie.title + " (" + movie.year + ") - " + movie.ersbRating.name);
	$("#movie_rating_value").html(movie.ersbRating.name);
	if(movie.genres.length > 0)
	{
		$("#movie_genre").show();
		var genres = movie.genres[0].name;
		for(var i = 1; i < movie.genres.length; i++)
		{
			genres += " | " + movie.genres[i].name;
		}
		$("#movie_genre_value").html(genres);
	}
	else
	{
		$("#movie_genre").hide();
	}
	
	$("#movie_duration_value").html(movie.runtime);
	
	if(movie.imdbRating > 0.0)
	{
		$("#movie_imdb_value").show();
		$("#movie_imdb_value").html("  " + movie.imdbRating + "/10");
		$("#imdb_rating_control").rateit("value", movie.imdbRating);
	}
	else
	{
		$("#movie_imdb_value").hide();
	}
	
	if(movie.userrating >= 0.0)
	{
		$("#movie_user_value").show();
		$("#movie_user_value").html("  " + movie.userrating + "/10");
		$("#user_rating_control").rateit("value", movie.userrating);
	}
	else
	{
		$("#movie_user_value").hide();
	}
	
	$("#movie_description_value").html(movie.description);
	
	
	if(movie.castMembers.length > 0)
	{
		$("#movie_cast_value").show();
		var members = movie.castMembers[0].name + "(<i>" + movie.castMembers[0].memberType + "</i>)";
		for(var i = 1; i < movie.castMembers.length; i++)
		{
			members += "; " + movie.castMembers[i].name + "(<i>" + movie.castMembers[i].memberType + "</i>)";
		}
		$("#movie_cast_value").html(members);
	}
	else
	{
		$("#movie_cast_value").hide();
	}
	
	if(is_movie_in_queue(movie.id))
	{
		$("#add_to_queue").hide();
	}
	else
	{
		$("#add_to_queue").show();
		$("#add_to_queue_button").unbind();
		$("#add_to_queue_button").bind("click", function() {
			add_movie_to_queue(movie.id);
		});
	}
	
	if(movie.trailerurl.indexOf("google") >= 0)
	{
		$("#trailer").hide();
	}
	else
	{
		$("#trailer").show();
		$("#trailer_button").unbind(); //remove all the event handlers		
		$("#trailer_button").bind( "click", function(){
			trailerWindow(movie.title, movie.trailerurl);
		});
	}
}


function queue_callback( response )
{
	if(response.status)
	{ 
		simpleAlert("Queue List", "Found: " + response.entities.length);
		if(response.entities.length == 0)
		{
			var html = "<div style='font-size: 2em;'>";
			html += "No movie was retrieved.</div>";
			document.getElementById("tabs-1").innerHTML = html;
		}
		else
		{
			queue = response.entities;
			var html = "<div style='overflow:auto; overflow-y: hidden;'><table><tr>";
			for(var i = 0; i < response.entities.length; i++)
			{
				var entity = response.entities[i];
				html += "<td><img onmouseover='emphasize(this, true)' onmouseout='emphasize(this, false)' onclick='load_movie_from_queue(\"" + entity.id + "\")' id='q-" + 
						entity.id + "'style='width: 80px; height: 120px;' alt='" + 
						entity.title + "' src='images/movieitem/" + entity.id + ".jpg' /></td>";
			}
			html += "</tr></table></div>";
			document.getElementById("tabs-1").innerHTML = html;
		}
	}
	else
	{
		errorAlert("Application Error", response.message);
	}
	hideGlassPane();
}

function watched_callback( response )
{
	if(response.status)
	{ 
		simpleAlert("Watched List", "Found: " + response.entities.length);
		if(response.entities.length == 0)
		{
			var html = "<div style='font-size: 2em;'>";
			html += "No movie was retrieved.</div>";
			document.getElementById("tabs-2").innerHTML = html;
		}
		else
		{
			//recommendations = response.entities;
			watched = response.entities;
			var html = "<div style='overflow:auto; overflow-y: hidden;'><table><tr>";
			for(var i = 0; i < response.entities.length; i++)
			{
				var entity = response.entities[i];
				html += "<td><img onmouseover='emphasize(this, true)' onmouseout='emphasize(this, false)' onclick='load_movie_from_watched(\"" + entity.id + "\")' id='w-" + 
				entity.id + "'style='width: 80px; height: 120px;' alt='" + 
				entity.title + "' src='images/movieitem/" + entity.id + ".jpg' /></td>";
			}
			html += "</tr></table></div>";
			document.getElementById("tabs-2").innerHTML = html;
		}
	}
	else
	{
		errorAlert("Application Error", response.message);
	}
	hideGlassPane();
}


function browsed_callback( response )
{
	if(response.status)
	{ 
		if(response.entities.length == 0)
		{
			var html = "<div style='font-size: 2em;'>";
			html += "Browsed movie list is empty.</div>";
			document.getElementById("tabs-3").innerHTML = html;
		}
		else
		{
			//recommendations = response.entities;
			browsed = response.entities;
			var html = "<div style='overflow:auto; overflow-y: hidden;'><table><tr>";
			for(var i = 0; i < response.entities.length; i++)
			{
				var entity = response.entities[i];
				html += "<td><img onmouseover='emphasize(this, true)' onmouseout='emphasize(this, false)' onclick='load_movie_from_browsed(\"" + entity.id + "\")' id='b-" + 
				entity.id + "'style='width: 80px; height: 120px;' alt='" + 
				entity.title + "' src='images/movieitem/" + entity.id + ".jpg' /></td>";
			}
			html += "</tr></table></div>";
			document.getElementById("tabs-3").innerHTML = html;
		}
	}
	else
	{
		errorAlert("Application Error", response.message);
	}
	hideGlassPane();
}



function add_queue_callback( response ) 
{ 
	if(!response.status)
	{
		errorAlert("Application Error", data.message);
	}
	else
	{
		load_movie_queue( );	
		$("#add_to_queue").hide();
	}
	hideGlassPane();
}

function update_rating_callback( response ) 
{ 
	if(!response.status)
	{
		errorAlert("Application Error", response.message);
	}
	else
	{
		simpleAlert("Application Message", response.message);
	}
	hideGlassPane();
}

function random_movies_callback( response )
{
	if(response.status)
	{
		simpleAlert("Application Message", response.entities.length + " Recommendations.");
		recommendations = response.entities;
		build_attribute_matrix( recommendations );
		console.log(attribute_matrix.length);
	    
		var sys = arbor.ParticleSystem(1000, 600, 0.5) // create the system with sensible repulsion/stiffness/friction
	    sys.parameters({gravity:true}) // use center-gravity to make the graph settle nicely (ymmv)
	    sys.renderer = get_current_renderer("#viewport") // our newly created renderer will have its .init() method called shortly by sys...
		var defaultWidth = 80; //53
		var defaultHeight = 120; //80
		var defaultWeight = 5

		//alert("Drawing the nodes");
	    
		//adding the nodes to the graph
		for(var i = 0; i < recommendations.length; i++)
		{
			var iconpath = "images/movieitem/" + recommendations[i].id + ".jpg";
			sys.addNode(recommendations[i].id, {icon:iconpath, width:defaultWidth, height:defaultHeight});	
		}
	    
	    //drawing the edges
	    for(var key in attribute_matrix)
	    {
	    	for(var value in attribute_matrix[ key ])
	    	{
	    		var movies = attribute_matrix[ key ][ value ];
	    		for(var i = 0; i < movies.length - 1; i++)
	    		{
	    			var current_item = sys.getNode(movies[ i ].id);
	    			var current_next = sys.getNode(movies[ i + 1 ].id);
	    			sys.addEdge(current_item.name, current_next.name, {weight:defaultWeight})
	    		}
	    	}
	    }
		
	}
	else
	{
		errorAlert("Application Error", response.message);
	}
	hideGlassPane();
}


function recommend_movies_callback( response )
{
	clear_attribute_matrix( );
	
	if(response.status)
	{
		simpleAlert("Application Message", response.entities.length + " Recommendations.");
		recommendations = response.entities;
		build_attribute_matrix( recommendations );
		console.log(attribute_matrix.length);
	    
		//var sys = arbor.ParticleSystem(1000, 600, 0.5) // create the system with sensible repulsion/stiffness/friction
	    sys.parameters({gravity:true}) // use center-gravity to make the graph settle nicely (ymmv)
	    sys.renderer = get_current_renderer("#viewport") // our newly created renderer will have its .init() method called shortly by sys...
		var defaultWidth = 80; //53
		var defaultHeight = 120; //80
		var defaultWeight = 5

		//alert("Drawing the nodes");
	    
		//adding the nodes to the graph
		for(var i = 0; i < recommendations.length; i++)
		{
			var iconpath = recommendations[i].posterExistence ? "images/movieitem/" + recommendations[i].id + ".jpg" : "images/nomovieicon.jpg";
			sys.addNode(recommendations[i].id, {icon:iconpath, width:defaultWidth, height:defaultHeight, grow_by_attribute: false});	
		}
	    
	    //drawing the edges
	    for(var key in attribute_matrix)
	    {
	    	for(var value in attribute_matrix[ key ])
	    	{
	    		var movies = attribute_matrix[ key ][ value ];
	    		for(var i = 0; i < movies.length - 1; i++)
	    		{
	    			var current_item = sys.getNode(movies[ i ].id);
	    			var current_next = sys.getNode(movies[ i + 1 ].id);
	    			if(sys.getEdges(current_item, current_next).length > 0 || sys.getEdges(current_next, current_item).length > 0)
	    			{
	    				edges = sys.getEdges(current_item, current_next).concat(sys.getEdges(current_next, current_item));
	    				for(var i = 0; i < edges.length; i++)
	    				{
	    					if(edges[i].data.label == "")
	    					{
	    						edges[i].data.label = value;
	    					}
	    					else
	    					{
	    						edges[i].data.label += " | " + value;
	    					}
	    				}
	    			}
	    			else
	    			{
		    			sys.addEdge(current_item.name, current_next.name, {weight:defaultWeight, showlabel:false, label:value, length:6})
	    			}
	    		}
	    	}
	    }
		
	}
	else
	{
		errorAlert("Application Error", response.message);
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
		errorAlert("Application Error", data.message);
	}
	hideGlassPane();
}

/*******************************GRAPH RENDERING CODE*******************************************/
function get_current_renderer( canvas )
{
    var canvas = $(canvas).get(0)
    var ctx = canvas.getContext("2d");
	var defaultWidth = 80;   //53
	var defaultHeight = 120; //80
	var defaultWeight = 5
	var tweenLength = 0.3
	var now, duration, previous_click;

    var that = {
      init:function(system){
        //
        // the particle system will call the init function once, right before the
        // first frame is to be drawn. it's a good place to set up the canvas and
        // to pass the canvas size to the particle system
        //
        // save a reference to the particle system for use in the .redraw() loop
        particleSystem = system

        // inform the system of the screen dimensions so it can map coords for us.
        // if the canvas is ever resized, screenSize should be called again with
        // the new dimensions
        particleSystem.screenSize(canvas.width, canvas.height) 
        particleSystem.screenPadding(80) // leave an extra 80px of whitespace per side
        
        // set up some event handlers to allow for node-dragging
        that.initMouseHandling()
      },
      
      redraw:function(){
        // 
        // redraw will be called repeatedly during the run whenever the node positions
        // change. the new positions for the nodes can be accessed by looking at the
        // .p attribute of a given node. however the p.x & p.y values are in the coordinates
        // of the particle system rather than the screen. you can either map them to
        // the screen yourself, or use the convenience iterators .eachNode (and .eachEdge)
        // which allow you to step through the actual node objects but also pass an
        // x,y point in the screen's coordinate system
        // 
        ctx.fillStyle = "white"
        ctx.fillRect(0,0, canvas.width, canvas.height)
        
        particleSystem.eachEdge(function(edge, pt1, pt2){
          // edge: {source:Node, target:Node, length:#, data:{}}
          // pt1:  {x:#, y:#}  source position in screen coords
          // pt2:  {x:#, y:#}  target position in screen coords

          // draw a line from pt1 to pt2
          ctx.strokeStyle = "rgba(0,0,0, .333)"
          ctx.lineWidth = edge.data.weight
          ctx.beginPath()
          ctx.moveTo(pt1.x, pt1.y)
          ctx.lineTo(pt2.x, pt2.y)
          ctx.stroke()
          
          if(edge.data.showlabel)
          {
        	  ctx.fillStyle = "black";
              ctx.font = '15px bold sans-serif';
              ctx.fillText (edge.data.label, (pt1.x + pt2.x) / 2, (pt1.y + pt2.y) / 2);        	  
          }
          
        })

        particleSystem.eachNode(function(node, pt)
		{
		  var x = pt.x - node.data.width / 2;
		  var y = pt.y - node.data.height / 2;
		  if(node.data.icon == null)
		  {
			  node.data.icon = new Image();
			  node.data.icon.src = 'images/test_icon.png';
		  }
		  
		  var icon = new Image();
		  icon.onload = function ( ) { }; //nothing happens
		  icon.onerror = function( ) {
			  console.log("Could not find icon for this movie.");
			  icon.src = 'images/test_icon.png';
		  }
		  icon.src = node.data.icon;
		  //alert(node.data.icon);
		  ctx.drawImage(icon, x, y, node.data.width, node.data.height);
		  if(selectedNode != null && selectedNode == node)
		  {
			  ctx.beginPath();
			  ctx.lineWidth="8";
			  ctx.strokeStyle="orange";
			  ctx.rect(x - 2, y - 2, node.data.width + 2, node.data.height + 2);
			  ctx.stroke();		  
		  }
		  
		  if(node.data.grow_by_attribute)
		  {
			  ctx.beginPath();
			  ctx.lineWidth="12";
			  ctx.strokeStyle="#3399FF";
			  ctx.rect(x - 2, y - 2, node.data.width + 2, node.data.height + 2);
			  ctx.stroke();		  
		  }
		  
        })    			
      },
      
      initMouseHandling:function(){
        // no-nonsense drag and drop (thanks springy.js)
        var dragged = null;

        // set up a handler object that will initially listen for mousedowns then
        // for moves and mouseups while dragging
        var handler = {
          moved:function(e) {
              var pos = $(canvas).offset();
              _mouseP = arbor.Point(e.pageX-pos.left, e.pageY-pos.top)
              dragged = particleSystem.nearest(_mouseP);
              
              if (dragged && dragged.node !== null && hoverNode !== dragged.node) {
                  dragged.node.fixed = true
                  
//                  var all_edges;
//                  
//                  if(hoverNode != null)
//                  {
//                      all_edges = sys.getEdgesFrom(hoverNode).concat(sys.getEdgesTo(hoverNode));
//                      for(var i = 0; i < all_edges.length; i++)
//                      {
//                    	  all_edges[i].data.showlabel = false;
//                      }
//                  }
//                  
//                  hoverNode = dragged.node
//                  
//                  all_edges = sys.getEdgesFrom(dragged.node).concat(sys.getEdgesTo(dragged.node));
//                  for(var i = 0; i < all_edges.length; i++)
//                  {
//                	  all_edges[i].data.showlabel = true;
//                  }
              }
              return false
          },	
          
          clicked:function(e){
            var pos = $(canvas).offset();
            _mouseP = arbor.Point(e.pageX-pos.left, e.pageY-pos.top)
            dragged = particleSystem.nearest(_mouseP);

			
            if (dragged && dragged.node !== null){
              // while we're dragging, don't let physics move the node
              dragged.node.fixed = true
              
              
              if(hoverNode !== dragged.node)
              {
                  var all_edges;
                  
                  if(hoverNode != null)
                  {
                      all_edges = sys.getEdgesFrom(hoverNode).concat(sys.getEdgesTo(hoverNode));
                      for(var i = 0; i < all_edges.length; i++)
                      {
                    	  all_edges[i].data.showlabel = false;
                      }
                  }
                  
                  hoverNode = dragged.node
                  
                  all_edges = sys.getEdgesFrom(dragged.node).concat(sys.getEdgesTo(dragged.node));
                  for(var i = 0; i < all_edges.length; i++)
                  {
                	  all_edges[i].data.showlabel = true;
                  }
              }
              
	            //handling double click here
	            now = new Date().getTime();
				if(!previous_click)
				{
					previous_click = now;
				}
				else
				{
					duration = now - previous_click;
					previous_click = undefined;
					if(duration <= 400 && selectedNode.name == dragged.node.name) //doubleclicking the same node
					{
						//alert("Olay, double click detected");
						clear_attribute_matrix( );
						load_recommendations( selectedNode.name );
						add_movie_to_browsed( selectedNode.name );
						$("#movie_profile_container").hide();
						//dragged.node = null;
						
						return false
					}
				}
			  
			  if(selectedNode != dragged.node)
			  {
				if(selectedNode != null)
				{
					//particleSystem.tweenNode(selectedNode, tweenLength, {width:defaultWidth, height:defaultHeight})
				}
								
				selectedNode = dragged.node
//				particleSystem.tweenNode(selectedNode, tweenLength, {width:80, height:120})
//				resizeNode(particleSystem, null, selectedNode, 0.10, tweenLength, 80, 120, defaultWeight, 0.2)
//				traversed.length = 0;
				load_movie_from_recommendations( selectedNode.name );
			  }
            }

            
			$(canvas).bind('mousemove', handler.dragged)
            $(window).bind('mouseup', handler.dropped)
            $(canvas).unbind('mousemove', handler.moved);            

            return false
          },
                    
          dragged:function(e){
            var pos = $(canvas).offset();
            var s = arbor.Point(e.pageX-pos.left, e.pageY-pos.top)

            if (dragged && dragged.node !== null){
              var p = particleSystem.fromScreen(s)
              dragged.node.p = p
            }
            

            return false
          },

          dropped:function(e){
            if (dragged===null || dragged.node===undefined) return
            if (dragged.node !== null) dragged.node.fixed = false
            dragged.node.tempMass = 1000
            dragged = null
            $(canvas).unbind('mousemove', handler.dragged)
            $(window).unbind('mouseup', handler.dropped)
            $(canvas).bind('mousemove', handler.moved);
            
            _mouseP = null
            
            return false
          }
        }
        
        // start listening
        $(canvas).mousedown(handler.clicked);
        $(canvas).mousemove(handler.moved);
      },
      
    }
    return that
}

function resizeNode(sys, parentNode, node, x, tweenLength, w, h, wgt, wx)
{
	if(traversed.indexOf(node.name) < 0)
	{
		traversed.push(node.name);
		var edges = sys.getEdgesFrom(node)	
		for(var i = 0; i < edges.length; i++)
		{
			var childNode = edges[i].target
			_resizeNode_(sys, parentNode, node, childNode, x, tweenLength, w, h, wgt, wx)
		}
		edges = sys.getEdgesTo(node)
		for(var i = 0; i < edges.length; i++)
		{
			var childNode = edges[i].source
			_resizeNode_(sys, parentNode, node, childNode, x, tweenLength, w, h, wgt, wx)
		}
	}
}

function _resizeNode_(sys, parentNode, node, childNode, x, tweenLength, w, h, wgt, wx)
{
	if(childNode != parentNode)
	{
		var node_size_coef = linear(x)
		new_w = w * node_size_coef
		new_h = h * node_size_coef
		sys.tweenNode(childNode, tweenLength, {width:new_w, height:new_h})
		
		var edge_size_coef = linear(wx)
		var new_weight = wgt * edge_size_coef
		var edges = sys.getEdges(node, childNode)
		if(edges.length != 1)
		{
			edges = sys.getEdges(childNode, node)
		}
		sys.tweenEdge(edges[0], tweenLength, {weight:new_weight})
		
		resizeNode(sys, node, childNode, x + 0.05, tweenLength, new_w, new_h, new_weight, wx + 0.1)
	}
}

function linear(x)
{
	return -0.5 * x + 0.5;
}


/**********************************************************************************************/


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

function send_get_request( url, show_pane )
{
	if(typeof(show_pane)==='undefined') show_pane = true;
	if(show_pane)
	{ 
		showGlassPane();
	}
	
    var _url = url + "?callback=?";
    $.ajax({
       type: 'GET',
        url: _url,
        async: true,
        contentType: "application/json",
        dataType: 'jsonp',
    });	
}


function simpleAlert(_title, msg)
{
	bootbox.dialog({
		message: msg,
		title: _title,
		buttons: {
			main: {
				label: "Close",
				className: "btn-primary",
			}
		}
	});
}

function errorAlert(title, message)
{
	var formatted_title = "<div style='font-size:1.4em;'><img style='width: 30px; height:30px' src='images/error.png' />&nbsp;&nbsp;" + title + "</div>";
	simpleAlert(formatted_title, message);
}

function trailerWindow(_title, trailerid)
{
	$( "#trailer-dialog" ).dialog({
		title : _title,
		modal : true,
		height: "auto",
		width : "auto"
	});	
	$( "#trailer-dialog" ).html('<iframe width="560" height="315" src="http://www.youtube.com/embed/' + trailerid + '" frameborder="0" allowfullscreen></iframe>');
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


function update_visualization(selected_key)
{
    clear_movies_from_canvas( );

	for(var i = 0; i < recommendations.length; i++)
	{
		var iconpath = recommendations[i].posterExistence ? "images/movieitem/" + recommendations[i].id + ".jpg" : "images/nomovieicon.jpg";
		sys.addNode(recommendations[i].id, {icon:iconpath, width:defaultWidth, height:defaultHeight, grow_by_attribute: false});	
	}
    
    //drawing the edges
    //for(var key in attribute_matrix)
    {
    	for(var value in attribute_matrix[ selected_key ])
    	{
    		var movies = attribute_matrix[ selected_key ][ value ];
    		for(var i = 0; i < movies.length - 1; i++)
    		{
    			var current_item = sys.getNode(movies[ i ].id);
    			var current_next = sys.getNode(movies[ i + 1 ].id);
    			if(sys.getEdges(current_item, current_next).length > 0 || sys.getEdges(current_next, current_item).length > 0)
    			{
    				edges = sys.getEdges(current_item, current_next).concat(sys.getEdges(current_next, current_item));
    				for(var i = 0; i < edges.length; i++)
    				{
    					if(edges[i].data.label == "")
    					{
    						edges[i].data.label = value;
    					}
    					else
    					{
    						edges[i].data.label += " | " + value;
    					}
    				}
    			}
    			else
    			{
	    			sys.addEdge(current_item.name, current_next.name, {weight:defaultWeight, showlabel:false, label:value, length:6})
    			}
    		}
    	}
    }
}


function clear_movies_from_canvas()
{
	sys.eachNode(function(node, pt) {
		sys.pruneNode(node);
	});
}

function clear_attribute_matrix( )
{
	//doing a detailed cleaning
	for(var key in attribute_matrix)
	{
		for(var value in attribute_matrix[key])
		{
			attribute_matrix[key][value].length = 0;
		}
		attribute_matrix[key].length = 0;
	}
	
	if(attribute_matrix != null)
	{
		attribute_matrix.length = 0;
	}
}

function build_attribute_matrix( movies )
{
	attribute_matrix = null; //trying to trigger the garbage collection
	attribute_matrix = { };
	clear_attribute_widget( );
	
	for(var i = 0; i < movies.length; i++)
	{
		var movie = movies[i];
		//alert(movie.title + " - " + movie.attributes.length);
		//console.log(movie);
		for(var a = 0; a < movie.attributes.length; a++)
		{
			var attribute = movie.attributes[a];
			if(!attribute_matrix.hasOwnProperty(attribute.key))
			{
				attribute_matrix[attribute.key] = { };
			}
			if(!attribute_matrix[attribute.key].hasOwnProperty(attribute.value))
			{
				attribute_matrix[attribute.key][attribute.value] = new Array();
			}
			attribute_matrix[attribute.key][attribute.value].push(movie);
		}
	}
	
	load_attributes_widget( );
}

function emphasize_by_attributes(_selected_key, selected_value, new_w, new_h, new_w1, new_h1, grow_by_attribute)
{
	var movies = attribute_matrix[_selected_key][selected_value];
	
	sys.eachNode(function(node, pt) {
		if( find_movie_by_id(node.name, movies) !== null )
		{
			node.data.grow_by_attribute = grow_by_attribute;
			sys.tweenNode(node, 0.01, {width:new_w, height:new_h});
		}
		else
		{
			sys.tweenNode(node, 0.01, {width:new_w1, height:new_h1});
		}
	});		
}

function load_attributes_widget( )
{
	var navigation = $("#attribute_list");
	for(var key in attribute_matrix)
	{
		var element;
		if(key == selected_key) element = '<li><a href="#" style="background: #444444; color: white">' + key + '</a>';
		else element = '<li><a href="#">' + key + '</a>';
		
		//console.log(attribute_matrix);
		if(get_value_size( key ) > 0)
		{
			element += '<ul>';
			for(var value in attribute_matrix[key])
			{
				element += '<li><a href="#">' + value + '</a></li>';
			}
			element += '</ul>';
		}
		navigation.prepend(element);
	}
	
	$("#attribute_list").quiccordion();
	
	$("#attribute_list a").mouseover(function(e) {
		if($(this).parent().parent().parent().attr("id") == "attribute_list_container") { }
		else { 
			grow_by_attribute = true;
			emphasize_by_attributes(selected_key, $(this).text(), 120, 180, 40, 60, true);
		}
	});
	
	$("#attribute_list a").mouseout(function(e) {
		if($(this).parent().parent().parent().attr("id") == "attribute_list_container") { }
		else {
			grow_by_attribute = false;
			emphasize_by_attributes(selected_key, $(this).text(), 80, 120, 80, 120, false);
		}
	});
	
    $("#attribute_list a").click(function(e) {
        e.preventDefault();
    	var selected_text = $(this).text();
    	if($(this).parent().parent().parent().attr("id") == "attribute_list_container") //this is a key
    	{
    		if(selected_text !== selected_key)
    		{
    			$(this).parent().siblings("li").children("a").css('background', "");
    			$(this).parent().siblings("li").children("a").css('color', "#444444");
    			selected_key = selected_text;
    	        $(this).css('background', "#AAAAAA");
    	        $(this).css('color', "white");
    	            	        
    	        update_visualization(selected_key);
    		}
    	}
    	
//    	else
//    	{
//    		if(selected_text !== selected_value)
//    		{
//    			$(this).parent().siblings("li").children("a").css('background-color', "");
//    			$(this).parent().siblings("li").children("a").css('color', "#444444");
//    			selected_value = selected_text;
//    	        $(this).css('background', "#AAAAAA");
//    	        $(this).css('color', "white");
//    		}
//    	}
    });
    
}


function clear_attribute_widget( )
{
	$("#attribute_list").empty();
}

function get_key_size( )
{
	var count = 0;
	for(var key in attribute_matrix) count++;
	
	return count;
}

function get_value_size( key )
{
	var count = 0;
	for(var value in attribute_matrix[key]) count++;
	return count;
}

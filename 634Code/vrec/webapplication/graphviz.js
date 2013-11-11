//  main.js
//
//  A project template for using arbor.js
//


(function($){


  var Renderer = function(canvas){
    var canvas = $(canvas).get(0)
    var ctx = canvas.getContext("2d");
    var particleSystem
	var selectedNode = null
	var defaultWidth = 40
	var defaultHeight = 40
	var defaultWeight = 4
	var tweenLength = 0.3

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
        })

        particleSystem.eachNode(function(node, pt)
		{
          // node: {mass:#, p:{x,y}, name:"", data:{}}
          // pt:   {x:#, y:#}  node position in screen coords

		  var x = pt.x - node.data.width / 2;
		  var y = pt.y - node.data.height / 2;
		  if(node.data.icon == null)
		  {
			  node.data.icon = new Image();
			  node.data.icon.src = 'images/test_icon.png';
		  }
//		  ctx.font="30px Verdana"
//		  ctx.fillStyle = 'red'
//		  ctx.fillText(node.name, x, y);
		  var icon = new Image();
		  icon.src = node.data.icon;
		  ctx.drawImage(icon, x, y, node.data.width, node.data.height);
		  
          // draw a rectangle centered at pt
          //var w = 100
          //ctx.fillStyle = (node.data.alone) ? "orange" : "black"
          //ctx.fillRect(pt.x-w/2, pt.y-w/2, w,w)
        })    			
      },
      
      initMouseHandling:function(){
        // no-nonsense drag and drop (thanks springy.js)
        var dragged = null;

        // set up a handler object that will initially listen for mousedowns then
        // for moves and mouseups while dragging
        var handler = {
          clicked:function(e){
            var pos = $(canvas).offset();
            _mouseP = arbor.Point(e.pageX-pos.left, e.pageY-pos.top)
            dragged = particleSystem.nearest(_mouseP);

			
            if (dragged && dragged.node !== null){
              // while we're dragging, don't let physics move the node
              dragged.node.fixed = true
			  
			  if(selectedNode != dragged.node)
			  {
				if(selectedNode != null)
				{
					//particleSystem.tweenNode(selectedNode, tweenLength, {width:defaultWidth, height:defaultHeight})
				}
				selectedNode = dragged.node
				particleSystem.tweenNode(selectedNode, tweenLength, {width:85, height:85})
				resizeNode(particleSystem, null, selectedNode, 0.35, tweenLength, 85, 85, defaultWeight, 0.2)
			  }
            }

            
			$(canvas).bind('mousemove', handler.dragged)
            $(window).bind('mouseup', handler.dropped)

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
            _mouseP = null
            return false
          }
        }
        
        // start listening
        $(canvas).mousedown(handler.clicked);

      },
      
    }
    return that
  }    

//  $(document).ready(function(){
//    var sys = arbor.ParticleSystem(1000, 600, 0.5) // create the system with sensible repulsion/stiffness/friction
//    sys.parameters({gravity:true}) // use center-gravity to make the graph settle nicely (ymmv)
//    sys.renderer = Renderer("#viewport") // our newly created renderer will have its .init() method called shortly by sys...
//	var defaultWidth = 80
//	var defaultHeight = 53
//	var defaultWeight = 4
//
//	//alert("Drawing the nodes");
//    
//	//adding the nodes to the graph
//	for(var recommendation in recommendations)
//	{
//		var iconpath = "images/movieitem/" + recommendation.id + ".jpg";
//		sys.addNode(recommendation.id, {icon:iconpath, width:defaultWidth, height:defaultHeight});	
//	}
//    
//    //drawing the edges
//    for(var key in attribute_matrix)
//    {
//    	for(var value in attribute_matrix[ key ])
//    	{
//    		var movies = attribute_matrix[ key ][ value ];
//    		for(var i = 0; i < movies.length - 1; i++)
//    		{
//    			var current_item = sys.getNode(movies[ i ].id);
//    			var current_next = sys.getNode(movies[ i + 1 ].id);
//    			sys.addEdge(current_item, current_next, {weight:defaultWeight})
//    		}
//    	}
//    }
//    
//	
////    // add some nodes to the graph and watch it go...
////	sys.addNode('a', {size:40, icon:null, width:defaultWidth, height:defaultHeight})
////	sys.addNode('b', {size:40, icon:null, width:defaultWidth, height:defaultHeight})
////	sys.addNode('c', {size:40, icon:null, width:defaultWidth, height:defaultHeight})
////	sys.addNode('d', {size:40, icon:null, width:defaultWidth, height:defaultHeight})
////	sys.addNode('e', {size:40, icon:null, width:defaultWidth, height:defaultHeight})
////	sys.addNode('f', {size:40, icon:null, width:defaultWidth, height:defaultHeight})
////	sys.addNode('g', {size:40, icon:null, width:defaultWidth, height:defaultHeight})
////		
////    sys.addEdge('a','b', {weight:defaultWeight})
////    sys.addEdge('a','c', {weight:defaultWeight})
////    sys.addEdge('a','d', {weight:defaultWeight})
////    sys.addEdge('a','e', {weight:defaultWeight})
////	sys.addEdge('e','f', {weight:defaultWeight})
////	sys.addEdge('f','g', {weight:defaultWeight})
//    
//  })
//
//})(this.jQuery)


function resizeNode(sys, parentNode, node, x, tweenLength, w, h, wgt, wx)
{
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
	return -1.0 * x + 1.0
}

function inverse_square_root(x)
{
	return -1.0 * Math.pow(x, 0.5) + 1.0
}
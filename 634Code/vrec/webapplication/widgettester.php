<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>VREC Visual Media Recommender</title>
<link rel="stylesheet" href="extplugins/simplemodal/assets/css/simplemodal.css" type="text/css" media="screen" title="no title" charset="utf-8">
<link rel="stylesheet" type="text/css" href="extplugins/bootbox/bootstrap.min.css">

<link rel="stylesheet" type="text/css" href="extplugins/rating/rateit.css">

<link rel="stylesheet" type="text/css" href="style.css">

<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />

<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>

 <script type="text/javascript" src="extplugins/youtubepopup/jquery.youtubepopup.min.js"></script>

<script src="extplugins/bootbox/bootstrap.min.js"></script>
<script src="extplugins/bootbox/bootbox.min.js"></script>

<script src="extplugins/rating/jquery.rateit.min.js"></script>

<script src="extplugins/arbor/arbor.js"></script>  
<script src="extplugins/arbor/arbor-tween.js"></script>  


<script type="text/javascript" src="client.js"></script>
<script type="text/javascript">
$(function() { $( "#tabs" ).tabs({ collapsible: true }); }); //loading the movie list tabs


$(function() { $( "#user_rating_control" ).bind('rated', 
		function() 
		{ 
			post_rating($(this).rateit('value'))
		}
	); 
});

$(function() { $( "#user_rating_control" ).bind('over', 
		function() 
		{ 
			update_user_rating($(this).rateit('value'))
		}
	); 
});

</script>
<style type="text/css">
.ajaxLoader {
    position: fixed;
    top: 40%;
    left: 45%;
    margin-top: -50px;
    margin-left: -50px;
    z-index: 99999;
}
</style>
</head>
    <body>
    	<div id="glass_pane" style=" display: none; background-color: 4F4F4F; z-index: 999; width : 100%; height: 100%; opacity: 0.7; position: fixed; top: 0; left: 0;">
    		<img id="imgAjaxLoader" class="ajaxLoader" src="images/ajax-loader2.gif" />
    	</div>
    	<table style="width: 100%; height:100%;">
    		<tr>
    			<td><?php include 'widgets/user_profile_widget.php'; ?></td>
    			<td><?php include 'widgets/user_movies_widget.php'; ?></td>
    			<td><?php include 'widgets/movie_profile_widget.php'; ?></td>
    		</tr>
    		<tr>
    			<td></td>
    			<td><?php include 'widgets/rec_visualization_widget.php'; ?></td>
    			<td></td>
    		</tr>
    	</table>
    </body>
</html>
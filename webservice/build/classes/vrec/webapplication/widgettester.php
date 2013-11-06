<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>VREC Visual Media Recommender</title>
<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<script type="text/javascript" src="spin.min.js"></script>
<script type="text/javascript" src="client.js"></script>
<script type="text/javascript">
$(function() { $( "#tabs" ).tabs({ collapsible: true }); }); //loading the movie list tabs
</script>
<style type="text/css">
.ajaxLoader {
    position: fixed;
    top: 50%;
    left: 50%;
    margin-top: -24px;
    margin-left: -24px;
    z-index: 99999;
}
</style>
</head>
    <body>
    	<div id="glass_pane" style=" display: none; background-color: gray; z-index: 999; width : 100%; height: 100%; opacity: 0.4; position: fixed; top: 0; left: 0;">
    		<img id="imgAjaxLoader" class="ajaxLoader" src="images/ajax-loader.gif" />
    	</div>
    	<table>
    		<tr>
    			<td><?php include 'widgets/user_profile_widget.php'; ?></td>
    			<td><?php include 'widgets/user_movies_widget.php'; ?></td>
    		</tr>
    	</table>
    </body>
</html>
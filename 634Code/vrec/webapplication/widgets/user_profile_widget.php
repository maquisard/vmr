
<div id="user_profile_container" style="width: 200px;">
	<div id="random_users" style="width: 100%">
		<select id="random_user_list" style="width: 150px;" onchange="set_current_user_in_session()">
		</select>	
	</div>
	<div id="profile_image_container">
		<img id="profile_image" alt="Current User Profile Image" src="" />
	</div>
	<div id="user_info_container">
		<table style="width: 100%;" >
			<tr>
				<td id="user_name_label" style="text-align: left;">Name: </td>
				<td id="user_name_value" style="text-align: right;"></td>
			</tr>
			<tr>
				<td id="user_age_label" style="text-align: left;">Age: </td>
				<td id="user_age_value" style="text-align: right;"></td>
			</tr>
			<tr>
				<td id="user_gender_label" style="text-align: left;">Gender: </td>
				<td id="user_gender_value" style="text-align: right;"></td>
			</tr>
			<tr>
				<td id="user_occupation_label" style="text-align: left;">Occupation: </td>
				<td id="user_occupation_value" style="text-align: right;"></td>
			</tr>
			<tr>
				<td id="user_zipcode_label" style="text-align: left;">Zipcode: </td>
				<td id="user_zipcode_value" style="text-align: right;"></td>
			</tr>
		</table>
	</div>
	<script type="text/javascript">
		retrieve_random_users();
	</script>
</div>

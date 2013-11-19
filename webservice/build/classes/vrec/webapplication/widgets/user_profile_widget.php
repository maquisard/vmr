
<div id="user_profile_container" style="width: 200px;">
	<div id="random_users" style="width: 100%">
		<select id="random_user_list" style="width: 202px;" onchange="set_current_user_in_session()">
		</select>	
	</div>
	<div id="profile_image_container" style="border: 0.5px solid black">
		<img id="profile_image" alt="Current User Profile Image" src="" />
	</div>
	<div id="user_info_container">
		<table style="width: 100%; margin-top : 10px;" >
			<tr class="user_info_row">
				<td id="user_name_label" style="text-align: left; font-weight: bold">Name: </td>
				<td id="user_name_value" style="text-align: right;"></td>
			</tr>
			<tr class="user_info_row">
				<td id="user_age_label" style="text-align: left; font-weight: bold">Age: </td>
				<td id="user_age_value" style="text-align: right;"></td>
			</tr>
			<tr class="user_info_row">
				<td id="user_gender_label" style="text-align: left; font-weight: bold">Gender: </td>
				<td id="user_gender_value" style="text-align: right;"></td>
			</tr>
			<tr class="user_info_row">
				<td id="user_occupation_label" style="text-align: left; font-weight: bold">Occupation: </td>
				<td id="user_occupation_value" style="text-align: right;"></td>
			</tr>
			<tr class="user_info_row">
				<td id="user_zipcode_label" style="text-align: left; font-weight: bold">Zipcode: </td>
				<td id="user_zipcode_value" style="text-align: right;"></td>
			</tr>
		</table>
	</div>
	<script type="text/javascript">
		retrieve_random_users();
	</script>
</div>

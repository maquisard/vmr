<div id="movie_profile_container" style="width: 300px; vertical-align: top; display: none;">
	<div id="movie_profile_image_container">
		<img id="movie_profile_image" alt="Selected Movie Profile Image" src="" style="width: 201px; height: 300px;" />
	</div>
	<div id="movie_info_container" style="width: 100%;">
		<div id="movie_title" class="movie_title_profile_row">
		</div>
		<div id="movie_genre" class="movie_profile_row">
			<span id="movie_genre_label" style="font-weight: bold;">Genres: </span>
			<span id="movie_genre_value" style=""></span>
		</div>
		<div id="movie_duration" class="movie_profile_row">
			<span id="movie_duration_label" style="font-weight: bold;">Run Time: </span>
			<span id="movie_duration_value" style=""></span>
		</div>
		<div id="movie_imdb_rating" class="movie_profile_row">
			<span id="movie_imdb_label" style="font-weight: bold;">IMDb: </span>
			<input type="range" min="0" max="10" value="0" step="0.5" id="imdb_rating_backing">
			<div id="imdb_rating_control" class="rateit" data-rateit-backingfld="#imdb_rating_backing" data-rateit-readonly="true"></div>		
			<span id="movie_imdb_value" style=""></span>
		</div>		
		<div id="movie_user_rating" class="movie_profile_row">
			<span id="movie_user_label" style="font-weight: bold;">Yours: </span>
			<input type="range" min="0" max="10" value="0" step="0.5" id="user_rating_backing">
			<div id="user_rating_control" class="rateit" data-rateit-backingfld="#user_rating_backing"></div>		
			<span id="movie_user_value" style=""></span>
			</div>		
		<div id="movie_description" class="movie_profile_row">
			<span id="movie_description_label" style="font-weight: bold;"></span>
			<span id="movie_description_value" style=""></span>
		</div>		
		<div id="movie_cast" class="movie_profile_row">
			<span id="movie_cast_label" style="font-weight: bold;">Cast: </span>
			<span id="movie_cast_value" style=""></span>
		</div>
		<div id="add_to_queue" class="movie_profile_row">
			<button id="add_to_queue_button" class="auto-generate-button">Add to Queue</button>
		</div>
		<div id="trailer" class="movie_profile_row">
			<button id="trailer_button" class="auto-generate-button">Watch Trailer</button>
			<div id="trailer-dialog" title="movie title" style="display: none"></div>
		</div>
	</div>
</div>

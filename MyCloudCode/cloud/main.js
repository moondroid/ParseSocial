
// Use Parse.Cloud.define to define as many cloud functions as you want.
// For example:
Parse.Cloud.define("hello", function(request, response) {
  response.success("Hello world!");
});

Parse.Cloud.define("countComments", function(request, response) {
  var query = new Parse.Query("Comment");
  query.equalTo("postId", request.params.postId);
  query.find({
    success: function(results) {
      response.success(results.length);
    },
    error: function() {
      response.error("comments lookup failed");
    }
  });
});

Parse.Cloud.afterSave("Comment", function(request) {
  Parse.Cloud.useMasterKey();
  query = new Parse.Query("Post");
  query.get(request.object.get("post").id, {
    success: function(post) {
      post.increment("num_comments");
	  var relation = post.relation("comments");
	  relation.add(request.object);
      post.save();
    },
    error: function(error) {
      console.error("Got an error " + error.code + " : " + error.message);
    }
  });
});

Parse.Cloud.afterDelete("Comment", function(request) {
  Parse.Cloud.useMasterKey();
  query = new Parse.Query("Post");
  query.get(request.object.get("post").id, {
    success: function(post) {
      post.increment("num_comments", -1);
	  var relation = post.relation("comments");
	  relation.remove(request.object);
      post.save();
    },
    error: function(error) {
      console.error("Got an error " + error.code + " : " + error.message);
    }
  });
});

var countLikes = function(postPointer, callback) {
	var query = new Parse.Query("Like");
	query.equalTo("post", postPointer);
	query.find({
		success: function(results) {
		  console.log("countLikes: "+results.length);
		  //return results.length;
		  callback(results.length);		  
		},
		error: function() {
		  console.error("Like lookup failed");
		  callback(null);
		}
  });

}

//Before adding a new like, checks if the user already liked the same post
Parse.Cloud.beforeSave("Like", function(request, response) {
  query = new Parse.Query("Like");
  query.equalTo("post", request.object.get("post"));
  query.equalTo("from", request.object.get("from"));
  query.count({
	  success: function(count) {
		  if(count == 0){
			response.success();
		  }else{
			response.error("user already liked this post");
		  }		
	  },
	  error: function(error) {
		response.error(error);
	  }
  });

});




//After adding a new like, updates the post fields num_likes and user_likes	  
Parse.Cloud.afterSave("Like", function(request) {
  Parse.Cloud.useMasterKey();
  query = new Parse.Query("Post");
  query.get(request.object.get("post").id, {
    success: function(post) {
      post.increment("num_likes");
	  var user = request.object.get("from");
	  var relation = post.relation("user_likes");
	  relation.add(user);
	  post.add("user_array_likes", user);
      post.save();	  
    },
    error: function(error) {
      console.error("Got an error " + error.code + " : " + error.message);
    }
  });
});

//After removing a like, updates the post fields num_likes and user_likes
Parse.Cloud.afterDelete("Like", function(request) {
  Parse.Cloud.useMasterKey();
  query = new Parse.Query("Post");
  query.get(request.object.get("post").id, {
    success: function(post) {
      post.increment("num_likes", -1);
	  var user = request.object.get("from");
	  var relation = post.relation("user_likes");
	  relation.remove(user);
	  post.remove("user_array_likes", user);
	  console.log("numLikes: "+post.get("num_likes"));
	  post.save();
	  // post.save(null, {
            // success:function (post) {
                // console.log("Successfully saved a post");
                // response.success();
            // },
            // error:function (post, error) {
                // console.log("Could not save a post " + error.message);
                // response.error(error.message);
            // }
        // }
	  // );
	  //countLikes(request.object.get("post"), function(count){
		//  console.log("numLikes: "+count);
		  // if (count != null){post.set("num_likes", count-1);} 
		  // var relation = post.relation("user_likes");
		  // var user = request.object.get("from");
		  // relation.remove(user);
		  // var users = post.get("user_array_likes");
		  //remove user
		  //for (var i =0; i < users.length; i++)
			//if (users[i].id === user.id) {
			//users.splice(i,1);
			//break;
		  //}
		  //post.set("user_array_likes", users);
		  // post.remove("user_array_likes", user);
		  // post.save();
	  
	  // });
	  
    },
    error: function(error) {
      console.error("Got an error " + error.code + " : " + error.message);
    }
  });
});

//Get a post and retrieve if is liked by current user
Parse.Cloud.define("getPostWithLike", function(request, response) {
  var query = new Parse.Query("Post");
  query.include("from"); //retrieve user also
  query.get(request.params.postId, {
    success: function(post) {
	  query = new Parse.Query("Like");
	  query.equalTo("post", post);
	  query.equalTo("from", request.user);
	  query.count({
		  success: function(count) {
			  if(count == 0){			    
				response.success({"post" : post, "isLikedByMe": false});
			  }else{			   
				response.success({"post" : post, "isLikedByMe": true});
			  }
		  },
		  error: function(error) {
			response.error(error);
		  }
	  });	
      
    },
    error: function() {
      response.error(error);
    }
  });
});

Parse.Cloud.define("getPostsWithLike", function(request, response) {

  var PostCollection = Parse.Collection.extend({
	model: "Post",
	query: (new Parse.Query("Post")).include("from").include("user_array_likes").descending("date")
  });
  var collection = new PostCollection();
  collection.fetch({
	  success: function(collection) {
		collection.each(function(object) {
		  var users = object.get("user_array_likes");
		  console.warn("users: "+users.length);
		  object.isLikedByMe = false;
		  for (i = 0; i < users.length; i++) {
			if(users[i].id==request.user.id){
				object.isLikedByMe = true;
				break;
			}
          }
		  console.warn("object.isLikedByMe = "+object.isLikedByMe);					  
		  
		  console.warn(object);
		});
		response.success(collection);
	  },
	  error: function(collection, error) {
		// The collection could not be retrieved.
		console.error("Got an error " + error.code + " : " + error.message);
		response.error(error);
	  }
  });
});



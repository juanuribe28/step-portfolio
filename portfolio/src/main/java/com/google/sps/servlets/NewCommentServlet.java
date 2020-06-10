// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.sps.data.Comment;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.datastore.*;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.*;

import com.google.gson.Gson;

/** Servlet responisble for creating new comments.*/
@WebServlet("/new-comment")
public class NewCommentServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    if (!userService.isUserLoggedIn()) {
      response.sendRedirect("/");
      return;
    }

    String title = request.getParameter("comment-title");
    String author = request.getParameter("name");
    long currentTime = System.currentTimeMillis();
    String comment = request.getParameter("textfield");
    long rating = Long.parseLong(request.getParameter("rating"));
    String userId = userService.getCurrentUser().getUserId();
    Key userKey = getUserKey(userId);

    Entity commentEntity = new Entity("Comment", userKey);
    commentEntity.setProperty("title", title);
    commentEntity.setProperty("author", author);
    commentEntity.setProperty("timestamp", currentTime);
    commentEntity.setProperty("comment", comment);
    commentEntity.setProperty("rating", rating);
    commentEntity.setProperty("userId", userId);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(commentEntity);

    response.sendRedirect("/contact.html");
  }

  private Key getUserKey(String userId) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Query.Filter userFilter = new Query.FilterPredicate("id", Query.FilterOperator.EQUAL, userId);
    Query query = new Query("User").setFilter(userFilter);
    PreparedQuery results = datastore.prepare(query);
    Entity userEntity = results.asSingleEntity();
    return userEntity.getKey();
  }
}

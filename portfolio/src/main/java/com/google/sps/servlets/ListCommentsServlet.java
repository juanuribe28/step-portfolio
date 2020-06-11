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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.*;

import com.google.gson.Gson;

/** Servlet that lists the comments.*/
@WebServlet("/list-comments")
public class ListCommentsServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String sortingParameter = request.getParameter("sorting");
    String sortingDirectionString = request.getParameter("dir");
    String showOnlyUserComments = request.getParameter("mine");

    SortDirection sortingDirection = sortingDirectionString.equals("descending") ? SortDirection.DESCENDING : SortDirection.ASCENDING;

    Query query = new Query("Comment").addSort(sortingParameter, sortingDirection);
    if (showOnlyUserComments.equals("true")) {
      UserService userService = UserServiceFactory.getUserService();
      String userId = userService.getCurrentUser().getUserId();
      Query.Filter userFilter = new Query.FilterPredicate("userId", Query.FilterOperator.EQUAL, userId);
      query = query.setFilter(userFilter);
    }

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    int nComments = getNCommentsChoice(request);

    if (nComments == -1) {
      response.setContentType("text/html");
      response.getWriter().println("Please enter an integer greater than or equal to 1 for nComments");
      return;
    }
    
    List<Entity> topResults = results.asList(FetchOptions.Builder.withLimit(nComments));

    ArrayList<Comment> commentList = new ArrayList<Comment>();
    for (Entity entity : topResults) {
      String title = (String) entity.getProperty("title");
      String author = (String) entity.getProperty("author");
      long timestamp = (long) entity.getProperty("timestamp");
      long rating = (long) entity.getProperty("rating");
      String comment = (String) entity.getProperty("comment");
      String imageUrl = (String) entity.getProperty("imageUrl");
      String userId = (String) entity.getProperty("userId");
      double sentimentScore = (double) entity.getProperty("sentimentScore");
      long id = entity.getKey().getId();

      Comment commentObject = new Comment.CommentBuilder(id, userId)
      .title(title)
      .author(author)
      .timestamp(timestamp)
      .rating(rating)
      .comment(comment)
      .imageUrl(imageUrl)
      .sentimentScore(sentimentScore)
      .build();
      
      commentList.add(commentObject);
    }

    Gson gson = new Gson();

    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(commentList));
  }

  /** Returns the number of comments entered by the user, or -1 if the choice was invalid. */
  private int getNCommentsChoice(HttpServletRequest request) {
    String nCommentsString = request.getParameter("nComments");
    int nComments;
    try {
      nComments = Integer.parseInt(nCommentsString);
    } catch (NumberFormatException e) {
      System.err.println("Could not convert to int: " + nCommentsString);
      return -1;
    }

    if (nComments < 1) {
      System.err.println("Number of comments choice is out of range: " + nCommentsString);
      return -1;
    }

    return nComments;
  }
}

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
import com.google.appengine.api.blobstore.*;
import com.google.appengine.api.images.*;

import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.*;
import java.net.*;

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
    String blobKeyString = getBlobKeyString(request, "comment-image");
    float sentimentScore = getSentimentScore(comment);

    Entity commentEntity = new Entity("Comment", userKey);
    commentEntity.setProperty("title", title);
    commentEntity.setProperty("author", author);
    commentEntity.setProperty("timestamp", currentTime);
    commentEntity.setProperty("comment", comment);
    commentEntity.setProperty("rating", rating);
    commentEntity.setProperty("userId", userId);
    commentEntity.setProperty("blobKeyString", blobKeyString);
    commentEntity.setProperty("sentimentScore", sentimentScore);

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

  /** Returns a URL that points to the uploaded file, or null if the user didn't upload a file. */
  private String getBlobKeyString(HttpServletRequest request, String formInputElementName) {
    // TODO: Investigate options for image validation (https://stackoverflow.com/q/10779564/873165).
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(request);
    List<BlobKey> blobKeys = blobs.get(formInputElementName);

    // User submitted form without selecting a file, so we can't get a URL. (dev server)
    if (blobKeys == null || blobKeys.isEmpty()) {
      return null;
    }

    // Our form only contains a single file input, so get the first index.
    BlobKey blobKey = blobKeys.get(0);

    // User submitted form without selecting a file, so we can't get a URL. (live server)
    BlobInfo blobInfo = new BlobInfoFactory().loadBlobInfo(blobKey);
    if (blobInfo.getSize() == 0) {
      blobstoreService.delete(blobKey);
      return null;
    }

    return blobKey.getKeyString();
  }



  /** Returns the sentiment score of the given comment, or 0 if the user didn't write a comment. */
  private float getSentimentScore(String comment) throws IOException {
    if (comment == null || comment.equals("")) {
      return 0;
    }
    Document commentDoc = Document.newBuilder()
                   .setContent(comment)
                   .setType(Document.Type.PLAIN_TEXT)
                   .build();
    LanguageServiceClient languageService = LanguageServiceClient.create();
    Sentiment sentiment = languageService.analyzeSentiment(commentDoc).getDocumentSentiment();
    float score = sentiment.getScore();
    languageService.close();
    return score;
  }
}

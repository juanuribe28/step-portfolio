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
    String imageUrl = getUploadedFileUrl(request, "comment-image");

    Entity commentEntity = new Entity("Comment", userKey);
    commentEntity.setProperty("title", title);
    commentEntity.setProperty("author", author);
    commentEntity.setProperty("timestamp", currentTime);
    commentEntity.setProperty("comment", comment);
    commentEntity.setProperty("rating", rating);
    commentEntity.setProperty("userId", userId);
    commentEntity.setProperty("imageUrl", imageUrl);

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
  private String getUploadedFileUrl(HttpServletRequest request, String formInputElementName) {
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(request);
    List<BlobKey> blobKeys = blobs.get(formInputElementName);

    BlobKey blobKey = getBlobKey(blobKeys, blobstoreService);
    if (blobKey == null) {
      return null;
    }

    // TODO: Investigate options for image validation (https://stackoverflow.com/q/10779564/873165).

    // Use ImagesService to get a URL that points to the uploaded file.
    ImagesService imagesService = ImagesServiceFactory.getImagesService();
    ServingUrlOptions options = ServingUrlOptions.Builder.withBlobKey(blobKey);

    // To support running in Google Cloud Shell with AppEngine's devserver, we must use the relative
    // path to the image, rather than the path returned by imagesService which contains a host.
    try {
      URL url = new URL(imagesService.getServingUrl(options));
      return url.getPath();
    } catch (MalformedURLException e) {
      return imagesService.getServingUrl(options);
    }
  }

  private BlobKey getBlobKey(List<BlobKey> blobKeys, BlobstoreService blobstoreService) {
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
    return blobKey;
  }
}

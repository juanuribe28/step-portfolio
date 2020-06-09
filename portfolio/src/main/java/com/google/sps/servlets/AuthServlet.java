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

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/auth")
public class AuthServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json;");
    PrintWriter out = response.getWriter();

    UserService userService = UserServiceFactory.getUserService();

    boolean loginStatus = userService.isUserLoggedIn();

    String authUrl;

    if (!loginStatus) {
      authUrl = userService.createLoginURL("/"); //  TODO: Redirect to the page where the reuqest was made.
    } else {
      authUrl = userService.createLogoutURL("/"); //  TODO: Redirect to the page where the reuqest was made.

      Entity userEntity = makeUserEntity(userService);
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      datastore.put(userEntity);
    }
    out.println(String.format("{ \"login\" : %b, \"url\" : \"%s\"}", loginStatus, authUrl));
  }

  private Entity makeUserEntity(UserService userService) {
    String userId = userService.getCurrentUser().getUserId();
    String userEmail = userService.getCurrentUser().getEmail();
    String username = userEmail.split("@", 0)[0];  //  TODO: Let the user choose their username.

    Entity userEntity = new Entity("User", userId);
    userEntity.setProperty("id", userId);
    userEntity.setProperty("email", userEmail);
    userEntity.setProperty("username", username);

    return userEntity;
  }
}

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

package com.google.sps;

/**
 * Utility class for creating greeting messages.
 */
public class Greeter {
  /**
   * Returns a greeting for the given name.
   */
  public String greet(String name) {
    name = removeWhitespaces(name);
    return "Hello " + name;
  }

  public String removeWhitespaces(String string) {
    return string.replaceAll("\\s", "");
  }
}

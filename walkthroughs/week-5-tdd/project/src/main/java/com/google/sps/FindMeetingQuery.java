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

import java.util.Collection;
import java.util.Collections;
import java.util.Arrays;
import java.util.TreeSet;
import java.util.SortedSet;
import java.util.ArrayList;

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    if (request.getDuration() > TimeRange.WHOLE_DAY.duration()) {
      return Collections.emptyList();
    }
    if (events.isEmpty()) {
      return Arrays.asList(TimeRange.WHOLE_DAY);
    }
    Collection<TimeRange> meetingTimes = new TreeSet<>(TimeRange.ORDER_BY_START);
    meetingTimes.add(TimeRange.WHOLE_DAY);
    for (Event event : events) {
      TimeRange eventTime = event.getWhen();
      for (TimeRange meetingTime : meetingTimes) {
        if (eventTime.overlaps(meetingTime)) {
          TimeRange before = TimeRange.fromStartEnd(meetingTime.start(), eventTime.start(), false);
          TimeRange after = TimeRange.fromStartEnd(eventTime.end(), meetingTime.end(), false);
          meetingTimes.remove(meetingTime);
          meetingTimes.add(before);
          meetingTimes.add(after);
        }
      }
    }
    Collection<TimeRange> finalMeetingTimes = new ArrayList<>();
    finalMeetingTimes.addAll(meetingTimes);
    return finalMeetingTimes;
  }
}

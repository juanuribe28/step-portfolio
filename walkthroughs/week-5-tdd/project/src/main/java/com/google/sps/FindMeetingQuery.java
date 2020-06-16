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
    long meetingDuration = request.getDuration();
    if (meetingDuration > TimeRange.WHOLE_DAY.duration()) {
      return Collections.emptyList();
    }
    if (events.isEmpty()) {
      return Arrays.asList(TimeRange.WHOLE_DAY);
    }
    Collection<String> mandatoryMeetingAttendees = request.getAttendees();
    Collection<String> optionalMeetingAttendees = request.getOptionalAttendees();
    Collection<TimeRange> meetingTimes = new TreeSet<>(TimeRange.ORDER_BY_START);
    meetingTimes.add(TimeRange.WHOLE_DAY);
    Collection<Event> optionalEvents = new ArrayList<>();
    for (Event event : events) {
      Collection<String> eventAttendees = event.getAttendees();
      if (Collections.disjoint(eventAttendees, mandatoryMeetingAttendees)) {
        if (!Collections.disjoint(eventAttendees, optionalMeetingAttendees)) {
          optionalEvents.add(event);
        }
        continue;
      }
      meetingTimes = splitMeetingTimes(event, meetingTimes, meetingDuration);
      if (meetingTimes.isEmpty()) {
        break;
      }
    }

    Collection<TimeRange> optionalMeetingTimes = new TreeSet<>(TimeRange.ORDER_BY_START);
    optionalMeetingTimes.addAll(meetingTimes);
    for (Event optionalEvent : optionalEvents) {
      optionalMeetingTimes = splitMeetingTimes(optionalEvent, optionalMeetingTimes, meetingDuration);
      if (optionalMeetingTimes.isEmpty()) {
        break;
      }
    }

    if (mandatoryMeetingAttendees.isEmpty()) {
      meetingTimes = optionalMeetingTimes;
    } else {
      meetingTimes = optionalMeetingTimes.isEmpty() ? meetingTimes : optionalMeetingTimes;
    }

    Collection<TimeRange> finalMeetingTimes = new ArrayList<>();
    finalMeetingTimes.addAll(meetingTimes);
    return finalMeetingTimes;
  }

  public Collection<TimeRange> splitMeetingTimes(Event event, Collection<TimeRange> meetingTimes, long meetingDuration) {
    TimeRange eventTime = event.getWhen();
    for (TimeRange meetingTime : meetingTimes) {
      if (eventTime.overlaps(meetingTime)) {
        meetingTimes.remove(meetingTime);
        if (meetingTime.start() < eventTime.start()) {
          TimeRange before = TimeRange.fromStartEnd(meetingTime.start(), eventTime.start(), false);
          if (before.duration() >= meetingDuration) {
            meetingTimes.add(before);
          }
        }
        if (eventTime.end() < meetingTime.end()) {
          TimeRange after = TimeRange.fromStartEnd(eventTime.end(), meetingTime.end(), false);
          if (after.duration() >= meetingDuration) {
            meetingTimes.add(after);
          }
        }
      }
    }
    return meetingTimes;
  }
}

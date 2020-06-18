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
import java.util.HashSet;
import java.util.TreeSet;
import java.util.SortedSet;
import java.util.ArrayList;
import java.util.Optional;

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
    int mostOptionalAttendees = 0;
    int numberOptionalMeetingAttendees = optionalMeetingAttendees.toArray().length;
    Collection<TimeRange> mostOptionalMeetingTimes = new TreeSet<>(TimeRange.ORDER_BY_START);
    for (Event optionalEvent : optionalEvents) {
      if (optionalEvent.getWhen().duration() > request.getDuration()) {
        Collection<String> optionalEventAttendees = optionalEvent.getAttendees();
        Collection<String> freeMeetingAttendees = new HashSet<>();
        freeMeetingAttendees.addAll(optionalMeetingAttendees);
        freeMeetingAttendees.removeAll(optionalEventAttendees);
        int numberFreeOptionalAttendees = freeMeetingAttendees.toArray().length;
        if (numberFreeOptionalAttendees > mostOptionalAttendees) {
          mostOptionalMeetingTimes.clear();
          mostOptionalMeetingTimes.add(optionalEvent.getWhen());
          mostOptionalAttendees = numberFreeOptionalAttendees;
        } else if (numberFreeOptionalAttendees == mostOptionalAttendees) {
          mostOptionalMeetingTimes.add(optionalEvent.getWhen());
        }
      }
      optionalMeetingTimes = splitMeetingTimes(optionalEvent, optionalMeetingTimes, meetingDuration);
      if (optionalMeetingTimes.isEmpty()) {
        break;
      }
    }

    if (mandatoryMeetingAttendees.isEmpty()) {
      meetingTimes = optionalMeetingTimes;
    } else {
      meetingTimes = mostOptionalMeetingTimes.isEmpty() ? meetingTimes : mostOptionalMeetingTimes;
      meetingTimes = optionalMeetingTimes.isEmpty() ? meetingTimes : optionalMeetingTimes;
    }

    Collection<TimeRange> finalMeetingTimes = new ArrayList<>();
    finalMeetingTimes.addAll(meetingTimes);
    return finalMeetingTimes;
  }

  public Collection<TimeRange> splitMeetingTimes(Event event, Collection<TimeRange> meetingTimes, long meetingDuration) {
    Collection<TimeRange> newMeetingTimes = new TreeSet<>(TimeRange.ORDER_BY_START);
    TimeRange eventTime = event.getWhen();
    for (TimeRange meetingTime : meetingTimes) {
      newMeetingTimes = updateMeetingTimes(newMeetingTimes, eventTime, meetingTime, meetingDuration);
    }
    return newMeetingTimes;
  }

  public Collection<TimeRange> updateMeetingTimes(Collection<TimeRange> meetingTimes, TimeRange eventTime, TimeRange meetingTime, long meetingDuration) {
    if (eventTime.overlaps(meetingTime)) {
      meetingTimes = addMeetingTime(meetingTimes, meetingTime.start(), eventTime.start(), meetingDuration);
      meetingTimes = addMeetingTime(meetingTimes, eventTime.end(), meetingTime.end(), meetingDuration);
    } else {
      meetingTimes.add(meetingTime);
    }
    return meetingTimes;
  }

  public Collection<TimeRange> addMeetingTime(Collection<TimeRange> meetingTimes, int startTime, int endTime, long meetingDuration) {
    TimeRange timeRange = TimeRange.fromStartEnd(startTime, endTime, false);
    if (timeRange.duration() >= meetingDuration) {
      meetingTimes.add(timeRange);
    }
    return meetingTimes;
  }
}

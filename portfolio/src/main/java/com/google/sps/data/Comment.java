package com.google.sps.data;

import java.util.*;

/** Class containing user comments */
public final class Comment {

  private final String title;
  private final String author;
  private final Date timeStamp;
  private final String comment;
  private final long id;

  public Comment(String title, String author, Date timeStamp, String comment, long id) {
    this.title = title;
    this.author = author;
    this.timeStamp = timeStamp;
    this.comment = comment;
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public String getAuthor() {
    return author;
  }

  public Date getTimeStamp() {
    return timeStamp;
  }

  public String getComment() {
    return comment;
  }

  public long getId() {
    return id;
  }
}

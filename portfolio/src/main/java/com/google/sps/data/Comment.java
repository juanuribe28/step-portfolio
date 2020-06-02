package com.google.sps.data;

import java.util.*;

/** Class containing user comments */
public final class Comment {

  private final String title;
  private final String author;
  private final Date timestamp;
  private final String comment;
  private final long id;

  public Comment(String title, String author, Date timestamp, String comment, long id) {
    this.title = title;
    this.author = author;
    this.timestamp = timestamp;
    this.comment = comment;
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public String getAuthor() {
    return author;
  }

  public Date getTimestamp() {
    return timestamp;
  }

  public String getComment() {
    return comment;
  }

  public long getId() {
    return id;
  }
}

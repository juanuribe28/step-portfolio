package com.google.sps.data;

import java.util.*;

/** Class containing user comments */
public final class Comment {

  private final String title;
  private final String author;
  private final long timestamp;
  private final String comment;
  private final long id;

  public Comment(String title, String author, long timestamp, String comment, long id) {
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

  public long getTimestamp() {
    return timestamp;
  }

  public String getComment() {
    return comment;
  }

  public long getId() {
    return id;
  }
}

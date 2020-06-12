package com.google.sps.data;

/** Class containing an image label. */
public final class Label {

  private final String description;
  private final double score;

  public Label(String description, double score) {
    this.description = description;
    this.score = score;
  }

  public String getDescription() {
    return description;
  }

  public double getScore() {
    return score;
  }
}

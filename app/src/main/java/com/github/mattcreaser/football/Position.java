package com.github.mattcreaser.football;

/**
 *
 */
public enum Position {
  QB(R.color.QB),
  WR(R.color.WR),
  RB(R.color.RB),
  TE(R.color.TE),
  K(R.color.K),
  DEF(R.color.DEF);

  private final int mColor;

  Position(int color) {
    mColor = color;
  }

  public int getColorId() {
    return mColor;
  }

  public static Position fromString(String position) {
    for (Position pos : values()) {
      if (pos.name().equals(position)) {
        return pos;
      }
    }
    throw new IllegalArgumentException("Unknown position name:" + position);
  }
}
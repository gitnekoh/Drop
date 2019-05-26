package pl.thathard.drop.util;

import pl.thathard.drop.turbo.TurboDrop;

public class TurboUtil {

  public static String serialize(TurboDrop turbo) {
    if (turbo == null) {
      turbo = new TurboDrop(false, false, 0, 0);
    }
    String sb = turbo.isActive()
        + "@"
        + turbo.isExp()
        + "@"
        + turbo.getStart()
        + "@"
        + turbo.getLenght();
    return sb;
  }

  public static TurboDrop deserialize(String string) {
    String[] turboString = string.split("@");
    return new TurboDrop(Boolean.valueOf(turboString[0]), Boolean.valueOf(turboString[1]),
        Long.valueOf(turboString[2]), Long.valueOf(turboString[3]));
  }
}

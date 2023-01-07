package org.infernalstudios.jsonentitymodels.util;

import java.util.UUID;

public class RandomUtil {

  public static int getPseudoRandomInt(UUID uuid, int bound) {
    long seed = uuid.getLeastSignificantBits() ^ uuid.getMostSignificantBits();
    seed = (seed ^ 0x5DEECE66DL) & ((1L << 48) - 1);
    return (int) ((seed * bound) >> 48);
  }
}

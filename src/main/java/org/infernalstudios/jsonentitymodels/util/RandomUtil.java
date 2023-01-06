package org.infernalstudios.jsonentitymodels.util;

import java.util.Random;
import java.util.UUID;

public class RandomUtil {
  private static final Random rand = new Random();

  public static int getRandomInt(UUID uuid, int bound) {
    rand.setSeed(uuid.getLeastSignificantBits() ^ uuid.getMostSignificantBits());
    return rand.nextInt(bound);
  }
}

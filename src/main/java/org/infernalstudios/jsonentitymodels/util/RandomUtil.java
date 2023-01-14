package org.infernalstudios.jsonentitymodels.util;

import java.util.UUID;

public class RandomUtil {
  public static final UUID modelUUID = UUID.fromString("eafe83a0-6867-48b6-a3e6-0fa0417929d2");
  public static final UUID textureUUID = UUID.fromString("35b6c69a-2e88-4782-804f-a3659f6263f2");
  public static final UUID animationUUID = UUID.fromString("fcc63193-088b-4d48-aedb-d1332d24c9bf");

  public static int getPseudoRandomInt(long uuid, long resourceUUID, int bound) {
    long seed = uuid ^ resourceUUID;
    seed = (seed ^ 0x5DEECE66DL) & ((1L << 48) - 1);
    return (int) ((seed * bound) >> 48);
  }
}

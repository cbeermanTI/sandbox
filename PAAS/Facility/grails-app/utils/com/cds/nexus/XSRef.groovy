// TODO: move this into a library (plugin) for sharing

package com.cds.nexus

import java.util.concurrent.ConcurrentHashMap

/**
 * XSRef is short for Cross-Service Reference: a way to uniquely identify entities (such as FHIR Resources) across a distributed
 * system of services.
 *
 * Conceptually, an XSRef is an array of 128 bits, consisting of 4 parts:
 * <ol>
 *   <li>16 bit serviceId: identifies the service (not the service provider)</li>
 *   <li>64 bit issuerId, populated by the MAC Address of the machine issuing the reference.
 *      This will typically be a MAC 48 address, but we allow for MAC 64.</li>
 *   <li>16 bit shard id. This identifies the shard where the resource resides.</li>
 *   <li>32 bit instance id</li>
 * </ol>
 *
 * The in-memory representation is two Java Shorts and a Java BigInteger, and the Persistent representation is a Base64 encoded String.
 */
class XSRef {

  private byte[] bytes
  private short serviceId
  private byte[] issuerId
  private short shardId
  private int instanceId
  private String serialized

  private XSRef(short serviceId, byte[] issuerId, short shardId, int instanceId) {
    this.serviceId = serviceId
    this.issuerId = issuerId
    this.shardId = shardId
    this.instanceId = instanceId
  }

  XSRef(String s) {
    if (!serialized) {
      serialized = s
    }
  }

  private void buildBytes() {
    bytes = new byte[16]

    // service Id
    bytes[0] = (byte) ((serviceId >>> 8) & 0xff)
    bytes[1] = (byte) (serviceId & 0xff)

    // issuer Id
    for (int i = 2; i < 10; i++) {
      bytes[i] = issuerId[i - 2]
    }

    // shard Id
    bytes[10] = (byte) ((shardId >>> 8) & 0xff)
    bytes[11] = (byte) (shardId & 0xff)

    // instance Id
    bytes[12] = (byte) ((instanceId >>> 24) & 0xff)
    bytes[13] = (byte) ((instanceId >>> 16) & 0xff)
    bytes[14] = (byte) ((instanceId >>> 8) & 0xff)
    bytes[15] = (byte) (instanceId & 0xff)
  }

  String getSerialized() {
    if (!serialized) {
      buildBytes()
      serialized = Base64.encoder.encodeToString(bytes)
    }
    serialized
  }

  /**
   *   For use only by GORM
   */
  protected void setSerialized(String s) {
    serialized = s
  }

  private void parse() {
    if (!bytes && serialized) {
      byte[] bytes = Base64.decoder.decode(serialized)
      serviceId = deserialize(bytes, serviceId, 0, 1)
      issuerId = deserialize(bytes, issuerId, 2, 9)
      shardId = deserialize(bytes, shardId, 10, 11)
      instanceId = deserialize(bytes, instanceId, 12, 15)
    }
  }

  private deserialize(byte[] bytes, Object number, int from, int to) {
    number = 0
    for (int i = from; i++; i < to) {
      number |= bytes[i]
      number <<= 8
    }
    number |= bytes[to]
    number
  }

  public boolean equals(Object o) {
    if (o instanceof XSRef) {
      XSRef oRef = (XSRef) o
      return oRef.serialized == o.serialized
    }
    return false
  }

  /**
   * Issues cross-service references for a service. Every service (in a jvm) needs one (and only one)
   * of these. Essentially, it's a per-service XSRef factory.
   * The serviceId and issuerId are fixed for any issuer.
   * The shardId is determined dynamically by a Sharder provided by the creator of the issuer.
   * The nextInstance keeps track of the next instanceId to be issued issued for the given shard. When
   * the service comes up,
   * this must be initialized with a value that is greater than the last instance id previously issued.
   *
   * To reduce traffic on the disk, the issuer reserves a batch of id's, writing the last reserved Id
   * into a file. When that id has been assigned, it reserves another bath. If the service goes down,
   * any unassigned id's in the batch will be lost (will never be assigned).
   *
   */
  public static class XSRefIssuer {
    private static final byte[] myMacAddress = getMyMacAddress()

    public static byte[] getMyMacAddress() {
      def macs = NetworkInterface.networkInterfaces.collect { iface -> iface.hardwareAddress
      }
      byte[] mac = macs?.get(0)
      if (mac) {
        if (mac.length == 8) {
          return mac
        } else {
          int len = mac.length
          int lead = 8 - len
          if (lead >= 0) {
            byte[] bytes = new byte[8]
            for (int i = 0; i < lead; i++) {
              bytes[i] = 0
            }
            for (int i = lead; i < 8; i++) {
              bytes[i] = mac[i - lead]
            }
            return bytes
          }
        }
      }
      throw new IllegalStateException("Could not get MAC address!")
    }

    private short serviceId
    private Sharder sharder
    /**
     * Map of the last instance ID  that can be assigned for a shard before reserving another
     * batch of Id's
     */
    Map<Integer, ReservationInfo> reservations = new HashMap<Integer, ReservationInfo>()

    XSRefIssuer(short serviceId, Sharder sharder = Sharder.one) {
      this.serviceId = serviceId
      this.sharder = sharder
    }

    public XSRef issueId() {

      short shardId = sharder.pickShard()
      ReservationInfo resInfo = reservations.get(shardId)
      if (!resInfo) {
        resInfo = new ReservationInfo()
        reservations.put(shardId, resInfo)
      }
      int instanceId = resInfo.nextInstanceId()
      new XSRef(serviceId, myMacAddress, shardId, instanceId)
    }

    /**
     * For test use only.
     * @param macAddress
     * @param shardId
     * @param instanceId
     * @return
     */
    public XSRef issueTestId(byte[] macAddress, short shardId, int instanceId) {
      new XSRef(serviceId, macAddress, shardId, instanceId)
    }

    static class ReservationInfo {

      private int lastReserved
      private int lastAssigned

      // TODO: persist lastReserved and improve the reservation process
      int nextInstanceId() {
        if (lastAssigned == lastReserved) {
          // reserve more
          lastReserved += 10
        }
        ++lastAssigned
      }
    }

  }

  abstract static class Sharder {
    // A concrete sharder that assigns everything to one "shard"
    static Sharder one = new Sharder() {
      short pickShard() {
        1
      }

    }

    abstract short pickShard()
  }

}

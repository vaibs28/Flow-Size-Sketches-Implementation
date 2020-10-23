import java.util.*;

public class CounterSketch {
  Random rand = new Random();
  String[] flowIds;
  int[][] counterArr;
  int[][] hashes;
  int[] s;
  Map<String, Integer> estimatedSize;
  Map<String, Integer> actualSize;
  Map<String, Integer> error;
  int k;
  int w;
  int n;

  public CounterSketch(int n, int k, int w, String[] flowIds) {
    this.k = k;
    this.n = n;
    this.w = w;
    s = new int[k];
    counterArr = new int[k][w];
    hashes = new int[k][];
    estimatedSize = new HashMap();
    actualSize = new HashMap();
    error = new HashMap();
    generateHash();
    rand = new Random();
    this.flowIds = flowIds;
  }

  public void generateHash() {
    for (int i = 0; i < k; i++) {
      s[i] = Math.abs(rand.nextInt());
    }
  }

  public void recordAll() {
    for (int i = 0; i < flowIds.length; i++) {
      String s = flowIds[i];
      if (s == null)
        continue;
      String[] arr = s.split("\\s+");
      String flowId = arr[0];
      String flowSize = arr[1];
      actualSize.put(flowId, Integer.parseInt(flowSize));
      record(flowId);
    }
  }

  public void record(String flowId) {
    // get a hash for each of the k arrays by xoring with the string hashcode and record 1
    for (int i = 0; i < k; i++) {
      int value = actualSize.get(flowId);

      for (int j = 0; j < value; j++) {
        int hash = Math.abs(flowId.hashCode() ^ s[i]) % w;
        if (getFirstBit(hash) == '1')
          counterArr[i][hash]++;
        else
          counterArr[i][hash]--;
      }

    }
  }

  public char getFirstBit(int hash) {
    String binary = Integer.toBinaryString(hash);
    return binary.charAt(0);
  }

  public void query() {
    for (int i = 0; i < flowIds.length; i++) {
      String s = flowIds[i];
      if (s == null)
        continue;
      String flowId = s.split("\\s+")[0];
      query(flowId);
    }
  }

  public void query(String flowId) {
    int estimate = 0;
    List<Integer> estimates = new ArrayList();
    for (int i = 0; i < k; i++) {
      int hash = Math.abs(s[i] ^ flowId.hashCode()) % w;
      if (getFirstBit(hash) == '1') {
        estimate = counterArr[i][hash];
      } else {
        estimate = -counterArr[i][hash];
      }
      estimates.add(estimate);
    }
    Collections.sort(estimates);
    int n = estimates.size();
    estimate = 0;
    if (n % 2 == 1) {
      estimate = estimates.get(n / 2);
    } else {
      estimate = (estimates.get(n / 2) + estimates.get((n / 2) - 1)) / 2;
    }
    error.put(flowId, estimate - actualSize.get(flowId));
  }

  public double getAverage() {
    double avg = 0.0;
    double total = 0.0;
    for (String key : error.keySet()) {
      total += error.get(key);
    }
    avg = total / error.size();
    return avg;
  }
}

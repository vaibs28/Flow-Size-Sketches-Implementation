import java.util.*;

public class CounterSketch {
  Random rand = new Random();
  String[] flowIds;
  int[][] counterArr;
  int[][] hashes;
  int[] s;
  Map<String, Integer> hashcodes;
  Map<String, Integer> estimatedSize;
  Map<String, Integer> actualSize;
  Map<String, Integer> error;
  Set<Integer> seenHashcodes;
  int k;
  int w;
  int n;
  int neg;
  int pos;

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
    hashcodes = new HashMap();
    rand = new Random();
    this.flowIds = flowIds;
    generateHash();
  }

  public void generateHash() {
    for (int i = 0; i < k; i++) {
      s[i] = Math.abs(rand.nextInt(65536));
    }
    for (int i = 0; i < flowIds.length; i++) {
      String s = flowIds[i];
      if (s == null)
        continue;
      String[] arr = s.split("\\s+");
      String flowId = arr[0];
      hashcodes.put(flowId, getRandomNumberUsingNextInt(-65536, 65535));
    }
  }

  public int getRandomNumberUsingNextInt(int min, int max) {
    return rand.nextInt(max - min) + min;
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
    //print();
  }

  public void print() {
    System.out.println("neg=" + neg);
    System.out.println("pos=" + pos);
  }

  public void record(String flowId) {
    // get a hash for each of the k arrays by xoring with the string hashcode and record 1
    int value = actualSize.get(flowId);
    for (int i = 0; i < k; i++) {
      int hash = (hashcodes.get(flowId) ^ s[i]) % w;
      // int hash = (flowId.hashCode() ^ s[i]) % w;
      char firstBit = getFirstBit(hash);
      if(firstBit=='0') neg++;
      else pos++;
      hash = Math.abs(hash);
      for (int j = 0; j < value; j++) {
        if (firstBit == '0')
          counterArr[i][hash]--;
        else
          counterArr[i][hash]++;
      }

    }
  }

  public char getFirstBit(int hash) {
    String binary = Integer.toBinaryString(hash);
    //System.out.println(binary);
    if (binary.length() == 32)
      return binary.charAt(0);
    return '0';
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
      int hash = (hashcodes.get(flowId) ^ s[i]) % w;
      char firstBit = getFirstBit(hash);
      hash = Math.abs(hash);

      if (firstBit == '0') {
        estimate = -counterArr[i][hash];
      } else {
        estimate = counterArr[i][hash];
      }
      estimates.add(estimate);
    }
    Collections.sort(estimates);
    // System.out.println(estimates);
    int n = estimates.size();
    estimate = 0;
    if (n % 2 == 1) {
      estimate = estimates.get(n / 2);
    } else {
      estimate = (estimates.get(n / 2) + estimates.get((n / 2) - 1)) / 2;
    }
    estimatedSize.put(flowId, estimate);
    error.put(flowId, estimate - actualSize.get(flowId));
  }

  public double getAverage() {
    double avg = 0.0;
    double total = 0.0;
    for (String key : error.keySet()) {
      total += Math.abs(error.get(key));
    }
    avg = total / error.size();
    return avg;
  }

  public void sort() {
    List<Map.Entry<String, Integer>> list = new ArrayList<>(estimatedSize.entrySet());
    Collections.sort(list, ((Map.Entry<String, Integer> e1,
        Map.Entry<String, Integer> e2) -> e2.getValue() - e1.getValue()));
    for (int i = 0; i < 100; i++) {
      Map.Entry<String, Integer> e = list.get(i);
      String flowId = e.getKey();
      int estimated = e.getValue();
      int actual = actualSize.get(flowId);
      System.out.println(flowId + "\t" + estimated + "\t" + actual);
    }
    System.out.println();
  }
}

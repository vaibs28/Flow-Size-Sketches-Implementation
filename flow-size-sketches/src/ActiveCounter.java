import java.util.Arrays;

public class ActiveCounter {
  int[] counter;
  int n;
  int e;
  int current;
  int overflowCount;
  int count;
  final int max = 65536;
  int exponent;

  public ActiveCounter(int n, int e) {
    this.n = n;
    this.e = e;
    counter = new int[n + e];
  }

  public void add() {
    for (int i = 1; i <= 1000000; i++) {
      // update the counter array
      String ce = "";
      for (int j = n; j < n + e; j++)
        ce += counter[j];
      int numCe = Integer.parseInt(ce, 2);
      numCe = (int) Math.pow(2, numCe);
      for (int j = 0; j < numCe - 1; j++) {
        i++;
      }
      addOne(i);
    }
    String exp = "";
    for (int i = n; i < n+e; i++) {
      exp += counter[i];
    }
    exponent = Integer.parseInt(exp,2);
  }

  public void addOne(int temp) {

    StringBuilder sb = new StringBuilder();


    if (Integer.toBinaryString(current + 1).length() <= n) {
      // update counter array
      // System.out.println(current);

      if (overflowCount > 0)
        overflowCount++;

      String num = "";
      for (int i = 0; i < n; i++)
        num += counter[i];

      int sum = Integer.parseInt(num, 2) + 1;
      String sumStr = Integer.toBinaryString(sum);
      current = sum;
      int k = sumStr.length() - 1;
      for (int j = n - 1; j >= 0; j--)
        if (k >= 0)
          counter[j] = Character.getNumericValue(sumStr.charAt(k--));

    } else {
      // overflow
      // System.out.println(current);
      overflowCount++;
      current = (current + 1) / 2;
      String binary = Integer.toBinaryString(current);
      int k = binary.length() - 1;
      for (int i = n - 1; i >= 0; i--) {
        if (k >= 0)
          counter[i] = binary.charAt(k--) - '0';
        sb.append(counter[i]);
      }
      // exponent
      int carry = 0, x = 0;
      for (int i = n + e - 1; i >= n; i--) {
        x = counter[i];
        if (x == 1) {
          counter[i] = 0;
          carry = 1;
        } else if (x == 0) {
          counter[i] = 1;
          break;
        }
      }
    }
    /*
     * System.out.println(temp); System.out.println(Arrays.toString(counter)); System.out.println();
     */
    /*
     * String binaryStr = sb.reverse().toString(); if (!binaryStr.equals("")) current =
     * Integer.parseInt(binaryStr, 2);
     */
  }
}

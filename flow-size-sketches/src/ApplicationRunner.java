import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;

public class ApplicationRunner {

  public static void main(String[] args) throws Exception {
    // TODO Auto-generated method stub
    File file = new File(
        "/Users/vaibhav/git/Flow-Size-Sketches-Implementation/flow-size-sketches/src/project3input.txt");
    BufferedReader br = new BufferedReader(new FileReader(file));
    String line = null;
    int n = 0;
    String[] flowIds = null;
    int lineNo = 0;
    int i = 0;
    while ((line = br.readLine()) != null) {
      if (lineNo == 0) {
        n = Integer.parseInt(line);
        flowIds = new String[n + 1];
        lineNo++;
      } else {
        flowIds[i++] = line;
      }
    }
    br.close();

    System.out.println("******Count Min Implementation******");
    CountMin cm = new CountMin(n, 3, 3000, flowIds);
    cm.recordAll();
    cm.query();
    System.out.println("Average Error = " + cm.getAverage() + "\n");
    cm.sort();

    System.out.println("******Counter Sketch Implementation*******");
    CounterSketch cs = new CounterSketch(n, 3, 3000, flowIds);
    cs.recordAll();
    cs.query();
    System.out.println("Average Error = " + cs.getAverage() + "\n");
    cs.sort();

    System.out.println("******Active Counter Implementation*******");
    ActiveCounter ac = new ActiveCounter(16, 16);
    ac.add();
    System.out.println(Arrays.toString(ac.counter));
    System.out.println((int)(ac.current * Math.pow(2, ac.exponent)));
  }

}

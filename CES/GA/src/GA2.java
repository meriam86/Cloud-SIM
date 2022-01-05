import java.io.IOException;  
import java.util.Random;  
  // GA for knapsack problem
public class GA2 {  
  
    private int[] v = { 220,208,198,192,180,180,165,162,160,158,  
            155,130,125,122,120,118,115,110,105,101,  
            100,100,98,96,95,90,88,82,80,77,  
            75,73,72,70,69,66,65,63,60,58,  
            56,50,30,20,15,10,8,5,3,1};//   
    private int[] b = {80,82,85,70,72,70,66,50,55,25,  
            50,55,40,48,50,32,22,60,30,32,  
            40,38,35,32,25,28,30,22,50,30,  
            45,30,60,50,20,65,20,25,30,10,  
            20,25,15,10,10,10,4,4,2,1};//   
    private int pb = 1000;//   
  
    private int LL; //   
    private int scale;//   
    private int MAX_GEN; //   
  
    private int bestT;//   
    private int bestLength; //   
    private int[] bestTour; //   
  
    // ?????  
    private int[][] oldPopulation;  
    private int[][] newPopulation;// ?  
    private int[] fitness;// ?  
  
    private float[] Pi;//   
    private float Pc;//   
    private float Pm;//   
    private int t;//   
    private Random random;  
  
    // ?,??  
    public GA2(int s, int l, int g, float c, float m) {  
        scale = s;  
        LL = l;  
        MAX_GEN = g;  
        Pc = c;  
        Pm = m;  
    }  
  
    private void init() throws IOException {  
        bestLength = 0;  
        bestTour = new int[LL];  
        bestT = 0;  
        t = 0;  
  
        newPopulation = new int[scale][LL];  
        oldPopulation = new int[scale][LL];  
        fitness = new int[scale];  
        Pi = new float[scale];  
  
        random = new Random(System.currentTimeMillis());  
    }  
  
    //   
    void initGroup() {  
        int k, i;  
        for (k = 0; k < scale; k++)//   
        {  
            // 01  
            for (i = 0; i < LL; i++) {  
                oldPopulation[k][i] = random.nextInt(65535) % 2;  
            }  
        }  
    }  
  
    public int evaluate(int[] chromosome) {  
        // 010110  
        int vv = 0;  
        int bb = 0;  
        // ?,1,2...n  
        for (int i = 0; i < LL; i++) {  
            if (chromosome[i] == 1) {  
                vv += v[i];  
                bb += b[i];  
            }  
        }  
        if (bb > pb) {  
            //   
            return 0;  
        } else {  
            return vv;  
        }  
    }  
  
    // ?fitness[max]??Pi[max]  
    void countRate() {  
        int k;  
        double sumFitness = 0;//   
  
        int[] tempf = new int[scale];  
  
        for (k = 0; k < scale; k++) {  
            tempf[k] = fitness[k];  
            sumFitness += tempf[k];  
        }  
  
        Pi[0] = (float) (tempf[0] / sumFitness);  
        for (k = 1; k < scale; k++) {  
            Pi[k] = (float) (tempf[k] / sumFitness + Pi[k - 1]);  
        }  
    }  
  
    // ?  
    // Fitness[max]  
    public void selectBestGh() {  
        int k, i, maxid;  
        int maxevaluation;  
  
        maxid = 0;  
        maxevaluation = fitness[0];  
        for (k = 1; k < scale; k++) {  
            if (maxevaluation < fitness[k]) {  
                maxevaluation = fitness[k];  
                maxid = k;  
            }  
        }  
  
        if (bestLength < maxevaluation) {  
            bestLength = maxevaluation;  
            bestT = t;// ;  
            for (i = 0; i < LL; i++) {  
                bestTour[i] = oldPopulation[maxid][i];  
            }  
        }  
  
        // ?k?kk  
        copyGh(0, maxid);// k?0  
    }  
  
    // ?k?kk  
    public void copyGh(int k, int kk) {  
        int i;  
        for (i = 0; i < LL; i++) {  
            newPopulation[k][i] = oldPopulation[kk][i];  
        }  
    }  
  
    //   
    public void select() {  
        int k, i, selectId;  
        float ran1;  
        for (k = 1; k < scale; k++) {  
            ran1 = (float) (random.nextInt(65535) % 1000 / 1000.0);  
            // System.out.println(""+ran1);  
            //   
            for (i = 0; i < scale; i++) {  
                if (ran1 <= Pi[i]) {  
                    break;  
                }  
            }  
            selectId = i;  
            copyGh(k, selectId);  
        }  
    }  
  
    public void evolution() {  
        int k;  
        //   
        selectBestGh();  
        // scale-1  
        select();  
        float r;  
  
        //   
        for (k = 0; k < scale; k = k + 2) {  
            r = random.nextFloat();// /  
            // System.out.println("..." + r);  
            if (r < Pc) {  
                // System.out.println(k + "" + k + 1 + "...");  
                OXCross(k, k + 1);//   
            } else {  
                r = random.nextFloat();// /  
                // System.out.println("1..." + r);  
                //   
                if (r < Pm) {  
                    // System.out.println(k + "...");  
                    OnCVariation(k);  
                }  
                r = random.nextFloat();// /  
                // System.out.println("2..." + r);  
                //   
                if (r < Pm) {  
                    // System.out.println(k + 1 + "...");  
                    OnCVariation(k + 1);  
                }  
            }  
  
        }  
  
    }  
      
  
    //   
    void OXCross(int k1, int k2) {  
        int i, j, flag;  
        int ran1, ran2, temp = 0;  
  
        ran1 = random.nextInt(65535) % LL;  
        ran2 = random.nextInt(65535) % LL;  
  
        while (ran1 == ran2) {  
            ran2 = random.nextInt(65535) % LL;  
        }  
        if (ran1 > ran2)// ran1<ran2  
        {  
            temp = ran1;  
            ran1 = ran2;  
            ran2 = temp;  
        }  
        flag = ran2 - ran1 + 1;//   
        for (i = 0, j = ran1; i < flag; i++, j++) {  
            temp = newPopulation[k1][j];  
            newPopulation[k1][j] = newPopulation[k2][j];  
            newPopulation[k2][j] = temp;  
        }  
  
    }  
  
    //   
    public void OnCVariation(int k) {  
        int ran1, ran2, temp;  
        int count;//   
        count = random.nextInt(65535) % LL;  
  
        for (int i = 0; i < count; i++) {  
  
            ran1 = random.nextInt(65535) % LL;  
            ran2 = random.nextInt(65535) % LL;  
            while (ran1 == ran2) {  
                ran2 = random.nextInt(65535) % LL;  
            }  
            temp = newPopulation[k][ran1];  
            newPopulation[k][ran1] = newPopulation[k][ran2];  
            newPopulation[k][ran2] = temp;  
        }  
    }  
  
    public void solve() {  
        int i;  
        int k;  
  //initialisation de la première génération
        //   
        initGroup();  
        // ?Fitness[max]  
        for (k = 0; k < scale; k++) {  
            fitness[k] = evaluate(oldPopulation[k]);  
            // System.out.println(fitness[k]);  
        }  
  
        // ?Pi[max]  
        countRate();  
        System.out.println("...");  
        for (k = 0; k < scale; k++) {  
            for (i = 0; i < LL; i++) {  
                System.out.print(oldPopulation[k][i] + ",");  
            }  
            System.out.println();  
            System.out.println("----" + fitness[k] + " " + Pi[k]);  
        }  
  
      //initialisation de la première génération
        //evolution();  
  
        for (t = 0; t < MAX_GEN; t++) {  
            evolution();  
            // newGroupoldGroup?  
            for (k = 0; k < scale; k++) {  
                for (i = 0; i < LL; i++) {  
                    oldPopulation[k][i] = newPopulation[k][i];  
                }  
            }  
            //   
            for (k = 0; k < scale; k++) {  
                fitness[k] = evaluate(oldPopulation[k]);  
            }  
            //   
            countRate();  
        }  
  
        System.out.println("...");  
        for (k = 0; k < scale; k++) {  
            for (i = 0; i < LL; i++) {  
                System.out.print(oldPopulation[k][i] + ",");  
            }  
            System.out.println();  
            System.out.println("---" + fitness[k] + " " + Pi[k]);  
        }  
  
        System.out.println("?");  
        System.out.println(bestT);  
        System.out.println("");  
        System.out.println(bestLength);  
        System.out.println("?");  
        for (i = 0; i < LL; i++) {  
            System.out.print(bestTour[i] + ",");  
        }  
  
    }
    /** 
     * @param args 
     * @throws IOException 
     */  
    public static void main(String[] args) throws IOException {  
        System.out.println("Start....");  
        GA2 ga = new GA2(20, 50, 500, 0.8f, 0.9f);  
        ga.init();  
        ga.solve();  
    }  

    }
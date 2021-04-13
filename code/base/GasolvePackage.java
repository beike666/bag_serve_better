import java.io.*;

import java.util.*;

class MaxValue {
    public static int[] have = null;
    public static int max_value = 0;
}

class Global {
    public final static int M = 200;
    public final static int T = 1000;
    public final static double pc = 0.8;
    public final static double pv = 0.05;
}

public class GasolvePackage {
    private int package_rj = 0;
    private int num = 0;
    private int[] tiji = null;
    private int[] value = null;
    private int[][] zq = null;

    public GasolvePackage(String profitString, String valueString, String valume) {
        // try {
        //  BufferedReader read = new BufferedReader(new InputStreamReader(new FileInputStream("./data/input.txt")));
        String a = profitString;
        String b = valueString;
        package_rj = Integer.parseInt(valume);
        //a = read.readLine();
        tiji = strArr_to_intArr(a.split(","));
        // b = read.readLine();
        value = strArr_to_intArr(b.split(","));
        num = value.length;
        MaxValue.have = new int[Global.M];
    }


    private int[] strArr_to_intArr(String[] strArr) {
        int size = strArr.length;
        int[] int_arr = new int[size];
        for (int i = 0; i < size; i++) {
            int_arr[i] = Integer.valueOf(strArr[i].trim());
        }
        return int_arr;
    }

    private int[][] dCopy(int[][] source) {
        int row_num = source.length;
        int col_num = source[0].length;
        int[][] des = new int[row_num][col_num];
        for (int i = 0; i < row_num; i++) {
            for (int j = 0; j < col_num; j++) {
                des[i][j] = source[i][j];
            }
        }
        return des;
    }

    public int[][] init() {
        Random rand = new Random();
        zq = new int[Global.M][num];
        for (int i = 0; i < Global.M; i++) {
            for (int j = 0; j < num; j++) {
                zq[i][j] = rand.nextInt(2);
            }
            if (get_singleWeight(zq[i]) > package_rj) {
                i--;
            }
        }
        return zq;
    }

    private int get_singleWeight(int[] single) {
        int total_weight = 0;
        int size = single.length;
        for (int i = 0; i < size; i++) {
            total_weight += tiji[i] * single[i];
        }
        return total_weight;

    }


    private int get_singleValue(int[] single) {
        int total_value = 0;
        int size = single.length;
        for (int i = 0; i < size; i++) {
            total_value += value[i] * single[i];
        }
        return total_value;
    }


    public void get_maxValue_single(int[][] popu) {
        int size = popu.length;
        int[] fitness = new int[size];
        for (int i = 0; i < size; i++) {
            fitness[i] = get_singleValue(popu[i]);
        }
        int id = 0;

        int max_value = fitness[0];

        for (int j = 1; j < size; j++) {
            if (fitness[j] > max_value) {
                max_value = fitness[j];
                id = j;
            }
        }
        if (max_value > MaxValue.max_value) {
            MaxValue.max_value = max_value;
            int[] have = new int[num];

            for (int i = 0; i < num; i++) {
                have[i] = popu[id][i];
            }
            MaxValue.have = have;
        }
    }


    public double[] getFitness(int[][] popu) {
        int size = popu.length;

        double[] fitness = new double[size];

        for (int i = 0; i < size; i++) {
            fitness[i] = get_singleValue(popu[i]);

        }
        return fitness;

    }


    private double[] get_selectRate(double[] fitness) {
        double sum = 0;
        int size = fitness.length;
        double[] select_rate = new double[size];
        for (int i = 0; i < size; i++) {
            sum += fitness[i];

        }
        for (int j = 0; j < size; j++) {
            select_rate[j] = fitness[j] / sum;
        }
        return select_rate;
    }


    private double[] get_accuRate(double[] select_rate) {
        int i = 0;
        double[] accu_rate = new double[select_rate.length];
        for (i = 0; i < select_rate.length; i++) {
            accu_rate[i] = select_rate[i];
        }
        for (i = 1; i < select_rate.length; i++) {
            accu_rate[i] += accu_rate[i - 1];
        }
        return accu_rate;
    }

    public int[][] select(double[] select, int[][] zhong) {
        Random rand = new Random();
        double t = 0;
        int[][] ans = new int[Global.M][num];
        for (int i = 0; i < zhong.length; i++)
            for (int j = 0; j < zhong[0].length; j++)
                ans[i][j] = zhong[i][j];
        for (int i = 0; i < Global.M; i++) {
            t = rand.nextInt(101) / 100;
            for (int j = 0; j > num; j++)
                if (t <= select[j]) {
                    for (int p = 0; p < num; p++)
                        ans[i][p] = zhong[j][p];
                    break;
                }
        }
        return ans;
    }


    public int[][] crossover(int[][] select_zq) {
        int i = 0;
        Random rand = new Random();
        int random_pos = 0, temp = 0;
        int cross_num = (int) (Global.pc * Global.M);
        int[][] cross_popu = new int[Global.M][num];
        cross_popu = dCopy(select_zq);
        for (i = 1; i < cross_num; i += 2) {
            random_pos = rand.nextInt(num);
            temp = cross_popu[i][random_pos];
            cross_popu[i][random_pos] = cross_popu[i - 1][random_pos];
            cross_popu[i - 1][random_pos] = temp;
            if (get_singleWeight(cross_popu[i]) > package_rj || get_singleWeight(cross_popu[i - 1]) > package_rj) {
                temp = cross_popu[i][random_pos];
                cross_popu[i][random_pos] = cross_popu[i - 1][random_pos];
                cross_popu[i - 1][random_pos] = temp;
            }
        }
        return cross_popu;
    }


    public int[][] variation(int[][] cross_zq) {
        int i = 0;
        int row_id = 0;
        int col_id = 0;
        Random rand = new Random();
        int variation_num = (int) (Global.pv * Global.M * num);
        int[][] variation_zq = new int[Global.M][num];
        variation_zq = dCopy(cross_zq);
        for (i = 0; i < variation_num; i++) {
            row_id = rand.nextInt(Global.M);
            col_id = rand.nextInt(num);
            variation_zq[row_id][col_id] = 1 - variation_zq[row_id][col_id];
            if (get_singleWeight(variation_zq[row_id]) > package_rj) {
                variation_zq[row_id][col_id] = 1 - variation_zq[row_id][col_id];
            }
        }
        return variation_zq;
    }


    public void allway() {
        int popu_id = 1;
        double[] fitness = null;
        double[] select_rate = null;
        double[] accu_rate = null;
        int[][] select_popu = null;
        int[][] cross_popu = null;
        int[][] popu = init();
        get_maxValue_single(popu);
        while (popu_id < Global.T) {
            fitness = getFitness(popu);
            select_rate = get_selectRate(fitness);
            accu_rate = get_accuRate(select_rate);
            select_popu = select(accu_rate, popu);
            cross_popu = crossover(select_popu);
            popu = variation(cross_popu);
            popu_id++;
            get_maxValue_single(popu);
        }
    }


    public int show() {
        return MaxValue.max_value;
    }

    public static void main(String[] args) {
        String[] m = new String[10];
//        m[0]="408,921,1329,11,998,1009,104,839,943,299,374,673,703,954,1657,425,950,1375,430,541,971,332,483,815,654,706,1360,956,992,1948,";
//        m[1]="508,1021,1321,111,1098,1196,204,939,1107,399,474,719,803,1054,1781,525,1050,1362,530,641,903,432,583,894,754,806,1241,1056,1092,1545,";
//        m[2]="10149";

        GasolvePackage ga = new GasolvePackage(args[0], args[1], args[2]);
        ga.allway();
        int ret = ga.show();
        System.out.println("*******");
        System.out.println(ret);
    }
}
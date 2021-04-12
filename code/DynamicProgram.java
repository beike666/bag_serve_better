import java.util.ArrayList;
import java.util.List;


public class DynamicProgram {

    public static void main(String[] args) {
        ArrayList<Integer> profitList = new ArrayList<>();
        ArrayList<Integer> weightList = new ArrayList<>();
        String[] split = args[0].split(",");
        for (String s : split) {
            profitList.add(Integer.parseInt(s));
        }
        String[] split1 = args[1].split(",");
        for (String s : split1) {
            weightList.add(Integer.parseInt(s));
        }
        Integer volume=Integer.parseInt(args[2]);
        int ret=dp(profitList,weightList,volume);
        System.out.println(ret);

    }


    public static int dp(ArrayList<Integer> profitList, ArrayList<Integer> weightList, Integer currentVolume) {

        int N=profitList.size()/3;

        int S=3;
        int[][] p = new int[N+1][S];
        int[][] w = new int[N+1][S];

        int index=0;
        for (int i = 1; i <= N; i++) {
            List<Integer> pi = profitList.subList(index, index + 3);
            int[] pa = pi.stream().mapToInt(Integer::intValue).toArray();
            p[i]=pa;
            List<Integer> wi = weightList.subList(index, index + 3);
            int[] wa = wi.stream().mapToInt(Integer::intValue).toArray();
            w[i]=wa;
            index=index+3;
        }

        int[] dp = new int[currentVolume+1];
        for (int i = 1; i <= N; i++) {
            for (int j = currentVolume; j >= 0; j--) {
                for (int k = 0; k < 3; k++) {
                    if(j>=w[i][k]) {
                        dp[j] = Math.max(dp[j], dp[j - w[i][k]] + p[i][k]);
                    }
                }
            }
        }
        return dp[currentVolume];

    }
}

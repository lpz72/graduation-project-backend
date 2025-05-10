package org.lpz.graduationprojectbackend.utils;

import java.util.List;
import java.util.Objects;

/**
 * 算法工具类
 */
public class AlgorithmUtils {

    /**
     * 最短编辑距离(标签)
     * @param tagList1
     * @param tagList2
     * @return
     */
    public static int minDistance(List<String> tagList1, List<String> tagList2){
        int n = tagList1.size();
        int m = tagList2.size();

        if(n * m == 0)
            return n + m;

        int[][] d = new int[n + 1][m + 1];
        for (int i = 0; i < n + 1; i++){
            d[i][0] = i;
        }

        for (int j = 0; j < m + 1; j++){
            d[0][j] = j;
        }

        //将1的前i个元素变为2的前j的元素所需要的最少操作次数
        for (int i = 1; i < n + 1; i++){
            for (int j = 1; j < m + 1; j++){
                int a = d[i - 1][j] + 1; //1删除,1删除1[i]
                int b = d[i][j - 1] + 1; //1插入,1插入一个2[j]
                int c = d[i - 1][j - 1]; //替换
                if (!Objects.equals(tagList1.get(i - 1), tagList2.get(j - 1)))
                    c += 1;
                d[i][j] = Math.min(a, Math.min(b, c));
            }
        }
        return d[n][m];
    }

    /**
     * 最短编辑距离
     * @param word1
     * @param word2
     * @return
     */
    public static int minDistance(String word1, String word2){
        int n = word1.length();
        int m = word2.length();

        if(n * m == 0)
            return n + m;

        int[][] d = new int[n + 1][m + 1];
        for (int i = 0; i < n + 1; i++){
            d[i][0] = i;
        }

        for (int j = 0; j < m + 1; j++){
            d[0][j] = j;
        }

        for (int i = 1; i < n + 1; i++){
            for (int j = 1; j < m + 1; j++){
                int left = d[i - 1][j] + 1;
                int down = d[i][j - 1] + 1;
                int left_down = d[i - 1][j - 1];
                if (word1.charAt(i - 1) != word2.charAt(j - 1))
                    left_down += 1;
                d[i][j] = Math.min(left, Math.min(down, left_down));
            }
        }
        return d[n][m];
    }
}

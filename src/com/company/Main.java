package com.company;

import java.util.ArrayList;
import java.util.List;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.*;

public class Main {

    public static List<Long> cache = new ArrayList<Long>();

    public static void main(String[] args) {
	// write your code here
        int N=40, Iters=100;
        System.out.printf("%-15s %d\n", "Recursive", fibRecur(N));
        System.out.printf("%-15s %d\n", "Cache", fibCache(N));
        System.out.printf("%-15s %d\n", "Loop", fibLoop(N));
        System.out.printf("%-15s %d\n", "Matrix", fibMatrix(N));

        long beforeTime, afterTime;
        long[] oldRTime = new long[Iters], oldCTime= new long[Iters], oldLTime= new long[Iters], oldMTime= new long[Iters];
        long rTime, cTime, lTime, mTime;
        String rPrefix, cPrefix, lPrefix, mPrefix;
        String n="N", doub = "Doubling", edoub = "Expected Doubling", time="Time", xchar="X";
        double rDouble, cDouble, lDouble, mDouble;
        double reDouble, ceDouble, leDouble, meDouble;

        System.out.printf("\n\n%25s %50s %53s %50s", "Recursive", "Cache", "Loop", "Matrix\n");
        System.out.printf("%-5s %5s %10s %15s %20s %15s %15s %20s %15s %15s %20s %15s %15s %15s\n", n, xchar, time, doub, edoub,time, doub, edoub,time, doub, edoub,time, doub, edoub);

        for(int x=2; x<Iters; x++) {
            //============================
            //Time the algorithms
            //============================

            //Recursive


            beforeTime = getCpuTime();
            if(x<45)
                fibRecur(x);
            afterTime = getCpuTime();
            rTime = afterTime - beforeTime;

            //Cache
            beforeTime = getCpuTime();
            fibCache(x);
            afterTime = getCpuTime();
            cTime = afterTime - beforeTime;

            //Loop
            beforeTime = getCpuTime();
            fibLoop(x);
            afterTime = getCpuTime();
            lTime = afterTime - beforeTime;

            //Matrix
            beforeTime = getCpuTime();
            fibMatrix(x);
            afterTime = getCpuTime();
            mTime = afterTime - beforeTime;


            //============================
            //Calculate Doubling
            //============================



                rDouble = (double) rTime / oldRTime[x/2];
                cDouble = (double) cTime / oldCTime[x/2];
                lDouble = (double) lTime / oldLTime[x/2];
                mDouble = (double) mTime / oldMTime[x/2];

                oldRTime[x] = rTime;
                oldCTime[x] = cTime;
                oldLTime[x] = lTime;
                oldMTime[x] = mTime;

                if(x>2 && x%2==0) {


                    //============================
                    //Print out results
                    //============================
                    rPrefix = getSecondType(rTime);
                    if(x>=45){
                        rTime = 0;
                        rPrefix = "N/A";
                    }

                    reDouble = x*x;
                    ceDouble = 2;
                    leDouble = 2;
                    meDouble = Math.log(x)/Math.log((double)x/2);

                    cPrefix = getSecondType(cTime);
                    lPrefix = getSecondType(lTime);
                    mPrefix = getSecondType(mTime);
                    rTime = convertNanoSeconds(rTime);
                    cTime = convertNanoSeconds(cTime);
                    lTime = convertNanoSeconds(lTime);
                    mTime = convertNanoSeconds(mTime);
                    double Nval = Math.ceil(Math.log(x+1)/Math.log(2));
                    System.out.printf("%-5.1f %5d %7d %-3s %15.3f %20.1f %12d %-3s %15.3f %20f %12d %-3s %15.3f %20f %12d %-3s %10.3f %15.3f\n", Nval, x, rTime, rPrefix, rDouble, reDouble, cTime, cPrefix, cDouble, ceDouble, lTime, lPrefix, lDouble, leDouble, mTime, mPrefix, mDouble, meDouble);


                }
            }


    }


    public static long fibRecur(int val){
        if(val==0 || val==1)
            return val;
        else
            return fibRecur(val-1) + fibRecur(val-2);
    }

    public static long fibCache(int N) {
        for(int i=0; i<=N; i++) {
            if (cache.size() > i) {
                cache.set(i, (long) 0);
            }
            else
                cache.add((long) 0);
        }

        return fibCacheHelper(N);
    }

    public static long fibCacheHelper(int val) {
        if(val==0||val==1)
            return val;
        else if(cache.get(val) != 0)
            return cache.get(val);
        else
        {
            cache.set(val, (fibCacheHelper(val-1)+fibCacheHelper(val-2)));
            return cache.get(val);
        }

    }

    public static long fibLoop(int N){
        long next = 0, A=0, B=1;
        if(N < 2)
            return N;
        for(int i=1; i<N; i++) {
            next = A + B;
            A = B;
            B = next;
        }
        return next;
    }

    public static long fibMatrix(int N){
        long val, power;
        final long[] V={1,1,1,0};   //This is the matrix that gets multiplied N-2 times so we must make sure it is only read only
        long[] M = {1,1,1,0}, A= {1,1,1,0};
        long a=V[0],b=V[1],c=V[2],d=V[3];
        for(int i=2; i<=N; i++) {
            //Matrix Multiply
            //A is temporary array
            A[0] = a;
            A[1] = b;
            A[2] = c;
            A[3] = d;

            a = (A[0] * V[0]) + (A[1] * V[2]);
            b = (A[0] * V[1]) + (A[1] * V[3]);
            c = (A[2] * V[0]) + (A[3] * V[2]);
            d = (A[2] * V[1]) + (A[3] * V[3]);
        }
        val = A[0];
        return val;
    }

    //=========================================
    //Utilies
    //=========================================

    public static String getSecondType (long time) {
        if((time/1000000000) > 60)
            return "m ";
        if(time >= 1000000000)
            return "s ";
        else if(time >= 1000000)
            return "µs";
        else if(time >= 1000)
            return "ms";
        else
            return "ns";
    }

    public static long convertNanoSeconds (long time) {
        long convertedTime=0;

        if(time >= 1000000000) {
            convertedTime = time / 1000000000;
            if(convertedTime>=60)   //Check if its a minute or more
                convertedTime = convertedTime/60;
        }
        else if(time >= 1000000)
            convertedTime = time / 1000000;
        else if(time >= 1000)
            convertedTime = time / 1000;
        else
            convertedTime = time;

        return convertedTime;
    }

    /* Get CPU time in nanoseconds since the program(thread) started. */
    /** from: http://nadeausoftware.com/articles/2008/03/java_tip_how_get_cpu_and_user_time_benchmarking#TimingasinglethreadedtaskusingCPUsystemandusertime **/
    public static long getCpuTime( ) {

        ThreadMXBean bean = ManagementFactory.getThreadMXBean( );
        return bean.isCurrentThreadCpuTimeSupported( ) ?
                bean.getCurrentThreadCpuTime( ) : 0L;

    }

}

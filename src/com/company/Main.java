package com.company;

import java.util.Random;
import java.util.Scanner;

public class Main {
    public static boolean checkPrime(long num) {
        long temp;
        boolean isPrime=true;
        for(int i=2;i<=num/2;i++)
        {
            temp=num%i;
            if(temp==0)
            {
                isPrime=false;
                break;
            }
        }
        return isPrime;
    }

    private static long gcd(long a, long b) {
        long t;
        while(b != 0){
            t = a;
            a = b;
            b = t%b;
        }
        return a;
    }
    private static boolean relativelyPrime(long a, long b) {
        return gcd(a,b) == 1;
    }

    private static long invModulo(long a, long b) {
        long a0,n0,p0,p1,q,r,t;

        p0 = 0; p1 = 1; a0 = a; n0 = b;
        q  = n0 / a0;
        r  = n0 % a0;
        while(r > 0)
        {
            t = p0 - q * p1;
            if(t >= 0)
                t = t % b;
            else
                t = b - ((-t) % b);
            p0 = p1; p1 = t;
            n0 = a0; a0 = r;
            q  = n0 / a0;
            r  = n0 % a0;
        }
        return p1;
    }

    public static void main(String[] args) {
        long p, q;
        int randRange = 10000;
        Random r = new Random();
        do {
            p = r.nextInt(randRange)+randRange;
        } while(checkPrime(p)==false);
        do {
            q = r.nextInt(randRange)+randRange;
        } while(checkPrime(q)==false || q==p);
        System.out.println("random prime number - p: " + p);
        System.out.println("random prime number - q: " + q);

        long n=p*q;
        System.out.println("n: " + n);

        long m = (p-1)*(q-1);
        System.out.println("m: " + m);

        long e;
        do {
            e = r.nextInt(randRange)+randRange;
        } while(e>=n || !relativelyPrime(e,m));

        System.out.println("e: " + e);
        System.out.println("Public key e: " + e + " i n: " + n);

        long d = invModulo(e, m);

        System.out.println("d: " + d);
        System.out.println("d is relatively prime to n: " + relativelyPrime(d,n));

        System.out.println("result of (d*e)%m = " + (d*e)%m);

        System.out.println("Private key d: " + d + " i n: " + n);

        //encrypt:      c=m^e%n
        //decrypt:      m=c^d%n
        //sign:         s=m^d%n
        //verification: v=s^e%n  (if v=m -> correct)
    }
}

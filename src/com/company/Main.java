package com.company;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.*;
import java.util.Random;
import java.util.Scanner;

public class Main {
    private static boolean checkPrime(long num) {
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

    // Funkcja oblicza modulo potęgę podanej liczby
//---------------------------------------------
    private static long pot_mod(int a, long w, long n)
    {
        long pot,wyn,q;

// wykładnik w rozbieramy na sumę potęg 2
// przy pomocy algorytmu Hornera. Dla reszt
// niezerowych tworzymy iloczyn potęg a modulo n.

        pot = a; wyn = 1;
        for(q = w; q > 0; q /= 2)
        {
            if(q % 2 == 1) wyn = (wyn * pot) % n;
            pot = (pot * pot) % n; // kolejna potęga
        }
        return wyn;
    }

    private static String createHash (String plaintext) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.reset();
        md.update(plaintext.getBytes());
        byte[] digest = md.digest();
        BigInteger bigInt = new BigInteger(1, digest);
        String hashtext = bigInt.toString();

        return hashtext;
    }

    private static String RSA_sign (String hashtext, long d, long n) {
        String enc = "";
        for(int i=0; i<hashtext.length(); i++) {
            int digit = Integer.parseInt(hashtext.charAt(i)+"");
            long pow = pot_mod(digit,d,n);
            enc += pow + " ";
        }

        return enc;
    }

    private static String RSA_ver (String signature, long e, long n) {
        String[] parts = signature.split(" ");
        String result = "";
        for (String x: parts) {
            int val = Integer.parseInt(x);
            long pow = pot_mod(val,e,n);
            result += pow;
        }

        return result;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        long p, q;
        int randRange = 10;
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

        String plaintext = "Litwo! Ojczyzno moja! Ty jestes jak zdrowie. Ile cie trzeba cenic, ten tylko sie od Moskali, skakal kryc sie w zamku sien wielka, jeszcze przez okno, swiecaca nagla, cicha radosc byla ekonomowi poczciwemu swieta. Bo nie stalo na Tadeusza, rzucil kilku goscmi poszedl do kraju. Mowy starca krazyly we snie.";
        String hash = createHash(plaintext);
        System.out.println("digest of the text1: \n" + hash);

        String signature = RSA_sign(hash, d, n);
        System.out.println("signature: " + signature);

        String verification = RSA_ver(signature, e, n);
        System.out.println("verification: \n" + verification);


        String plaintextWOneChar = "Litwo Ojczyzno moja! Ty jestes jak zdrowie. Ile cie trzeba cenic, ten tylko sie od Moskali, skakal kryc sie w zamku sien wielka, jeszcze przez okno, swiecaca nagla, cicha radosc byla ekonomowi poczciwemu swieta. Bo nie stalo na Tadeusza, rzucil kilku goscmi poszedl do kraju. Mowy starca krazyly we snie.";
        String hash2 = createHash(plaintextWOneChar);
        System.out.println("digest of the text2: \n" + hash2);

        String verification2 = RSA_ver(signature, e, n);
        System.out.println("verification: \n" + verification2);

        if(verification2 != hash2) {
            System.out.println(verification2 + "!=" + hash2);
            System.out.println("dokument zmieniony!");
        }
    }
}

package rsa;

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.math.*;

public class RSA {

    private BigInteger Q, P, N, phi, e, d;
    private Random R;
    private int maxLength = 1024;

    public RSA() {
        R = new Random();
        P = BigInteger.probablePrime(maxLength, R);
        Q = BigInteger.probablePrime(maxLength, R);
        N = compute_N(P, Q);
        phi = compute_phi(P, Q);
        e = compute_e(phi);
        d = compute_d(e, phi);
    }

    public RSA(BigInteger e, BigInteger d, BigInteger N) {
        this.e = e;
        this.d = d;
        this.N = N;
    }

    public static BigInteger compute_N(BigInteger P, BigInteger Q) {
        BigInteger N = P.multiply(Q);
        return N;
    }

    public static BigInteger compute_phi(BigInteger P, BigInteger Q) {
        BigInteger phi = P.subtract(BigInteger.ONE).multiply(Q.subtract(BigInteger.ONE));
        return phi;
    }

    public static BigInteger compute_e(BigInteger phi) {
        BigInteger e = BigInteger.valueOf(2);
        while (e.compareTo(phi) < 0) {
            if (phi.gcd(e).equals(BigInteger.ONE)) {
                break;
            }
            e = e.add(BigInteger.ONE);
        }
        return e;
    }

    public static BigInteger compute_d(BigInteger e, BigInteger phi) {
        BigInteger d = BigInteger.ZERO;
        for (int i = 0; i < 10; i++) {
            BigInteger x = BigInteger.ONE.add(phi.multiply(BigInteger.valueOf(i)));
            if (x.mod(e).equals(BigInteger.ZERO)) {
                d = x.divide(e);
                break;
            }
        }
        return d;
    }

    private static String ByteToString(byte[] cipher) {
        String temp = "";
        for (byte b : cipher) {
            temp += Byte.toString(b);
        }
        return temp;
    }

    public byte[] encryptMessage(byte[] message) {
        return (new BigInteger(message)).modPow(e, N).toByteArray();
    }

    public byte[] decryptMessage(byte[] message) {
        return (new BigInteger(message)).modPow(d, N).toByteArray();
    }

    public static void main(String[] args) throws IOException {

        RSA rsaAlgo = new RSA();

        System.out.print("Please enter a plaintext:");
        Scanner in = new Scanner(System.in);
        String inputString = in.nextLine();
        System.out.println(" \n Message that you want to encrypting: " + inputString);
        System.out.println(" Message in bytes: " + ByteToString(inputString.getBytes()));

        System.out.println(" The value of n: " + rsaAlgo.N);
        System.out.println(" The value of phi: " + rsaAlgo.phi);
        System.out.println(" The value of e: " + rsaAlgo.e);
        System.out.println(" The value of d: " + rsaAlgo.d);

        byte[] cipher = rsaAlgo.encryptMessage(inputString.getBytes());

        byte[] plain = rsaAlgo.decryptMessage(cipher);
        System.out.println(" Decrypting Message in Bytes: " + ByteToString(plain));
        System.out.println(" The original message is: " + new String(plain));
    }
}

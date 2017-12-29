package com.powerboat9.EVOL;

import java.security.InvalidParameterException;

public class EVOLMain {
    public static void main(String[] args) {
        byte[] b = randomBytes(1024);
        Program p = new Program(b, 0, b.length);
        try {
            p.runTicks(1024);
        } catch (InterruptedException e) {}
        p.printOutput();
        p.dataDumpData();
        p.printInstructions();
    }

    public static byte randomByte() {
        return (byte) ((Math.random() * 256) - 127);
    }

    public static byte[] randomBytes(int length) {
        if (length < 1) throw new InvalidParameterException("Length can not be less than 1");
        byte[] b = new byte[length];
        for (int i = 0; i < length; ++i) {
            b[i] = randomByte();
        }
        return b;
    }

    public static byte randomize(byte in, float chance) {
        return (Math.random() < chance) ? randomByte() : in;
    }

    public static byte randomize(byte in) {
        return randomize(in, 1/32);
    }

    public static Instruction getInstruction(byte b) {
        return Instruction.values()[(26 + (b % 26)) % 26];
    }

    public static void randomizeBytes(byte[] in, float chance) {
        for (int i = 0; i < in.length; ++i) {
            in[i] = randomize(in[i], chance);
        }
    }
}
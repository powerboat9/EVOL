package com.powerboat9.EVOL;

import sun.misc.HexDumpEncoder;

import java.security.InvalidParameterException;
import java.util.ArrayList;

public class Program {
    private byte[] templet;
    private byte[] data;
    private int progStart;
    private int progLen;
    private int progPos;
    private ArrayList<Integer> out = new ArrayList<>();
    private ArrayList<Integer> stack = new ArrayList<>();

    public Program(byte[] templetIn, int progStartIn, int progLenIn) {
        if (progLenIn < 1) throw new InvalidParameterException("Program length can not be less than 1");
        if (progStartIn < 0) throw new InvalidParameterException("Program start can not be less than 0");
        if (progStartIn >= templetIn.length) throw new InvalidParameterException("Program start can not be too high (out of bounds)");
        if ((progStartIn + progLenIn - 1) >= templetIn.length) throw new InvalidParameterException("Program end can not be too high (out of bounds)");
        templet = templetIn;
        data = templet.clone();
        progStart = progStartIn;
        progLen = progLenIn;
    }

    public Program(int dataLen, int progStartIn, int progLenIn) {
        this(EVOLMain.randomBytes(dataLen), progStartIn, progLenIn);
    }

    private static void dataDump(byte[] b) {
        HexDumpEncoder dump = new HexDumpEncoder();
        System.out.println(dump.encode(b));
    }

    public void dataDumpOriginal() {
        dataDump(templet);
    }

    public void dataDumpData() {
        dataDump(data);
    }

    public void printOutput() {
        out.iterator().forEachRemaining(System.out::println);
    }

    public void printInstructions() {
        for (int i = 0; i < data.length; ++i) {
            System.out.println(EVOLMain.getInstruction(data[i]));
        }
    }

    public void runTick() throws InterruptedException {
        progPos = EVOLMain.getInstruction(data[progPos]).run(data, stack, progStart, progLen, progPos, out);
    }

    public void runTicks(int ticks) throws InterruptedException {
        for (int i = 0; i < ticks; ++i) runTick();
    }
}
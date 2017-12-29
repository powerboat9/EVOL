package com.powerboat9.EVOL;

import com.sun.istack.internal.Nullable;

import java.util.ArrayList;

public enum Instruction {
    ADD ((byte) 0),
    SUB ((byte) 1),
    MUL ((byte) 2),
    DIV ((byte) 3),
    PUSH ((byte) 4),
    PUSHC ((byte) 5), //(Push Current)
    REMOVE ((byte) 6),
    AND ((byte) 7),
    OR ((byte) 8),
    NOT ((byte) 9),
    BNOT ((byte) 10), //(Boolean Not)
    WRITE ((byte) 11),
    WRITES ((byte) 12),
    FILL ((byte) 13),
    FILLS ((byte) 14),
    JMP ((byte) 15),
    JMC ((byte) 16),
    REDO ((byte) 17),
    LESS ((byte) 18),
    LESS_EQUAL ((byte) 19),
    EQUAL ((byte) 20),
    MORE_EQUAL ((byte) 21),
    MORE ((byte) 22),
    NOT_EQUAL ((byte) 23),
    SHARE ((byte) 24),
    EXIT ((byte) 25);

    private byte n;
    Instruction(byte nIn) {
        n = (byte) (nIn % 26);
    }

    private int pullStack(ArrayList<Integer> stack) {
        return (stack.size() == 0) ? 0 : stack.remove(stack.size() - 1);
    }

    public int run(byte[] data, ArrayList<Integer> stack, int progStart, int progLen, int pos, @Nullable ArrayList<Integer> out) throws InterruptedException {
        int eax, ebx, ecx;
        int next = pos;
        switch (this) {
            case ADD:
                eax = pullStack(stack);
                eax += pullStack(stack);
                stack.add(eax);
                ++next;
                break;
            case SUB:
                eax = pullStack(stack);
                ebx = pullStack(stack);
                stack.add(ebx - eax);
                ++next;
                break;
            case MUL:
                eax = pullStack(stack);
                eax *= pullStack(stack);
                stack.add(eax);
                ++next;
                break;
            case DIV:
                eax = pullStack(stack);
                ebx = pullStack(stack);
                if (eax == 0) eax = 1;
                stack.add(ebx / eax);
                ++next;
                break;
            case PUSH:
                stack.add((int) data[(progStart + pos + 1) % progLen]);
                next += 2;
                break;
            case PUSHC:
                stack.add(stack.get(stack.size() - 1));
                break;
            case REMOVE:
                pullStack(stack);
                ++next;
                break;
            case AND:
                eax = pullStack(stack);
                eax &= pullStack(stack);
                stack.add(eax);
                ++next;
                break;
            case OR:
                eax = pullStack(stack);
                eax |= pullStack(stack);
                stack.add(eax);
                ++next;
                break;
            case NOT:
                stack.add(~pullStack(stack));
                ++next;
                break;
            case BNOT:
                if (pullStack(stack) == 0) {
                    stack.add(0);
                } else {
                    stack.add(1);
                }
                ++next;
                break;
            case WRITE:
                eax = pullStack(stack);
                data[progStart + (pullStack(stack) % progLen)] = EVOLMain.randomize((byte) eax);
                ++next;
                break;
            case WRITES:
                eax = pullStack(stack);
                data[pullStack(stack) % data.length] = EVOLMain.randomize((byte) eax);
                ++next;
                break;
            case FILL:
                eax = pullStack(stack);
                ebx = pullStack(stack);
                ecx = pullStack(stack);
                for (int i = 0; i < ebx; ++i) {
                    data[progStart + ((ecx + i) % progLen)] = EVOLMain.randomize((byte) eax);
                }
                ++next;
                break;
            case FILLS:
                eax = pullStack(stack);
                ebx = pullStack(stack);
                ecx = pullStack(stack);
                for (int i = 0; i < ebx; ++i) {
                    data[(ecx + i) % data.length] = EVOLMain.randomize((byte) eax);
                }
                ++next;
                break;
            case JMP:
                next = pullStack(stack);
                break;
            case JMC:
                eax = pullStack(stack);
                if (pullStack(stack) == 0) {
                    ++next;
                } else {
                    next = eax;
                }
                break;
            case REDO:
                next = progStart;
                stack.clear();
            case LESS:
                eax = pullStack(stack);
                ebx = pullStack(stack);
                if (ebx < eax) {
                    stack.add(1);
                } else {
                    stack.add(0);
                }
                ++next;
                break;
            case LESS_EQUAL:
                eax = pullStack(stack);
                ebx = pullStack(stack);
                if (ebx <= eax) {
                    stack.add(1);
                } else {
                    stack.add(0);
                }
                ++next;
                break;
            case EQUAL:
                eax = pullStack(stack);
                ebx = pullStack(stack);
                if (ebx == eax) {
                    stack.add(1);
                } else {
                    stack.add(0);
                }
                ++next;
                break;
            case MORE_EQUAL:
                eax = pullStack(stack);
                ebx = pullStack(stack);
                if (ebx >= eax) {
                    stack.add(1);
                } else {
                    stack.add(0);
                }
                ++next;
                break;
            case MORE:
                eax = pullStack(stack);
                ebx = pullStack(stack);
                if (ebx > eax) {
                    stack.add(1);
                } else {
                    stack.add(0);
                }
                ++next;
                break;
            case NOT_EQUAL:
                eax = pullStack(stack);
                ebx = pullStack(stack);
                if (ebx != eax) {
                    stack.add(1);
                } else {
                    stack.add(0);
                }
                ++next;
                break;
            case SHARE:
                if (out != null) out.add(pullStack(stack));
                ++next;
                break;
            case EXIT:
                throw new InterruptedException("DONE");
        }
        return progStart + ((progLen + ((next - progStart) % progLen)) % progLen);
    }
}
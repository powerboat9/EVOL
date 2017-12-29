struct stack {
    void *data;
    unsigned int length;
    unsigned int pos;
}

unsigned char 

class Instruction {
    private:
        unsigned char value;
    public:
        int run(struct stack data, unsigned int *pos, unsigned int progStart, unsigned int progLen, struct stack out) {
            unsigned char eax, ebx, ecx;
            switch (value) {
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

package org.n1vnhil.jvm;

import java.util.ArrayDeque;
import java.util.Deque;

public class JvmStack {

    private Deque<StackFrame> stack = new ArrayDeque<>();
    
    public StackFrame peek() {
        return stack.peek();
    }
    
    public StackFrame pop() {
        return stack.pop();
    }

    public void push(StackFrame frame) {
        stack.push(frame);
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }
}

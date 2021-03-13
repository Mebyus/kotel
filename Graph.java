import java.util.Arrays;

public class Graph {
    private static class VisitStackUnsafe {
        private int size;
        private int[] stack;
        private int[] count;
        private int[] index;

        VisitStackUnsafe(int cap) {
            this.size = 0;
            this.stack = new int[cap];
            this.count = new int[cap];
            this.index = new int[cap];
        }

        public boolean isEmpty() {
            return this.size == 0;
        }

        public void push(int x) {
            this.stack[this.size++] = x;
            this.index[x] = this.size;
        }

        public int peek() {
            return this.stack[this.size - 1];
        }

        public int pop() {
            int x = this.stack[--this.size];
            this.count[this.size] = 0;
            this.index[x] = 0;
            return x;
        }

        public int[] pop(int n) {
            if (n < 1) {
                return null;
            }
            int oldSize = this.size;
            this.size -= n;
            int[] ret = Arrays.copyOfRange(this.stack, this.size, oldSize);
            for (int x : ret) {
                this.index[x] = 0;
            }
            for (int i = this.size; i < oldSize; i++) {
                this.count[i] = 0;
            }
            return ret;
        }

        public int inc() {
            return this.count[this.size - 1]++;
        }

        public int contains(int n) {
            int i = this.index[n];
            if (i == 0) {
                return 0;
            }
            return this.size - i + 1;
        }
    }

    public static int[] rotateMinFirst(int[] a) {
        if (a == null) {
            return null;
        }
        int[] b = new int[a.length];
        if (a.length == 0) {
            return b;
        }
        int minIndex = 0;
        for (int i = 1; i < a.length; i++) {
            if (a[i] < a[minIndex]) {
                minIndex = i;
            }
        }
        for (int i = 0; i < a.length - minIndex; i++) {
            b[i] = a[minIndex + i];
        }
        for (int i = 0; i < minIndex; i++) {
            b[a.length - minIndex + i] = a[i];
        }
        return b;
    }

    public static int[] findCycle(int[][] g) {
        return rotateMinFirst(findCycleNoRotation(g));
    }

    private static int[] findCycleNoRotation(int[][] g) {
        if (g == null || g.length == 0) {
            return null;
        }

        Graph.VisitStackUnsafe stack = new Graph.VisitStackUnsafe(g.length);
        boolean[] visited = new boolean[g.length];

        stack.push(0);
        while (!stack.isEmpty()) {
            int current = stack.peek();
            visited[current] = true;

            int[] adjacent = g[current];
            if (adjacent == null || adjacent.length == 0) {
                stack.pop();
                continue;
            }

            int next = 0;
            boolean found = false;
            int adjacentIndex = stack.inc();
            while (adjacentIndex < adjacent.length) {
                next = adjacent[adjacentIndex];
                if (!visited[next]) {
                    found = true;
                    break;
                } else {
                    int n = stack.contains(next);
                    if (n != 0) {
                        return stack.pop(n);
                    }
                }
                adjacentIndex = stack.inc();
            }
            if (!found) {
                stack.pop();
                continue;
            }

            stack.push(next);
        }
        return null;

    }
}
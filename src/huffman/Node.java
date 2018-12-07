package huffman;

public class Node {

    // TODO try making with builder

    private int value;
    private long weight;
    private Node left;
    private Node right;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public long getWeight() {
        return weight;
    }

    public void setWeight(long weight) {
        this.weight = weight;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public boolean isLeaf() {
        boolean isLeaf = false;
        if (this.right == null && this.left == null)
            isLeaf = true;
        return isLeaf;
    }
}

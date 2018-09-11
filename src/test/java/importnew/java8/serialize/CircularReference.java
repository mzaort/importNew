package importnew.java8.serialize;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class CircularReference {
	private int n;
	private CircularReference next;
	
	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}

	public CircularReference getNext() {
		return next;
	}

	public void setNext(CircularReference next) {
		this.next = next;
	}

	@Override
	public String toString() {
		// stack overflow
		return "CircularReference [n=" + n + ", next=" + next + "]";
	}

	public static void main(String[] args) {
		CircularReference c = new CircularReference();
		c.setN(12);
		c.setNext(c);
		System.out.println(c.toString());
	}
}

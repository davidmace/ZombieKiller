/**
 * Simple node in our simplified linkedlist implementation
 */
public class LListNode {
	Object data;
	LListNode next;
	LListNode prev;
		
	public LListNode(Object data, LListNode next, LListNode prev) {
		this.data=data;
		this.next=next;
		this.prev=prev;
	}
}
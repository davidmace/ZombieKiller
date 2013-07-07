/**
 * Easier implementation of linkedlist that allows simpler access to nodes
 */
public class LList {
	private LListNode head,tail;
	private int size;
	
	public LList() {
		head=null;
		tail=null;
		size=0;
	}
	
	//add element
	public void add(Object o) {
		size++;
		if(head==null) {
			head=new LListNode(o,null,null);
			tail=head;
			return;
		}
		LListNode oldHead=head;
		head=new LListNode(o,oldHead,null);
		oldHead.prev=head;
	}
	
	//get size
	public int size() {
		return size;
	}
	
	//get first node
	public LListNode getFirstNode() {
		return head;
	}
	
	//get first node
	public LListNode getLastNode() {
		return tail;
	}
	
	//remove element
	public void remove(LListNode n) {
		if(tail==n && head==n) {
			head=null;
			tail=null;
		}
		else if(tail==n) {
			n.prev.next=null;
			tail=n.prev;
		}
		else if(head==n) {
			n.next.prev=null;
			head=n.next;
		}
		else {
			n.prev.next=n.next;
			n.next.prev=n.prev;
		}
		size--;
	}
}

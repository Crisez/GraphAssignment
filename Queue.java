
public interface Queue<T> {
	/**
	 * 
	 * @return removed element from the front of the queue
	 * @throws Exception 
	 */
	public T dequeue() throws Exception;
	/**
	 * 
	 * @param e  adding a new element to the queue ( at the end)
	 */
	public void enqueue(T e);
	
	/**
	 * 
	 * @return  the element in front without removing it
	 */
	public T peek();

	/**
	 * 
	 * @return number of elements in queue
	 */
	public int size();
	/**
	 * 
	 * @return true only if queue is empty
	 */
	public boolean isEmpty();
	

}

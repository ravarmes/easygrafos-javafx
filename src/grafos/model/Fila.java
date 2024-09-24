
package grafos.model;

/**
 *  classe do fila. Esta estrutura é utilizada pelo BFS
 *  @author Jonathas
 */
public class Fila<Item> {
   private Node first;  // mais recente
   private Node last;   // mais antigo
   private int N;       //quantidade de elementos

    public Node getFirst() {
        return first;
    }

    public void setFirst(Node first) {
        this.first = first;
    }

    public Node getLast() {
        return last;
    }

    public void setLast(Node last) {
        this.last = last;
    }

    public int getN() {
        return N;
    }

    public void setN(int N) {
        this.N = N;
    }
   
   

   public class Node {
      private Item item;
      private Node next;
   }

   public boolean isEmpty() {
      return first == null;
   }

   public int tamanho() {
      return N;
   }

   /**
    * 
    * @param item Elemento a ser inserido na fila
    */
   public void enfileira(Item item) {
      Node oldlast = last;
      last = new Node();
      last.item = item;
      last.next = null;
      if (isEmpty()) first = last;
      else oldlast.next = last;
      N++;
   }

   /**
    * 
    * @return retorna o elemento retirado da fila
    */
   public Item desenfileira() {
      Item item = first.item;
      first = first.next;
      N--;
      if (isEmpty()) last = null;
      return item;
   }
   
   /**
    * 
    * @return retorna o primeito item da fila
    */
   public Item firstItem() {
       
       if (isEmpty()) {
           return null;
       } else {
           Item item = first.item;
           return item;
       }
      
   }
   
   

   

}

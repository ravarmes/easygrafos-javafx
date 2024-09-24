
package grafos.model;

public class KruskalItemTablewViewPQ implements Comparable<KruskalItemTablewViewPQ>{
    
    private String aresta; 
    private Double peso;

    public KruskalItemTablewViewPQ(String aresta, Double peso) {
        this.aresta = aresta;
        this.peso = peso;
    }

    public String getAresta() {
        return aresta;
    }

    public void setAresta(String aresta) {
        this.aresta = aresta;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }
    
    @Override
    public int compareTo(KruskalItemTablewViewPQ aquela) {
        return Double.compare(this.peso, aquela.peso);
    }
      
}

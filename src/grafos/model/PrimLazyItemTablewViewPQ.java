
package grafos.model;
/**
 *
 * @author Danielle
 */
public class PrimLazyItemTablewViewPQ implements Comparable<PrimLazyItemTablewViewPQ>{
    
    private String aresta; 
    private Double peso;

    public PrimLazyItemTablewViewPQ(String aresta, Double peso) {
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
    public int compareTo(PrimLazyItemTablewViewPQ aquela) {
        return Double.compare(this.peso, aquela.peso);
    }
      
}

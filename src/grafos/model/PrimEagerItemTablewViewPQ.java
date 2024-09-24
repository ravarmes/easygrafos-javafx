
package grafos.model;


/**
 *
 * @author Danielle
 */
public class PrimEagerItemTablewViewPQ implements Comparable<PrimEagerItemTablewViewPQ>{
     
    private Integer i;
    private Double peso;
    private Integer outroVertice; // vai ser utilizado par ajudar a colorir as arestas

    public PrimEagerItemTablewViewPQ() {
    }
    
    public PrimEagerItemTablewViewPQ(Integer i, Double peso) {
        this.i = i;
        this.peso = peso;
    }

    public PrimEagerItemTablewViewPQ(Integer i, Double peso, Integer outroVertice) {
        this.i = i;
        this.peso = peso;
        this.outroVertice = outroVertice;
    }

    public Integer getI() {
        return i;
    }

    public void setI(Integer i) {
        this.i = i;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public Integer getOutroVertice() {
        return outroVertice;
    }

    public void setOutroVertice(Integer outroVertice) {
        this.outroVertice = outroVertice;
    }
    
    @Override
    public int compareTo(PrimEagerItemTablewViewPQ aquela) {
        return Double.compare(this.peso, aquela.peso);
    }

}

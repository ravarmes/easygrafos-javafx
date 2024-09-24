
package grafos.model;
/**
 *
 * @author Danielle
 */
public class PrimEagerItemTablewViewMST {
    
    private Integer v; // indica o vertice
    private String arestaPara; // aresta
    private Double distPara; // peso, distancia

    public PrimEagerItemTablewViewMST(Integer v, String arestaPara, Double distPara) {
        this.v = v;
        this.arestaPara = arestaPara;
        this.distPara = distPara;
    }

    public PrimEagerItemTablewViewMST(Integer v, String arestaPara) {
        this.v = v;
        this.arestaPara = arestaPara;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public String getArestaPara() {
        return arestaPara;
    }

    public void setArestaPara(String arestaPara) {
        this.arestaPara = arestaPara;
    }

    public Double getDistPara() {
        return distPara;
    }

    public void setDistPara(Double distPara) {
        this.distPara = distPara;
    }
      
}

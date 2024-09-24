/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grafos.model;

/**
 *
 * @author Danielle
 */
public class PrimMarcado {
    
    private Integer vertice;
    private String marcado;

    public PrimMarcado() {
    }

    public PrimMarcado(Integer vertice, String marcado) {
        this.vertice = vertice;
        this.marcado = marcado;
    }

    public Integer getVertice() {
        return vertice;
    }

    public void setVertice(Integer vertice) {
        this.vertice = vertice;
    }

    public String getMarcado() {
        return marcado;
    }

    public void setMarcado(String marcado) {
        this.marcado = marcado;
    }
    
    
}

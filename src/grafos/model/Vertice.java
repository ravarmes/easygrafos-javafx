/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grafos.model;

/**
 *
 * @author john_
 */
public class Vertice {
    
    
    private int id;             // ide do vértice
    private int x;              // coordenada x do vértice
    private int y;              // coordenada y do vértice
    private boolean pronto;     // indica se o vértice esta pronto
    private boolean utilizado;  // indica que o vértice já foi finalizado
    

    
    
    /**
     * Construtor
     * @param id id do vértice
     * @param x coordenada x do vértice
     * @param y coordenada y do vértice
     */
    public Vertice(int id, int x, int y){
        
        this.id = id;
        this.x = x;
        this.y = y;
        
        
    }

    public boolean isUtilizado() {
        return utilizado;
    }

    public void setUtilizado(boolean utilizado) {
        this.utilizado = utilizado;
    }
    
    

    public boolean isPronto() {
        return pronto;
    }

    public void setPronto(boolean pronto) {
        this.pronto = pronto;
    }

    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    
    
    
}

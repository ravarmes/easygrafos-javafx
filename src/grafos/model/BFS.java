
package grafos.model;

/**
 * Modelo utilizado para preencher a tabela de parâmetros. Na tabela cada linha possui um elemento BFS. Ele guarda dados sobre o vértice que tem id igual a sua posição na tabela.
 * 
 * @author Jonathas
 */
public class BFS {
    
    private String itemMarcado;  //indica se este vértice já foi visitado
    private String arestaPara; //indica por qual vértice ele foi descoberto
    private int id; //id do elemento BFS
    private int distanciaPara; //distancia do vértice para a origem

    
    //get e set
    public String getItemMarcado() {
        return itemMarcado;
    }

    public void setItemMarcado(String itemMarcado) {
        this.itemMarcado = itemMarcado;
    }

    public String getArestaPara() {
        return arestaPara;
    }

    public void setArestaPara(String arestaPara) {
        this.arestaPara = arestaPara;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDistanciaPara() {
        return distanciaPara;
    }

    public void setDistanciaPara(int distanciaPara) {
        this.distanciaPara = distanciaPara;
    }
    
    
    
}

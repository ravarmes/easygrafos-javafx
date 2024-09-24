
package grafos.model;

/**
 * Modelo utilizado para preencher a tabela de parâmetros. Na tabela cada linha possui um elemento DFS. 
 * Ele guarda dados sobre o vértice que tem id igual a sua posição na tabela.
 * @author Jonathas
 */
public class DFS {
    
    public String itemMarcado;  //indica se o vértice já foi visitado
    public String arestaPara;   //indica por qual vértice, este vértice foi descoberto
    public int id;              //id do elemento DFS

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

}

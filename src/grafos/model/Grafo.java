/*******************************************************************************
 *  Compilação:        javac Grafo.java
 *  Execução:          java Grafo dados.txt
 *  Dependências:      Aresta.java
 *  Arquivos de dados: Grafo1.txt
 *  Link dos dados:    https://drive.google.com/open?id=0B3q56TwNCeXoMlQ1c1dGOXJRbG8
 *
 *  Um grafo de arestas, implementado utilizando listas de adjacências.
 *
 *
 ******************************************************************************/
package grafos.model;

import grafos.arquivo.In;

import java.util.ArrayList;
import java.util.List;


/**
 * Esta classe implementa a representação do grafo com lista de adjacências.
 * Para documentação adicional, acesse:
 * <a href="http://algs4.cs.princeton.edu/41graph/">Section 4.1</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 */


public class Grafo {

    private static final String NEWLINE = System.getProperty("line.separator");
    
    private final int V;                // número de vértices no grafo
    private int A;                      // número de arestas no grafo
    private List<Aresta>[] adj;         // adj[v1] = lista de adjacência do vértice v1
    private List<Vertice> listVertice;  // list com os vértices do grafo
    private int contArestas;            // quantidade de arestas

    /**
     * Inicializa um dígrafo com V vertices e 0 arestas.
     
     * @param  V o número de vértices
     * 
     * @throws IllegalArgumentException se V menor 0
     */
    public Grafo(int V) {
        if (V < 0) {
            throw new IllegalArgumentException("Número de vértices no grafo deve ser não negativo");
        }
        this.V = V;
        this.A = 0;
        adj = new ArrayList[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new ArrayList<>();
        }
    }

    /**  
     * Inicializa um grafo à partir de um arquivo de dados.
     * O formato é o número de vértices V e o número de arestas A
     * seguido por pares de pares de vértices.
     * @param  in o arquivo de entrada de dados
     * @throws IndexOutOfBoundsException se os pontos finais de qualquer borda estão fora da área prescrita
     * @throws IllegalArgumentException se o número de vértices ou arestas for negativo
     */
    
    
    public Grafo(In in) {
        this.listVertice = new ArrayList<>();
        
        this.V = in.readInt();
        this.A = in.readInt();
        
        this.adj = new ArrayList[this.V];
        for (int v = 0; v < this.V; v++) {
            this.adj[v] = new ArrayList<>();
        }
        
        if (this.V < 0) {
            throw new IllegalArgumentException("Número de Vértices deve ser não negativo");
        }
        
        Vertice novoVertice;
        int id = 0, x = 0, y = 0;
        
        for (int i = 0; i < this.V; i++) {
            x = in.readInt();
            y = in.readInt();
            novoVertice = new Vertice(id, x, y);
            this.listVertice.add(novoVertice);
            id++;
        }
        
        if (this.A < 0) {
            throw new IllegalArgumentException("Número de arestas deve ser não negativo");
        }
        for (int i = 0; i < A; i++) {
            int v1 = in.readInt();
            int v2 = in.readInt();
            
            ///CRIAR METODO PARA VERIFICAR SE O VERTICE EXISTE
            Vertice vertice1 = this.listVertice.get(v1);
            Vertice vertice2 = this.listVertice.get(v2);
            addAresta(new Aresta(vertice1, vertice2, i));
        }
        
    }

    /**
     * Retorna o número de vértices do dígrafo.
     * @return o número de vértices do dígrafo
     */
    public int V() {
        return V;
    }

    /**
     * Retorna o número de arestas do dígrafo.
     * @return o número de arestas do dígrafo
     */
    public int A() {
        return A;
    }

    /**
     * Retorna o número de arestas do dígrafo.
     * @return o número de arestas do dígrafo
     */
    public int getA() {
        return A;
    }

    /**
     * 
     * @param A quantidade de arestas
     */
    public void setA(int A) {
        this.A = A;
    }

    /**
     * 
     * @return retorna a lista de adjacencias do grafo
     */
    public List<Aresta>[] getAdj() {
        return adj;
    }

    /**
     * 
     * @return retorna a lista de vértices do grafo
     */
    public List<Vertice> getListVertice() {
        return listVertice;
    }

 

    /**
     * Adiciona aresta direcionada a no dígrafo.
     * @param  a a aresta
     * @throws IndexOutOfBoundsException caso extremidades não estejam entre 0 e V-1
     */
    public void addAresta(Aresta a) {
        Vertice v1 = a.umVertice();
        Vertice v2 = a.outroVertice(v1);
        ///validaVertice(v1);
        ///validaVertice(v2);
        adj[v1.getId()].add(0, a);
        Aresta a2 = new Aresta(a.getV2(), a.getV1(), this.A() + a.getId());
        adj[v2.getId()].add (0, a2);
        
    }

    /**
     * Retorna as arestas incidentes no vértice v.
     * @param  v o vértice
     * @return as arestas incidentes no vértice v como um Iterable
     * @throws IndexOutOfBoundsException caso v não seja 0 menor/igual a v menor V
     */
    public List<Aresta> adj(int v) {
        ///validaVertice(v);
        return adj[v];
    }

    /**
     * Retorna o grau do vértice v.
     * @param v o vértice
     * @return o grau do vértice v
     * @throws IndexOutOfBoundsException caso não seja 0 menor igual v menor V
     */
    public int grau(int v) {
        ///validaVertice(v);
        return adj[v].size();
    }

    /**
     * Retorna todas as arestas neste grafo.
     * @return todas as arestas neste grafo, como um Iterable
     */
    public List<Aresta> arestas() {
        List<Aresta> lista = new ArrayList();
        for (int v = 0; v < V; v++) {
            for (Aresta a : adj(v)) {
                lista.add(a);
            }
        }
        return lista;
    }

    /**
     * Retorna uma representação String deste grafo.
     * @return uma representação String deste grafo
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(V + " " + A + NEWLINE);
        for (int v = 0; v < V; v++) {
            s.append(v + ": ");
            for (Aresta a : adj[v]) {
                s.append(a + "  ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }
    
    /**
     * 
     * @param v vértice a ser procurado na lista
     * @return returna true se o vertice exista na lista e false caso contrario
     */
    private boolean verificaLista(Vertice v) {
        if (listVertice.isEmpty()) {
            return true;
        } else {

            for (int i = 0; i < listVertice.size(); i++) {
                if (v.getId()!= listVertice.get(i).getId()) {

                    
                }
            }
            return true;
        }

    }
    
    /**
     * verifica se o vértice esta pronto, ou seja, se todas as suas arestas foram exploradas até o fim
     * @param grafo grafo, com as arestas e vértices
     * @param id do vértice a ser verificado
     * @return retorna true se o vértice estiver pronto e false se o contrário
     */
    private boolean verticePronto(Grafo grafo, int id) {
        
        boolean pronto = true;
        for (int i = 0; i < grafo.getAdj()[id].size(); i++) {
            if (!grafo.getAdj()[id].get(i).isUtilizada()) {
                pronto = false;
            }
        }
        return pronto;
    }
}

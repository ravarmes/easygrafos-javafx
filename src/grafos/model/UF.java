/******************************************************************************
 *  Compilação:         javac UF.java
 *  Execução:           java UF < input.txt
 *  Dependências:       StdIn.java StdOut.java
 *  Arquivo de Dados:   http://algs4.cs.princeton.edu/15uf/tinyUF.txt
 *                      http://algs4.cs.princeton.edu/15uf/mediumUF.txt
 *                      http://algs4.cs.princeton.edu/15uf/largeUF.txt
 *
 *  Rápida união ponderada por tamanho dos conjuntos (sem redução do caminho pela metade: pai[p] = pai[pai[p]]).
 *
 *  % java UF < tinyUF.txt
 *  4 3
 *  3 8
 *  6 5
 *  9 4
 *  2 1
 *  5 0
 *  7 2
 *  6 1
 *  2 components
 *
 ******************************************************************************/

package grafos.model;


/**
 * Esta classe implementa Um algoritmo union-find é um algoritmo que realiza duas operações úteis nesta estrutura de dados:
 * Find (encontra): Determina a qual conjunto um determinado elemento pertence. Também útil para determinar se dois elementos estão no mesmo conjunto.
 * Union (junta): Combina ou agrupa dois conjuntos em um único conjunto.
 * Para documentação adicional, acesse:
 * <a href="http://algs4.cs.princeton.edu/43mst">Section 4.3</a>
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 */
public class UF {

    private int[] pai;      // pai[i] = pai of i
    private int[] tamanho;  // tamanho[i] = tamanho da árvore com raíz em i: tamanho é a quantidade de filhos + 1
    private int quantidade; // número de componentes

    /**
     * Inicializa uma estrutura de dados union-find (conjunto-disjunto) vazia com n conjuntos.
     *
     * @param  n o número de conjuntos
     * @throws IllegalArgumentException se n menor que zero
     */
    public UF(int n) {
        if (n < 0) throw new IllegalArgumentException();
        quantidade = n;
        pai = new int[n];
        tamanho = new int[n];
        for (int i = 0; i < n; i++) {
            pai[i] = i;
            tamanho[i] = 1;
        }
    }

    
    
    /**
     * Retorna a identificação do componente ao qual p pertence. Ou seja, retorna a raiz do componente no qual p faz parte
     *
     * @param  p o inteiro representando um conjunto
     * @return a identificação do componente ao qual p pertence.
     */
    public int encontra(int p) {
        valida(p);
        while (p != pai[p]) {
            //pai[p] = pai[pai[p]];    // caminho reduzido pela metade (comentado para facilitar o entendimento)
            p = pai[p];
        }
        return p;
    }

    /**
     * Retorna o número de componentes
     *
     * @return o número de componentes entre 1 e n
     */
    public int quantidade() {
        return quantidade;
    }
  
    /**
     * Retorna true se os dois elementos estão no mesmo componente.
     *
     * @param  p o inteiro representando um elemento
     * @param  q o inteiro representando outro elemento
     * @return true se os dois elementos estão no mesmo componente;
     *         false caso contrário
     */
    public boolean conectado(int p, int q) {
        return encontra(p) == encontra(q);
    }
  
    /**
     * Une o componente contendo o elemento p com o componente contendo o elemento q 
     *
     * @param  p o inteiro representando um elemento
     * @param  q o inteiro representando outro elemento
     */
    public void junta(int p, int q) {
        int raizP = encontra(p);
        int raizQ = encontra(q);
        if (raizP == raizQ) return;

        // fazer a raiz do elemento de menor tamanho como raiz do elemento de maior tamanho
        if  (tamanho[raizP] < tamanho[raizQ]){ 
            pai[raizP] = raizQ;
            tamanho[raizQ]+= tamanho[raizP];
        }
        else{
            pai[raizQ] = raizP;
            tamanho[raizP]+= tamanho[raizQ];
        }
        quantidade--;
    }

    // valida se p é um índice válido
    private void valida(int p) {
        int n = pai.length;
        if (p < 0 || p >= n) {
            throw new IndexOutOfBoundsException("index " + p + " não está entre 0 e " + (n-1));  
        }
    }

    /**
     * Testa a classe UF
     */
    public static void main(String[] args) {
        /*
        //Lista 02 - Exercício 01
        UF uf = new UF(5);
        
        uf.junta(0, 1);
        uf.junta(2, 3);
        uf.junta(1, 3);
        uf.junta(4, 3);
        
        System.out.println(uf.quantidade() + " componente(s)");
        */
        
        
        //Lista 02 - Exercício 02
        UF uf = new UF(8);
        
        if(!uf.conectado(0, 7)) uf.junta(0, 7);
        if(!uf.conectado(2, 3)) uf.junta(2, 3);
        if(!uf.conectado(1, 7)) uf.junta(1, 7);
        if(!uf.conectado(0, 2)) uf.junta(0, 2);
        if(!uf.conectado(5, 7)) uf.junta(5, 7);
        if(!uf.conectado(1, 3)) uf.junta(1, 3);
        if(!uf.conectado(1, 5)) uf.junta(1, 5);
        if(!uf.conectado(2, 7)) uf.junta(2, 7);
        if(!uf.conectado(4, 5)) uf.junta(4, 5);
        if(!uf.conectado(1, 2)) uf.junta(1, 2);
        if(!uf.conectado(4, 7)) uf.junta(4, 7);
        if(!uf.conectado(0, 4)) uf.junta(0, 4);
        if(!uf.conectado(6, 2)) uf.junta(6, 2);
        //if(!uf.conectado(3, 6)) uf.junta(3, 6);
        //if(!uf.conectado(6, 0)) uf.junta(6, 0);
        //if(!uf.conectado(6, 4)) uf.junta(6, 4);
        
        System.out.println(uf.quantidade() + " componente(s)");
        
        
        
        
        /*
        //Testando lendo arquivo
        int n = StdIn.readInt();
        UF uf = new UF(n);
        while (!StdIn.isEmpty()) {
            int p = StdIn.readInt();
            int q = StdIn.readInt();
            if (uf.conectado(p, q)) continue;
            uf.junta(p, q);
            System.out.println(p + " " + q);
        }
        System.out.println(uf.quantidade() + " componentes");
        */
    }
}

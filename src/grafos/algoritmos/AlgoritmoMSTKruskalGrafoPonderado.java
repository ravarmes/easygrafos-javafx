
/** *****************************************************************************
 *  Compilação:       javac AlgoritmoMSTKruskalGrafoPonderado.java
 *  Execução:         java  AlgoritmoMSTKruskalGrafoPonderado dados.txt
 *  Dependências:     GrafoPonderado.java Aresta.java Fila.java
 *                    UF.java In.java
 *  Arquivo de dados: GrafoPonderado1.txt
 *  Link dos dados:   https://drive.google.com/open?id=0B3q56TwNCeXoenFyMnlzX2ZyXzg
 *
 *  Calcula a árvore geradora mínima (MTS) utilizando o algoritmo de Kruskal.
 *
 *  %  java AlgoritmoMSTKruskalGrafoPonderado GrafoPonderado1.txt
 *  0-7 0.16000
 *  2-3 0.17000
 *  1-7 0.19000
 *  0-2 0.26000
 *  5-7 0.28000
 *  4-5 0.35000
 *  6-2 0.40000
 *  1.81000
 *
 ***************************************************************************** */
package grafos.algoritmos;

import grafos.controller.FXMLExplorarComKruskalController;
import grafos.model.Aresta;
import grafos.model.Fila;
import grafos.model.FilaMSTKruskal;
import grafos.model.FilaPrioridadeMin;
import grafos.model.GrafoPonderado;
import grafos.model.KruskalItemTablewViewMST;
import grafos.model.KruskalItemTablewViewPQ;
import grafos.model.UF;
import grafos.runnable.RunnableKruskal;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;

/**
 * Esta classe implementa a geração da árvore geradora mínima utilizando o
 * algoritmo de KruskalItemTablewViewMST. Para documentação adicional, acesse:
 * <a href="http://algs4.cs.princeton.edu/43mst">Section 4.3</a>
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 */
public class AlgoritmoMSTKruskalGrafoPonderado {

    private static final double FLOATING_POINT_EPSILON = 1E-12;

    public static int velocidade = 750;  //Velocidade inicial de execução
    private double peso;                            // peso da árvore geradora mínima (MST)
    private FilaMSTKruskal<Aresta> mst = new FilaMSTKruskal<Aresta>();  // arestas na árvore geradora mínima (MST)

    //Marcadores da linha do algoritmo que estava colorida e que vai ser colorida
    public int linhaDeExecucao;
    public int linhaDeExecucaoAntiga;

    //Elementos gráficos que são recebidos pelo construtor e passados para a runnable
    public TableView tableViewParametros;           // tabela com os valores das variaves do codigo
    public ListView listViewCodigo;                 // lista com o codigo que será colorido
    public Fila<Integer> VerticesNaoExplorados;     // representa a fila com a ordem de exploração dos vértices
    public List<Integer> fila;                      // auxiliar da fila
    private RunnableKruskal runnableKruskal;        // Objeto do tipo RunnableKruskal, será utilizado para verificar se a Thread que a utiliza ainda esta ativa

    public Thread main; //Objeto do tipo Thread uilizado para receber a instancia da JavaFX Application Thread (Ela é utilizada para pausar a aplicação) 

    public List<KruskalItemTablewViewMST> listKruskalItemTablewViewMST = new ArrayList<KruskalItemTablewViewMST>();     // lista que preenche a tableViewParametros

    public List<KruskalItemTablewViewPQ> listKruskalItemTablewViewPQ = new ArrayList<KruskalItemTablewViewPQ>();     // lista que preenche a tableViewParametros
    public FilaPrioridadeMin<KruskalItemTablewViewPQ> filaKruskalItemTablewViewPQ = new FilaPrioridadeMin<>();

    /**
     * Calcula a árvore geradora mínima do grafo ponderado.
     *
     * @param G grafo criado a partir do documento txt
     * @param runnableKruskal instância da runnableKruskal, utilizada para
     * verificar se a Thread que a utiliza ainda esta ativa (Ela fica desativada
     * quando o usuário clica em voltar na tela)
     * @param main instancia da JavaFX Application Thread
     * @throws InterruptedException exception do sleep
     */
    public AlgoritmoMSTKruskalGrafoPonderado(GrafoPonderado G, RunnableKruskal runnableKruskal, Thread main) throws InterruptedException {

        System.out.println("\n\nAlgoritmoMSTKruskalGrafoPonderado");

        //iniciando os atributos da classe
        this.runnableKruskal = runnableKruskal;
        this.main = main;

        if (this.runnableKruskal.isExecutar()) {

            RunnableKruskal.exibirVariaveis("Variável  v1:   --" + "\n" + "Variável  v2:   --" + "\n" + "Variável  peso:   " + peso, true);
            parar();

            parar();
            linhaDeExecucaoAntiga = 0;
            linhaDeExecucao = 5;
            RunnableKruskal.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnableKruskal.isExecutar());
            Thread.sleep(velocidade);
            parar();
            FilaPrioridadeMin<Aresta> pq = new FilaPrioridadeMin<>();

            parar();
            linhaDeExecucaoAntiga = linhaDeExecucao;
            linhaDeExecucao = 6;
            RunnableKruskal.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnableKruskal.isExecutar());
            Thread.sleep(velocidade);
            parar();

            for (Aresta e : G.arestasNaoRepetidas()) {

                parar();
                linhaDeExecucaoAntiga = linhaDeExecucao;
                linhaDeExecucao = 7;
                RunnableKruskal.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnableKruskal.isExecutar());
                Thread.sleep(velocidade);
                parar();

                filaKruskalItemTablewViewPQ.insere(new KruskalItemTablewViewPQ(e.getV1().getId() + " - " + e.getV2().getId(), e.getPeso()));
                RunnableKruskal.alterarTablewViewPQ(filaKruskalItemTablewViewPQ, this.runnableKruskal.isExecutar());

                pq.insere(e);

                parar();
                linhaDeExecucaoAntiga = linhaDeExecucao;
                linhaDeExecucao = 6;
                RunnableKruskal.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnableKruskal.isExecutar());
                Thread.sleep(velocidade);
                parar();
            }
            parar();
            linhaDeExecucaoAntiga = linhaDeExecucao;
            linhaDeExecucao = 8;
            RunnableKruskal.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnableKruskal.isExecutar());
            Thread.sleep(velocidade);
            parar();

            UF uf = new UF(G.V()); // instanciando uma estrutura do tipo union-find com o número de vértices do grafo
            parar();
            linhaDeExecucaoAntiga = linhaDeExecucao;
            linhaDeExecucao = 9;
            RunnableKruskal.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnableKruskal.isExecutar());
            Thread.sleep(velocidade);
            parar();

            parar();
            linhaDeExecucaoAntiga = linhaDeExecucao;
            linhaDeExecucao = 10;
            RunnableKruskal.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnableKruskal.isExecutar());
            Thread.sleep(velocidade);
            parar();
            //while (!pq.isEmpty() && mst.tamanho() < G.V() - 1) {
            while (!pq.isEmpty()) {
                Aresta a = pq.delMin();
                parar();
                linhaDeExecucaoAntiga = linhaDeExecucao;
                linhaDeExecucao = 11;
                RunnableKruskal.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnableKruskal.isExecutar());
                Thread.sleep(velocidade);
                parar();

                filaKruskalItemTablewViewPQ.delMin();
                RunnableKruskal.alterarTablewViewPQ(filaKruskalItemTablewViewPQ, this.runnableKruskal.isExecutar());

                int v1 = a.umVertice().getId(); //[Modificado]
                parar();
                linhaDeExecucaoAntiga = linhaDeExecucao;
                linhaDeExecucao = 12;
                RunnableKruskal.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnableKruskal.isExecutar());
                Thread.sleep(velocidade);
                parar();

                RunnableKruskal.exibirVariaveis("Variável  v1:   " + v1 + "\n" + "Variável  v2:   --" + "\n" + "Variável  peso:   " + peso, true);

                int v2 = a.outroVertice(a.umVertice()).getId();
                parar();
                linhaDeExecucaoAntiga = linhaDeExecucao;
                linhaDeExecucao = 13;
                RunnableKruskal.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnableKruskal.isExecutar());
                Thread.sleep(velocidade);
                parar();

                RunnableKruskal.exibirVariaveis("Variável  v1:   " + v1 + "\n" + "Variável  v2:   " + v2 + "\n" + "Variável  peso:   " + peso, true);

                parar();
                linhaDeExecucaoAntiga = linhaDeExecucao;
                linhaDeExecucao = 14;
                RunnableKruskal.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnableKruskal.isExecutar());
                Thread.sleep(velocidade);
                parar();
                if (!uf.conectado(v1, v2)) {
                    uf.junta(v1, v2);
                    parar();
                    linhaDeExecucaoAntiga = linhaDeExecucao;
                    linhaDeExecucao = 15;
                    RunnableKruskal.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnableKruskal.isExecutar());
                    Thread.sleep(velocidade);
                    parar();

                    mst.enfileira(a);
                    parar();
                    linhaDeExecucaoAntiga = linhaDeExecucao;
                    linhaDeExecucao = 16;
                    RunnableKruskal.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnableKruskal.isExecutar());
                    Thread.sleep(velocidade);
                    parar();

                    listKruskalItemTablewViewMST.add(new KruskalItemTablewViewMST(a.getV1().getId() + " - " + a.getV2().getId()));
                    RunnableKruskal.alterarTablewViewMST(listKruskalItemTablewViewMST, this.runnableKruskal.isExecutar());
                    RunnableKruskal.colorirAresta(a.getV1().getId(), a.getV2().getId(), 60, 195, 100, this.runnableKruskal.isExecutar(), 0, 0, 0);

                    peso += a.getPeso();
                    parar();
                    linhaDeExecucaoAntiga = linhaDeExecucao;
                    linhaDeExecucao = 17;
                    RunnableKruskal.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnableKruskal.isExecutar());
                    Thread.sleep(velocidade);
                    parar();

                    RunnableKruskal.exibirVariaveis("Variável  v1:   " + v1 + "\n" + "Variável  v2:   " + v2 + "\n" + "Variável  peso:   " + peso, true);
                } else {
                    RunnableKruskal.colorirArestaCinza(a.getV2().getId(), a.getV1().getId(), 211, 211, 211, this.runnableKruskal.isExecutar(), 0, 0, 0);
                }
                parar();
                linhaDeExecucaoAntiga = linhaDeExecucao;
                linhaDeExecucao = 18;
                RunnableKruskal.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnableKruskal.isExecutar());
                Thread.sleep(velocidade);
                parar();

                parar();
                linhaDeExecucaoAntiga = linhaDeExecucao;
                linhaDeExecucao = 10;
                RunnableKruskal.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnableKruskal.isExecutar());
                Thread.sleep(velocidade);
                parar();
            }

            parar();
            linhaDeExecucaoAntiga = linhaDeExecucao;
            linhaDeExecucao = 19;
            RunnableKruskal.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnableKruskal.isExecutar());
            Thread.sleep(velocidade);
            parar();
        }
        parar();
        linhaDeExecucaoAntiga = linhaDeExecucao;
        linhaDeExecucao = 20;
        RunnableKruskal.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnableKruskal.isExecutar());
        Thread.sleep(velocidade);
        parar();
        
        parar();
        linhaDeExecucaoAntiga = linhaDeExecucao;
        linhaDeExecucao = 21;
        RunnableKruskal.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnableKruskal.isExecutar());
        Thread.sleep(velocidade);
        parar();

        // checa as condições de otimização
        assert checa(G);
    }

    /**
     * Retorna as arestas da árvore geradora mínima (MST).
     *
     * @return as arestas da árvores geradora mínima como um iterable de arestas
     */
    public Iterable<Aresta> arestas() {
        return mst;
    }

    /**
     * Retorna a soma das arestas ponderadas na árvores geradora mínima (MST).
     *
     * @return a soma das arestas ponderadas na árvores geradora mínima (MST)
     */
    public double peso() {
        return peso;
    }

    /**
     * Checa as condições de otimização
     *
     * @param G o grafo ponderado
     * @return verdadeiro se as condições forem satisfeitas, a falso, caso
     * contrário
     */
    private boolean checa(GrafoPonderado G) {

        // checa peso total
        double total = 0.0;
        for (Aresta a : arestas()) {
            total += a.getPeso();
        }
        if (Math.abs(total - peso()) > FLOATING_POINT_EPSILON) {
            System.err.printf("Peso das arestas não é igual peso(): %f vs. %f\n", total, peso());
            return false;
        }

        // checa que é acíclico
        UF uf = new UF(G.V());
        for (Aresta a : arestas()) {
            int v1 = a.umVertice().getId(), v2 = a.outroVertice(a.umVertice()).getId();
            if (uf.conectado(v1, v2)) {
                System.err.println("Não é floresta");
                return false;
            }
            uf.junta(v1, v2);
        }

        // chega que é uma árvores geradora
        for (Aresta a : G.arestas()) {
            int v1 = a.umVertice().getId(), v2 = a.outroVertice(a.umVertice()).getId();
            if (!uf.conectado(v1, v2)) {
                System.err.println("Não é uma árvore geradora mínima");
                return false;
            }
        }

        // checa que é uma árvore geradora mínima ( cortar condições de otimização )
        for (Aresta a : arestas()) {

            // todas arestas na árvores geradora mínima (MST) exceto 'a'
            uf = new UF(G.V());
            for (Aresta f : mst) {
                int x = f.umVertice().getId(), y = f.outroVertice(f.umVertice()).getId();
                if (f != a) {
                    uf.junta(x, y);
                }
            }

            // checa que é aresta de peso min em corte de cruzamento
            for (Aresta f : G.arestas()) {
                int x = f.umVertice().getId(), y = f.outroVertice(f.umVertice()).getId();
                if (!uf.conectado(x, y)) {
                    if (f.getPeso() < a.getPeso()) {
                        System.err.println("Aresta " + f + " viola as condições de corte de optimização");
                        return false;
                    }
                }
            }

        }

        return true;
    }

    /**
     *
     * @throws InterruptedException excepition para o wait
     */
    //método para parar a aplicação (botão de pause)
    public void parar() throws InterruptedException {
        if (FXMLExplorarComKruskalController.pausado == 1) {
            synchronized (main) {
                main.wait();
            }
        }
    }

    /**
     * Testa a classe AlgoritmoMSTKruskalGrafoPonderado
     */
    public static void main(String[] args) {
        /*
        In in = new In(args[0]);
        GrafoPonderado G = new GrafoPonderado(in);
        AlgoritmoMSTKruskalGrafoPonderado kruskal = new AlgoritmoMSTKruskalGrafoPonderado(G);
        for (Aresta a : kruskal.arestas()) {
            System.out.println(a);
        }
        System.out.printf("%.5f\n", kruskal.peso());
         */
    }

}

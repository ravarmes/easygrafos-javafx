/** *****************************************************************************
 *  Compilação:       javac AlgoritmoMSTPrimLazy.java
 *  Execução:         java  AlgoritmoMSTPrimLazy dados.txt
 *  Dependências:     GrafoPonderado.java Aresta.java Fila.java
 *                    UF.java In.java
 *  Arquivo de dados: GrafoPonderado1.txt
 *  Link dos dados:   https://drive.google.com/open?id=0B3q56TwNCeXoenFyMnlzX2ZyXzg
 *
 *  Calcula a árvore geradora mínima (MTS) utilizando o algoritmo de Prim Lazy.
 *
 *  %  java AlgoritmoMSTPrimLazy GrafoPonderado1.txt
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

import grafos.controller.FXMLExplorarComPrimLazyController;
import grafos.model.Aresta;
import grafos.model.Fila;
import grafos.model.FilaMSTPrimLazy;
import grafos.model.FilaPrioridadeMin;
import grafos.model.GrafoPonderado;
import grafos.model.PrimLazyItemTablewViewMST;
import grafos.model.PrimLazyItemTablewViewPQ;
import grafos.model.PrimMarcado;
import grafos.model.UF;
import grafos.model.Vertice;
import grafos.runnable.RunnablePrimLazy;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;

/**
 * Esta classe implementa a geração da árvore geradora mínima utilizando o
 * algoritmo de PrimLazyItemTablewViewMST. Para documentação adicional, acesse:
 * <a href="http://algs4.cs.princeton.edu/43mst">Section 4.3</a>
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 */
public class AlgoritmoMSTPrimLazy {

    private static final double FLOATING_POINT_EPSILON = 1E-12;

    public static int velocidade = 750;  //Velocidade inicial de execução
    private double peso;                            // peso da árvore geradora mínima (MST)
    private FilaMSTPrimLazy<Aresta> mst = new FilaMSTPrimLazy<Aresta>();  // arestas na árvore geradora mínima (MST)

    //Marcadores da linha do algoritmo que estava colorida e que vai ser colorida
    public int linhaDeExecucao;
    public int linhaDeExecucaoAntiga;

    //Elementos gráficos que são recebidos pelo construtor e passados para a runnable
    public TableView tableViewParametros;           // tabela com os valores das variaves do codigo
    public ListView listViewCodigo;                 // lista com o codigo que será colorido
    public Fila<Integer> VerticesNaoExplorados;     // representa a fila com a ordem de exploração dos vértices
    public List<Integer> fila;                      // auxiliar da fila
    private RunnablePrimLazy runnablePrimLazy;        // Objeto do tipo RunnablePrimLazy, será utilizado para verificar se a Thread que a utiliza ainda esta ativa

    public Thread main; //Objeto do tipo Thread uilizado para receber a instancia da JavaFX Application Thread (Ela é utilizada para pausar a aplicação) 

    public List<PrimLazyItemTablewViewMST> listPrimLazyItemTablewViewMST = new ArrayList<PrimLazyItemTablewViewMST>();     // lista que preenche a tableViewParametros
    public PrimLazyItemTablewViewMST itemMST;

    public PrimLazyItemTablewViewPQ itemPQ;
    // lista que preenche a tableViewParametros
    public FilaPrioridadeMin<PrimLazyItemTablewViewPQ> filaPrimLazyItemTablewViewPQ = new FilaPrioridadeMin<>();

    boolean[] marcado;
    List<PrimMarcado> listMarcado;
    PrimMarcado m;
    FilaPrioridadeMin<Aresta> pq;

    Integer confere = 0;
    Integer ver = 0;

    /**
     * Calcula a árvore geradora mínima do grafo ponderado.
     *
     * @param G grafo criado a partir do documento txt
     * @param runnablePrimLazy instância da runnablePrimLazy, utilizada para
     * verificar se a Thread que a utiliza ainda esta ativa (Ela fica desativada
     * quando o usuário clica em voltar na tela)
     * @param main instancia da JavaFX Application Thread
     * @param v0 vertice de origem
     * @throws InterruptedException exception do sleep
     */
    public AlgoritmoMSTPrimLazy(GrafoPonderado G, RunnablePrimLazy runnablePrimLazy, Thread main, int v0) throws InterruptedException {

        //iniciando os atributos da classe
        this.runnablePrimLazy = runnablePrimLazy;
        this.main = main;

        if (this.runnablePrimLazy.isExecutar()) {

            RunnablePrimLazy.exibirVariaveis("Vértice 1:   --" + "\n" + "Vértice 2:   --" + "\n" + "Aresta Atual:   --" + "\n" + "Peso:   " + peso, true);
            parar();

            parar();
            linhaDeExecucaoAntiga = 0;
            linhaDeExecucao = 5;
            RunnablePrimLazy.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimLazy.isExecutar());
            Thread.sleep(velocidade);
            parar();

            FilaMSTPrimLazy<Aresta> mst = new FilaMSTPrimLazy<>();
            
            parar();
            linhaDeExecucaoAntiga = 5;
            linhaDeExecucao = 6;
            RunnablePrimLazy.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimLazy.isExecutar());
            Thread.sleep(velocidade);
            pq = new FilaPrioridadeMin<>();
            parar();

            parar();
            linhaDeExecucaoAntiga = linhaDeExecucao;
            linhaDeExecucao = 7;
            RunnablePrimLazy.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimLazy.isExecutar());
            Thread.sleep(velocidade);
            parar();

            parar();
            linhaDeExecucaoAntiga = linhaDeExecucao;
            linhaDeExecucao = 8;
            RunnablePrimLazy.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimLazy.isExecutar());
            Thread.sleep(velocidade);
            marcado = new boolean[G.V()];
            listMarcado = new ArrayList<PrimMarcado>();
            parar();

            //acho que tenho que mudar isso, ao invés de pegar o indice do vetor, tenho que pegar o vertice mesmo
            
            // para preenceher a tableviewMaracado - inicialmete FALSE
            for (int i = 0; i < marcado.length; i++) {
                m = new PrimMarcado();
                m.setVertice(i);
                m.setMarcado(String.valueOf(marcado[i]).toUpperCase());
                listMarcado.add(m);
            }

            //preenche a tableviewMaracado - inicialmete todos terão o status FALSE
            parar();
            RunnablePrimLazy.alterarTablewMarcado(listMarcado, this.runnablePrimLazy.isExecutar());
            Thread.sleep(velocidade);
            parar();

            parar();
            linhaDeExecucaoAntiga = linhaDeExecucao;
            linhaDeExecucao = 9;
            RunnablePrimLazy.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimLazy.isExecutar());
            Thread.sleep(velocidade);
            visita(G, v0);
            parar();

            parar();
            linhaDeExecucaoAntiga = linhaDeExecucao;
            linhaDeExecucao = 19;
            RunnablePrimLazy.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimLazy.isExecutar());
            Thread.sleep(velocidade);
            parar();

            parar();
            linhaDeExecucaoAntiga = linhaDeExecucao;
            linhaDeExecucao = 20;
            RunnablePrimLazy.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimLazy.isExecutar());
            Thread.sleep(velocidade);
            parar();

            parar();
            RunnablePrimLazy.alterarTablewMarcado(listMarcado, this.runnablePrimLazy.isExecutar());
            RunnablePrimLazy.colorirVertice(v0, 60, 195, 100, this.runnablePrimLazy.isExecutar());
            RunnablePrimLazy.exibirVariaveis("Vértice 1:   " + ver + "\n" + "Vértice 2:   --" + "\n" + "Aresta Atual:   --" + "\n" + "Peso:   " + peso, true);
            Thread.sleep(velocidade);
            parar();

            for (Aresta a : G.adj(ver)) {
                parar();
                linhaDeExecucaoAntiga = linhaDeExecucao;
                linhaDeExecucao = 21;
                RunnablePrimLazy.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimLazy.isExecutar());
                Thread.sleep(velocidade);
                parar();

                parar();
                linhaDeExecucaoAntiga = linhaDeExecucao;
                linhaDeExecucao = 22;
                RunnablePrimLazy.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimLazy.isExecutar());
                Thread.sleep(velocidade);
                parar();

                if (!marcado[a.outroVerticeInt(ver)]) {
                    confere++; // pra não precisar fzr outra coisa, coloquei esse contador que será utilizado p/ verificar se o foreach acabou

                    parar();
                    RunnablePrimLazy.exibirVariaveis("Vértice 1:   " + ver + "\n" + "Vértice 2:   " + a.outroVerticeInt(ver) + "\n"
                            + "Aresta Atual:   " + a.getV1().getId() + " - " + a.getV2().getId() + "\n" + "Peso:   " + a.getPeso(), true);
                    Thread.sleep(velocidade);
                    parar();

                    parar();
                    linhaDeExecucaoAntiga = linhaDeExecucao;
                    linhaDeExecucao = 23;
                    RunnablePrimLazy.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimLazy.isExecutar());
                    Thread.sleep(velocidade);
                    parar();

                    itemPQ = new PrimLazyItemTablewViewPQ(a.getV1().getId() + " - " + a.getV2().getId(), a.getPeso());
                    filaPrimLazyItemTablewViewPQ.insere(itemPQ);

                    parar();
                    RunnablePrimLazy.alterarTablewViewPQ(filaPrimLazyItemTablewViewPQ, true);
                    Thread.sleep(velocidade);
                    parar();
                }

                if (confere == G.adj(ver).size()) {
                    parar();
                    linhaDeExecucaoAntiga = linhaDeExecucao;
                    linhaDeExecucao = 21;
                    RunnablePrimLazy.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimLazy.isExecutar());
                    Thread.sleep(velocidade);
                    parar();
                }
            }

            parar();
            linhaDeExecucaoAntiga = linhaDeExecucao;
            linhaDeExecucao = 10;
            RunnablePrimLazy.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimLazy.isExecutar());
            Thread.sleep(velocidade);
            parar();

            while (!pq.isEmpty()) {
                parar();
                linhaDeExecucaoAntiga = linhaDeExecucao;
                linhaDeExecucao = 11;
                RunnablePrimLazy.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimLazy.isExecutar());
                Thread.sleep(velocidade);
                parar();

                Aresta ares = pq.delMin();
                
                parar();
                RunnablePrimLazy.exibirVariaveis("Vértice 1:   " + ares.getV1().getId() + "\n" + "Vértice 2:   " + ares.getV2().getId() + "\n"
                        + "Aresta Atual:   " + ares.getV1().getId() + " - " + ares.getV2().getId() + "\n" + "Peso:   " + ares.getPeso(), true);
                Thread.sleep(velocidade);
                parar();

                filaPrimLazyItemTablewViewPQ.delMin();
                
                parar();
                RunnablePrimLazy.alterarTablewViewPQ(filaPrimLazyItemTablewViewPQ, true);
                Thread.sleep(velocidade);
                parar();
                
                parar();
                linhaDeExecucaoAntiga = linhaDeExecucao;
                linhaDeExecucao = 12;
                RunnablePrimLazy.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimLazy.isExecutar());
                Thread.sleep(velocidade);
                parar();

                int v1 = ares.getV1().getId(), v2 = ares.getV2().getId();

                parar();
                linhaDeExecucaoAntiga = linhaDeExecucao;
                linhaDeExecucao = 13;
                RunnablePrimLazy.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimLazy.isExecutar());
                Thread.sleep(velocidade);
                parar();

                if (marcado[v1] && marcado[v2]) {
                    RunnablePrimLazy.colorirAresta(ares.getV2().getId(), ares.getV1().getId(), 211, 211, 211, this.runnablePrimLazy.isExecutar(), 0, 0, 0);
                    continue;
                }

                parar();
                linhaDeExecucaoAntiga = linhaDeExecucao;
                linhaDeExecucao = 14;
                RunnablePrimLazy.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimLazy.isExecutar());
                Thread.sleep(velocidade);
                parar();

                mst.enfileira(ares);
                itemMST = new PrimLazyItemTablewViewMST(ares.getV1().getId() + " - " + ares.getV2().getId());
                listPrimLazyItemTablewViewMST.add(itemMST);

                parar();
                RunnablePrimLazy.alterarTablewViewMST(listPrimLazyItemTablewViewMST, this.runnablePrimLazy.isExecutar());
                Thread.sleep(velocidade);
                parar();

                ares.setUtilizada(true);
                
                RunnablePrimLazy.colorirAresta(ares.getV1().getId(), ares.getV2().getId(), 60, 195, 100, this.runnablePrimLazy.isExecutar(), 0, 0, 0);

                parar();
                linhaDeExecucaoAntiga = linhaDeExecucao;
                linhaDeExecucao = 15;
                RunnablePrimLazy.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimLazy.isExecutar());
                Thread.sleep(velocidade);
                parar();

                if (!marcado[v1]) {
                    visita(G, v1);

                    parar();
                    linhaDeExecucaoAntiga = linhaDeExecucao;
                    linhaDeExecucao = 19;
                    RunnablePrimLazy.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimLazy.isExecutar());
                    Thread.sleep(velocidade);
                    parar();

                    parar();
                    linhaDeExecucaoAntiga = linhaDeExecucao;
                    linhaDeExecucao = 20;
                    RunnablePrimLazy.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimLazy.isExecutar());
                    Thread.sleep(velocidade);
                    parar();

                    parar();
                    RunnablePrimLazy.alterarTablewMarcado(listMarcado, this.runnablePrimLazy.isExecutar());
                    RunnablePrimLazy.colorirVertice(ver, 60, 195, 100, this.runnablePrimLazy.isExecutar());
                    RunnablePrimLazy.exibirVariaveis("Vértice 1:   " + ver + "\n" + "Vértice 2:   --" + "\n" + "Aresta Atual:   --" + "\n" + "Peso:   " + peso, true);
                    Thread.sleep(velocidade);
                    parar();

                    for (Aresta a : G.adj(ver)) {
                        parar();
                        linhaDeExecucaoAntiga = linhaDeExecucao;
                        linhaDeExecucao = 21;
                        RunnablePrimLazy.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimLazy.isExecutar());
                        Thread.sleep(velocidade);
                        parar();

                        parar();
                        linhaDeExecucaoAntiga = linhaDeExecucao;
                        linhaDeExecucao = 22;
                        RunnablePrimLazy.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimLazy.isExecutar());
                        Thread.sleep(velocidade);
                        parar();

                        if (!marcado[a.outroVerticeInt(ver)]) {
                            confere++; // pra não precisar fzr outra coisa, coloquei esse contador que será utilizado p/ verificar se o foreach acabou

                            parar();
                            RunnablePrimLazy.exibirVariaveis("Vértice 1:   " + ver + "\n" + "Vértice 2:   " + a.outroVerticeInt(ver) + "\n"
                                    + "Aresta Atual:   " + a.getV1().getId() + " - " + a.getV2().getId() + "\n" + "Peso:   " + a.getPeso(), true);
                            Thread.sleep(velocidade);
                            parar();

                            parar();
                            linhaDeExecucaoAntiga = linhaDeExecucao;
                            linhaDeExecucao = 23;
                            RunnablePrimLazy.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimLazy.isExecutar());
                            Thread.sleep(velocidade);
                            parar();

                            itemPQ = new PrimLazyItemTablewViewPQ(a.getV1().getId() + " - " + a.getV2().getId(), a.getPeso());
                            filaPrimLazyItemTablewViewPQ.insere(itemPQ);

                            parar();
                            RunnablePrimLazy.alterarTablewViewPQ(filaPrimLazyItemTablewViewPQ, true);
                            Thread.sleep(velocidade);
                            parar();
                        }

                        if (confere == G.adj(ver).size()) {
                            parar();
                            linhaDeExecucaoAntiga = linhaDeExecucao;
                            linhaDeExecucao = 21;
                            RunnablePrimLazy.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimLazy.isExecutar());
                            Thread.sleep(velocidade);
                            parar();
                        }
                    }
                }

                parar();
                linhaDeExecucaoAntiga = linhaDeExecucao;
                linhaDeExecucao = 16;
                RunnablePrimLazy.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimLazy.isExecutar());
                Thread.sleep(velocidade);
                parar();

                if (!marcado[v2]) {
                    visita(G, v2);

                    parar();
                    linhaDeExecucaoAntiga = linhaDeExecucao;
                    linhaDeExecucao = 19;
                    RunnablePrimLazy.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimLazy.isExecutar());
                    Thread.sleep(velocidade);
                    parar();

                    parar();
                    linhaDeExecucaoAntiga = linhaDeExecucao;
                    linhaDeExecucao = 20;
                    RunnablePrimLazy.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimLazy.isExecutar());
                    Thread.sleep(velocidade);
                    parar();

                    parar();
                    RunnablePrimLazy.alterarTablewMarcado(listMarcado, this.runnablePrimLazy.isExecutar());
                    RunnablePrimLazy.colorirVertice(ver, 60, 195, 100, this.runnablePrimLazy.isExecutar());
                    RunnablePrimLazy.exibirVariaveis("Vértice 1:   " + ver + "\n" + "Vértice 2:   --" + "\n" + "Aresta Atual:   --" + "\n" + "Peso:   " + peso, true);
                    Thread.sleep(velocidade);
                    parar();

                    for (Aresta a : G.adj(ver)) {
                        parar();
                        linhaDeExecucaoAntiga = linhaDeExecucao;
                        linhaDeExecucao = 21;
                        RunnablePrimLazy.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimLazy.isExecutar());
                        Thread.sleep(velocidade);
                        parar();

                        parar();
                        linhaDeExecucaoAntiga = linhaDeExecucao;
                        linhaDeExecucao = 22;
                        RunnablePrimLazy.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimLazy.isExecutar());
                        Thread.sleep(velocidade);
                        parar();

                        if (!marcado[a.outroVerticeInt(ver)]) {
                            confere++; // pra não precisar fzr outra coisa, coloquei esse contador que será utilizado p/ verificar se o foreach acabou

                            parar();
                            RunnablePrimLazy.exibirVariaveis("Vértice 1:   " + ver + "\n" + "Vértice 2:   " + a.outroVerticeInt(ver) + "\n"
                                    + "Aresta Atual:   " + a.getV1().getId() + " - " + a.getV2().getId() + "\n" + "Peso:   " + a.getPeso(), true);
                            Thread.sleep(velocidade);
                            parar();

                            parar();
                            linhaDeExecucaoAntiga = linhaDeExecucao;
                            linhaDeExecucao = 23;
                            RunnablePrimLazy.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimLazy.isExecutar());
                            Thread.sleep(velocidade);
                            parar();

                            itemPQ = new PrimLazyItemTablewViewPQ(a.getV1().getId() + " - " + a.getV2().getId(), a.getPeso());
                            filaPrimLazyItemTablewViewPQ.insere(itemPQ);

                            parar();
                            RunnablePrimLazy.alterarTablewViewPQ(filaPrimLazyItemTablewViewPQ, true);
                            Thread.sleep(velocidade);
                            parar();
                        }

                        if (confere == G.adj(ver).size()) {
                            parar();
                            linhaDeExecucaoAntiga = linhaDeExecucao;
                            linhaDeExecucao = 21;
                            RunnablePrimLazy.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimLazy.isExecutar());
                            Thread.sleep(velocidade);
                            parar();
                        }
                    }

                    parar();
                    linhaDeExecucaoAntiga = linhaDeExecucao;
                    linhaDeExecucao = 10;
                    RunnablePrimLazy.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimLazy.isExecutar());
                    Thread.sleep(velocidade);
                    parar();

                }

            }
            parar();
            linhaDeExecucaoAntiga = linhaDeExecucao;
            linhaDeExecucao = 10;
            RunnablePrimLazy.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimLazy.isExecutar());
            Thread.sleep(velocidade);
            parar();
            
            parar();
            linhaDeExecucaoAntiga = linhaDeExecucao;
            linhaDeExecucao = 17;
            RunnablePrimLazy.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimLazy.isExecutar());
            Thread.sleep(velocidade);
            parar();
            
            parar();
            linhaDeExecucaoAntiga = linhaDeExecucao;
            linhaDeExecucao = 18;
            RunnablePrimLazy.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimLazy.isExecutar());
            Thread.sleep(velocidade);
            parar();
        } 
        if(checa(G)){
            parar();
            RunnablePrimLazy.showSuccess(this.runnablePrimLazy.isExecutar());
            Thread.sleep(velocidade);
            parar();
        }else{
            parar();
            RunnablePrimLazy.showFailure(this.runnablePrimLazy.isExecutar());
            Thread.sleep(velocidade);
            parar();
        }
    }
    
    private void visita(GrafoPonderado G, int v) {
        ver = v;
        marcado[v] = true;
        listMarcado.get(v).setMarcado(String.valueOf(marcado[v]).toUpperCase()); // essa lista é utilizada na tableviewMarcado
        for (Aresta a : G.adj(v)) {
            if (!marcado[a.outroVerticeInt(v)]) {
                pq.insere(a);
            }
        }
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

        for(int i = 0; i < marcado.length; i++){
            if(marcado[i] ==  true) continue;
            return false;
        }
        
//        // checa peso total
//        double total = 0.0;
//        for (Aresta a : arestas()) {
//            total += a.getPeso();
//        }
//        if (Math.abs(total - peso()) > FLOATING_POINT_EPSILON) {
//            System.err.printf("Peso das arestas não é igual peso(): %f vs. %f\n", total, peso());
//            return false;
//        }
//
//        // checa que é acíclico
        UF uf = new UF(G.V());
        for (Aresta a : arestas()) {
            int v1 = a.umVertice().getId(), v2 = a.outroVertice(a.umVertice()).getId();
            if (uf.conectado(v1, v2)) {
                System.err.println("Não é floresta");
                return false;
            }
            uf.junta(v1, v2);
        }
//
//        // chega que é uma árvores geradora
//        for (Aresta a : G.arestas()) {
//            int v1 = a.umVertice().getId(), v2 = a.outroVertice(a.umVertice()).getId();
//            if (!uf.conectado(v1, v2)) {
//                System.err.println("Não é uma árvore geradora mínima");
//                return false;
//            }
//        }
//
//        // checa que é uma árvore geradora mínima ( cortar condições de otimização )
//        for (Aresta a : arestas()) {
//
//            // todas arestas na árvores geradora mínima (MST) exceto 'a'
//            uf = new UF(G.V());
//            for (Aresta f : mst) {
//                int x = f.umVertice().getId(), y = f.outroVertice(f.umVertice()).getId();
//                if (f != a) {
//                    uf.junta(x, y);
//                }
//            }
//
//            // checa que é aresta de peso min em corte de cruzamento
//            for (Aresta f : G.arestas()) {
//                int x = f.umVertice().getId(), y = f.outroVertice(f.umVertice()).getId();
//                if (!uf.conectado(x, y)) {
//                    if (f.getPeso() < a.getPeso()) {
//                        System.err.println("Aresta " + f + " viola as condições de corte de optimização");
//                        return false;
//                    }
//                }
//            }
//
//        }

        return true;
    }

    /**
     *
     * @throws InterruptedException excepition para o wait
     */
    //método para parar a aplicação (botão de pause)
    public void parar() throws InterruptedException {
        if (FXMLExplorarComPrimLazyController.pausado == 1) {
            synchronized (main) {
                main.wait();
            }
        }
    }

//    /**
//     * Testa a classe AlgoritmoMSTPrimLazy
//     */
//    public static void main(String[] args) {
//        /*
//        In in = new In(args[0]);
//        GrafoPonderado G = new GrafoPonderado(in);
//        AlgoritmoMSTPrimLazy primLazy = new AlgoritmoMSTPrimLazy(G);
//        for (Aresta a : primLazy.arestas()) {
//            System.out.println(a);
//        }
//        System.out.printf("%.5f\n", primLazy.peso());
//         */
//    }
}

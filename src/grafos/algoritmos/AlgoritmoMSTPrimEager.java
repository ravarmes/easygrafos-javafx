
/** *****************************************************************************
 *  Compilação:       javac AlgoritmoMSTPrimEager.java
 *  Execução:         java  AlgoritmoMSTPrimEager dados.txt
 *  Dependências:     GrafoPonderado.java Aresta.java Fila.java
 *                    UF.java In.java
 *  Arquivo de dados: GrafoPonderado1.txt
 *  Link dos dados:   https://drive.google.com/open?id=0B3q56TwNCeXoenFyMnlzX2ZyXzg
 *
 *  Calcula a árvore geradora mínima (MTS) utilizando o algoritmo de Prim Eager.
 *
 *  %  java AlgoritmoMSTPrimEager GrafoPonderado1.txt
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


import grafos.controller.FXMLExplorarComPrimEagerController;
import grafos.model.Aresta;
import grafos.model.Fila;
import grafos.model.FilaMSTPrimEager;
import grafos.model.FilaPrioridadeMinIndex;
import grafos.model.GrafoPonderado;
import grafos.model.PrimEagerItemTablewViewMST;
import grafos.model.PrimEagerItemTablewViewPQ;
import grafos.model.PrimMarcado;
import grafos.model.UF;
import grafos.runnable.RunnablePrimEager;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;

/**
 * Esta classe implementa a geração da árvore geradora mínima utilizando o
 * algoritmo de PrimEagerItemTablewViewMST. Para documentação adicional, acesse:
 * <a href="http://algs4.cs.princeton.edu/43mst">Section 4.3</a>
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 */
public class AlgoritmoMSTPrimEager {

    private static final double FLOATING_POINT_EPSILON = 1E-12;

    public static int velocidade = 750;  //Velocidade inicial de execução
    private double peso;                            // peso da árvore geradora mínima (MST)
    private FilaMSTPrimEager<Aresta> mst = new FilaMSTPrimEager<Aresta>();  // arestas na árvore geradora mínima (MST)

    //Marcadores da linha do algoritmo que estava colorida e que vai ser colorida
    public int linhaDeExecucao;
    public int linhaDeExecucaoAntiga;

    //Elementos gráficos que são recebidos pelo construtor e passados para a runnable
    public TableView tableViewParametros;           // tabela com os valores das variaves do codigo
    public ListView listViewCodigo;                 // lista com o codigo que será colorido
    public Fila<Integer> VerticesNaoExplorados;     // representa a fila com a ordem de exploração dos vértices
    public List<Integer> fila;                      // auxiliar da fila
    private RunnablePrimEager runnablePrimEager;        // Objeto do tipo PrimEager, será utilizado para verificar se a Thread que a utiliza ainda esta ativa

    public Thread main; //Objeto do tipo Thread uilizado para receber a instancia da JavaFX Application Thread (Ela é utilizada para pausar a aplicação) 

    public List<PrimEagerItemTablewViewMST> listPrimEagerItemTablewViewMST = new ArrayList<PrimEagerItemTablewViewMST>();     // lista que preenche a tableViewParametros
    public PrimEagerItemTablewViewMST itemTablewViewMST;
    
    public PrimEagerItemTablewViewPQ itemPQ;
    // fila que preenche a tableViewParametros
    public FilaPrioridadeMinIndex<PrimEagerItemTablewViewPQ> filaPrimEagerItemTablewViewPQ;

    private Aresta[] arestaPara;
    private double[] distanciaPara;
    
    //FilaPrioridadeMin<Double> pq;
    
    FilaPrioridadeMinIndex<Double> pq;
    
    boolean[] marcado;
    List<PrimMarcado> listMarcado;
    PrimMarcado m;
    
    private Integer primeiro = 0; // para verificar se é o primeiro vértice visitado
    private Integer antigo; // recebe o vértice antigo para auxiliar ao colorir a aresta
    private Integer proximo; // vai receber o vertice que será visitado para colorir a aresta
    private List<Integer> listIdArestasMST = new ArrayList<>(); // recebe os ids das arestas pintadas de verde
    /**
     * Calcula a árvore geradora mínima do grafo ponderado.
     *
     * @param G grafo criado a partir do documento txt
     * @param runnablePrimEager instância da runnablePrimEager, utilizada para
     * verificar se a Thread que a utiliza ainda esta ativa (Ela fica desativada
     * quando o usuário clica em voltar na tela)
     * @param main instancia da JavaFX Application Thread
     * @throws InterruptedException exception do sleep
     */
    public AlgoritmoMSTPrimEager(GrafoPonderado G, RunnablePrimEager runnablePrimEager, Thread main, int v0) throws InterruptedException {

        System.out.println("\n\nAlgoritmoMSTPrimEager");

        //iniciando os atributos da classe
        this.runnablePrimEager = runnablePrimEager;
        this.main = main;

        if (this.runnablePrimEager.isExecutar()) {

            RunnablePrimEager.exibirVariaveis("Vértice 1:   --" + "\n" + "Vértice 2:   --" + "\n" + "ArestaPara:   --" + "\n" + "DistPara:   --", true);
            parar();

            parar();
            linhaDeExecucaoAntiga = 0;
            linhaDeExecucao = 6;
            RunnablePrimEager.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimEager.isExecutar());
            Thread.sleep(velocidade);
            parar();
            
            parar();
            linhaDeExecucaoAntiga = linhaDeExecucao;
            linhaDeExecucao = 7;
            RunnablePrimEager.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimEager.isExecutar());
            arestaPara = new Aresta[G.V()];
            Thread.sleep(velocidade);
            parar();
            
            // era para estar mais abaixo, mas como decidi setar os vertices de uma vez, trouxe aqui pra cima
            listMarcado = new ArrayList<PrimMarcado>();
            
            for(int i = 0; i < arestaPara.length; i++){
                itemTablewViewMST = new PrimEagerItemTablewViewMST(i, "   -   ");
                listPrimEagerItemTablewViewMST.add(itemTablewViewMST);
                m = new PrimMarcado();
                m.setVertice(i);
                listMarcado.add(m);
            }
            
            parar();
            RunnablePrimEager.alterarTablewMarcado(listMarcado, this.runnablePrimEager.isExecutar());
            RunnablePrimEager.alterarTablewViewMST(listPrimEagerItemTablewViewMST, this.runnablePrimEager.isExecutar());
            Thread.sleep(velocidade);
            parar();
            
            parar();
            linhaDeExecucaoAntiga = linhaDeExecucao;
            linhaDeExecucao = 8;
            RunnablePrimEager.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimEager.isExecutar());
            distanciaPara = new double[G.V()];
            Thread.sleep(velocidade);
            parar();
            
            for(int i = 0; i < distanciaPara.length; i++){// nesse caso não tem problema utilizar o i para setar os valores, já que nesse momento todos tem peso 0
                listPrimEagerItemTablewViewMST.get(i).setDistPara(distanciaPara[i]);
            }
            
                
            parar();
            RunnablePrimEager.alterarTablewViewMST(listPrimEagerItemTablewViewMST, this.runnablePrimEager.isExecutar());
            Thread.sleep(velocidade);
            parar();
            
            parar();
            linhaDeExecucaoAntiga = linhaDeExecucao;
            linhaDeExecucao = 9;
            RunnablePrimEager.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimEager.isExecutar());
            marcado = new boolean[G.V()];
            // eu iria inicialiar a listMarcado aqui, mas mudei
            Thread.sleep(velocidade);
            parar();
            
            // para preenceher a tableviewMaracado - inicialmete FALSE
            for (int i = 0; i < marcado.length; i++) {
                listMarcado.get(i).setMarcado(String.valueOf(marcado[i]).toUpperCase());
            }

            //preenche a tableviewMaracado - inicialmete todos terão o status FALSE
            parar();
            RunnablePrimEager.alterarTablewMarcado(listMarcado, this.runnablePrimEager.isExecutar());
            Thread.sleep(velocidade);
            parar();
            
            parar();
            linhaDeExecucaoAntiga = linhaDeExecucao;
            linhaDeExecucao = 10;
            RunnablePrimEager.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimEager.isExecutar());
            pq = new FilaPrioridadeMinIndex<Double>(G.V());
            filaPrimEagerItemTablewViewPQ = new FilaPrioridadeMinIndex<>(G.V());
            Thread.sleep(velocidade);
            parar();
            
            parar();
            linhaDeExecucaoAntiga = linhaDeExecucao;
            linhaDeExecucao = 11;
            RunnablePrimEager.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimEager.isExecutar());
            Thread.sleep(velocidade);
            parar();
            
            for(int v = 0; v < G.V(); v++) {
                
                parar();
                linhaDeExecucaoAntiga = linhaDeExecucao;
                linhaDeExecucao = 12;
                RunnablePrimEager.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimEager.isExecutar());
                Thread.sleep(velocidade);
                parar();
                
                distanciaPara[v] = Double.POSITIVE_INFINITY;
                
                parar();
                RunnablePrimEager.exibirVariaveis("Vértice 1:   " + v + "\n" + "Vértice 2:   --" + "\n" + 
                        "ArestaPara:   --" + "\n" + "DistPara:   " + String.valueOf(distanciaPara[v]).toUpperCase(), true);
                Thread.sleep(velocidade);
                parar();
                
                listPrimEagerItemTablewViewMST.get(v).setDistPara(distanciaPara[v]);
                
                parar();
                RunnablePrimEager.alterarTablewViewMST(listPrimEagerItemTablewViewMST, this.runnablePrimEager.isExecutar());
                Thread.sleep(velocidade);
                parar();
                
                parar();
                linhaDeExecucaoAntiga = linhaDeExecucao;
                linhaDeExecucao = 11;
                RunnablePrimEager.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimEager.isExecutar());
                Thread.sleep(velocidade);
                parar();
            }
            
            parar();
            linhaDeExecucaoAntiga = linhaDeExecucao;
            linhaDeExecucao = 14;
            RunnablePrimEager.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimEager.isExecutar());
            Thread.sleep(velocidade);
            parar();
            
            distanciaPara[v0] = 0.0;
            
            listPrimEagerItemTablewViewMST.get(v0).setDistPara(distanciaPara[v0]);
            
            parar();
            RunnablePrimEager.alterarTablewViewMST(listPrimEagerItemTablewViewMST, this.runnablePrimEager.isExecutar());
            Thread.sleep(velocidade);
            parar();
            
            
            parar();
            linhaDeExecucaoAntiga = linhaDeExecucao;
            linhaDeExecucao = 15;
            RunnablePrimEager.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimEager.isExecutar());
            Thread.sleep(velocidade);
            parar();
            
            parar();
            RunnablePrimEager.exibirVariaveis("Vértice 1:   " + v0 + "\n" + "Vértice 2:   --" + "\n" + 
                        "ArestaPara:   --" + "\n" + "DistPara:   " + String.valueOf(distanciaPara[0]).toUpperCase(), true);
            Thread.sleep(velocidade);
            parar();
            
            pq.insere(v0, distanciaPara[v0]);
            itemPQ = new PrimEagerItemTablewViewPQ(v0, distanciaPara[v0]);
            filaPrimEagerItemTablewViewPQ.insere(v0, itemPQ);
            
            parar();
            RunnablePrimEager.alterarTablewViewPQ(filaPrimEagerItemTablewViewPQ, this.runnablePrimEager.isExecutar());
            Thread.sleep(velocidade);
            parar();
            
            parar();
            linhaDeExecucaoAntiga = linhaDeExecucao;
            linhaDeExecucao = 16;
            RunnablePrimEager.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimEager.isExecutar());
            Thread.sleep(velocidade);
            parar();
            
            while(!pq.isEmpty()) {
                
                parar();
                linhaDeExecucaoAntiga = linhaDeExecucao;
                linhaDeExecucao = 17;
                RunnablePrimEager.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimEager.isExecutar());
                Thread.sleep(velocidade);
                parar();
                
                if(!primeiro.equals(0)){
                    antigo = filaPrimEagerItemTablewViewPQ.minChave().getOutroVertice();
                }
                
                int v = pq.removeMin();
                proximo = new Integer(v);
                
                filaPrimEagerItemTablewViewPQ.remove(v);
                
                parar();
                RunnablePrimEager.alterarTablewViewPQ(filaPrimEagerItemTablewViewPQ, this.runnablePrimEager.isExecutar());
                Thread.sleep(velocidade);
                parar();
                
                parar();
                linhaDeExecucaoAntiga = linhaDeExecucao;
                linhaDeExecucao = 18;
                RunnablePrimEager.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimEager.isExecutar());
                Thread.sleep(velocidade);
                parar();
                
                //preferi não usar um método visita aqui no eager
                //visita(G, v);
                
                parar();
                linhaDeExecucaoAntiga = linhaDeExecucao;
                linhaDeExecucao = 21;
                RunnablePrimEager.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimEager.isExecutar());
                Thread.sleep(velocidade);
                parar();
                
                parar();
                linhaDeExecucaoAntiga = linhaDeExecucao;
                linhaDeExecucao = 22;
                RunnablePrimEager.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimEager.isExecutar());
                Thread.sleep(velocidade);
                parar();
                
                if(!primeiro.equals(0)){
                   RunnablePrimEager.colorirAresta(antigo, proximo, 60, 195, 100, this.runnablePrimEager.isExecutar(), 0, 0, 0); 
                   for(Aresta a : G.adj(v)){
                       if((a.getV1().getId() == antigo && a.getV2().getId() == proximo) || (a.getV1().getId() == proximo && a.getV2().getId() == antigo)){
                           a.setUtilizada(true); 
                           listIdArestasMST.add(a.getId());
                           break;
                       }
                   }
                }else{// só entra 1 vez nesse else
                    primeiro = 1;
                    antigo = new Integer(v);
                }
                
                marcado[v] = true;
                
                // mudar aqui tbm
                listMarcado.get(v).setMarcado(String.valueOf(marcado[v]).toUpperCase());
                
                
                parar();
                RunnablePrimEager.alterarTablewMarcado(listMarcado, this.runnablePrimEager.isExecutar());
                RunnablePrimEager.exibirVariaveis("Vértice 1:   " + v + "\n" + "Vértice 2:   --" + "\n" + 
                        "ArestaPara:   --" + "\n" + "DistPara:   --", true);
                RunnablePrimEager.colorirVertice(v, 60, 195, 100, this.runnablePrimEager.isExecutar());
                Thread.sleep(velocidade);
                parar();
                
                parar();
                linhaDeExecucaoAntiga = linhaDeExecucao;
                linhaDeExecucao = 23;
                RunnablePrimEager.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimEager.isExecutar());
                Thread.sleep(velocidade);
                parar();
                
                for(Aresta a : G.adj(v)) {
                    
                    parar();
                    linhaDeExecucaoAntiga = linhaDeExecucao;
                    linhaDeExecucao = 24;
                    RunnablePrimEager.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimEager.isExecutar());
                    Thread.sleep(velocidade);
                    parar();
                    
                    int v2 = a.outroVerticeInt(v);
                    
                    parar();
                    RunnablePrimEager.exibirVariaveis("Vértice 1:   " + v + "\n" + "Vértice 2:   " + v2 + "\n" + "ArestaPara:   " + 
                            v + " - " + v2 + "\n" + "DistPara:   " + a.getPeso() , true);
                    Thread.sleep(velocidade);
                    parar();
                    
                    parar();
                    linhaDeExecucaoAntiga = linhaDeExecucao;
                    linhaDeExecucao = 25;
                    RunnablePrimEager.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimEager.isExecutar());
                    Thread.sleep(velocidade);
                    parar();
                    
                    if(marcado[v2]) {
                        if(!a.isUtilizada()){
                            RunnablePrimEager.colorirAresta(v, v2, 211, 211, 211, this.runnablePrimEager.isExecutar(), 0, 0, 0);
                            a.setUtilizada(true);
                        }
                        continue;
                    }
                    
                    parar();
                    linhaDeExecucaoAntiga = linhaDeExecucao;
                    linhaDeExecucao = 26;
                    RunnablePrimEager.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimEager.isExecutar());
                    Thread.sleep(velocidade);
                    parar();
                    
                    if(a.getPeso() < distanciaPara[v2]) {
                        int pos = -1;
                        for(PrimEagerItemTablewViewMST item : listPrimEagerItemTablewViewMST){
                            pos++;
                            if(item.getV().equals(v2)) break;
                        }
                        
                        parar();
                        linhaDeExecucaoAntiga = linhaDeExecucao;
                        linhaDeExecucao = 27;
                        RunnablePrimEager.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimEager.isExecutar());
                        Thread.sleep(velocidade);
                        parar();
                        
                        distanciaPara[v2] = a.getPeso();
                        
                        listPrimEagerItemTablewViewMST.get(pos).setDistPara(distanciaPara[v2]);
                        
//                        parar();
//                        RunnablePrimEager.exibirVariaveis("Vértice 1:   " + v + "\n" + "Vértice 2:   " + v2 + "\n" + "ArestaPara:   --" + 
//                            "\n" + "DistPara:   " + String.valueOf(distanciaPara[v2]).toUpperCase() , true);
//                        RunnablePrimEager.alterarTablewViewMST(listPrimEagerItemTablewViewMST, this.runnablePrimEager.isExecutar());
//                        Thread.sleep(velocidade);
//                        parar();
                        
                        parar();
                        linhaDeExecucaoAntiga = linhaDeExecucao;
                        linhaDeExecucao = 28;
                        RunnablePrimEager.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimEager.isExecutar());
                        Thread.sleep(velocidade);
                        parar();
                        
                        Aresta aresta = new Aresta(null, null, -1);
                        if(arestaPara[v2] != null){// se existir uma aresta para o vertice v2 dentro de arestaPara
                            // antes de substituir pego a aresta
                            aresta = new Aresta(arestaPara[v2].getV1(), arestaPara[v2].getV2(), arestaPara[v2].getId(), arestaPara[v2].getPeso());
                            aresta.setUtilizada(arestaPara[v2].isUtilizada());
                        }
                        
                        
                        arestaPara[v2] = a;
                        
                        listPrimEagerItemTablewViewMST.get(pos).setArestaPara(arestaPara[v2].getV1().getId() + " - " + arestaPara[v2].getV2().getId());
                        
                        parar();
                        RunnablePrimEager.exibirVariaveis("Vértice 1:   " + v + "\n" + "Vértice 2:   " + v2 + "\n" + "ArestaPara:   " + 
                            arestaPara[v2].getV1().getId() + " - " + arestaPara[v2].getV2().getId() + "\n" + "DistPara:   " + 
                                String.valueOf(distanciaPara[v2]).toUpperCase() , true);
                        RunnablePrimEager.alterarTablewViewMST(listPrimEagerItemTablewViewMST, this.runnablePrimEager.isExecutar());
                        Thread.sleep(velocidade);
                        parar();
                        
                        parar();
                        linhaDeExecucaoAntiga = linhaDeExecucao;
                        linhaDeExecucao = 29;
                        RunnablePrimEager.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimEager.isExecutar());
                        Thread.sleep(velocidade);
                        parar();
                        
                        if(pq.contem(v2)) {
                            parar();
                            linhaDeExecucaoAntiga = linhaDeExecucao;
                            linhaDeExecucao = 30;
                            RunnablePrimEager.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimEager.isExecutar());
                            Thread.sleep(velocidade);
                            parar();
                            
                            antigo = filaPrimEagerItemTablewViewPQ.chaveDe(v2).getOutroVertice();
                            
                            pq.diminuiChave(v2, distanciaPara[v2]);
                            
                            itemPQ = new PrimEagerItemTablewViewPQ(v2, distanciaPara[v2], v);
                            filaPrimEagerItemTablewViewPQ.diminuiChave(v2, itemPQ);
                            
                            parar();
                            RunnablePrimEager.alterarTablewViewPQ(filaPrimEagerItemTablewViewPQ, this.runnablePrimEager.isExecutar());
                            Thread.sleep(velocidade);
                            parar();
                            
                            if(!aresta.getV1().equals(null) && !aresta.getV2().equals(null) && !aresta.isUtilizada()){
                                for(Aresta ar : G.adj(v2)){
                                    if(ar.outroVerticeInt(v2) != aresta.getV1().getId() && ar.outroVerticeInt(v2) != aresta.getV2().getId()) 
                                        continue;
                                    RunnablePrimEager.colorirAresta(aresta.getV1().getId(), aresta.getV2().getId(), 211, 211, 211, this.runnablePrimEager.isExecutar(), 0, 0, 0);
                                    ar.setUtilizada(true);
                                    break;
                                }
                            }
                            
                            
                            parar();
                            linhaDeExecucaoAntiga = linhaDeExecucao;
                            linhaDeExecucao = 31;
                            RunnablePrimEager.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimEager.isExecutar());
                            Thread.sleep(velocidade);
                            parar();
                            
                        }else{
                            parar();
                            linhaDeExecucaoAntiga = linhaDeExecucao;
                            linhaDeExecucao = 32;
                            RunnablePrimEager.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimEager.isExecutar());
                            Thread.sleep(velocidade);
                            parar();
                            
                            pq.insere(v2, distanciaPara[v2]);
                            
                            itemPQ = new PrimEagerItemTablewViewPQ(v2, distanciaPara[v2], v);
                            filaPrimEagerItemTablewViewPQ.insere(v2, itemPQ);
                            
                            parar();
                            RunnablePrimEager.alterarTablewViewPQ(filaPrimEagerItemTablewViewPQ, this.runnablePrimEager.isExecutar());
                            Thread.sleep(velocidade);
                            parar();

                        }
                    }
                    
                    parar();
                    linhaDeExecucaoAntiga = linhaDeExecucao;
                    linhaDeExecucao = 23;
                    RunnablePrimEager.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimEager.isExecutar());
                    Thread.sleep(velocidade);
                    parar();
                }
                parar();
                linhaDeExecucaoAntiga = linhaDeExecucao;
                linhaDeExecucao = 16;
                RunnablePrimEager.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimEager.isExecutar());
                Thread.sleep(velocidade);
                parar();
            }
            
            parar();
            linhaDeExecucaoAntiga = linhaDeExecucao;
            linhaDeExecucao = 19;
            RunnablePrimEager.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimEager.isExecutar());
            Thread.sleep(velocidade);
            parar();
                
            parar();
            linhaDeExecucaoAntiga = linhaDeExecucao;
            linhaDeExecucao = 20;
            RunnablePrimEager.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnablePrimEager.isExecutar());
            Thread.sleep(velocidade);
            parar();
        }
        // como eu seto todas as arestas como utilizada ao final da execução todas ficam verde
        for(Aresta a : G.arestas()){
            if(listIdArestasMST.contains(a.getId())) continue;
            a.setUtilizada(false);
        }
        
        if(checa(G)){
            parar();
            RunnablePrimEager.showSuccess(this.runnablePrimEager.isExecutar());
            Thread.sleep(velocidade);
            parar();
        }else{
            parar();
            RunnablePrimEager.showFailure(this.runnablePrimEager.isExecutar());
            Thread.sleep(velocidade);
            parar();
        }
    }

//    private void visita(GrafoPonderado G, int v) {
//        marcado[v] = true;
//        for(Aresta a : G.adj(v)) {
//            int v2 = a.outroVerticeInt(v);
//            if(marcado[v2]) continue;
//            if(a.getPeso() < distanciaPara[v2]) {
//                distanciaPara[v2] = a.getPeso();
//                arestaPara[v2] = a;
//                if(pq.contem(v2)) {
//                    pq.diminuiChave(v2, distanciaPara[v2]);
//                }else {
//                    pq.insere(v2, distanciaPara[v2]);
//                }
//            }
//        }
//    }
    
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
                //System.err.println("Não é floresta");
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
        if (FXMLExplorarComPrimEagerController.pausado == 1) {
            synchronized (main) {
                main.wait();
            }
        }
    }

    /**
     * Testa a classe AlgoritmoMSTPrimEager
     */
    public static void main(String[] args) {
        /*
        In in = new In(args[0]);
        GrafoPonderado G = new GrafoPonderado(in);
        AlgoritmoMSTPrimEager primEager = new AlgoritmoMSTPrimEager(G);
        for (Aresta a : primEager.arestas()) {
            System.out.println(a);
        }
        System.out.printf("%.5f\n", primEager.peso());
         */
    }

}

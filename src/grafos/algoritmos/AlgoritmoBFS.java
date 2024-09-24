
package grafos.algoritmos;

import grafos.controller.FXMLExplorarComBFSController;
import grafos.model.Aresta;
import grafos.model.BFS;
import grafos.model.Fila;
import grafos.model.Grafo;
import grafos.model.Vertice;
import grafos.runnable.RunnableBFS;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;

/**
 *
 * @author Jonathas
 */

//Nesta classe o algoritmo BFS é realmente executado, e depois de cada linha algumas ações devem ser realizadas
// -Chamar os métodos responsáveis pos modificar os elementos gráficos
// -Verificar se o botão de pause foi acionado
// -alterar qual linha esta sendo executada, para colorir o list com o código (linhaDeExecucao; linhaDeExecucaoAntiga;)
public class AlgoritmoBFS {
 
    
    public static int velocidade = 2000;  //Velocidade inicial de execução
    private String[] marcado;             //vetores do código do algoritmo BFS
    private String[] arestaPara;          //vetores do código do algoritmo BFS
    private int[] distanciaPara;          //vetores do código do algoritmo BFS
    public BFS bfs;                       //Objeto utilizado para preencher a lista que é utilizada na formação da tableView
    
    
    //Marcadores da linha do algoritmo que estava colorida e que vai ser colorida
    public int linhaDeExecucao;
    public int linhaDeExecucaoAntiga;
    
 
    //Elementos gráficos que são recebidos pelo construtor e passados para a runnable
    public TableView tableViewParametros;           // tabela com os valores das variaves do codigo
    public ListView listViewCodigo;                 // lista com o codigo que será colorido
    public List<BFS> listBFSParametros;             // lista que preenche a tableViewParametros
    public Fila<Integer> VerticesNaoExplorados;     // representa a fila com a ordem de exploração dos vértices
    public List<Integer> fila;                      // auxiliar da fila
    private RunnableBFS runnableBFS;                //Objeto do tipo RunnableBFS, será utilizado para verificar se a Thread que a utiliza ainda esta ativa
    
    
    public Thread main; //Objeto do tipo Thread uilizado para receber a instancia da JavaFX Application Thread (Ela é utilizada para pausar a aplicação) 

    
    /**
     * 
     * @param G             grafo criado a partir do documento txt
     * @param vo            vértice de origem para execução do algoritmo
     * @param runnableBFS   instância da runnableBFS, utilizada para verificar se a Thread que a utiliza ainda esta ativa (Ela fica desativada quando o usuário clica em voltar na tela)
     * @param main          instancia da JavaFX Application Thread 
     * @throws              InterruptedException exception do sleep
     */
    public AlgoritmoBFS(Grafo G, int vo, RunnableBFS runnableBFS, Thread main) throws InterruptedException {
        
        //iniciando os atributos da classe
        this.runnableBFS = runnableBFS;
        this.main = main;
        
        //a verificação "if this.runnable.isexecutar()" verifica se a thread ainda esta ativa/util
        if (this.runnableBFS.isExecutar()) {
            parar();
            linhaDeExecucao = 7;
            linhaDeExecucaoAntiga = 0;
            RunnableBFS.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnableBFS.isExecutar());

            Thread.sleep(velocidade);

            parar();
            arestaPara = new String[G.V()];
            
            parar();
            linhaDeExecucaoAntiga = linhaDeExecucao;
            linhaDeExecucao = 8;
            RunnableBFS.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnableBFS.isExecutar());

            Thread.sleep(velocidade);

            parar();
            marcado = new String[G.V()];
            
            parar();
            linhaDeExecucaoAntiga = linhaDeExecucao;
            linhaDeExecucao = 9;
            RunnableBFS.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnableBFS.isExecutar());

            Thread.sleep(velocidade);

            parar();
            distanciaPara = new int[G.V()];
            
            parar();
            linhaDeExecucaoAntiga = linhaDeExecucao;
            linhaDeExecucao = 10;
            RunnableBFS.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnableBFS.isExecutar());
            Thread.sleep(velocidade);

            listBFSParametros = new ArrayList();
            fila = new ArrayList();

            for (int i = 0; i < G.V(); i++) {
                arestaPara[i] = "--";
                marcado[i] = "FALSE";
                distanciaPara[i] = -1;

            }

            for (int i = 0; i < G.V(); i++) {
                bfs = new BFS();
                bfs.setArestaPara(arestaPara[i]);
                bfs.setItemMarcado(marcado[i]);
                bfs.setDistanciaPara(distanciaPara[i]);

                bfs.setId(i);
                listBFSParametros.add(bfs);

            }

            parar();
            RunnableBFS.iniciarTabela(listBFSParametros, this.runnableBFS.isExecutar());

            parar();
            linhaDeExecucaoAntiga = linhaDeExecucao;
            linhaDeExecucao = 11;
            RunnableBFS.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnableBFS.isExecutar());
            
            parar();
            RunnableBFS.colorirVertice(vo, 60, 195, 100, this.runnableBFS.isExecutar());
            Thread.sleep(velocidade);
            
            parar();
            bfs(G, vo);
            
            RunnableBFS.exibirVerticeExplorando("Grafo pronto!", this.runnableBFS.isExecutar());
        }

    }

    /**
     * 
     * @param G     grafo criado a partir do documento txt
     * @param vo    vértice de origem para execução do algoritmo
     * @throws      InterruptedException exception do sleep
     */
    private void bfs(Grafo G, int vo) throws InterruptedException {

        if (this.runnableBFS.isExecutar()) {

            RunnableBFS.exibirVariaveis("Variável  v:   --" + "\n" + "Variável  x:   --", true);
            parar();
            linhaDeExecucaoAntiga = linhaDeExecucao;
            linhaDeExecucao = 14;
            RunnableBFS.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnableBFS.isExecutar());
            Thread.sleep(velocidade);

            parar();
            RunnableBFS.exibirVerticeExplorando("Explorando vértice " + vo, this.runnableBFS.isExecutar());

            parar();
            linhaDeExecucaoAntiga = linhaDeExecucao;
            linhaDeExecucao = 15;
            RunnableBFS.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnableBFS.isExecutar());
            Thread.sleep(velocidade);
            VerticesNaoExplorados = new Fila<Integer>();

            parar();
            linhaDeExecucaoAntiga = linhaDeExecucao;
            linhaDeExecucao = 16;
            RunnableBFS.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnableBFS.isExecutar());

            parar();
            VerticesNaoExplorados.enfileira(vo);
            fila.add(vo);
            RunnableBFS.adicionarFila(fila, this.runnableBFS.isExecutar());
            Thread.sleep(velocidade);

            parar();
            marcado[vo] = "TRUE";
            linhaDeExecucaoAntiga = linhaDeExecucao;
            linhaDeExecucao = 17;
            RunnableBFS.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnableBFS.isExecutar());
            
            parar();
            atualizarVariavel(arestaPara[vo], marcado[vo], vo, distanciaPara[vo]);
            RunnableBFS.alterarTabela(listBFSParametros, this.runnableBFS.isExecutar());
            Thread.sleep(velocidade);

            parar();
            distanciaPara[vo] = 0;
            linhaDeExecucaoAntiga = linhaDeExecucao;
            linhaDeExecucao = 18;
            RunnableBFS.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnableBFS.isExecutar());
            
            parar();
            atualizarVariavel(arestaPara[vo], marcado[vo], vo, distanciaPara[vo]);
            RunnableBFS.alterarTabela(listBFSParametros, this.runnableBFS.isExecutar());
            Thread.sleep(velocidade);

            parar();
            while (!VerticesNaoExplorados.isEmpty() && this.runnableBFS.isExecutar()) {
                
                parar();
                linhaDeExecucaoAntiga = linhaDeExecucao;
                linhaDeExecucao = 19;
                RunnableBFS.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnableBFS.isExecutar());
                Thread.sleep(velocidade);

                parar();
                linhaDeExecucaoAntiga = linhaDeExecucao;
                linhaDeExecucao = 20;
                RunnableBFS.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnableBFS.isExecutar());

                parar();
                int v = VerticesNaoExplorados.desenfileira();
                RunnableBFS.exibirVariaveis("Variável  v:   " + v + "\n" + "Variável  x:   --", true);
                
                parar();
                RunnableBFS.colorirVertice(v, 60, 195, 100, this.runnableBFS.isExecutar());
                
                parar();
                RunnableBFS.removerFila(fila, this.runnableBFS.isExecutar());
                
                parar();
                RunnableBFS.exibirVerticeExplorando("Explorando vértice " + v, this.runnableBFS.isExecutar());
                Thread.sleep(velocidade);

                parar();
                atualizarVariavel(arestaPara[v], marcado[v], v, distanciaPara[v]);
                RunnableBFS.alterarTabela(listBFSParametros, this.runnableBFS.isExecutar());

                parar();
                for (Aresta a : G.adj(v)) {
                    if (!this.runnableBFS.isExecutar()) {
                        break;
                    } else {
                        parar();
                        linhaDeExecucaoAntiga = linhaDeExecucao;
                        linhaDeExecucao = 21;
                        RunnableBFS.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnableBFS.isExecutar());
                        Thread.sleep(velocidade);

                        parar();
                        linhaDeExecucaoAntiga = linhaDeExecucao;
                        linhaDeExecucao = 22;
                        RunnableBFS.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnableBFS.isExecutar());
                        Thread.sleep(velocidade);
                        Vertice x = a.getV2();
                        RunnableBFS.exibirVariaveis("Váriável  v:   " + v + "\n" + "Variável  x:   " + x.getId(), true);

                        parar();
                        linhaDeExecucaoAntiga = linhaDeExecucao;
                        linhaDeExecucao = 23;
                        RunnableBFS.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnableBFS.isExecutar());
                        Thread.sleep(velocidade);
                        
                        parar();
                        RunnableBFS.colorirAresta(v, x.getId(), 60, 195, 100, this.runnableBFS.isExecutar(), 0,0,0);
                        Thread.sleep(velocidade);
                        
                        parar();
                        if (marcado[x.getId()].equals("FALSE")) {
                            if (this.runnableBFS.isExecutar()) {
                                
                                parar();
                                a.setUtilizada(true);
                                
                                parar();
                                G.getListVertice().get(x.getId()).setUtilizado(true);

                                parar();
                                arestaPara[x.getId()] = String.valueOf(v);
                                
                                parar();
                                RunnableBFS.colorirVertice(x.getId(), 152, 251, 152, this.runnableBFS.isExecutar());
                                
                                parar();
                                linhaDeExecucaoAntiga = linhaDeExecucao;
                                linhaDeExecucao = 24;
                                RunnableBFS.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnableBFS.isExecutar());
                                
                                parar();
                                atualizarVariavel(arestaPara[x.getId()], marcado[x.getId()], x.getId(), distanciaPara[x.getId()]);
                                RunnableBFS.alterarTabela(listBFSParametros, this.runnableBFS.isExecutar());
                                Thread.sleep(velocidade);
                                
                                parar();
                                RunnableBFS.colorirAresta(v, x.getId(), 211, 211, 211, this.runnableBFS.isExecutar(), 60, 195, 100);

                                
                                parar();
                                distanciaPara[x.getId()] = distanciaPara[v] + 1;
                                atualizarVariavel(arestaPara[x.getId()], marcado[x.getId()], x.getId(), distanciaPara[x.getId()]);
                                RunnableBFS.alterarTabela(listBFSParametros, this.runnableBFS.isExecutar());
                                Thread.sleep(velocidade);
                                
                                parar();
                                linhaDeExecucaoAntiga = linhaDeExecucao;
                                linhaDeExecucao = 25;
                                RunnableBFS.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnableBFS.isExecutar());
                                Thread.sleep(velocidade);
                                
                                parar();
                                marcado[x.getId()] = "TRUE";
                                linhaDeExecucaoAntiga = linhaDeExecucao;
                                linhaDeExecucao = 26;
                                RunnableBFS.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnableBFS.isExecutar());
                                Thread.sleep(velocidade);
                                
                                parar();
                                atualizarVariavel(arestaPara[x.getId()], marcado[x.getId()], x.getId(), distanciaPara[x.getId()]);
                                RunnableBFS.alterarTabela(listBFSParametros, this.runnableBFS.isExecutar());
                                Thread.sleep(velocidade);

                                parar();
                                linhaDeExecucaoAntiga = linhaDeExecucao;
                                linhaDeExecucao = 27;
                                RunnableBFS.alterarlistCodigo(linhaDeExecucao, linhaDeExecucaoAntiga, this.runnableBFS.isExecutar());
                                Thread.sleep(velocidade);
                                
                                parar();
                                VerticesNaoExplorados.enfileira(x.getId());
                                fila.add(x.getId());
                                
                                parar();
                                RunnableBFS.adicionarFila(fila, this.runnableBFS.isExecutar());
                            }

                        } else {
                            parar();
                            RunnableBFS.colorirAresta(v, x.getId(), 211, 211, 211, this.runnableBFS.isExecutar(), 60, 195, 100);
                        }

                    }

                }
                parar();
                RunnableBFS.colorirVertice(v, 211, 211, 211, this.runnableBFS.isExecutar());
                
                parar();
                RunnableBFS.exibirVerticeExplorando("Vértice " + v + " Pronto!", this.runnableBFS.isExecutar());

            }
        }

    }

    /**
     * 
     * @param arestaPara    Novo valor para o atributo arestaPara
     * @param marcado       Novo valor para o atributo marcado
     * @param id            Id do objeto que deve ser alterado
     * @param distanciaPara Novo valor para o atributo distanciaPara
     */
    
    //Este método recebe o id do objeto do tipo BFS que será alterado na tabela. Ele é alterado na lista, para assim a alteração se refletir na tabela
    public void atualizarVariavel(String arestaPara, String marcado, int id, int distanciaPara) {

        listBFSParametros.get(id).setArestaPara(arestaPara);
        listBFSParametros.get(id).setItemMarcado(marcado);
        listBFSParametros.get(id).setDistanciaPara(distanciaPara);
        listBFSParametros.get(id).setId(id);

    }
    
    /**
     * 
     * @throws InterruptedException excepition para o wait
     */
    
    //método para parar a aplicação (botão de pause)
    public void parar() throws InterruptedException {
        if (FXMLExplorarComBFSController.pausado == 1) {
            synchronized (main) {
                main.wait();
            }
        }

    }

}

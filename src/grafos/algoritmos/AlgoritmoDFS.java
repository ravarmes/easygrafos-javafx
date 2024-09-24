
package grafos.algoritmos;

import grafos.controller.FXMLExplorarComDFSController;
import grafos.model.Aresta;
import grafos.model.DFS;
import grafos.model.Grafo;
import grafos.runnable.RunnableDFS;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;

/**
 *
 * @author Jonathas
 */
//Nesta classe o algoritmo DFS é realmente executado, e depois de cada linha algumas ações devem ser realizadas
// -Chamar os métodos responsáveis pos modificar os elementos gráficos
// -Verificar se o botão de pause foi acionado
// -alterar qual linha esta sendo executada, para colorir o list com o código (linhaDeExecucao; linhaDeExecucaoAntiga;)
public class AlgoritmoDFS {

    public static String[] marcado;      //atributo do algoritmo DFS
    public static String[] arestaPara;   //atributo do algoritmo DFS
    public static int velocidade = 2000; //velocidade inicial    
    public DFS dfs;                      //essa variavel guarda o estado atual de uma posição do vetor marcado, uma posição do vetor arestaPara e o valor de vertice origem

    
    public TableView tableViewParametros;       // tabela com os valores das variaves do codigo
    public ListView listViewCodigo;             // lista com o codigo que será colorido
    public ListView listViewpilhaDeExecucao;    // lista com a pilha de chamadas recursivas
    public List<DFS> listDFSParametros;         // list com dados para preencher a tabela
    public List<String> listRecursiva;          // lista com dados para preencher a tabela com os valores das variaves do codigo

    //Marcadores da linha do algoritmo que estava colorida e que vai ser colorida
    public int linhaEmExecucao;
    public int linhaEmExecucaoAnterior;
    
    
    public RunnableDFS runnableDFS;     //Objeto do tipo RunnableBFS, será utilizado para verificar se a Thread que a utiliza ainda esta ativa
    public Thread main;                 //Objeto do tipo Thread uilizado para receber a instancia da JavaFX Application Thread (Ela é utilizada para pausar a aplicação)
    public int arestaUtilizadaInt = 0;  //contador

    /**
     * 
     * @param grafo         grafo criado a partir do documento txt
     * @param v             vértice de origem para execução do algoritmo
     * @param runnableDFS   instância da runnableDFS, utilizada para verificar se a Thread que a utiliza ainda esta ativa (Ela fica desativada quando o usuário clica em voltar na tela)
     * @param main          instancia da JavaFX Application Thread 
     * @throws              InterruptedException exception do sleep
     */
    public AlgoritmoDFS(int v, Grafo grafo, RunnableDFS runnableDFS, Thread main) throws InterruptedException {

        
        this.main = main;
        this.runnableDFS = runnableDFS;

        if (this.runnableDFS.isExecutar()) {
            parar();
            linhaEmExecucao = 5;
            linhaEmExecucaoAnterior = 0;
            RunnableDFS.alterarlistCodigo(linhaEmExecucao, linhaEmExecucaoAnterior, this.runnableDFS.isExecutar());
            Thread.sleep(velocidade);

            dfs = new DFS();

            parar();
            arestaPara = new String[grafo.V()];
            linhaEmExecucaoAnterior = linhaEmExecucao;
            linhaEmExecucao = 6;
            RunnableDFS.alterarlistCodigo(linhaEmExecucao, linhaEmExecucaoAnterior, this.runnableDFS.isExecutar());
            Thread.sleep(velocidade);

            parar();
            marcado = new String[grafo.V()];
            linhaEmExecucaoAnterior = linhaEmExecucao;
            linhaEmExecucao = 7;
            RunnableDFS.alterarlistCodigo(linhaEmExecucao, linhaEmExecucaoAnterior, this.runnableDFS.isExecutar());
            Thread.sleep(velocidade);

            listDFSParametros = new ArrayList();
            listRecursiva = new ArrayList();

            //instanciando os valores nos vetores
            for (int i = 0; i < grafo.V(); i++) {
                arestaPara[i] = "--";
                marcado[i] = "--";

            }

            for (int i = 0; i < grafo.V(); i++) {
                dfs = new DFS();
                dfs.setArestaPara(arestaPara[i]);
                dfs.setItemMarcado(marcado[i]);
                dfs.setId(i);
                listDFSParametros.add(dfs);

            }

            parar();
            RunnableDFS.iniciarTabela(listDFSParametros, this.runnableDFS.isExecutar());
            linhaEmExecucaoAnterior = linhaEmExecucao;
            linhaEmExecucao = 8;
            RunnableDFS.alterarlistCodigo(linhaEmExecucao, linhaEmExecucaoAnterior, this.runnableDFS.isExecutar());
            Thread.sleep(velocidade);

            parar();
            RunnableDFS.colorirVertice(v, 60, 195, 100, this.runnableDFS.isExecutar());
            Thread.sleep(velocidade);

            parar();
            
            DFS(v, grafo);

            for (Aresta aresta : grafo.adj(v)) {
                if (runnableDFS.isExecutar()) {
                    RunnableDFS.colorirAresta(v, aresta.getV2().getId(), 211, 211, 211, true, 152, 251, 152);
                    parar();
                    
                }
                
            }
            
        

            parar();
            RunnableDFS.colorirVertice(v, 211, 211, 211, this.runnableDFS.isExecutar());
            

            parar();
            RunnableDFS.desempilharPilhaRecursiva(listRecursiva, this.runnableDFS.isExecutar());

            parar();
            RunnableDFS.exibirVerticeExplorando("Vértice " + v + " pronto!", this.runnableDFS.isExecutar());
            Thread.sleep(velocidade);
            
            RunnableDFS.exibirVerticeExplorando("Grafo pronto!", this.runnableDFS.isExecutar());

        }

    }

    /**
     * Método que explora o grafo utilizando o algoritmo de busca DFS
     *
     * @param v indica o id do vertice onde esta se inicializando a execução do DFS
     * @param grafo objeto do tipo grafo(com vértice e arestas...) escolhido pelo usuário para ser explorado pelo algoritmo
     * @throws java.lang.InterruptedException exception do sleep
     */
    public void DFS(int v, Grafo grafo) throws InterruptedException {

        
        if (this.runnableDFS.isExecutar()) {
            
            RunnableDFS.exibirVariaveis("Variável v:   " + v + "\n" + "Váriável x:   --", true);
            parar();
            linhaEmExecucaoAnterior = linhaEmExecucao;
            linhaEmExecucao = 11;
            RunnableDFS.alterarlistCodigo(linhaEmExecucao, linhaEmExecucaoAnterior, this.runnableDFS.isExecutar());

            parar();
            listRecursiva.add(0, "DFS(" + String.valueOf(v) + ", grafo);");
            RunnableDFS.alterarPilhaRecursiva(listRecursiva, this.runnableDFS.isExecutar());

            parar();
            RunnableDFS.exibirVerticeExplorando("Explorando vértice " + v, this.runnableDFS.isExecutar());
            Thread.sleep(velocidade);

            marcado[v] = "TRUE";
            linhaEmExecucaoAnterior = linhaEmExecucao;
            linhaEmExecucao = 12;

            parar();
            RunnableDFS.alterarlistCodigo(linhaEmExecucao, linhaEmExecucaoAnterior, this.runnableDFS.isExecutar());
            atualizarVariavel(arestaPara[v], marcado[v], v);

            parar();
            RunnableDFS.alterarTabela(listDFSParametros, this.runnableDFS.isExecutar());

            Thread.sleep(velocidade);

            for (Aresta a : grafo.adj(v)) {
                if (!this.runnableDFS.isExecutar()) {
                    break;
                } else {

                    parar();
                    linhaEmExecucaoAnterior = linhaEmExecucao;
                    linhaEmExecucao = 13;
                    RunnableDFS.alterarlistCodigo(linhaEmExecucao, linhaEmExecucaoAnterior, this.runnableDFS.isExecutar());
                    Thread.sleep(velocidade);

                    parar();
                    linhaEmExecucaoAnterior = linhaEmExecucao;
                    linhaEmExecucao = 14;
                    RunnableDFS.alterarlistCodigo(linhaEmExecucao, linhaEmExecucaoAnterior, this.runnableDFS.isExecutar());
                    Thread.sleep(velocidade);
                    int x = a.getV2().getId();
                    RunnableDFS.exibirVariaveis("Variável v:   " + v + "\n" + "Váriável x:   " + x, true);
                    
                    parar();
                    linhaEmExecucaoAnterior = linhaEmExecucao;
                    linhaEmExecucao = 15;
                    RunnableDFS.alterarlistCodigo(linhaEmExecucao, linhaEmExecucaoAnterior, this.runnableDFS.isExecutar());

                    parar();
                    arestaUtilizadaInt = 0;
                    for (Aresta arestaUtilizada : grafo.arestas()) {
                        if ((arestaUtilizada.getV1().getId() == v) && (arestaUtilizada.getV2().getId() == x) || (arestaUtilizada.getV1().getId() == x) && (arestaUtilizada.getV2().getId() == v)) {
                            System.out.println("Vertive " + grafo.arestas().size() + this.arestaUtilizadaInt);
                            if (arestaUtilizada.isUtilizada()) {
                                arestaUtilizadaInt++;
                                System.out.println(this.arestaUtilizadaInt);
                            }
                        }

                    }
                    if (this.arestaUtilizadaInt > 0) {
                        RunnableDFS.colorirAresta(v, x, 60, 195, 100, this.runnableDFS.isExecutar(), 152, 251, 152);
                        Thread.sleep(velocidade);

                    } else {

                        RunnableDFS.colorirAresta(v, x, 60, 195, 100, this.runnableDFS.isExecutar(), 0, 0, 0);
                        Thread.sleep(velocidade);
                    }

                    if (!marcado[x].equals("TRUE")) {
                        if (this.runnableDFS.isExecutar()) {
                            a.setUtilizada(true);

                            parar();
                            linhaEmExecucaoAnterior = linhaEmExecucao;
                            linhaEmExecucao = 16;
                            RunnableDFS.alterarlistCodigo(linhaEmExecucao, linhaEmExecucaoAnterior, this.runnableDFS.isExecutar());

                            parar();
                            arestaPara[x] = String.valueOf(v);
                            atualizarVariavel(arestaPara[x], marcado[x], x);

                            parar();
                            RunnableDFS.alterarTabela(listDFSParametros, this.runnableDFS.isExecutar());
                            Thread.sleep(velocidade);

                            parar();
                            linhaEmExecucaoAnterior = linhaEmExecucao;
                            linhaEmExecucao = 17;
                            RunnableDFS.alterarlistCodigo(linhaEmExecucao, linhaEmExecucaoAnterior, this.runnableDFS.isExecutar());
                            Thread.sleep(velocidade);

                            parar();
                            RunnableDFS.colorirVertice(v, 152, 251, 152, this.runnableDFS.isExecutar());

                            parar();
                            RunnableDFS.colorirAresta(v, x, 152, 251, 152, this.runnableDFS.isExecutar(), 60, 195, 100);

                            parar();
                            RunnableDFS.colorirVertice(x, 60, 195, 100, this.runnableDFS.isExecutar());

                            parar();
                            DFS(x, grafo);
                            
                            parar();
                            RunnableDFS.exibirVerticeExplorando("Vértice " + x + " pronto!", this.runnableDFS.isExecutar());

                            parar();

                            for (Aresta aresta : grafo.adj(x)) {

                                RunnableDFS.colorirAresta(x, aresta.getV2().getId(), 211, 211, 211, true, 152, 251, 152);
                                
                            }
                            parar();
                            RunnableDFS.colorirVertice(Integer.parseInt(arestaPara[x]), 60, 195, 100, this.runnableDFS.isExecutar());

                            parar();
                            RunnableDFS.colorirVertice(x, 211, 211, 211, this.runnableDFS.isExecutar());
                            

                            parar();
                            RunnableDFS.desempilharPilhaRecursiva(listRecursiva, this.runnableDFS.isExecutar());
                        }

                    } else {
                        parar();
                        RunnableDFS.colorirAresta(v, x, 152, 251, 152, this.runnableDFS.isExecutar(), 60, 195, 100);
                    }
                }

            }
            

        }
    }

    /**
     * 
     * @param arestaPara    Novo valor para o atributo arestaPara
     * @param marcado       Novo valor para o atributo marcado
     * @param id            Id do objeto que deve ser alterado
     */
    
    //Este método recebe o id do objeto do tipo DFS que será alterado na tabela. Ele é alterado na lista, para assim a alteração se refletir na tabela
    public void atualizarVariavel(String arestaPara, String marcado, int id) {

        listDFSParametros.get(id).setArestaPara(arestaPara);
        listDFSParametros.get(id).setItemMarcado(marcado);
        listDFSParametros.get(id).setId(id);

    }

    
    /**
     * 
     * @throws InterruptedException excepition para o wait
     */
    
    //método para parar a aplicação (botão de pause)
    public void parar() throws InterruptedException {

        if (FXMLExplorarComDFSController.pausado == 1) {
            synchronized (main) {
                main.wait();
            }
        }

    }

}
